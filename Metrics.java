public class Metrics {
    /*
     * TO DO
     * -> Entropy function (for multiclass target variable like in the Iris dataset)
     * -> B(q) function (for binary class tartget variable)
     * -> Remainder function
     * -> Gain function
     * 
     * -> B(q) = -(q * log2(q) + (1-q) * log2(1-q))
     * -> Remainder(A) = for each class k of A: sum[ (k->pos + k->neg)/total * B(k->pos/(total_k)) ]
     * 
     * How it works:
     * -> Binary classification for target variable Y:
     *      -> Gain(A) = B(positive(Y)/total(Y)) - Remainder(A)
     *      
     * -> Multi classification for target variable Y:
     *      -> Gain(A) = Entropy(Y) - Remainder(A)
     *      -> how to calculate Remainder?
     */
}
