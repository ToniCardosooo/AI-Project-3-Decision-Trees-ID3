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

    // To add an element to the end of the Series
    public void add(Object obj){
        data.add(obj);
        unique_values.add(obj);
    }

    // To get the value of a row in the Series
    public Object getValue(int index){return data.get(index);}

    // For printing
    @Override
    public String toString(){
        String out = "";
        out += '\t' + name + "\n\n";
        for (int i = 0; i < data.size(); i++)
            out += (i+1) + "\t" + data.get(i).toString() + '\n';
        return out;
    }

    // To get the median value of a numerical series
    public Double getNumericalMedian(){
        if (getDataType().equals("string"))
            return null;

        // at this point, data type == double
        if (getSize() % 2 == 1){
            return (Double)data.get(getSize()/2);
        }
        // else
        double tmp1 = (Double)data.get(getSize()/2 - 1);
        double tmp2 = (Double)data.get(getSize()/2);
        double tmp = (tmp1 + tmp2) / 2.0;
        return tmp;
    }

    // To get the mean value of a numerical series
    public Double getNumericalMean(){
        if (getDataType().equals("string"))
            return null;

        // at this point, data type == double
        double sum = 0;
        int size = data.size();
        for (int i = 0; i < size; i++)
            sum += (Double)data.get(i);
        return sum/size;
    }

}
