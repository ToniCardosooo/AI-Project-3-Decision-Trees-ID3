import java.util.ArrayList;
import java.util.HashSet;

public class Series {
    private String name;
    private ArrayList<Object> data;
    private HashSet<Object> unique_values;

    // Construtor
    Series(String name){
        this.name = name;
        data = new ArrayList<Object>();
        unique_values = new HashSet<Object>();
    }

    // Copy Constructor
    Series(Series to_copy_from){
        this.name = to_copy_from.name;
        this.data = new ArrayList<Object>();
        for (Object obj : to_copy_from.data)
            this.data.add(obj);
        this.unique_values = new HashSet<Object>();
        for (Object obj : to_copy_from.unique_values)
            this.unique_values.add(obj);
    }

    // Getters
    public String getName(){return name;}
    public int getSize(){return data.size();}
    public ArrayList<Object> getDataList(){return data;}
    public HashSet<Object> getUniqueValues(){return unique_values;}
    
    public String getDataType(){
        if (data.get(0) instanceof Double) return "double";
        return "string";
    }
    public Object getValue(int index){return data.get(index);}

    // Setters
    public void setValue(int index, Object obj){data.set(index, obj);}

    // To add an element to the end of the Series
    public void add(Object obj){
        data.add(obj);
        unique_values.add(obj);
    }

    // For printing
    @Override
    public String toString(){
        String out = "";
        out += '\t' + name + "\n\n";
        for (int i = 0; i < data.size(); i++)
            out += (i+1) + "\t" + data.get(i).toString() + '\n';
        return out;
    }

}
