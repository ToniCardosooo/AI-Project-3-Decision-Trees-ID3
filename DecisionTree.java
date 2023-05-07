import java.util.ArrayList;
import java.util.HashSet;
import java.util.Collections;

public class DecisionTree {

    // Root attribute and Getter -------------------------------------

    private DTNode root;
    public DTNode getRoot(){return root;}

    // Constructor -------------------------------------

    DecisionTree(){root = null;}

    // Methods -------------------------------------

    // returns a leaf node whose classification is equal to the
    // most frequent target classification in the DataFrame data
    private DTNode pluralityValue(DataFrame data, DTNode parent_node, Object parent_value){
        Series target = Utility.ModelSelection.getTarget(data);
        HashSet<Object> unique = target.getUniqueValues();

        Object most_frequent_value = null;
        int highest_freq = -1;

        for (Object value : unique){
            int freq = Collections.frequency(target.getDataList(), value);
            if (freq > highest_freq){
                highest_freq = freq;
                most_frequent_value = value;
            }
        }

        return new DTNode(most_frequent_value, parent_node, parent_value);
    }

    private int getAttributeHighestGain(DataFrame examples, ArrayList<Integer> attributes_id){
        Series example_target = Utility.ModelSelection.getTarget(examples);
        int highest_gain_att_id = -1;
        double highest_gain = -1;
        for (int id : attributes_id){
            Series attribute = examples.getColumn(id);
            double gain = Utility.Metrics.getGain(example_target, attribute);
            
            //System.out.println(attribute.getName() + ": " + gain);
            
            if (gain > highest_gain){
                highest_gain = gain;
                highest_gain_att_id = id;
            }
        }
        return highest_gain_att_id;
    }

    private DTNode learnDecisionTree(DataFrame examples, ArrayList<Integer> attributes_id, DataFrame parent_examples, DTNode parent_node, Object parent_value){
        /* System.out.println("Current examples:");
        System.out.println(examples);
        System.out.println("Number of attributes: " + attributes_id);
        System.out.println(); */

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
        HashSet<Object> highest_gain_att_values = examples.getColumn(highest_gain_att_id).getUniqueValues();
        for (Object value : highest_gain_att_values){
            DataFrame exs = examples.filterBySpecificAttributeValue(highest_gain_att_id, value);
            
            ArrayList<Integer> new_attributes_id = new ArrayList<>();
            for (int i = 0; i < attributes_id.size()-1; i++) new_attributes_id.add(i);

            DTNode child = learnDecisionTree(
                exs,
                new_attributes_id,
                examples,
                node,
                value
            );
            node.addChild(value, child);
        }
        return node;
    }

    // method that calls the Learning-Decision-Tree funtion
    public void fit(DataFrame data){
        ArrayList<Integer> attributes_id = new ArrayList<Integer>();
        for (int i = 0; i < data.getNumberColumns()-1; i++)
            attributes_id.add(i);

        root = learnDecisionTree(data, attributes_id, null, null, null);
    }

    // method to use the DT to make predictions
    public Series predict(){
        // implement later
        return new Series("");
    }

    // Printing -------------------------------------

    private void DFS(DTNode cur){
        System.out.println(cur);
        if (cur.getChildren() == null)
            return;
        for (Object value : cur.getChildren().keySet())
            DFS(cur.getChild(value));
    }

    private void BFS(DTNode root){
        ArrayList<DTNode> queue = new ArrayList<DTNode>();
        queue.add(root);
        while (queue.size() != 0){
            DTNode cur = queue.remove(0);
            System.out.println(cur);
            if (cur.isLeaf())
                continue;
            
            System.out.println(cur.getChildren().size());
            for (Object value : cur.getChildren().keySet())
                queue.add(cur.getChild(value));
        }
    }

    public void printDFS(){DFS(root);}
    public void printBFS(){BFS(root);}
    
}
