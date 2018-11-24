package Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

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
public class Doc implements Serializable {
    private String doc_num;
    private String doc_content;
    private String path;
    private int max_tf;
    private int specialWordCount;
    private String city;
    private HashMap<String,Term> termsInDoc;
    private String mostFrequentTerm; //extra
    private String max_tf_String;


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
    public Doc(String path, String doc_num, String doc_content, String city) {
        this.doc_num = doc_num;
        this.doc_content = doc_content;
        this.city = city;
        this.path = path;
        this.termsInDoc = new HashMap<>();
        this.max_tf = 0;
        this.specialWordCount = 0;
        this.max_tf_String = "";
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
    public HashMap<String, Term> getTermsInDoc() {
        return termsInDoc;
    }


    /**
     * this method add term to doc
     * @param term - to add
     */
    public void addTermToDoc(String term){//todo
        Term currentTerm = null;
        if(termsInDoc.containsKey(term)){//term already exists in this doc
            currentTerm = termsInDoc.get(term);
            termsInDoc.get(term).incAmounts(this.doc_num);
        }
        else {//it's not in the doc in it's form
            //termsInDoc contains Upper case of current word and current word is lower case
            if(termsInDoc.containsKey(term.toUpperCase())) {
                changeUL(term);
                currentTerm = termsInDoc.get(term);
                termsInDoc.get(term).incAmounts(this.doc_num);
            }
            //termsInDoc contains Lower case of current word and current word is Upper case
            else if(termsInDoc.containsKey(term.toLowerCase())) {
                currentTerm = termsInDoc.get(term.toLowerCase());
                termsInDoc.get(term.toLowerCase()).incAmounts(this.doc_num);
            }
            else {//new term for the doc
                specialWordCount++;
                currentTerm = new Term(term);
                currentTerm.incAmounts(this.doc_num);
                this.termsInDoc.put(term, currentTerm);
            }
        }
        int currTF = currentTerm.getTf(doc_num);//set maxTF for DOC
        if(currTF>max_tf) {
            max_tf = currTF;
            max_tf_String = term;
        }
    }

//    public void changeUL(Term term){
//        changeUL(term.getTermName());
//    }
//
    /**
     * Changes terms from Upper case to Lower case
     * @param term - term name to change to
     */
    private void changeUL(String term) {
        Term tmpTerm = termsInDoc.get(term.toUpperCase());
        termsInDoc.remove(term.toUpperCase());
        tmpTerm.setTerm(term);
        termsInDoc.put(term,tmpTerm);
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

    public void setSpecialWordCount() {
        specialWordCount = termsInDoc.size();
        //System.out.println(specialWordCount + " : " + doc_num);
        //System.out.println(city);
    }
}