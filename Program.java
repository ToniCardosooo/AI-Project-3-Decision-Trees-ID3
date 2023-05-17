import java.io.File;
import java.util.Scanner;

public class Program {

    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);

        // type the training set filepath
        System.out.print("Type the training CSV filepath: ");
        String training_filepath = in.next();
        System.out.println();

        // create the DataFrame object with the given CSV file
        DataFrame training = new DataFrame();
        training.readCSV(training_filepath);
        training.removeColumn(0); // remove ID column

        // processing continuous values to discrete ones
        for (int i = 0; i < training.getNumberColumns()-1; i++)
            Utility.Encoding.discretize(training.getColumn(i), "sturges");

        // create and train the Decision Tree
        DecisionTree dt = new DecisionTree();
        dt.fit(training);

        // ask for which format the user wants to have the tree printed out
        System.out.println("Choose the output format of the Decision Tree model:");
        System.out.println("1) Project's worksheet format");
        System.out.println("2) My custom format");
        System.out.print("Choice: ");
        int format = in.nextInt(); in.nextLine();
        
        // print the decision tree
        if (format == 1) dt.print();
        else dt.printFormated();

        // ask if the user wants to predict classifications using the trained Decision Tree
        System.out.println("Would you like to predict the classification of some examples?");
        System.out.println("1) Yes");
        System.out.println("2) No");
        System.out.print("Choice: ");
        int want_to_predict = in.nextInt(); in.nextLine();

        // going to predict examples
        if (want_to_predict == 1){

            // type the training set filepath
            System.out.print("\nType the testing CSV filepath: ");
            String testing_filepath = in.next();
            System.out.println();

            // read the contents of the testing set into a dataframe and process the continuous values with the same method as for the training set
            DataFrame testing = new DataFrame();
            testing.readCSV(testing_filepath);

            // processing the continuous values
            for (int i = 0; i < testing.getNumberColumns()-1; i++)
                Utility.Encoding.discretize(testing.getColumn(i), "sturges");

            // making the predictions using the Decision Tree and printing them
            Series prediction = dt.predict(testing);
            System.out.println(prediction);
        }

        System.out.println("Thank you for trying me out! :]");

        in.close();
    }
}
