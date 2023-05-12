import java.io.BufferedReader;  
import java.io.FileReader;  
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class DataFrame{

    // Attributes ----------------------------------------------------------------------

    private int num_columns;
    private int num_rows;
    private ArrayList<Series> table; // a list containing Series (implemented by another list) with their respective data in each row
    private ArrayList<String> column_names;

    // Constructor ----------------------------------------------------------------------

    DataFrame(){
        num_columns = 0;
        num_rows = 0;
        table = new ArrayList<Series>();
        column_names = new ArrayList<String>();
    }

    // Getters ----------------------------------------------------------------------

    public int getNumberColumns(){return num_columns;}
    public int getNumberRows(){return num_rows;}
    public ArrayList<Series> getTable(){return table;}
    public Series getColumn(int col_index){return table.get(col_index);}

    // Function to convert a string into a object of type Integer or Double if the string holds any integer or real numerical value ---------------------------------

    private Object convertToRepresentedType(String str){
        try {
            double doubleValue = Double.parseDouble(str);
            return doubleValue;
        }
        catch (NumberFormatException e) {
            return str;
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
                Series new_col = new Series(col_names[i]);
                table.add(new_col);
            }

            // read the data
            while ((line = br.readLine()) != null){
                String row[] = line.split(splitBy);
                for (int i = 0; i < num_columns; i++){
                    table.get(i).add(convertToRepresentedType(row[i]));
                }
            }
            num_rows = table.get(0).getSize();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    // For printing ----------------------------------------------------------------------

    @Override
    public String toString(){
        String out = "";
        for (String name : column_names)
            out += '\t' + name;
        out += '\n';

        for (int i = 0; i < num_rows; i++){
            for (Series s : table)
                out += '\t' + s.getValue(i).toString();
            out += '\n';
        }
        return out;
    }

    // For filtering ----------------------------------------------------------------------

    // Get a new DataFrame with the rows given by the list of row indexes as argument
    public DataFrame filterRows(ArrayList<Integer> row_indexes){
        DataFrame filtered = new DataFrame();
        for (int i = 0; i < this.num_columns; i++){
            filtered.table.add(new Series(this.column_names.get(i)));
            filtered.column_names.add(this.column_names.get(i));
        }
        filtered.num_columns = this.num_columns;
        
        for (int row_id : row_indexes){
            if (row_id < 0 || row_id >= this.num_rows)
                continue;
            for (int i = 0; i < this.num_columns; i++){
                filtered.table.get(i).add(
                    this.table.get(i).getValue(row_id)
                );
            }
        }
        filtered.num_rows = row_indexes.size();

        return filtered;
    }

    // Get a new DataFrame with the columns given by the list of column indexes as argument
    public DataFrame filterColumns(ArrayList<Integer> col_indexes){
        DataFrame filtered = new DataFrame();
        for (int col_id : col_indexes){
            if (col_id < 0 || col_id >= this.num_columns)
                continue;
            filtered.table.add(this.table.get(col_id));
            filtered.column_names.add(this.column_names.get(col_id));
            filtered.num_columns++;
        }
        filtered.num_rows = this.table.get(0).getSize();

        return filtered;
    }

    // Get a DataFrame with the examples that have "att_value" in the "att_id" column
    // This method also takes the "att_id" column away
    public DataFrame filterBySpecificAttributeValue(int att_id, Object att_value){
        ArrayList<Integer> filtered_id = new ArrayList<Integer>();
        Series att = table.get(att_id);
        for (int i = 0; i < att.getSize(); i++)
            if (att.getValue(i).equals(att_value))
                filtered_id.add(i);
        
        DataFrame filtered = filterRows(filtered_id);
        filtered.removeColumn(att_id);
        
        return filtered;
    }

    // DataFrame columns manipulation ----------------------------------------------------------------------

    public void addColumn(int col_index, Series col){table.add(col_index, col); column_names.add(col_index, col.getName()); num_columns++;}
    public void removeColumn(int col_index){table.remove(col_index); column_names.remove(col_index); num_columns--;}

}