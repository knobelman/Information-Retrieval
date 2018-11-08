package Model;

import java.io.*;
import java.util.*;

/**
 * Created by Maor on 11/8/2018.
 */
public class Posting {
    private ArrayList<Term> Dictionary;
    byte[] objectToByteArray;
    private String rootPath;
    private static int postingFilecounter;

    public Posting(String rootPath) {
        this.Dictionary = new ArrayList<>();
        this.rootPath = rootPath + "\\PostingFiles";
    }

    public ArrayList<Term> getDictionary() {
        return Dictionary;
    }

    public void posting(HashMap<String, Term> termsInDoc) {
        for (Term term : termsInDoc.values()) {
//            System.out.println(term.toString());
            this.Dictionary.add(term);
        }
        sort();
        //ArrayList<Term> dictionary2 = readDictionary();
    }

    private void sort(){
        this.Dictionary.sort((o1, o2) -> (o1.getTerm().compareTo(o2.getTerm())));
    }

    public void createPostingFile() {
        try {
            this.objectToByteArray = convertToBytes(this.Dictionary);
            FileOutputStream fos = new FileOutputStream(this.rootPath + "\\" + postingFilecounter);
            fos.write(objectToByteArray);
            fos.close();
            postingFilecounter++;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private ArrayList readDictionary(){
        try {
            FileInputStream fis = new FileInputStream(this.rootPath + "\\" + postingFilecounter);
            ByteArrayInputStream in = new ByteArrayInputStream(this.objectToByteArray);
            ObjectInputStream is = new ObjectInputStream(in);
            return (ArrayList)is.readObject();
        } catch (Exception e){

        }
        return null;
    }
    private byte[] convertToBytes(Object object) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutput out = new ObjectOutputStream(bos)) {
            out.writeObject(object);
            return bos.toByteArray();
        }
    }

    public void clearDic() {
        this.Dictionary = new ArrayList<>();
    }
}

