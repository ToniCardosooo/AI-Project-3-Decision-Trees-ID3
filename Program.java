public class Program {
    public static void main(String[] args) {
        /*
         * if args[] length is 0, the program will print a message to please type a path for a CSV file
         * 
         * if args[] length is 1 (a path to a CSV file), it will train a Decision Tree on the given CSV file, and print the Decision Tree
         * 
         * if args[] length is 2 (both paths to CSV files), it will train a Decision Tree on the first given CSV file, print the Decision Tree,
         * and print the predicted result of each example in the testing set
         */

        if (args.length == 0){
            System.out.println("Please type a path to a CSV file in order to train a Decision Tree.");
            System.out.println("Execute the program with following format:\n\n\tjava Program <training CSV filepath> (optional)<testing CSV filepath>\n");
            return;
        }

        // create dataframe, read the contents of the CSV file into it, and remove the ID column
        DataFrame training = new DataFrame();
        training.readCSV(args[0]);
        training.removeColumn(0); // remove ID column
        
        // processing continuous values to discrete ones
        for (int i = 0; i < training.getNumberColumns()-1; i++)
            Utility.Encoding.discretize(training.getColumn(i), "sturges");
        
        // create, train and print the Decision Tree
        DecisionTree dt = new DecisionTree();
        dt.fit(training);
        dt.print();
        
        // if a testing set is given on the command line
        if (args.length == 2){
            // read the contents of the testing set into a dataframe and process the continuous values with the same method as for the training set
            DataFrame testing = new DataFrame();
            testing.readCSV(args[1]);
            // processing the continuous values
            for (int i = 0; i < testing.getNumberColumns()-1; i++)
                Utility.Encoding.discretize(testing.getColumn(i), "sturges");

            // making the predictions using the Decision Tree and printing them
            Series prediction = dt.predict(testing);
            System.out.println(prediction);
        }

    }
}
