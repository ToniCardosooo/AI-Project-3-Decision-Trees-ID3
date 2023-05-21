import java.util.HashMap;

public class DTNode {
    
    // Attributes ------------------------------------------

    // if is non-leaf
        private String node_attribute;
        private HashMap<Object, DTNode> children;

    // if is leaf
        private Object classification;
        private int count;

    // Constructor ------------------------------------------

    // constructor for non-leaf / internal node
    DTNode(String attribute){
        node_attribute = attribute;
        children = new HashMap<Object, DTNode>();

        classification = null;
        count = -1;
    }

    // constructor for leaf node
    DTNode(Object c, int count_){
        node_attribute = null;
        children = null;

        classification = c;
        count = count_;
    }

    // Getters ------------------------------------------

    public String getNodeAttribute(){return node_attribute;}
    public Object getClassification(){return classification;}
    public Object getCount(){return count;}
    public HashMap<Object, DTNode> getChildren(){return children;}

    // children-related methods
    public DTNode getChild(Object value){return children.get(value);}
    public void addChild(Object attribute_value, DTNode child){children.put(attribute_value, child);}

    // check if node is a leaf
    public boolean isLeaf(){return (children == null);}

}
