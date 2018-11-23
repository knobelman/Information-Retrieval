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
    private HashMap<String,Integer> Dictionary; //term and position in posting file
    private HashMap<Character,String> letters; // every letter and the name of the file
    private HashSet<String> filesExist;
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
        this.letters = new HashMap<>();
        this.filesExist = new HashSet<>();
        letters.put('a',"ABCD"); letters.put('b',"ABCD"); letters.put('c',"ABCD"); letters.put('d',"ABCD");
        letters.put('e',"EFGH"); letters.put('f',"EFGH"); letters.put('g',"EFGH"); letters.put('h',"EFGH");
        letters.put('i',"IJKL"); letters.put('j',"IJKL"); letters.put('k',"IJKL"); letters.put('l',"IJKL");
        letters.put('m',"MNOP"); letters.put('n',"MNOP"); letters.put('o',"MNOP"); letters.put('p',"MNOP");
        letters.put('q',"QRST"); letters.put('r',"QRST"); letters.put('s',"QRST"); letters.put('t',"QRST");
        letters.put('u',"UVWXYZ"); letters.put('v',"UVWXYZ"); letters.put('w',"UVWXYZ"); letters.put('x',"UVWXYZ");
        letters.put('y',"UVWXYZ"); letters.put('z',"UVWXYZ");


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
                        if(!Dictionary.containsKey(termName)){
                            Dictionary.put(termName,0); //term name, file name, position
                        }
                        String doc_name = d.getDoc_num();
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

    public void splitFinalPosting(){
        HashMap<String,BufferedWriter> fileWriters = new HashMap<>();//hashmap for Filewriters
        try {
            fileWriters.put("ABCD",new BufferedWriter(new FileWriter(postingObject.getRootPath() + "\\ABCD",true)));
            fileWriters.put("EFGH",new BufferedWriter(new FileWriter(postingObject.getRootPath() + "\\EFGH",true)));
            fileWriters.put("IJKL",new BufferedWriter(new FileWriter(postingObject.getRootPath() + "\\IJKL",true)));
            fileWriters.put("MNOP",new BufferedWriter(new FileWriter(postingObject.getRootPath() + "\\MNOP",true)));
            fileWriters.put("QRST",new BufferedWriter(new FileWriter(postingObject.getRootPath() + "\\QRST",true)));
            fileWriters.put("UVWXYZ",new BufferedWriter(new FileWriter(postingObject.getRootPath() + "\\UVWXYZ",true)));
            fileWriters.put("OTHER",new BufferedWriter(new FileWriter(postingObject.getRootPath() + "\\OTHER",true)));
        }catch(Exception e){
        }
        BufferedReader postingFile;
        BufferedWriter fileBuffer=null;
        String line;
        boolean newLetter = true;//if true- new buffer is needed
        try {
            postingFile = new BufferedReader(new FileReader(postingObject.getRootPath()+"\\0"));//read posting
            line = postingFile.readLine();
            do{
                if(!letters.containsKey(line.charAt(0))) {//if first char isn't a known letter
                    fileBuffer = fileWriters.get("OTHER");
                    //newLetter = false;
                }
                else{// if(newLetter){
                    fileBuffer = fileWriters.get(letters.get(line.charAt(0)));
                }
                fileBuffer.write(line+"\n");
                line = postingFile.readLine();
            }while(line!=null);
            fileBuffer.close();
        } catch (Exception e) {
            e.printStackTrace();
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



