import java.util.HashMap;

public class DTNode {
    
    // Attributes ------------------------------------------

    // if is non-leaf
        private String node_attribute;
    // if is leaf
        private Object classification;

    // attributs parent / children wise
    private DTNode parent;
    private Object parent_value;
    private HashMap<Object, DTNode> children;

    // Constructor ------------------------------------------

    // constructor for non-leaf node
    DTNode(String attribute, DTNode p, Object pv){
        node_attribute = attribute;
        classification = null;
        parent = p;
        parent_value = pv;
        children = new HashMap<Object, DTNode>();
    }

    // constructor for leaf node
    DTNode(Object c, DTNode p, Object pv){
        node_attribute = null;
        classification = c;
        parent = p;
        parent_value = pv;
        children = null;
    }

    // Getters ------------------------------------------

    public String getNodeAttribute(){return node_attribute;}
    public Object getClassification(){return classification;}
    public DTNode getParent(){return parent;}
    public Object getParentValue(){return parent_value;}
    public HashMap<Object, DTNode> getChildren(){return children;}

    public DTNode getChild(Object value){return children.get(value);}
    public boolean isLeaf(){return (children == null);}

    // Setters / Adders / Modifiers ------------------------------------------

    public void addChild(Object attribute_value, DTNode child){children.put(attribute_value, child);}

}
