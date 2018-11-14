package Model;

import java.io.*;
import java.util.*;

/**
 * This class create the posting file
 */
public class Posting {
    ArrayList<String> allLines;
    private String rootPath;
    private static int postingFilecounter;

    /**
     * C'tor
     * @param rootPath
     */
    public Posting(String rootPath) {
        allLines = new ArrayList<>();
        this.rootPath = rootPath;
    }

    /**
     *
     * @param linkedHashMap String - term, String - doc, Integer - tf
     * @param dictionary
     */
    public void createPostingFile(HashMap<String, HashMap<String, Integer>> linkedHashMap, HashMap<String, Term> dictionary) {
        try {
            Iterator it = linkedHashMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                int size = ((HashMap<String, Integer>)pair.getValue()).size();
                allLines.add(pair.getKey() + "| DF: " + size + " |" + pair.getValue()+"\n");
                it.remove();
            }
            sort();
            FileWriter fw = new FileWriter(this.rootPath + "\\" + postingFilecounter);
            for(String s: allLines){
                fw.write(s);
            }
            fw.close();
            clearDic();
            postingFilecounter++;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * this function sort the lines in a posting file before writing to disk
     */
    private void sort(){
        allLines.sort((o1, o2) -> {
            String s1 = o1.substring(0, o1.indexOf('|'));
            String s2 = o2.substring(0, o2.indexOf('|'));
            return s1.compareTo(s2);
        });
    }

    /**
     * this function clear the current posting file content for the next one
     */
    public void clearDic() {
        this.allLines = new ArrayList<>();
    }

    /**
     * this function merge between two posting files
     * @param firstFile - the first file
     * @param secondFile - the second file
     */

    //todo - to continue
    public void mergeBetweenPostFiles(String firstFile, String secondFile) {
        try {
            BufferedReader first;
            BufferedReader second;
            String firstCurrentLine = null;
            String secondCurrentLine = null;
            first = new BufferedReader(new FileReader(firstFile));
            second = new BufferedReader(new FileReader(secondFile));
            while(firstCurrentLine!=null && secondCurrentLine!=null) {
                firstCurrentLine = first.readLine();
                secondCurrentLine = second.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String getRootPath() {
        return rootPath;
    }

    //    private ArrayList readDictionary(){
//        try {
//            FileInputStream fis = new FileInputStream(this.rootPath + "\\" + postingFilecounter);
//            ByteArrayInputStream in = new ByteArrayInputStream(this.objectToByteArray);
//            ObjectInputStream is = new ObjectInputStream(in);
//            return (ArrayList)is.readObject();
//        } catch (Exception e){
//
//        }
//        return null;
//    }
//    private byte[] convertToBytes(Object object) throws IOException {
//        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
//             ObjectOutput out = new ObjectOutputStream(bos)) {
//            out.writeObject(object);
//            return bos.toByteArray();
//        }
//    }

}

