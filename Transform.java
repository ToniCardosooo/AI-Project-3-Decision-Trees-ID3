import java.util.ArrayList;

public class Transform {

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
            long k = Math.round(3.322 * Math.log10(s.getSize()) + 1); // k represents the number of intervals

            Double minimo = Double.MAX_VALUE, maximo = Double.MIN_VALUE;
            for (Object obj : s.getDataList()){
                minimo = Math.min(minimo, (Double)obj);
                maximo = Math.max(maximo, (Double)obj);
            }

            double amplitude = (maximo - minimo) / k;
            amplitude = Math.round(amplitude * 100) / 100.0; // round to 2 decimals
            ArrayList<String> intervals = new ArrayList<String>();
            for (int i = 0; i < k+1; i++){
                if (i == k) intervals.add(String.valueOf(maximo));
                else intervals.add(String.valueOf(minimo + i * amplitude));
            }

            /* TO KEEP WORKING */

            return;
        }

        return;
    }
}
