package Model;

import java.util.ArrayList;

/**
 * Created by Maor on 11/2/2018.
 */
public class Term {
    private String term;
    private String df;
    private String tf;
    private ArrayList<Doc> documents;

    public Term() {
    }

    public String getDf() {
        return df;
    }

    public void setDf(String df) {
        this.df = df;
    }

    public String getTf() {
        return tf;
    }

    public void setTf(String tf) {
        this.tf = tf;
    }

    public ArrayList<Doc> getDocuments() {
        return documents;
    }

    public void setDocuments(ArrayList<Doc> documents) {
        this.documents = documents;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }
}
