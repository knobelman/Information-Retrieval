package Model;

import java.io.*;
import java.util.*;

/**
 * Created by Maor on 11/8/2018.
 */
public class Posting {
    ArrayList<String> allLines;
    private String rootPath;
    private static int postingFilecounter;

    public Posting(String rootPath) {
        allLines = new ArrayList<>();
        this.rootPath = "C:\\Users\\Maor\\Desktop\\PostingFile";
    }

    public void createPostingFile(HashMap<String, HashMap<String, Integer>> linkedHashMap) {
        try {
            Iterator it = linkedHashMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                allLines.add(pair.getKey() + "|" + pair.getValue()+"\n");
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

    private void sort(){
        allLines.sort((o1, o2) -> {
            String s1 = o1.substring(0, o1.indexOf('|'));
            String s2 = o2.substring(0, o2.indexOf('|'));
            return s1.compareTo(s2);
        });
    }

    public void clearDic() {
        this.allLines =new ArrayList<>();
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

