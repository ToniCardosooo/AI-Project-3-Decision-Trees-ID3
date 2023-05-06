import java.util.ArrayList;
import java.util.HashSet;

public class DecisionTree {

    // Root attribute and Getter -------------------------------------

    private DTNode root;
    public DTNode getRoot(){return root;}

    // Constructor -------------------------------------

    DecisionTree(){root = null;}

    // Methods -------------------------------------

    // returns a leaf node which classification is equal to the
    // most frequent target classification in the DataFrame data
    private DTNode pluralityValue(DataFrame data){
        // to implement
    }

    private int getAttributeHighestGain(DataFrame examples, ArrayList<Integer> attributes_id){
        Series example_target = Utility.ModelSelection.getTarget(examples);
        int highest_gain_att_id = -1
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

    private DTNode learnDecisionTree(DataFrame examples, ArrayList<Integer> attributes_id, DataFrame parent_examples, DTNode parent_node, Object parent_value){
        // if examples is empty
        if (examples.getNumberRows() == 0)
            return pluralityValue(parent_examples);

        // if there are no attributes, only target
        if (attributes_id.size() == 0)
            return pluralityValue(examples);

        // if all examples have the same target value
        Series example_target = Utility.ModelSelection.getTarget(examples);
        if (example_target.getUniqueValues().size() == 1)
            return pluralityValue(examples);
        
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
            Node child = learnDecisionTree(
                exs,
                attributes_id.remove(highest_gain_att_id),
                examples,
                node,
                value
            );
            node.addChild(value, child);
        }
        return node;
    }

    // method that calls the Learning-Decision-Tree funtion
    public void fit(){
        root = learnDecisionTree();
    }

    // method to use the DT to make predictions
    public Series predict(){
        // implement later
        return new Series("");
    }

    // Printing -------------------------------------

    @Override
    public String toString(){
        return "";
    }
    
}
