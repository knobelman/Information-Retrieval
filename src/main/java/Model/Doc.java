package Model;

import java.io.Serializable;

/**
 * This class represents a document
 */
public class Doc implements Serializable {
    private String doc_num;
    private String doc_content;
    private int max_tf;
    private int specialWordCount;
    private String city;
    private String mostFrequentTerm; //extra

    /**
     * C'tor
     * @param doc_num
     * @param doc_content
     */
    public Doc(String doc_num, String doc_content,String city) {
        this.doc_num = doc_num;
        this.doc_content = doc_content;
        this.city = city;
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
}
