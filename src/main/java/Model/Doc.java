package Model;

import java.io.Serializable;

/**
 * This class represents a document
 */
public class Doc implements Serializable {
    private String doc_num;
    private String doc_content;
    private int max_tf;
    private int specialCounter;
    private String city;

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

    public int getSpecialCounter() {
        return specialCounter;
    }

    public void setSpecialCounter(int specialCounter) {
        this.specialCounter = specialCounter;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
