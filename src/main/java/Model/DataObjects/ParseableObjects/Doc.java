package Model.DataObjects.ParseableObjects;
import Model.DataObjects.Term;
import java.io.Serializable;
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
public class Doc implements IParseableObject,Serializable {
    private String doc_num;
    private String doc_content;
    private String path;
    private int max_tf;
    private int specialWordCount;
    private String city;
    private HashMap<String,Term> termsInDoc;
    private String max_tf_String;
    private String language;
    private HashSet<Integer> positionOfCity;


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
    public Doc(String path, String doc_num, String doc_content, String city, HashSet positionOfCity, String language) {
        this.doc_num = doc_num;
        this.language = language;
        this.doc_content = doc_content;
        this.city = city;
        this.path = path;
        this.termsInDoc = new HashMap<>();
        this.max_tf = 0;
        this.specialWordCount = 0;
        this.positionOfCity = new HashSet<>();
        this.positionOfCity = positionOfCity;
        this.max_tf_String = "";
    }

    /**
     * Copy C'tor
     * @param path
     * @param city
     * @param max_tf
     * @param SpecialWordCount
     */
    public Doc(String path, String city, int max_tf, int SpecialWordCount,String max_tf_String) {
        this.city = city;
        this.path = path;
        this.max_tf = max_tf;
        this.specialWordCount = SpecialWordCount;
        this.max_tf_String = max_tf_String;
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

    public HashSet<Integer> getPositionOfCity() {
        return positionOfCity;
    }

    /**
     * this method add term to doc
     * @param term - to add
     */
    public void addTermToDoc(String term){//todo
        Term currentTerm;
        if(Character.isLowerCase(term.charAt(0))) {
            term = term.toLowerCase();
        }
        else if(Character.isUpperCase(term.charAt(0))){
            term = term.toUpperCase();
        }

        if(termsInDoc.containsKey(term)){//term already exists in this doc
            currentTerm = termsInDoc.get(term);
            termsInDoc.get(term).incAmounts(this.doc_num);
        }
        else {//it's not in the doc in it's from
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
     * Getter
     * @return the max tf
     */
    public int getMax_tf() {
        return max_tf;
    }

    /**
     * Getter
     * @return the path of the file
     */
    public String getPath() {
        return path;
    }

    /**
     * Getter
     * @return the special word count
     */
    public int getSpecialWordCount() {
        return this.getTermsInDoc().size();
    }

    /**
     * Getter
     * @return the city name
     */
    public String getCity() {
        return city;
    }

    /**
     * Getter
     * @return the language name
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Getter
     * @return the most frequency string
     */
    public String getMax_tf_String() {
        return max_tf_String;
    }
}