import java.util.ArrayList;
import java.util.HashSet;
import java.util.Collections;

public class Utility {

    // class with method to encode continuos values into categorical ones
    static class Encoding {

        // transforms a series of numerical data into a series of categorical data
        public static void discretize(Series s, String method){
            if (s.getDataType().equals("string"))
                return;
    
            // all transformations below will only apply to numerical values (double type)

            // replaces every data with "< median_value" or ">= median_value"
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
    
            // replaces every data with "< mean_value" or ">= mean_value"
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
            
            /*
             * this method gives a value to each data based on which interval they are on
             * all intervals have the same amplitude and precision of 2 decimal cases
             * the number of intervals, k, for a data set with n examples, follows the "Sturges' rule" -> k = log_2(n) + 1 (the value is rounded to an integer)
             */
            if (method.equals("sturges")){
                long num_intervals = Math.round(3.322 * Math.log10(s.getSize()) + 1);
                
                // get minimum, maximum and amplitude of the intervals
                Double minimo = Double.MAX_VALUE, maximo = Double.MIN_VALUE;
                for (Object obj : s.getDataList()){
                    minimo = Math.min(minimo, (Double)obj);
                    maximo = Math.max(maximo, (Double)obj);
                }
                double amplitude = (maximo - minimo) / num_intervals;
                amplitude = Math.round(amplitude * 100) / 100.0; // round to 2 decimals
                
                // clear the set of unique values
                s.getUniqueValues().clear();

                // discretize each value in the series
                for (int i = 0; i < s.getSize(); i++){
                    // get the lower and upper limit
                    double value = (Double) s.getValue(i);
                    int k = (int)((value - minimo) / amplitude);
                    double lim_inf = Math.round((minimo + k*amplitude) * 100) / 100.0;
                    double lim_sup = Math.round((minimo + (k+1)*amplitude) * 100) / 100.0;
                    
                    // swap the value by its corresponding interval
                    String interval = "";
                    if (value <= minimo) interval = "<= " + minimo;
                    else if (value >= maximo) interval = ">= " + maximo;
                    else interval = "[" + lim_inf + " , " + lim_sup + "[";
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

        // method inspired in the function "train_test_split" from the python's library "sklearn.model_selection"
        public static ArrayList<DataFrame> getTrainTestDataFrames(DataFrame data, double test_size){   
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
                    if (i < test_index_limit) testing_indexs.add(indexes.get(i));
                    else training_indexs.add(indexes.get(i));
                }
            }

            DataFrame training = data.filterRows(training_indexs);
            DataFrame testing = data.filterRows(testing_indexs);
        
            ArrayList<DataFrame> sets = new ArrayList<DataFrame>();
            sets.add(training); sets.add(testing);
            return sets;
        }

        // method to get the predicting columns from a dataframe
        public static DataFrame getPredictor(DataFrame data){
            // we assume that data's 1st column is the ID column and the
            // last column is the target variable column
            ArrayList<Integer> pred_indexes = new ArrayList<>();
            for (int i = 0; i < data.getNumberColumns()-1; i++)
                pred_indexes.add(i);
            return data.filterColumns(pred_indexes);
        }

        // method to get the target column from a dataframe
        public static Series getTarget(DataFrame data){
            return data.getColumn(data.getNumberColumns()-1);
        }
    }

    // ====================================================================

    static class Metrics{
        private static double log2(double x){
            if (x == 0) return 0;
            return Math.log(x) / Math.log(2);
        }

        private static double[] getProbabilities(Series s){
            Object unique_values[] = s.getUniqueValues().toArray();
            double probs[] = new double[s.getUniqueValues().size()];
            for (int i = 0; i < s.getSize(); i++){
                for (int j = 0; j < unique_values.length; j++){
                    if (s.getValue(i).equals(unique_values[j]))
                        probs[j]++;
                }
            }

            for (int i = 0; i < probs.length; i++)
                probs[i] /= s.getSize();
            
            return probs;
        }

        public static double getEntropy(Series target){
            double probs[] = getProbabilities(target);
            double entr = 0.0;
            for (double p : probs) entr += p * log2(p);
            return -1.0 * entr;
        }

        public static double getEntropy(Series target, Series attribute){
            Object unique_values_attribute[] = attribute.getUniqueValues().toArray();
            double probs_attribute[] = getProbabilities(attribute);
            double entr = 0.0;

            // for each possible value in attribute calculate the entropy
            // of target restrained to the examples with those values
            for (int i = 0; i < unique_values_attribute.length; i++){

                // get the target values within the new domain
                Series unique_attribute_target = new Series("");
                for (int j = 0; j < target.getSize(); j++)
                    if (attribute.getValue(j).equals(unique_values_attribute[i]))
                        unique_attribute_target.add(target.getValue(j));

                entr += probs_attribute[i] * getEntropy(unique_attribute_target);
            }

            return entr;
        }

        public static double getGain(Series target, Series attribute){
            return (getEntropy(target) - getEntropy(target, attribute));
        }

        public static double getAccuracy(Series target_true, Series target_pred){
            double accuracy = 0.0;
            for (int i = 0; i < target_true.getSize(); i++)
                if (target_true.getValue(i).equals(target_pred.getValue(i)))
                    accuracy++;
            accuracy /= target_true.getSize();
            return accuracy;
        }
    }
}
