package Model.Indexers;

import Model.DataObjects.CityData;
import Model.DataObjects.ParseableObjects.Doc;
import Model.DataObjects.Term;
import Model.Parsers.ParsingProcess.DocParsingProcess;
import Model.Parsers.ParsingProcess.CityParsingProcess;
import Model.Parsers.ParsingProcess.IParsingProcess;
import javafx.util.Pair;

import java.io.*;
import java.util.*;

/**
 * This class represents the Indexer class
 * Indexer class create index for all the files
 *
 * @rootPath - path of the corpus
 * @readFileObject - read document content using ReadFile class
 * @DocumentsToParse - save a list of document to parse
 * @postingObject - a posting class object
 * @TermAndDocumentsData - save date in the given format <<String,HashMap<String,Integer>> "moshe | FBIS-3 - 5"
 * @toStem - boolean field indicates stemming or not
 * @parser - parser class object which parsing the document content
 */
public class Indexer {

    private String rootPath;

    private static ReadFile readFileObject;
    private Posting postingObject;
    private DocParsingProcess ParserObject;
    private IParsingProcess cityParsingProcess;

    private HashMap<String, Pair<ArrayList<String>, CityData>> cityDictionary;
    private HashMap<String, Pair<Integer, Integer>> corpusDictionary; //term,totalTF,position in merged posting file
    private HashMap<String, Doc> DocumentDictionary;
    private HashSet<String> LanguageCollection;

    private HashSet<Doc> DocumentsToParse;
    private HashMap<String, HashMap<String, Integer>> TermAndDocumentsData = new LinkedHashMap<>();
    private HashMap<Character, String> letters; // every letter and the name of the file
    private List<Thread> threadList;
    private Boolean toStem;

    private int numberofDocs;

    /**
     * C'tor
     * initialize DocumentsToParse HashSet and Dictionary HashMap
     */
    public Indexer() {
    }

    public Indexer(String rootPath, boolean toStem) {
        this.rootPath = rootPath;
        this.DocumentsToParse = new HashSet<>();

        this.corpusDictionary = new HashMap<>();
        this.DocumentDictionary = new HashMap<>();
        this.cityDictionary = new HashMap<>();
        this.LanguageCollection = new HashSet<>();

        this.threadList = new ArrayList<>();
        this.letters = new HashMap<>();
        this.cityParsingProcess = new CityParsingProcess();
        this.ParserObject = new DocParsingProcess(rootPath, toStem);

        this.numberofDocs = 0;
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
     * @param file - entry file
     *             this function send date to posting class which create the posting files
     */
    public void init(final File file) throws IOException {
        for (final File fileEntry : file.listFiles()) {
            if (fileEntry.isDirectory()) {
                init(fileEntry);
            } else {
                DocumentsToParse = readFileObject.fromFileToDoc(fileEntry);
                for (Doc d : DocumentsToParse) {
                    numberofDocs++;
                    //create document dictionary
                    LanguageCollection.add(d.getLanguage()); //add language to language collection

                    //add to city dictionary
                    if (!d.getCity().equals("")) {
                        addToCityDictionary(d);
                    }
                    //parsing
                    ParserObject.parsing(d);
                    Doc toInsert = new Doc(d.getPath(), d.getCity(), d.getMax_tf(), d.getSpecialWordCount()); //doc to insert to the dictionary
                    DocumentDictionary.put(d.getDoc_num(), toInsert); //insert to dictionary (Doc name | Doc object)
                    for (Map.Entry<String, Term> entry : d.getTermsInDoc().entrySet()) {
                        String termName = entry.getKey();
                        String termNameTmp = termName;
                        Term value = entry.getValue();
                        if (corpusDictionary.containsKey(termName)) {//Dic contains the term
                            updateDF(termName);
                        } else if (corpusDictionary.containsKey(termName.toLowerCase())) {//if Dic has lowercase of this word
                            termName = termName.toLowerCase();
                            value.setTerm(termName);
                            updateDF(termName);
                        } else if (corpusDictionary.containsKey(termName.toUpperCase())) {
                            changeUL(termName);
                            updateDF(termName);
                        } else {
                            corpusDictionary.put(termName, new Pair<>(1, 0)); //term name, file name, position
                        }
                        String doc_name = d.getDoc_num();
                        if (TermAndDocumentsData.containsKey(termName)) {
                            Integer newInt = new Integer(d.getTermsInDoc().get(termNameTmp).getTf(doc_name));
                            TermAndDocumentsData.get(termName).put(d.getDoc_num(), newInt);
                        } else {
                            HashMap<String, Integer> current = new HashMap();
                            current.put(doc_name, new Integer(value.getTf(doc_name)));
                            TermAndDocumentsData.put(termName, current);
                        }
                    }
                }
            }
            if (!this.TermAndDocumentsData.isEmpty()) {
                Thread t = new Thread(() -> {
                    postingObject.createTempPostingFile(TermAndDocumentsData);
                    TermAndDocumentsData = new LinkedHashMap<>();
                });
                t.start();
                threadList.add(t);
            }
            while (!threadList.isEmpty()) {
                for (Thread t : threadList) {
                    try {
                        t.join();
                        threadList.remove(t);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void updateDF(String termName) {
        Pair<Integer, Integer> tmp = corpusDictionary.get(termName);
        tmp = new Pair<>(tmp.getKey().intValue() + 1, tmp.getValue());
        corpusDictionary.replace(termName, tmp);
    }


    private void changeUL(String termName) {
        Pair<Integer, Integer> tmpPair = corpusDictionary.get(termName.toUpperCase());
        corpusDictionary.remove(termName.toUpperCase());
        corpusDictionary.put(termName, tmpPair);
    }


    /**
     * This method add city data from document to the city dictionary
     *
     * @param document - current document
     */
    public void addToCityDictionary(Doc document) {
        if (!cityDictionary.containsKey(document.getCity())) { //if city not in the dictionary
            String city = document.getCity(); //get city name from the doc
            String doc_num = document.getDoc_num(); // get doc name from the dock
            String positions_in_doc = document.getPositionOfCity().toString();
            CityData cityData = ((CityParsingProcess) cityParsingProcess).getData(city); //get data about the city
            ArrayList<String> list = new ArrayList<>();
            list.add(doc_num + ":" + positions_in_doc); //add doc name to array list with positions in doc
            Pair<ArrayList<String>, CityData> pair = new Pair<>(list, cityData); //insert data into new pair
            this.cityDictionary.put(city, pair); //insert to dictionary
        } else {
            //if city is in the dictionary
            Pair<ArrayList<String>, CityData> tmp = cityDictionary.get(document.getCity()); //get existing pair
            ArrayList<String> current = tmp.getKey(); //get array list from the pair
            CityData city_data = tmp.getValue(); //get city data from the pair
            current.add(document.getDoc_num() + ":" + document.getPositionOfCity());//add new doc and position in doc to the pair
            Pair<ArrayList<String>, CityData> newPair = new Pair<>(current, city_data); //create new pair with the additional data
            cityDictionary.replace(document.getCity(), newPair); //replace the existing data in the dictionary with the new data
        }
    }

    public void createFinalPosting() {
        postingObject.createFinalPosting();
        try {
            //check in the dictionary the right format (upper / lower)
            BufferedReader last = new BufferedReader(new FileReader(this.getPostingFilePath() + "\\" + "0"));
            String current = last.readLine();
            FileWriter fw = new FileWriter(this.getPostingFilePath() + "\\" + "new");
            while (current != null) {
                String t1 = current.substring(0, current.indexOf('|'));
                String more = current.substring(current.indexOf('|'), current.length());
                if (corpusDictionary.containsKey(t1.toLowerCase())) {
                    current = t1.toLowerCase() + more;
                    fw.write(current + "\n");
                } else {
                    current = t1.toUpperCase() + more;
                    fw.write(current + "\n");
                }
                current = last.readLine();
            }
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
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
            postingFile = new BufferedReader(new FileReader(postingObject.getRootPath() + "\\new"));//read posting
            line = postingFile.readLine();
            do {
                if (!letters.containsKey(line.charAt(0)) && !Character.isUpperCase(line.charAt(0))) {//if first char isn't a known letter
                    fileBuffer = fileWriters.get("OTHER");//get hte buffer to write
                    position = filePosition.get("OTHER").intValue();//get position to update Dic
                    fileName = "OTHER";//get name of file to update Dic
                } else {
                    char tmp = Character.toLowerCase(line.charAt(0));
                    fileBuffer = fileWriters.get(letters.get(tmp));
                    position = filePosition.get(letters.get(tmp)).intValue();//todo
                    fileName = letters.get(tmp);
                }
                currTerm = line.substring(0, line.indexOf('|'));//get the term
                //System.out.println(currTerm+" "+corpusDictionary.containsKey(currTerm));
                Pair tmpPair = new Pair<>(corpusDictionary.get(currTerm).getKey(), position);//create tmppair to insert into Dic
                corpusDictionary.replace(currTerm, tmpPair);//change the position for the term in the Dic
                fileBuffer.write(line + "\n");
                position += line.length() + 1;//increase the position for next line //todo
                filePosition.replace(fileName, position);//insert new position for next line //todo
                line = postingFile.readLine();
            } while (line != null);
            File pFile = new File(postingObject.getRootPath() + "\\" + "0");
            fileWriters.get("ABCD").close();
            fileWriters.get("EFGH").close();
            fileWriters.get("IJKL").close();
            fileWriters.get("MNOP").close();
            fileWriters.get("QRST").close();
            fileWriters.get("UVWXYZ").close();
            fileWriters.get("OTHER").close();
            pFile.delete();
        } catch (Exception e) {
            //e.printStackTrace();
        }
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

    public HashMap<String, Pair<ArrayList<String>, CityData>> getCityDictionary() {
        return cityDictionary;
    }

    public void writeCityDictionaryToDisk() {
        try {
            ArrayList<String> s = new ArrayList();
            Iterator it = this.cityDictionary.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                String cityName = pair.getKey().toString();
                String alist = (((Pair) pair.getValue()).getKey()).toString();
                CityData c = (CityData) ((Pair) pair.getValue()).getValue();
                try {
                    String data = c.getCountryName() + "|" + c.getPopulation() + "|" + c.getCurrency();
                    s.add(cityName + "|" + alist + "|" + data + "\n");
                } catch (Exception e) {
                }
                it.remove();
            }
            s.sort(Comparator.naturalOrder());
            FileWriter fw = new FileWriter(this.getPostingFilePath() + "\\" + "citiesTest");
            for (String a : s) {
                fw.write(a);
            }
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method save the dictionary to the disk
     *
     * @param stem - if stem or not
     */
    public void saveDictionary(boolean stem) {
        try {
            File fileOne;
            //without stemming
            if (!stem) {
                fileOne = new File(this.getPostingFilePath() + "\\" + "CorpusDictionaryWithoutStem");
            }
            //with stemming
            else {
                fileOne = new File(this.getPostingFilePath() + "\\" + "CorpusDictionaryWithStem");
            }
            FileOutputStream fos = new FileOutputStream(fileOne);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(corpusDictionary);
            oos.flush();
            oos.close();
            fos.close();
        } catch (Exception e) {
        }
        corpusDictionary.clear();//todo - delete
        System.out.println("Dictionary saved and Empty" + corpusDictionary.isEmpty());
    }

    /**
     * This method load the dictionary from the disk
     *
     * @param file - to load
     */
    public void loadDictionary(File file) {
        try {
            try {
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                this.corpusDictionary = (HashMap<String, Pair<Integer, Integer>>) ois.readObject();
                ois.close();
                fis.close();
                System.out.println("Dictionary loaded");
            } catch (Exception e) {
            }
        } catch (Exception e) {
        }
    }

    public int getNumberOfDocs() {
        return numberofDocs;
    }

    public int getUniqueTermsCount() {
        return this.corpusDictionary.size();
    }
}