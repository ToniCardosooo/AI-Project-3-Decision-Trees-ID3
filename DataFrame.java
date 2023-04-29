import java.io.BufferedReader;  
import java.io.FileReader;  
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class DataFrame{

    // Attributes ----------------------------------------------------------------------

    private int num_columns;
    private int num_rows;

    private ArrayList<ArrayList<Object>> table; // a list containing the rows (implemented by another list) with their respective data in each row
    private ArrayList<ArrayList<Object>> columns; // a list containing the columns (implemented by another list) with their respective data in each column

    // related to the columns
    private ArrayList<String> column_names;
    private HashMap<String, Integer> column_name_to_index;
    private ArrayList<HashSet<Object>> columns_unique_values;

    // Constructor ----------------------------------------------------------------------

    DataFrame(){
        num_columns = 0;
        num_rows = 0;

        table = new ArrayList<ArrayList<Object>>();
        columns = new ArrayList<ArrayList<Object>>();

        column_names = new ArrayList<String>();
        column_name_to_index = new HashMap<String, Integer>();
        columns_unique_values = new ArrayList<HashSet<Object>>();
    }

    // Getters ----------------------------------------------------------------------

    public int getNumberColumns(){return num_columns;}
    public int getNumberRows(){return num_rows;}

    public ArrayList<ArrayList<Object>> getTable(){return table;}

    public ArrayList<ArrayList<Object>> getColumns(){return columns;}
    public ArrayList<String> getColumnNames(){return column_names;}

    public int getColumnIndex(String col_name){return column_name_to_index.get(col_name);}

    public ArrayList<Object> getColumn(int col_index){return columns.get(col_index);}
    public ArrayList<Object> getColumn(String col_name){return columns.get(getColumnIndex(col_name));}

    public HashSet<Object> getUniqueValues(int col_index){return columns_unique_values.get(col_index);}
    public HashSet<Object> getUniqueValues(String col_name){return columns_unique_values.get(getColumnIndex(col_name));}

    // Function to convert a string into a object of type Integer or Double if the string holds any integer or real numerical value ---------------------------------

    private Object convertToRepresentedType(String str){
        try {
            int intValue = Integer.parseInt(str);
            return intValue;
        } catch (NumberFormatException e) {
            try {
                double doubleValue = Double.parseDouble(str);
                return doubleValue;
            } catch (NumberFormatException ex) {
                return str;
            }
        }
    }

    // Turn the data in a CSV file to a DataFrame object ----------------------------------------------------------------------

    public void readCSV(String file_path){
        try {
            String splitBy = ",";
            String line = "";
            BufferedReader br = new BufferedReader(new FileReader(file_path));

            // read header with the columns' names 
            line = br.readLine();
            String col_names[] = line.split(splitBy);
            num_columns = col_names.length;

            // add names to the names' list and their mapping
            // create the containers for each column and the set of unique values for each column
            for (int i = 0; i < num_columns; i++){
                column_names.add(col_names[i]);
                column_name_to_index.put(col_names[i], i);

                ArrayList<Object> new_col = new ArrayList<Object>();
                columns.add(new_col);

                HashSet<Object> unique = new HashSet<Object>();
                columns_unique_values.add(unique);
            }

            // read the data
            while ((line = br.readLine()) != null){
                ArrayList<Object> row = new ArrayList<Object>();
                String info[] = line.split(splitBy);
                for (int i = 0; i < num_columns; i++){
                    row.add(convertToRepresentedType(info[i]));
                    columns.get(i).add(convertToRepresentedType(info[i]));
                    columns_unique_values.get(i).add(convertToRepresentedType(info[i]));
                }
                table.add(row);
            }
            num_rows = table.size();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    // For printing ----------------------------------------------------------------------

    @Override
    public String toString(){
        String out = "";
        for (String name : column_names){
            out += '\t' + name;
        }
        out += '\n';
        for (ArrayList<Object> row : table){
            for (Object o : row){
                out += '\t' + o.toString();
            }
            out += '\n';
        }
        return out;
    }

    // Other ----------------------------------------------------------------------

}