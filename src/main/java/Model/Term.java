package Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Maor on 11/2/2018.
 */
public class Term implements Serializable,Comparable<Term>{
    private String term;
    //private int df;
    private HashMap<String,Integer> amountInDoc; //String=Doc name, Integer = tf
    //private String position;

    public Term(String term) {
        this.term = term;
        //df = 0;
        amountInDoc = new HashMap<>();
    }

    public void incAmounts(String docName){
        //incDf(docName);
        incTf(docName);
    }

//    public int getDf() {
//        return this.df;
//    }

//    private void incDf(String docName) {
//        if(!amountInDoc.containsKey(docName))
//            this.df+=1;
//    }

    public int getTf(String docName) {
        return this.amountInDoc.get(docName);
    }

    private void incTf(String docName) {
        if(amountInDoc.containsKey(docName))
            this.amountInDoc.replace(docName,this.amountInDoc.get(docName).intValue() + 1);
        else
            this.amountInDoc.put(docName,1);
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    @Override
    public int compareTo(Term o) {
        return 0;
    }

    @Override
    public String toString() {
        return  "'" + term + '\'' +
                ", amountInDoc=" + amountInDoc;
    }
}
