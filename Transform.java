public class Transform {

    public static Series discretize(Series s, String method){
        if (s.getDataType().equals("string"))
            return new Series(s);

        // all transformations below will only apply to numerical values (double)
        if (method.equals("median")){
            Series discrete = new Series(s.getName());
            double median = s.getNumericalMedian();
            String lower_class = "<" + median + " (median)"; 
            String upper_class = ">=" + median + " (median)"; 
            for (int i = 0; i < s.getSize(); i++){
                if ((Double) s.getValue(i) < median)
                    discrete.add(lower_class);
                else
                    discrete.add(upper_class);
            }
            return discrete;
        }

        if (method.equals("mean")){

        }

        if (method.equals("intervals")){

        }

        return new Series(s);
    }
}
