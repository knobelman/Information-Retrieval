package Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * This class represents a document
 */
public class Doc implements Serializable {
    private String doc_num;
    private String doc_content;
    private String path;
    private int max_tf;
    private int specialWordCount;
    private String city;
    private HashMap<String,Term> termsInDoc;
    private String mostFrequentTerm; //extra

    public Doc(){
        this.termsInDoc = new HashMap<>();
    }
    /**
     * C'tor
     * @param path
     * @param doc_num
     * @param doc_content
     */
    public Doc(String path, String doc_num, String doc_content, String city) {
        this.doc_num = doc_num;
        this.doc_content = doc_content;
        this.city = city;
        this.path = path;
        this.termsInDoc = new HashMap<>();
    }

    public String getDoc_num() {
        return doc_num;
    }

    public void setDoc_num(String doc_num) {
        this.doc_num = doc_num;
    }

    public String getDoc_content() {
        return doc_content;
    }

    public void setDoc_content(String doc_content) {
        this.doc_content = doc_content;
    }

    public int getMax_tf() {
        return max_tf;
    }

    public void setMax_tf(int max_tf) {
        this.max_tf = max_tf;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getSpecialWordCount() {
        return specialWordCount;
    }

    public void setSpecialWordCount(int specialWordCount) {
        this.specialWordCount = specialWordCount;
    }

    public String getMostFrequentTerm() {
        return mostFrequentTerm;
    }

    public void setMostFrequentTerm(String mostFrequentTerm) {
        this.mostFrequentTerm = mostFrequentTerm;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public HashMap<String, Term> getTermsInDoc() {
        return termsInDoc;
    }

    public void addTermToDoc(String term){
        if(termsInDoc.containsKey(term)){
            termsInDoc.get(term).incAmounts(this.doc_num);
        }
        else {
            Term nTerm = new Term(term);
            this.termsInDoc.put(term, nTerm);
            nTerm.incAmounts(this.doc_num);
        }
    }

    public boolean contains(String term){
        return termsInDoc.containsKey(term);
    }

    public void removeFromDoc(String term){
        termsInDoc.remove(term);
    }
}
