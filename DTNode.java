import java.util.HashMap;

public class DTNode {
    
    // Attributes ------------------------------------------

    // if is non-leaf
        private String node_attribute;
    // if is leaf
        private Object classification;
        private int count;

    // attributs related to parent node and children nodes
    private DTNode parent;
    private Object parent_value;
    private HashMap<Object, DTNode> children;

    // Constructor ------------------------------------------

    // constructor for non-leaf / internal node
    DTNode(String attribute, DTNode p, Object pv){
        node_attribute = attribute;
        classification = null;
        count = -1;
        parent = p;
        parent_value = pv;
        children = new HashMap<Object, DTNode>();
    }

    // constructor for leaf node
    DTNode(Object c, int count_, DTNode p, Object pv){
        node_attribute = null;
        classification = c;
        count = count_;
        parent = p;
        parent_value = pv;
        children = null;
    }

    // Getters ------------------------------------------

    // attriute-related
    public String getNodeAttribute(){return node_attribute;}
    public Object getClassification(){return classification;}
    public Object getCount(){return count;}
    public DTNode getParent(){return parent;}
    public Object getParentValue(){return parent_value;}
    public HashMap<Object, DTNode> getChildren(){return children;}

    // access specific child node
    public DTNode getChild(Object value){return children.get(value);}

    // check if node is a leaf
    public boolean isLeaf(){return (children == null);}

    // Setters / Adders / Modifiers ------------------------------------------

    public void addChild(Object attribute_value, DTNode child){children.put(attribute_value, child);}

    // Printing ------------------------------------------

    @Override
    public String toString(){
        String out = "";
        if (parent != null) out += "Parent decision: " + parent_value.toString() + '\n';
        if (!isLeaf()) out += "Attribute: " + node_attribute + '\n';
        else{
            out += "Classification: " + classification.toString() + "; Count: " + count + '\n';
        }
        return out;
    }

}
