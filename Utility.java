import java.util.ArrayList;
import java.util.HashSet;
import java.util.Collections;

public class Utility {

    static class Encoding {

        // transforms a series of numerical data into a series of categorical data
        public static void discretize(Series s, String method){
            if (s.getDataType().equals("string"))
                return;
    
            // all transformations below will only apply to numerical values (double)
            if (method.equals("median")){
                double median = s.getNumericalMedian();
                String lower_class = "<" + median + " (median)"; 
                String upper_class = ">=" + median + " (median)"; 
                for (int i = 0; i < s.getSize(); i++){
                    if ((Double) s.getValue(i) < median)
                        s.setValue(i, lower_class);
                    else
                        s.setValue(i, upper_class);
                }
                return;
            }
    
            if (method.equals("mean")){
                double mean = s.getNumericalMean();
                String lower_class = "<" + mean + " (mean)"; 
                String upper_class = ">=" + mean + " (mean)"; 
                s.getUniqueValues().clear();
                for (int i = 0; i < s.getSize(); i++){
                    if ((Double) s.getValue(i) < mean)
                        s.setValue(i, lower_class);
                    else
                        s.setValue(i, upper_class);
                    s.getUniqueValues().add(s.getValue(i));
                }
                return;
            }
    
            if (method.equals("sturges")){
                long num_intervals = Math.round(3.322 * Math.log10(s.getSize()) + 1); // k represents the number of intervals
    
                Double minimo = Double.MAX_VALUE, maximo = Double.MIN_VALUE;
                for (Object obj : s.getDataList()){
                    minimo = Math.min(minimo, (Double)obj);
                    maximo = Math.max(maximo, (Double)obj);
                }
    
                double amplitude = (maximo - minimo) / num_intervals;
                amplitude = Math.round(amplitude * 100) / 100.0; // round to 2 decimals
                
                s.getUniqueValues().clear();
                for (int i = 0; i < s.getSize(); i++){
                    double value = (Double) s.getValue(i);
                    long k = Math.round((value - minimo) / amplitude);
                    double lim_inf = Math.round((minimo + k*amplitude) * 100) / 100.0;
                    double lim_sup = Math.round((minimo + (k+1)*amplitude) * 100) / 100.0;
    
                    String interval = "[" + lim_inf + " , " + lim_sup + "[";
                    s.setValue(i, interval);
                    s.getUniqueValues().add(interval);
                }
    
                return;
            }
    
            return;
        }
    }

    // ====================================================================

    static class ModelSelection{
        /* 
        public static ArrayList<Series> getSeriesForEachClass(Series column){
            Object classes_values[] = column.getUniqueValues().toArray();
            ArrayList<Series> classes_series = new ArrayList<>();
            for (int i = 0; i < classes_values.length; i++)
                classes_series.add(new Series(classes_values[i].toString()));
            
            for (int i = 0; i < column.getSize(); i++){
                for (int j = 0; j < classes_values.length; j++){
                    if (column.getValue(i).equals(classes_values[j]))
                        classes_series.get(j).add(column.getValue(i));
                }
            }
            
            return classes_series;
        }
        */

        // Method inspired in the function "train_test_split" from the python's library "sklearn.model_selection"
        public static ArrayList<DataFrame> getTrainTest(DataFrame data, double test_size){   
            Object target_classes[] = data.getColumn(data.getNumberColumns()-1).getUniqueValues().toArray();
            ArrayList<ArrayList<Integer>> target_classes_indexes = new ArrayList<>();
            for (int i = 0; i < target_classes.length; i++)
                target_classes_indexes.add(new ArrayList<>());

            Series target = data.getColumn(data.getNumberColumns()-1);
            for (int i = 0; i < target.getSize(); i++){
                for (int j = 0; j < target_classes.length; j++){
                    if (target.getValue(i).equals(target_classes[j]))
                        target_classes_indexes.get(j).add(i);
                }
            }     
            
            ArrayList<Integer> training_indexs = new ArrayList<Integer>();
            ArrayList<Integer> testing_indexs = new ArrayList<Integer>();

            for (ArrayList<Integer> indexes : target_classes_indexes){
                Collections.shuffle(indexes);
                long test_index_limit = Math.round(indexes.size() * test_size);
                for (int i = 0; i < indexes.size(); i++){
                    if (i <= test_index_limit) testing_indexs.add(indexes.get(i));
                    else training_indexs.add(indexes.get(i));
                }
            }

            DataFrame training = data.filterRows(training_indexs);
            DataFrame testing = data.filterRows(testing_indexs);
        
            ArrayList<DataFrame> sets = new ArrayList<DataFrame>();
            sets.add(training); sets.add(testing);
            return sets;
        }
    }
}
