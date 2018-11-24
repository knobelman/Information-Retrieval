package Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class represent the Term object
 * term - the name of the term
 * amountInDoc - HashMap of all the docs and the tf in the current doc
 */
public class Term implements Serializable{
    private String term;
    private HashMap<String,Integer> amountInDoc; //String=Doc name, Integer = tf
    //private int df;
    //private String position;

    /**
     * C'tor
     * @param term - term name
     */
    public Term(String term) {
        this.term = term;
        this.amountInDoc = new HashMap<>();
    }

    /**
     * this method increment the tf
     * @param docName - increment amount of term in a given document
     */
    public void incAmounts(String docName){
        incTf(docName);
    }

    public int getTf(String docName) {
        return this.amountInDoc.get(docName);
    }

    private void incTf(String docName) {
        if(amountInDoc.containsKey(docName))
            this.amountInDoc.replace(docName,this.amountInDoc.get(docName).intValue() + 1);
        else
            this.amountInDoc.put(docName,1);
    }

    @Override
    public String toString() {
        return  "'" + term + '\'' +
                ", amountInDoc=" + amountInDoc;
    }

    public void setTerm(String term){
        this.term = term;
    }

    public String getTermName(){
        return term;
    }
}
