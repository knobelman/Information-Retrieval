package Model;

import javafx.util.Pair;

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
    private HashMap<String, HashMap<String, Integer>> TermAndDocumentsData = new LinkedHashMap<>();
    private HashMap<String, Pair<Integer, Integer>> Dictionary; //term,totalTF,position in merged posting file
    private HashMap<Character, String> letters; // every letter and the name of the file
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
        letters.put('a', "ABCD");
        letters.put('b', "ABCD");
        letters.put('c', "ABCD");
        letters.put('d', "ABCD");
        letters.put('e', "EFGH");
        letters.put('f', "EFGH");
        letters.put('g', "EFGH");
        letters.put('h', "EFGH");
        letters.put('i', "IJKL");
        letters.put('j', "IJKL");
        letters.put('k', "IJKL");
        letters.put('l', "IJKL");
        letters.put('m', "MNOP");
        letters.put('n', "MNOP");
        letters.put('o', "MNOP");
        letters.put('p', "MNOP");
        letters.put('q', "QRST");
        letters.put('r', "QRST");
        letters.put('s', "QRST");
        letters.put('t', "QRST");
        letters.put('u', "UVWXYZ");
        letters.put('v', "UVWXYZ");
        letters.put('w', "UVWXYZ");
        letters.put('x', "UVWXYZ");
        letters.put('y', "UVWXYZ");
        letters.put('z', "UVWXYZ");


    }

    /**
     * this function initialize index
     * send all the docs to parsing
     * insert to TermAndDocumentsData structure and to the dictionary if need to update
     *
     * @param file  - entry file
     * @param stemm - boolean field indicates stemm or not
     *              this function send date to posting class which create the posting files
     */
    public void init(final File file, boolean stemm) throws IOException {
        for (final File fileEntry : file.listFiles()) {
            if (fileEntry.isDirectory()) {
                init(fileEntry, stemm);
            } else {
                DocumentsToParse = readFileObject.fromFileToDoc(fileEntry);
                for (Doc d : DocumentsToParse) {
                    ParserObject.parsing(d, stemm);
                    for (Map.Entry<String, Term> entry : d.getTermsInDoc().entrySet()) {
                        String termName = entry.getKey();
                        Term value = entry.getValue();
                        if(!Dictionary.containsKey(termName)){
                            Dictionary.put(termName,new Pair<>(1,0)); //term name, file name, position
                        }
                        //addToDic(termName);
                        String doc_name = d.getDoc_num();
                        if (TermAndDocumentsData.containsKey(termName)) {
                            Integer newint = new Integer(d.getTermsInDoc().get(termName).getTf(doc_name));
                            //int df = d.getTermsInDoc().get(termname).getDf();
                            TermAndDocumentsData.get(termName).put(d.getDoc_num(), newint);
                        } else {
                            HashMap<String, Integer> current = new HashMap();
                            current.put(doc_name, new Integer(value.getTf(doc_name)));
                            TermAndDocumentsData.put(termName, current);
                        }
                    }
                }
                if (!this.TermAndDocumentsData.isEmpty()) {
                    Thread t = new Thread(() -> {
                        postingObject.createPostingFile(TermAndDocumentsData);
                        TermAndDocumentsData = new LinkedHashMap<>();
                    });
                    t.start();
                    threadList.add(t);
                }
                for (Thread t : threadList) {
                    try {
                        t.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void splitFinalPosting() {
        HashMap<String, BufferedWriter> fileWriters = new HashMap<>();//hashmap for Filewriters
        HashMap<String, Integer> filePosition = new HashMap<>();//hashmap for Filewriters
        try {
            fileWriters.put("ABCD", new BufferedWriter(new FileWriter(postingObject.getRootPath() + "\\ABCD")));
            fileWriters.put("EFGH", new BufferedWriter(new FileWriter(postingObject.getRootPath() + "\\EFGH")));
            fileWriters.put("IJKL", new BufferedWriter(new FileWriter(postingObject.getRootPath() + "\\IJKL")));
            fileWriters.put("MNOP", new BufferedWriter(new FileWriter(postingObject.getRootPath() + "\\MNOP")));
            fileWriters.put("QRST", new BufferedWriter(new FileWriter(postingObject.getRootPath() + "\\QRST")));
            fileWriters.put("UVWXYZ", new BufferedWriter(new FileWriter(postingObject.getRootPath() + "\\UVWXYZ")));
            fileWriters.put("OTHER", new BufferedWriter(new FileWriter(postingObject.getRootPath() + "\\OTHER")));
            filePosition.put("ABCD", new Integer(0));
            filePosition.put("EFGH", new Integer(0));
            filePosition.put("IJKL", new Integer(0));
            filePosition.put("MNOP", new Integer(0));
            filePosition.put("QRST", new Integer(0));
            filePosition.put("UVWXYZ", new Integer(0));
            filePosition.put("OTHER", new Integer(0));
        } catch (Exception e) {
        }
        BufferedReader postingFile;
        BufferedWriter fileBuffer;
        String currTerm, line, fileName;
        int position;
        try {
            postingFile = new BufferedReader(new FileReader(postingObject.getRootPath() + "\\0"));//read posting
            line = postingFile.readLine();
            do {
                if (!letters.containsKey(line.charAt(0)) && !Character.isUpperCase(line.charAt(0))) {//if first char isn't a known letter
                    fileBuffer = fileWriters.get("OTHER");//get hte buffer to write
                    position = filePosition.get("OTHER").intValue();//get position to update Dic
                    fileName = "OTHER";//get name of file to update Dic
                } else {
                    char tmp = Character.toLowerCase(line.charAt(0));
                    fileBuffer = fileWriters.get(letters.get(tmp));
                    position = filePosition.get(letters.get(tmp)).intValue();
                    fileName = letters.get(tmp);
                }
                currTerm = line.substring(0, line.indexOf('|'));//get the term
                Pair tmpPair = new Pair<>(Dictionary.get(currTerm).getKey(), position);//create tmppair to insert into Dic
                Dictionary.replace(currTerm, tmpPair);//change the position for the term in the Dic
                fileBuffer.write(line + "\n");
                position += line.length() + 1;//increase the position for next line
                filePosition.replace(fileName, position);//insert new position for next line
                line = postingFile.readLine();
            } while (line != null);
            File pFile = new File(postingObject.getRootPath() + "\\0");
            pFile.delete();
            fileWriters.get("ABCD").close();
            fileWriters.get("EFGH").close();
            fileWriters.get("IJKL").close();
            fileWriters.get("MNOP").close();
            fileWriters.get("QRST").close();
            fileWriters.get("UVWXYZ").close();
            fileWriters.get("OTHER").close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createEvenPostingFiles(int currPostingNumber) {
        int lastPosting = currPostingNumber - 1;
        FileWriter mergedWriter;
        BufferedWriter mergedBuffer;
        File lFILE = new File(postingObject.getRootPath() + "\\" + lastPosting);
        File blFILE = new File(postingObject.getRootPath() + "\\" + (lastPosting - 1));
        try {
            mergedWriter = new FileWriter(postingObject.getRootPath() + "\\" + "merged");
            mergedBuffer = new BufferedWriter(mergedWriter);
            postingObject.mergeBetweenPostFiles("" + (lastPosting), "" + (lastPosting - 1), mergedBuffer);
            mergedBuffer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        lFILE.delete();
        blFILE.delete();
        File mergedF = new File(postingObject.getRootPath() + "\\" + "merged");
        mergedF.renameTo(blFILE);
    }

    public void createFinalPosting() {
        int postingCounter = this.postingObject.getPostingFilecounter();
        int newName = 0;
        while (postingCounter >= 2) {
            if (!(postingCounter % 2 == 0)) {
                createEvenPostingFiles(postingCounter);
                postingCounter--;
            }
            int currPostingCounter = postingCounter;
            for (int i = 0; i < currPostingCounter; i += 2) {
                FileWriter mergedWriter;
                BufferedWriter mergedBuffer;
                File lFILE = new File(postingObject.getRootPath() + "\\" + i);
                File blFILE = new File(postingObject.getRootPath() + "\\" + (i + 1));
                try {
                    mergedWriter = new FileWriter(postingObject.getRootPath() + "\\" + "merged");
                    mergedBuffer = new BufferedWriter(mergedWriter);
                    postingObject.mergeBetweenPostFiles("" + (i), "" + (i + 1), mergedBuffer);
                    mergedBuffer.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                lFILE.delete();
                blFILE.delete();
                postingCounter--;
                File mergedF = new File(postingObject.getRootPath() + "\\" + "merged");
                File newFile = new File(postingObject.getRootPath() + "\\" + newName);
                mergedF.renameTo(newFile);
                newName++;
            }
            newName = 0;
        }
    }

    /**
     * this method adds the words Upper\Lower like needed
     *
     * @param currValue - Value to add to terms
     */
    private void addToDic(String currValue) {
        Pair<Integer, Integer> tmpPair = new Pair<>(1, 0);
        if (Dictionary.containsKey(currValue.toUpperCase())) { //if Dic contains Upper case of current word
            if (currValue.toLowerCase().equals(currValue))//if current word is Lower case "yaniv"
                Dictionary.remove(currValue.toUpperCase());
            tmpPair = new Pair<>(Dictionary.get(currValue).getKey().intValue() + 1, 0);
        } else if (Dictionary.containsKey(currValue.toLowerCase())) {//if Dic contains Lower case of current word
            if (currValue.toUpperCase().equals(currValue)) //if current word is Upper case "YANIV"
                currValue = currValue.toLowerCase();
            tmpPair = new Pair<>(Dictionary.get(currValue).getKey().intValue() + 1, 0);
        }
        Dictionary.put(currValue, tmpPair); //term name, file name, position
    }

    /**
     * this method set posting file path
     *
     * @param path - to set
     */
    public void setPostingFilePath(String path) {
        this.postingObject = new Posting(path);
    }

    /**
     * this method get posting file path
     *
     * @return
     */
    public String getPostingFilePath() {
        return this.postingObject.getRootPath();
    }


    /**
     * this method set corpus file path
     *
     * @param path - to set
     */
    public void setCorpusFilePath(String path) {
        this.rootPath = path;
        this.ParserObject = new Parse(rootPath);
    }

    /**
     * Setter
     *
     * @param flag - set stemming to false or true
     */
    public void setStemming(boolean flag) {
        this.toStem = flag;
    }

    /**
     * Getter
     *
     * @return read file object
     */
    public File getReadFileObject() {
        return readFileObject.getRoot();
    }

    /**
     * Getter
     *
     * @return - if stem required ot not
     */
    public Boolean getToStemm() {
        return toStem;
    }

    /**
     * Setter
     *
     * @param readFileObject - set read file object
     */
    public static void setReadFileObject(ReadFile readFileObject) {
        Indexer.readFileObject = readFileObject;
    }

    /**
     * Getter
     *
     * @return the root path
     */
    public String getRootPath() {
        return rootPath;
    }

    /**
     * Setter
     *
     * @param postingObject - to set
     */
    public void setPostingObject(Posting postingObject) {
        this.postingObject = postingObject;
    }
}



