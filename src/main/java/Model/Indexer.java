package Model;
import java.io.*;
import java.util.*;

/**
 * Indexer class which create index for all the files
 */
public class Indexer {
    private String rootPath;
    private static ReadFile readFileObject;
    private HashSet<Doc> DocumentsToParse;
    private ArrayList<Thread> DocsThread;
    private Posting postingObject;
    private HashMap<String,HashMap<String,Integer>> TermAndDocumentsData = new LinkedHashMap<>();
    private HashMap<String,Term> Dictionary;
    private Boolean toStemm;
    private Parse Parser;

    public Indexer() {
        this.DocumentsToParse = new HashSet<>();
        this.DocsThread = new ArrayList<>();
        this.Dictionary = new HashMap<>();
//        try {
//            init(readFileObject.getRoot(),stemm);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
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
                        String termname = entry.getKey();
                        Term value = entry.getValue();
                        String doc_name = d.getDoc_num();
                        if(termname.equals("")){
                            continue;
                        }
                        if(TermAndDocumentsData.containsKey(termname)){
                            Integer newint =  new Integer(d.getTermsInDoc().get(termname).getTf(doc_name));
                            int df = d.getTermsInDoc().get(termname).getDf();
                            Dictionary.replace(termname,value); //todo - new line to check
                            TermAndDocumentsData.get(termname).put(d.getDoc_num(),newint);
                        }else {
                            Dictionary.put(termname,value); //todo - new line to check
                            HashMap<String, Integer> current = new HashMap();
                            current.put(doc_name, new Integer(value.getTf(doc_name)));
                            TermAndDocumentsData.put(termname, current);
                        }
                    }
                }
                postingObject.createPostingFile(this.TermAndDocumentsData,this.Dictionary);
                TermAndDocumentsData = new LinkedHashMap<>();
            }
        }
    }


    public void setPostingFilePath(String path){
        this.postingObject = new Posting(path);
    }

    public void setCorpusFilePath(String path){
        this.rootPath = path;
        this.Parser = new Parse(rootPath);
    }

    public void setStemming(boolean flag){
        this.toStemm = flag;
    }

    public File getReadFileObject() {
        return readFileObject.getRoot();
    }

    public Boolean getToStemm() {
        return toStemm;
    }

    public static void setReadFileObject(ReadFile readFileObject) {
        Indexer.readFileObject = readFileObject;
}
    public String getRootPath() {
        return rootPath;
    }

    public void setPostingObject(Posting postingObject) {
        this.postingObject = postingObject;
    }
}



