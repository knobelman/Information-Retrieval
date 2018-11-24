package Model.Indexers;
import Model.DataObjects.CityData;
import Model.DataObjects.ParseableObjects.Doc;
import Model.DataObjects.Term;
import Model.Parsers.JasonParser;
import Model.Parsers.DocParser;
import javafx.util.Pair;
import java.io.*;
import java.util.*;

/**
 * This class represents the Indexer class
 * Indexer class create index for all the files
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
    private JasonParser jasonParser;
    private HashMap <String,Pair<ArrayList<String>,CityData> > cityDictionary;
    private HashMap<String,Pair<Integer,Integer>> corpusDictionary; //term,totalTF,position in merged posting file
    private HashSet<Doc> DocumentsToParse;
    private HashMap<String,Doc> DocumentDicionary;
    private Posting postingObject;
    private DocParser ParserObject;
    private HashMap<String,HashMap<String,Integer>> TermAndDocumentsData = new LinkedHashMap<>();
    private HashMap<Character,String> letters; // every letter and the name of the file
    private List<Thread> threadList;
    private Boolean toStem;

    /**
     * C'tor
     * initialize DocumentsToParse HashSet and Dictionary HashMap
     */
    public Indexer(String rootPath, boolean toStem) {
        this.rootPath = rootPath;
        this.DocumentsToParse = new HashSet<>();
        this.corpusDictionary = new HashMap<>();
        this.DocumentDicionary = new HashMap<>();
        this.threadList = new ArrayList<>();
        this.letters = new HashMap<>();
        this.cityDictionary = new HashMap<>();
        this.jasonParser = new JasonParser();
        this.ParserObject = new DocParser(rootPath,toStem);
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
     * this function send date to posting class which create the posting files
     */
    public void init(final File file) throws IOException {
        for (final File fileEntry : file.listFiles()) {
            if (fileEntry.isDirectory()) {
                init(fileEntry);
            } else {
                DocumentsToParse = readFileObject.fromFileToDoc(fileEntry);
                for (Doc d : DocumentsToParse) {
                    //create document dictionary
                    Doc toInsert = new Doc(d.getPath(),d.getCity(),d.getMax_tf(),d.getSpecialWordCount());
                    DocumentDicionary.put(d.getDoc_num(),toInsert);
                    //create city dictionary
                    if(!d.getCity().equals("")){
                        if(!cityDictionary.containsKey(d.getCity())) {
                            String city = d.getCity();
                            String doc_num = d.getDoc_num();
                            CityData cityData = jasonParser.getData(city);
                            ArrayList<String> list = new ArrayList<>();
                            list.add(doc_num);
                            Pair<ArrayList<String>,CityData> pair = new Pair<>(list,cityData);
                            this.cityDictionary.put(city,pair);
                        }
                        else{
                            Pair<ArrayList<String>,CityData> tmp = cityDictionary.get(d.getCity());
                            ArrayList<String> current = tmp.getKey();
                            CityData city_data = tmp.getValue();
                            current.add(d.getDoc_num());
                            Pair<ArrayList<String>,CityData> newPair = new Pair<>(current,city_data);
                            cityDictionary.replace(d.getCity(),newPair);
                        }
                    }
                    ParserObject.parsing(d);
                    for(Map.Entry<String,Term> entry : d.getTermsInDoc().entrySet()) {
                        String termName = entry.getKey();
                        Term value = entry.getValue();
                        if(!corpusDictionary.containsKey(termName)){
                            corpusDictionary.put(termName,new Pair<>(1,0)); //term name, file name, position
                        }
                        String doc_name = d.getDoc_num();
                        if(TermAndDocumentsData.containsKey(termName.toLowerCase())){ //todo - add .toLowerCase
                            Integer newint =  new Integer(d.getTermsInDoc().get(termName).getTf(doc_name));
                            //int df = d.getTermsInDoc().get(termname).getDf();
                            TermAndDocumentsData.get(termName.toLowerCase()).put(d.getDoc_num(),newint);
                        }else {
                            HashMap<String, Integer> current = new HashMap();
                            current.put(doc_name, new Integer(value.getTf(doc_name)));
                            TermAndDocumentsData.put(termName.toLowerCase(), current);
                        }
                    }
                }
                if(!this.TermAndDocumentsData.isEmpty()) {
                    Thread t = new Thread(() -> {
                        postingObject.createTempPostingFile(TermAndDocumentsData);
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

    public void createFinalPosting() {
        postingObject.createFinalPosting();
        try {
            BufferedReader last = new BufferedReader(new FileReader(this.getPostingFilePath() + "\\" + "0"));
            String current = last.readLine();
            FileWriter fw = new FileWriter(this.getPostingFilePath() + "\\" + "new");
            while(current!=null) {
                String t1 = current.substring(0, current.indexOf('|'));
                String more = current.substring(current.indexOf('|'), current.length());
                if (corpusDictionary.containsKey(t1.toLowerCase())) {
                    current = t1.toLowerCase() + more;
                    fw.write(current +"\n");
                } else {
                    current = t1.toUpperCase() + more;
                    fw.write(current +"\n");
                }
                current = last.readLine();
            }
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void splitFinalPosting(){
        HashMap<String,BufferedWriter> fileWriters = new HashMap<>();//hashmap for Filewriters
        HashMap<String,Integer> filePosition = new HashMap<>();//hashmap for Filewriters
        try {
            fileWriters.put("ABCD",new BufferedWriter(new FileWriter(postingObject.getRootPath() + "\\ABCD")));
            fileWriters.put("EFGH",new BufferedWriter(new FileWriter(postingObject.getRootPath() + "\\EFGH")));
            fileWriters.put("IJKL",new BufferedWriter(new FileWriter(postingObject.getRootPath() + "\\IJKL")));
            fileWriters.put("MNOP",new BufferedWriter(new FileWriter(postingObject.getRootPath() + "\\MNOP")));
            fileWriters.put("QRST",new BufferedWriter(new FileWriter(postingObject.getRootPath() + "\\QRST")));
            fileWriters.put("UVWXYZ",new BufferedWriter(new FileWriter(postingObject.getRootPath() + "\\UVWXYZ")));
            fileWriters.put("OTHER",new BufferedWriter(new FileWriter(postingObject.getRootPath() + "\\OTHER")));
            filePosition.put("ABCD",new Integer(0));
            filePosition.put("EFGH",new Integer(0));
            filePosition.put("IJKL",new Integer(0));
            filePosition.put("MNOP",new Integer(0));
            filePosition.put("QRST",new Integer(0));
            filePosition.put("UVWXYZ",new Integer(0));
            filePosition.put("OTHER",new Integer(0));
        }catch(Exception e){
        }
        BufferedReader postingFile;
        BufferedWriter fileBuffer;
        String currTerm, line, fileName;
        int position;
        try {
            postingFile = new BufferedReader(new FileReader(postingObject.getRootPath()+"\\new"));//read posting
            line = postingFile.readLine();
            do{
                if(!letters.containsKey(line.charAt(0)) && !Character.isUpperCase(line.charAt(0))) {//if first char isn't a known letter
                    fileBuffer = fileWriters.get("OTHER");//get hte buffer to write
                    position = filePosition.get("OTHER").intValue();//get position to update Dic
                    fileName = "OTHER";//get name of file to update Dic
                }
                else{
                    char tmp = Character.toLowerCase(line.charAt(0));
                    fileBuffer = fileWriters.get(letters.get(tmp));
                    //position = filePosition.get(letters.get(tmp)).intValue();//todo
                    fileName = letters.get(tmp);
                }
                currTerm = line.substring(0, line.indexOf('|'));//get the term
                //Pair tmpPair = new Pair<>(corpusDictionary.get(currTerm).getKey(),position);//create tmppair to insert into Dic
                //corpusDictionary.replace(currTerm, tmpPair);//change the position for the term in the Dic
                fileBuffer.write(line+"\n");
                //position += line.length() + 1;//increase the position for next line //todo
                //filePosition.replace(fileName,position);//insert new position for next line //todo
                line = postingFile.readLine();
            }while(line!=null);
            File pFile = new File(postingObject.getRootPath()+"\\"+"0");
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
     * @return
     */
    public String getPostingFilePath() {
        return this.postingObject.getRootPath();
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

    public HashMap<String, Pair<ArrayList<String>, CityData>> getCityDictionary() {
        return cityDictionary;
    }

    //todo - just for testing
    public void writeDictionaryToDisk(){
        try {
            ArrayList<String> s = new ArrayList();
            Iterator it = this.cityDictionary.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                String cityName = pair.getKey().toString();
                String alist = (((Pair)pair.getValue()).getKey()).toString();
                CityData c = (CityData)((Pair)pair.getValue()).getValue();
                try {
                    String data = c.getCountryName() + ", " + c.getPopulation() + ", " + c.getCurrency();
                    s.add(cityName + "|" + alist + "|" + data + "\n");
                }catch(Exception e){
                }
                it.remove();
            }
            s.sort(Comparator.naturalOrder());
            FileWriter fw = new FileWriter(this.getPostingFilePath() + "\\" + "citiesTest");
            for(String a: s){
                fw.write(a);
            }
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}




