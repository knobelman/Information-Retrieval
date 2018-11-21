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
    private Parse ParserObject;
    private HashMap<String,HashMap<String,Integer>> TermAndDocumentsData = new LinkedHashMap<>();
    private HashMap<String,String> Dictionary; //term and paths to posting files
    private List<Thread> threadList;
   private Boolean toStem;

    /**
     * C'tor
     * initialize DocumentsToParse HashSet and Dictionary HashMap
     */
    public Indexer() {
        this.DocumentsToParse = new HashSet<>();
        this.Dictionary = new HashMap<>();
        this.threadList = new ArrayList<>();
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
                    ParserObject.parsing(d,stemm);
                    for(Map.Entry<String,Term> entry : d.getTermsInDoc().entrySet()) {
                        String termName = entry.getKey();
                        Term value = entry.getValue();
                        if(!Dictionary.containsKey(termName) && !termName.equals("")){
                            Dictionary.put(termName,Character.toUpperCase(termName.charAt(0)) +""); //todo - continue
                        }
                        String doc_name = d.getDoc_num();
                        if(termName.equals("")){
                            continue;
                        }
                        if(TermAndDocumentsData.containsKey(termName)){
                            Integer newint =  new Integer(d.getTermsInDoc().get(termName).getTf(doc_name));
                            //int df = d.getTermsInDoc().get(termname).getDf();
                            TermAndDocumentsData.get(termName).put(d.getDoc_num(),newint);
                        }else {
                            HashMap<String, Integer> current = new HashMap();
                            current.put(doc_name, new Integer(value.getTf(doc_name)));
                            TermAndDocumentsData.put(termName, current);
                        }
                    }
                }
                if(!this.TermAndDocumentsData.isEmpty()) {
                    Thread t = new Thread(() -> {
                        postingObject.createPostingFile(TermAndDocumentsData);
                        TermAndDocumentsData = new LinkedHashMap<>();
                    });
                    t.start();
                    threadList.add(t);
                }
                for(Thread t: threadList){
                    try {
                        t.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void createEvenPostingFiles(int currPostingNumber){
        int lastPosting = currPostingNumber-1;
        FileWriter mergedWriter;
        BufferedWriter mergedBuffer;
        File lFILE = new File(postingObject.getRootPath() + "\\" + lastPosting);
        File blFILE = new File(postingObject.getRootPath() + "\\" + (lastPosting-1));
        try {
            mergedWriter = new FileWriter(postingObject.getRootPath() + "\\" + "merged");
            mergedBuffer = new BufferedWriter(mergedWriter);
            postingObject.mergeBetweenPostFiles("" + (lastPosting), "" + (lastPosting-1), mergedBuffer);
            mergedBuffer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        lFILE.delete();
        blFILE.delete();
        File mergedF = new File(postingObject.getRootPath()+"\\"+"merged");
        mergedF.renameTo(blFILE);
    }

    public void createFinalPosting(){
        int postingCounter = this.postingObject.getPostingFilecounter();
        int newName = 0;
        while(postingCounter>=2) {
            if (!(postingCounter % 2 == 0)) {
                createEvenPostingFiles(postingCounter);
                postingCounter--;
            }
            int currPostingCounter = postingCounter;
            for (int i = 0; i < currPostingCounter; i+=2) {
                FileWriter mergedWriter;
                BufferedWriter mergedBuffer;
                File lFILE = new File(postingObject.getRootPath() + "\\" + i);
                File blFILE = new File(postingObject.getRootPath() + "\\" + (i+1));
                try {
                    mergedWriter = new FileWriter(postingObject.getRootPath() + "\\" + "merged");
                    mergedBuffer = new BufferedWriter(mergedWriter);
                    postingObject.mergeBetweenPostFiles("" + (i), "" + (i+1), mergedBuffer);
                    mergedBuffer.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                lFILE.delete();
                blFILE.delete();
                postingCounter--;
                File mergedF = new File(postingObject.getRootPath()+"\\"+"merged");
                File newFile = new File(postingObject.getRootPath()+"\\"+ newName);
                mergedF.renameTo(newFile);
                newName++;
            }
            newName = 0;
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
        this.ParserObject = new Parse(rootPath);
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



