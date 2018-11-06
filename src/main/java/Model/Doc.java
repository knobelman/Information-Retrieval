package Model;

import org.apache.lucene.index.IndexableField;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

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
    private ArrayList<Term> termsInDoc;
    private String mostFrequentTerm; //extra

    public Doc(){
        this.termsInDoc = new ArrayList<>();
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
        this.termsInDoc = new ArrayList<>();
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

    public Iterator<IndexableField> iterator() {
        return null;
    }

    public ArrayList<Term> getTermsInDoc() {
        return termsInDoc;
    }

    public void addTermToDoc(String term){
        this.termsInDoc.add(new Term(term));
    }

    public void setTermsInDoc(ArrayList<Term> termsInDoc) {
        this.termsInDoc = termsInDoc;
    }
}
