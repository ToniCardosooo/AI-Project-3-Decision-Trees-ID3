public class DecisionTree {

    // Attributes -------------------------------------

    private DTNode root;

    // Constructor -------------------------------------

    DecisionTree(){root = null;}

    // Methods -------------------------------------

    public DTNode getRoot(){return root;}

    private DTNode learnDecisionTree(){
        // implement later
        
        return new DTNode(null, null, null);
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
