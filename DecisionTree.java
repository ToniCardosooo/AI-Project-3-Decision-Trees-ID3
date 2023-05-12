import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Collections;
import java.util.HashMap;

public class DecisionTree {

    // Attributes and Getters -------------------------------------

    private DTNode root;
    private DataFrame training;

    public DTNode getRoot(){return root;}
    public DataFrame getTrainingDataFrame(){return training;}

    // Constructor -------------------------------------

    DecisionTree(){root = null; training = null;}

    // Methods -------------------------------------

    private Object getMostFrequentValue(Series s){
        HashSet<Object> unique = s.getUniqueValues();
        int highest_freq = -1;
        ArrayList<Object> most_frequent = new ArrayList<>();
        for (Object value : unique){
            int freq = Collections.frequency(s.getDataList(), value);
            if (freq > highest_freq){
                highest_freq = freq;
                most_frequent.clear();
                most_frequent.add(value);
            }
            else if (freq == highest_freq) most_frequent.add(value);
        }
        // breaking ties randomly, by choosing the first element of the shuffled list
        Collections.shuffle(most_frequent);
        return most_frequent.get(0);
    }

    // returns a leaf node whose classification is equal to the
    // most frequent target classification in the DataFrame data
    private DTNode pluralityValue(DataFrame data, DTNode parent_node, Object parent_value){
        Series target = Utility.ModelSelection.getTarget(data);
        Object classification = getMostFrequentValue(target);
        return new DTNode(
            classification,
            Collections.frequency(target.getDataList(), classification),
            parent_node,
            parent_value);
    }

    private int getAttributeHighestGain(DataFrame examples, ArrayList<Integer> attributes_id){
        Series example_target = Utility.ModelSelection.getTarget(examples);
        int highest_gain_att_id = -1;
        double highest_gain = -1;
        for (int id : attributes_id){
            Series attribute = examples.getColumn(id);
            double gain = Utility.Metrics.getGain(example_target, attribute);            
            if (gain > highest_gain){
                highest_gain = gain;
                highest_gain_att_id = id;
            }
        }
        return highest_gain_att_id;
    }

    private DTNode learnDecisionTree(DataFrame examples, ArrayList<Integer> attributes_id, DataFrame parent_examples, DTNode parent_node, Object parent_value, HashMap<String, HashSet<Object>> unique_values_per_attribute){
        // if examples is empty
        if (examples.getNumberRows() == 0)
            return pluralityValue(parent_examples, parent_node, parent_value);

        // if there are no attributes, only target
        if (attributes_id.size() == 0)
            return pluralityValue(examples, parent_node, parent_value);

        // if all examples have the same target value
        Series example_target = Utility.ModelSelection.getTarget(examples);
        if (example_target.getUniqueValues().size() == 1)
            return pluralityValue(examples, parent_node, parent_value);
        
        // else (recursive case)
        int highest_gain_att_id = getAttributeHighestGain(examples, attributes_id);
        DTNode node = new DTNode(
            examples.getColumn(highest_gain_att_id).getName(),
            parent_node,
            parent_value
        );
        HashSet<Object> highest_gain_att_values = unique_values_per_attribute.get(
            examples.getColumn(highest_gain_att_id).getName()
        );
        
        for (Object value : highest_gain_att_values){
            DataFrame exs = examples.filterBySpecificAttributeValue(highest_gain_att_id, value);
            
            ArrayList<Integer> new_attributes_id = new ArrayList<>();
            for (int i = 0; i < attributes_id.size()-1; i++) new_attributes_id.add(i);

            DTNode child = learnDecisionTree(
                exs,
                new_attributes_id,
                examples,
                node,
                value,
                unique_values_per_attribute
            );
            node.addChild(value, child);
        }
        return node;
    }

    // Fit method -------------------------------------

    public void fit(DataFrame fitting_data){
        this.training = fitting_data;
        // fitting_data already was preprocessed
        ArrayList<Integer> attributes_id = new ArrayList<Integer>();
        HashMap<String, HashSet<Object>> unique_values_per_attribute = new HashMap<>();
        for (int i = 0; i < fitting_data.getNumberColumns()-1; i++){
            attributes_id.add(i);
            unique_values_per_attribute.put(
                fitting_data.getColumn(i).getName(),
                fitting_data.getColumn(i).getUniqueValues()
            );
        }
        
        root = learnDecisionTree(fitting_data, attributes_id, null, null, null, unique_values_per_attribute);
    }

    // Predict method -------------------------------------

    // data_to_predict has no target value
    // data_to_predict has the same preprocessing as the fitting_data DataFrame in the fit function
    public Series predict(DataFrame data_to_predict){
        // get each column's index
        HashMap<String, Integer> col_name_to_index = new HashMap<>();
        for (int i = 0; i < data_to_predict.getNumberColumns(); i++)
            col_name_to_index.put(data_to_predict.getColumn(i).getName(), i);
        
        Series prediction = new Series("Predicted Class");
        for (int row = 0; row < data_to_predict.getNumberRows(); row++){
            DTNode cur = root;
            while (cur != null && !cur.isLeaf()){
                int col_index = col_name_to_index.get(cur.getNodeAttribute());
                cur = cur.getChildren().get(
                    data_to_predict.getColumn(col_index).getValue(row)
                );
            }
            // if is null, we'll classify the example with the most common classification
            if (cur == null) prediction.add(
                getMostFrequentValue(Utility.ModelSelection.getTarget(this.training))
            );
            // if cur is a leaf, therefore it already has predicted value
            else prediction.add(cur.getClassification());
        }
        
        return prediction;
    }

    // Printing -------------------------------------

    // print each tree node in a Depth-First-Search order
    private void DFS(DTNode cur){
        System.out.println(cur);
        if (cur.getChildren() == null)
            return;
        for (Object value : cur.getChildren().keySet())
            DFS(cur.getChild(value));
    }

    // print each tree node in a Breadth-First-Search order
    private void BFS(DTNode root){
        ArrayList<DTNode> queue = new ArrayList<DTNode>();
        queue.add(root);
        while (queue.size() != 0){
            DTNode cur = queue.remove(0);
            System.out.println(cur);
            if (cur.isLeaf())
                continue;
            
            System.out.println("Number of children: " + cur.getChildren().size());
            for (Object value : cur.getChildren().keySet())
                queue.add(cur.getChild(value));
        }
    }

    // print the tree in a formated style for better understanding of its structure
    // follows the DFS order
    private void formatPrint(DTNode cur, String prefix){
        if (cur.isLeaf()){
            System.out.println(prefix + "Classification: " + cur.getClassification().toString() + " (count = " + cur.getCount() + ")");
            System.out.println(prefix);
            return;
        }
        System.out.println(prefix + "Attribute: " + cur.getNodeAttribute());
        Set<Object> unique = cur.getChildren().keySet();
        for (Object value : unique){
            System.out.println(prefix + "|-> Value: " + value);
            formatPrint(cur.getChild(value), prefix + "|\t");
        }
        System.out.println(prefix);
    }

    public void printDFS(){DFS(root);}
    public void printBFS(){BFS(root);}
    public void print(){formatPrint(root, "");}
}
