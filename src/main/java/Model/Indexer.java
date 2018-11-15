package Model;
import java.io.*;
import java.util.*;

/**
 * This class represents the Indexer class
 * Indexer class create index for all the files
 * rootPath - path of the corpus
 * readFileObject - read document content using ReadFile class
 * DocumentsToParse - save a list of document to parse
 * postingObject - a posting class object
 * TermAndDocumentsData - save date in the given format <<String,HashMap<String,Integer>> "moshe | FBIS-3 - 5"
 * Dictionary
 * toStem - boolean field indicates stemming or not
 * parser - parser class object which parsing the document content
 */
public class Indexer {
    private String rootPath;
    private static ReadFile readFileObject;
    private HashSet<Doc> DocumentsToParse;
    private Posting postingObject;
    private HashMap<String,HashMap<String,Integer>> TermAndDocumentsData = new LinkedHashMap<>();
    private HashMap<String,Term> Dictionary;
    private Boolean toStem;
    private Parse Parser;

    /**
     * C'tor
     * initialize DocumentsToParse HashSet and Dictionary HashMap
     */
    public Indexer() {
        this.DocumentsToParse = new HashSet<>();
        this.Dictionary = new HashMap<>();
    }

    /**
     * this function initialize index
     * send all the docs to parsing
     * insert to TermAndDocumentsData structure and to the dictionary if need to update
     * @param file - entry file
     * @param stemm - boolean field indicates stemm or not
     * this function send date to posting class which create the posting files
     */
    public void init(final File file, boolean stemm) throws IOException {
        for (final File fileEntry : file.listFiles()) {
            if (fileEntry.isDirectory()) {
                init(fileEntry, stemm);
            } else {
                DocumentsToParse = readFileObject.fromFileToDoc(fileEntry);
                for (Doc d : DocumentsToParse) {
                    Parser.parsing(d,stemm);
                    for(Map.Entry<String,Term> entry : d.getTermsInDoc().entrySet()) {
                        String termName = entry.getKey();
                        Term value = entry.getValue();
                        String doc_name = d.getDoc_num();
                        if(termName.equals("")){
                            continue;
                        }
                        if(TermAndDocumentsData.containsKey(termName)){
                            Integer newint =  new Integer(d.getTermsInDoc().get(termName).getTf(doc_name));
                            //int df = d.getTermsInDoc().get(termname).getDf();
                            Dictionary.replace(termName,value); //todo - new line to check
                            TermAndDocumentsData.get(termName).put(d.getDoc_num(),newint);
                        }else {
                            Dictionary.put(termName,value); //todo - new line to check
                            HashMap<String, Integer> current = new HashMap();
                            current.put(doc_name, new Integer(value.getTf(doc_name)));
                            TermAndDocumentsData.put(termName, current);
                        }
                    }
                }
                postingObject.createPostingFile(this.TermAndDocumentsData,this.Dictionary);
                TermAndDocumentsData = new LinkedHashMap<>();
            }
        }
    }

    /**
     * create the final posting file
     * merge all the even files
     * merge all the odd files
     */
    public void createFinalPosting(){
        for(int i=postingObject.getPostingFilecounter()-2;i>=2;i=i-4){
            postingObject.mergeBetweenPostFiles(""+ (i),""+(i-2),postingObject.getFirstHalfWriter());
        }
        try {
            postingObject.getFirstHalfWriter().close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(int i=postingObject.getPostingFilecounter()-3;i>=2;i = i-2){
            postingObject.mergeBetweenPostFiles(""+ (i),""+(i-2),postingObject.getSecondHalfWriter());
        }
        try {
            postingObject.getSecondHalfWriter().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * this method set posting file path
     * @param path - to set
     */
    public void setPostingFilePath(String path){
        this.postingObject = new Posting(path);
    }

    /**
     * this method get posting file path
     * @return
     */
    public String getPostingFilePath() {
        return this.postingObject.getRootPath();
    }


    /**
     * this method set corpus file path
     * @param path - to set
     */
    public void setCorpusFilePath(String path){
        this.rootPath = path;
        this.Parser = new Parse(rootPath);
    }

    /**
     * Setter
     * @param flag - set stemming to false or true
     */
    public void setStemming(boolean flag){
        this.toStem = flag;
    }

    /**
     * Getter
     * @return read file object
     */
    public File getReadFileObject() {
        return readFileObject.getRoot();
    }

    /**
     * Getter
     * @return - if stem required ot not
     */
    public Boolean getToStemm() {
        return toStem;
    }

    /**
     * Setter
     * @param readFileObject - set read file object
     */
    public static void setReadFileObject(ReadFile readFileObject) {
        Indexer.readFileObject = readFileObject;
}

    /**
     * Getter
     * @return the root path
     */
    public String getRootPath() {
        return rootPath;
    }

    /**
     * Setter
     * @param postingObject - to set
     */
    public void setPostingObject(Posting postingObject) {
        this.postingObject = postingObject;
    }
}



