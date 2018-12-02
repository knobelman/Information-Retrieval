package Model.Indexers;

import Model.DataObjects.CityData;
import Model.DataObjects.ParseableObjects.Doc;
import Model.DataObjects.Term;
import Model.DataObjects.TermData;
import Model.Parsers.ParsingProcess.DocParsingProcess;
import Model.Parsers.ParsingProcess.CityParsingProcess;
import Model.Parsers.ParsingProcess.IParsingProcess;
import javafx.scene.control.Alert;
import javafx.util.Pair;
import java.io.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

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
    //paths
    private String rootPath;
    private String pathOfPosting;

    //objects
    private ReadFile readFileObject;
    private Posting postingObject;
    private DocParsingProcess ParserObject;
    private IParsingProcess cityParsingProcess;

    //dictionaries
    private HashMap<String, Pair<ArrayList<String>, CityData>> cityPostingData;// City -> cityData
    private HashMap<String, TermData> corpusDictionary; //term,totalTF,position in merged posting file
    private HashMap<String, Doc> DocumentDictionary; //Doc String - > Doc object
    private HashMap<String,String> LanguageDictionary; //Language -> Docs
    private HashMap<String,Integer> cityDictionary; //City name -> position in posting

    //other
    private ArrayList<Doc> DocumentsToParse;
    private List<Thread> threadList;
    private int numberofDocs;

    /**
     * C'tor
     */
    public Indexer() {
    }

    /**
     * C'tor
     * @param rootPath - the path of the corpus
     * @param pathOfPosting - the path of the posting
     * @param toStem - if stem or not
     */
    public Indexer(String rootPath,String pathOfPosting, boolean toStem) {
        this.rootPath = rootPath;
        this.pathOfPosting = pathOfPosting;
        this.DocumentsToParse = new ArrayList<>();

        this.postingObject = new Posting(pathOfPosting);
        this.postingObject.resetPostingCounter();

        this.readFileObject = new ReadFile();
        this.corpusDictionary = new HashMap<>();
        this.DocumentDictionary = new HashMap<>();
        this.cityPostingData = new HashMap<>();
        this.LanguageDictionary = new HashMap<>();
        this.cityDictionary = new HashMap<>();

        this.threadList = new CopyOnWriteArrayList<>();
        this.cityParsingProcess = new CityParsingProcess();
        this.ParserObject = new DocParsingProcess(rootPath, toStem);
        this.numberofDocs = 0;
    }

    /**
     * this function initialize index
     * send all the docs to parsing
     * insert to TermAndDocumentsData structure and to the dictionary if need to update
     *
     * @param root - entry file
     *             this function send date to posting class which create the posting files
     */
    public void init(final File root) throws IOException {
        HashMap <String, HashMap<String, Integer>> TermAndDocumentsData = new HashMap<>();
        for (final File directory : root.listFiles()){//for each folder in root
            if (directory.isDirectory())
                for (File currFile : directory.listFiles()) {//for each file in folder
                    DocumentsToParse = readFileObject.fromFileToDoc(currFile);
                    for (Doc d : DocumentsToParse) {
                        numberofDocs++;
                        //create document dictionary
                        if(!d.getLanguage().equals("")) {
                            addToLanguageDictionary(d);
                        }
                        //add to city dictionary
                        if (!d.getCity().equals("")) {
                            addToCityDictionary(d);
                        }
                        ParserObject.parsing(d);
                        Doc toInsert = new Doc(d.getPath(), d.getCity(), d.getMax_tf(), d.getSpecialWordCount(),d.getMax_tf_String()); //doc to insert to the dictionary
                        DocumentDictionary.put(d.getDoc_num(), toInsert); //insert to dictionary (Doc name | Doc object)
                        for (Map.Entry<String, Term> entry : d.getTermsInDoc().entrySet()) {
                            String termName = entry.getKey();//term from doc
                            Term value = entry.getValue();
                            String doc_name = d.getDoc_num();
                            if (corpusDictionary.containsKey(termName)) {//Dic contains the term
                                updateDF(termName);
                                updateTF(termName,value.getTf(doc_name));
                                //todo - Yaniv please check it
                                //check also if the first char is a letter (to filter only Words)
                            } else if (!termName.contains("Dollars") && !termName.contains("Yen") && Character.isLetter(termName.charAt(0)) && corpusDictionary.containsKey(termName.toLowerCase())) {//if Dic has lowercase of this word
                                termName = termName.toLowerCase();
                                updateDF(termName);
                                updateTF(termName,value.getTf(doc_name));
                                //todo - also here
                            } else if (!termName.contains("Dollars") && !termName.contains("Yen") && Character.isLetter(termName.charAt(0)) && (corpusDictionary.containsKey(termName.toUpperCase()))) {
                                changeULDic(termName);
                                updateDF(termName);
                                updateTF(termName,value.getTf(doc_name));
                            } else {
                                //todo - yaniv please check it
                                //if contains "-" the we save it as upper case
                                //if not contains Dollars or Yen and the first char is not a letter then we save as uppercase
                                if(termName.contains("-") || (!termName.contains("Dollars") && !termName.contains("Yen") && !Character.isLetter(termName.charAt(0)))){
                                    termName = termName.toUpperCase();
                                }
                                corpusDictionary.put(termName, new TermData(1,value.getTf(doc_name),0)); //term name, file name, position
                            }
                            if (TermAndDocumentsData.containsKey(termName)) {//term is in TermAndDocumentsData already
                                Integer newInt = new Integer(value.getTf(doc_name));
                                TermAndDocumentsData.get(termName).put(d.getDoc_num(), newInt);
                            }
                            else if(TermAndDocumentsData.containsKey(termName.toLowerCase())){//term name is upper, TADD contains lower
                                Integer newInt = new Integer(value.getTf(doc_name));
                                TermAndDocumentsData.get(termName.toLowerCase()).put(d.getDoc_num(), newInt);
                            }
                            else if(TermAndDocumentsData.containsKey(termName.toUpperCase())){//term is lowercase, TADD contains upper
                                HashMap<String, Integer> tmpHM = TermAndDocumentsData.get(termName.toUpperCase());
                                TermAndDocumentsData.remove(termName.toUpperCase());
                                TermAndDocumentsData.put(termName, tmpHM);
                                Integer newInt = new Integer(value.getTf(doc_name));
                                TermAndDocumentsData.get(termName).put(d.getDoc_num(), newInt);
                            }
                            else {
//                                if(termName.contains("-") || (!termName.contains("Dollars") && !termName.contains("Yen") && !Character.isLetter(termName.charAt(0)))){
//                                    termName = termName.toUpperCase();
//                                }
                                HashMap<String, Integer> current = new HashMap();
                                current.put(doc_name, new Integer(value.getTf(doc_name)));
                                TermAndDocumentsData.put(termName, current);
                            }
                        }
                    }
                    postingObject.createTempPostingFile(TermAndDocumentsData);
                    TermAndDocumentsData.clear();
                }
        }
        createFinalPosting();
        writeCityPostingFile();
    }

    private void addToLanguageDictionary(Doc d) {
        //if language dictionary contains the language
        if(this.LanguageDictionary.containsKey(d.getLanguage())){
            String Docs = this.LanguageDictionary.get(d.getLanguage()); //get old value
            Docs = Docs + "|" + d.getDoc_num(); //update docs string
            this.LanguageDictionary.remove(d.getLanguage()); //remove the old key
            this.LanguageDictionary.put(d.getLanguage(),Docs); //add new key
        }
        //if language dictionary not contains the language
        else{
            this.LanguageDictionary.put(d.getLanguage(),d.getDoc_num());
        }
    }


    /**
     *
     * @param termName - corpusDictionary contains this term
     */
    private void updateDF(String termName) {
        corpusDictionary.get(termName).incDF(1);
    }

    /**
     * Update totalTF in TD in the Dic
     * @param plusTF - the tf to add
     * @param termName - the term to update
     */
    private void updateTF(String termName, int plusTF) {
        corpusDictionary.get(termName).incTotalTF(plusTF);
    }

    /**
     *
     * @param termName - lowercase of something in corpusDictionary that is uppercase
     */
    private void changeULDic(String termName) {
        TermData td = corpusDictionary.get(termName.toUpperCase());
//        Pair<Integer, Integer> tmpPair = corpusDictionary.get(termName.toUpperCase());
        corpusDictionary.remove(termName.toUpperCase());
        corpusDictionary.put(termName, td);
    }

    /**
     * This method add city data from document to the city dictionary
     *
     * @param document - current document
     */
    public void addToCityDictionary(Doc document) {
        if (!cityPostingData.containsKey(document.getCity())) { //if city not in the dictionary
                String city = document.getCity(); //get city name from the doc
                //remove spam
                if (city.equals("--FOR") || city.equals("--") || Character.isDigit(city.charAt(0))) {
                    return;
                }
                String doc_num = document.getDoc_num(); // get doc name from the dock
                String positions_in_doc = document.getPositionOfCity().toString();
                CityData cityData = ((CityParsingProcess) cityParsingProcess).getData(city); //get data about the city
                ArrayList<String> list = new ArrayList<>();
                list.add(doc_num + ":" + positions_in_doc); //add doc name to array list with positions in doc
                Pair<ArrayList<String>, CityData> pair = new Pair<>(list, cityData); //insert data into new pair
                this.cityPostingData.put(city, pair); //insert to dictionary
        } else {
                //if city is in the dictionary
                Pair<ArrayList<String>, CityData> tmp = cityPostingData.get(document.getCity()); //get existing pair
                ArrayList<String> current = tmp.getKey(); //get array list from the pair
                CityData city_data = tmp.getValue(); //get city data from the pair
                current.add(document.getDoc_num() + ":" + document.getPositionOfCity());//add new doc and position in doc to the pair
                Pair<ArrayList<String>, CityData> newPair = new Pair<>(current, city_data); //create new pair with the additional data
                cityPostingData.replace(document.getCity(), newPair); //replace the existing data in the dictionary with the new data
            }
    }

    public void createFinalPosting() {
        postingObject.createFinalPosting(corpusDictionary);
        postingObject.splitFinalPosting(corpusDictionary);

    }

    public HashMap<String, TermData> getCorpusDictionary() {
        return corpusDictionary;
    }

    public HashMap<String, Pair<ArrayList<String>, CityData>> getCityDictionary() {
        return cityPostingData;
    }

    public void writeCityPostingFile() {
        int position = 0;
        try {
            ArrayList<String> s = new ArrayList();
            Iterator it = this.cityPostingData.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                String cityName = pair.getKey().toString();
                String alist = (((Pair) pair.getValue()).getKey()).toString();
                CityData c = (CityData) ((Pair) pair.getValue()).getValue();
                try {
                    if(c == null){
                        s.add(cityName + "|" + alist + "|" + "\n");
                    }else {
                        String data = c.getCountryName() + "|" + c.getPopulation() + "|" + c.getCurrency();
                        s.add(cityName + "|" + alist + "|" + data + "\n");
                    }
                } catch (Exception e) {
                }
                it.remove();
            }
            s.sort(Comparator.naturalOrder());
            FileWriter fw = new FileWriter(this.pathOfPosting + "\\" + "CityPostingFile");
            for (String a : s) {
                String toAdd = a.substring(a.indexOf("|")+1);
                cityDictionary.put(a.substring(0,a.indexOf("|")),new Integer(position));
                fw.write(toAdd);
                position += toAdd.length();
            }
            fw.close();
            s.clear();
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
                fileOne = new File(this.pathOfPosting + "\\" + "CorpusDictionaryWithoutStem");
            }
            //with stemming
            else {
                fileOne = new File(this.pathOfPosting + "\\" + "CorpusDictionaryWithStem");
            }
            if(corpusDictionary.isEmpty()){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Dictionary is empty");
                alert.setHeaderText("Dictionary is empty");
                alert.setContentText("Please run indexing to create the dictionary");
                alert.show();
            }
            else {
                FileOutputStream fos = new FileOutputStream(fileOne);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(corpusDictionary);
                oos.flush();
                oos.close();
                fos.close();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Dictionary saved successfully");
                alert.setHeaderText("Dictionary saved successfully");
                alert.setContentText("Dictionary saved successfully");
                alert.show();
            }
        } catch (Exception e) {
        }
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
                this.corpusDictionary = (HashMap<String, TermData>) ois.readObject();
                ois.close();
                fis.close();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Dictionary Loaded successfully");
                alert.setHeaderText("Dictionary Loaded successfully");
                alert.setContentText("Dictionary Loaded successfully");
                alert.show();
            } catch (Exception e) {
                e.printStackTrace();
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

    public String getLine(long position){
        try {
            RandomAccessFile rndFile = new RandomAccessFile(this.pathOfPosting + "\\" + "OTHER","r");
            rndFile.seek(position);
//            String line = rndFile.readLine();
//            System.out.println(line);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public void reset() {
        if(corpusDictionary != null){
            corpusDictionary.clear();
        }
        if(cityPostingData != null){
            cityPostingData.clear();
        }
        if(DocumentDictionary != null){
            DocumentDictionary.clear();
        }
        if(LanguageDictionary != null){
            LanguageDictionary.clear();
        }
    }

//    public void testSaveDictionaryToDisk(){
//        Iterator it = this.corpusDictionary.entrySet().iterator();
//        ArrayList<String> a = new ArrayList<>();
//        FileWriter fw = null;
//        try {
//            fw = new FileWriter(this.pathOfPosting + "\\" + "test");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        while (it.hasNext()) {
//            Map.Entry pair = (Map.Entry) it.next();
//            String term = pair.getKey().toString();
//            a.add(term +"\n");
//        }
//        a.sort(Comparator.comparing(String::toLowerCase));
//        try {
//            for(String s: a){
//                fw.write(s);
//            }
//            fw.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void checkDiffernces(){
//        BufferedReader zero = null,test = null;
//        try {
//             zero = new BufferedReader(new FileReader(this.pathOfPosting+"\\"+"0"));
//             test = new BufferedReader(new FileReader(this.pathOfPosting+"\\"+"test"));
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            String line = zero.readLine();
//            String line2 = test.readLine();
//            while(line!=null && line2!=null){
//                String term = line.substring(0,line.indexOf("|"));
//                if(!term.equals(line2)){
//                    System.out.println(term +"," + line2);
//                }
//                line = zero.readLine();
//                line2 = test.readLine();
//            }
//        }catch (Exception e){
//        }
//
//    }
}