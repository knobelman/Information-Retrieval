package Model.DataObjects.ParseableObjects;
import Model.DataObjects.Term;
import java.io.Serializable;
import java.util.HashMap;

/**
 * This class represents a document
 * doc_num - for example "FBSI-3"
 * doc_content - the the inside <text> </text>
 * path - the path of file the document exists in
 * max_tf - the tf of the most frequent term in the document
 * specialWordCount - number of special words
 * city - city the doc come from
 * termsInDoc - all the terms exists in current document
 */
public class Doc extends ParseableObject implements Serializable {
    private String doc_num;
    private String doc_content;
    private String path;
    private int max_tf;
    private int specialWordCount;
    private String city;
    private HashMap<String,Term> termsInDoc;
    private String mostFrequentTerm; //extra
    private String max_tf_String;
    private String language;


    /**
     * C'tor
     * initialize termsInDoc data structure
     */
    public Doc(){
        this.termsInDoc = new HashMap<>();
    }
    /**
     * C'tor
     * @param path
     * @param doc_num
     * @param doc_content
     */


    /**
     * C'tor with arguments
     */
    public Doc(String path, String doc_num, String doc_content, String city,String language) {
        this.doc_num = doc_num;
        this.language = language;
        this.doc_content = doc_content;
        this.city = city;
        this.path = path;
        this.termsInDoc = new HashMap<>();
        this.max_tf = 0;
        this.specialWordCount = 0;
        this.max_tf_String = "";
    }

    /**
     * Copy C'tor
     * @param path
     * @param city
     * @param max_tf
     * @param SpecialWordCount
     */
    public Doc(String path, String city, int max_tf, int SpecialWordCount) {
        this.city = city;
        this.path = path;
        this.max_tf = max_tf;
        this.specialWordCount = SpecialWordCount;
    }
    /**
     * Getter
     * @return - doc_num
     */
    public String getDoc_num() {
        return doc_num;
    }


    /**
     * Getter
     * @return - doc content
     */
    public String getDoc_content() {
        return doc_content;
    }


    /**
     * Setter
     * @param doc_content - set doc content
     */
    public void setDoc_content(String doc_content) {
        this.doc_content = doc_content;
    }

    /**
     * Getter
     * @return - all terms in doc
     */
    public HashMap<String,Term> getTermsInDoc() {
        return termsInDoc;
    }


    /**
     * this method add term to doc
     * @param term - to add
     */
    public void addTermToDoc(String term){//todo
        if(termsInDoc.containsKey(term)){//term already exists in this doc
            termsInDoc.get(term).incAmounts(this.doc_num);
        }
        else {//new term for the doc
            specialWordCount++;
            Term nTerm = new Term(term);
            nTerm.incAmounts(this.doc_num);
            this.termsInDoc.put(term, nTerm);
        }
        int currTF = termsInDoc.get(term).getTf(doc_num);//set maxTF for DOC
        if(currTF>max_tf) {
            max_tf = currTF;
            max_tf_String = term;
        }
    }

    /**
     * this method check if terms in doc contains a term
     * @param term
     * @return
     */
    public boolean contains(String term){
        return termsInDoc.containsKey(term);
    }

    /**
     * this method remove term from doc
     * @param term
     */
    public void removeFromDoc(String term){
        termsInDoc.remove(term);
    }

    public int getMax_tf() {
        return max_tf;
    }

    public String getPath() {
        return path;
    }

    public int getSpecialWordCount() {
        return this.getTermsInDoc().size();
    }

    public void setSpecialWordCount() {
        specialWordCount = termsInDoc.size();
    }

    public String getCity() {
        return city;
    }

    public String getLanguage() {
        return language;
    }
}