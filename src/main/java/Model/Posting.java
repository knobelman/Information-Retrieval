package Model;

import java.io.*;
import java.util.*;

/**
 * This class represents the Posting class
 * creates the posting files
 * allLines -
 * rootPath -
 * postingFilecounter -
 * firstHalfWriter -
 * secondHalfWriter -
 */
public class Posting {
    ArrayList<String> allLines;
    private String rootPath;
    private static int postingFilecounter;
    private BufferedWriter firstHalfWriter;
    private BufferedWriter secondHalfWriter;


    /**
     * C'tor
     * @param rootPath - tje path of the posting files
     */
    public Posting(String rootPath) {
        allLines = new ArrayList<>();
        this.rootPath = rootPath;
        try {
            firstHalfWriter = new BufferedWriter(new FileWriter(this.rootPath+"\\"+"FirstHalf"));
            secondHalfWriter = new BufferedWriter(new FileWriter(this.rootPath+"\\"+"SecondHalf"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * this method create the posting files
     * @param linkedHashMap String - term, String - doc, Integer - tf
     * @param dictionary
     */
    public void createPostingFile(HashMap<String, HashMap<String, Integer>> linkedHashMap, HashMap<String, Term> dictionary) {
        try {
            Iterator it = linkedHashMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                int size = ((HashMap<String, Integer>)pair.getValue()).size();
                allLines.add(pair.getKey() + "| DF: " + size + " >" + pair.getValue()+"\n");
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
     * this method sort the lines in a posting file before writing to disk
     */
    private void sort(){
        allLines.sort((o1, o2) -> {
            String s1 = o1.substring(0, o1.indexOf('>'));
            String s2 = o2.substring(0, o2.indexOf('>'));
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
     * This function merge between all the temp posting files
     *
     * @param firstFile - the first file
     * @param secondFile - the second file
     * @param choosen - file writer
     */
    public void mergeBetweenPostFiles(String firstFile, String secondFile, BufferedWriter choosen) {
        try {
            BufferedReader first;
            BufferedReader second;
            String firstCurrentLine;
            String secondCurrentLine;
            File fFILE = new File(firstFile);
            File sFILE = new File(secondFile);
            first = new BufferedReader(new FileReader(this.rootPath+"\\"+fFILE));
            second = new BufferedReader(new FileReader(this.rootPath +"\\"+sFILE));
            firstCurrentLine = first.readLine();
            secondCurrentLine = second.readLine();

            while(firstCurrentLine!=null && secondCurrentLine!=null) {
                String t1 = firstCurrentLine.substring(0, firstCurrentLine.indexOf('|'));
                String t2 = secondCurrentLine.substring(0, secondCurrentLine.indexOf('|'));

                //t1 < t2
                if(t1.compareTo(t2)<0){
                    choosen.write(firstCurrentLine + "\n");
                    firstCurrentLine = first.readLine();
                    // t1 > t2
                }else if(t1.compareTo(t2)>0){
                    choosen.write(secondCurrentLine + "\n");
                    secondCurrentLine = second.readLine();
                    //t1 = t2
                }else {
                    //firstCurrentLine = firstCurrentLine.substring(firstCurrentLine.indexOf('|') + 1, firstCurrentLine.length());
                    secondCurrentLine = createLine(firstCurrentLine,secondCurrentLine);//secondCurrentLine.concat(firstCurrentLine + "\n");
                    choosen.write(secondCurrentLine);
                    firstCurrentLine = first.readLine();
                    secondCurrentLine = second.readLine();
                }
            }

            if(firstCurrentLine == null){
                while (secondCurrentLine != null){
                    choosen.write(secondCurrentLine+"\n");
                    secondCurrentLine = second.readLine();
                }
            }

            if(secondCurrentLine == null){
                while(firstCurrentLine != null){
                    choosen.write(firstCurrentLine+"\n");
                    firstCurrentLine = first.readLine();
                }
            }

            first.close();
            second.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Example for a line: 0.256K| DF: 2 >{FBIS3-2314=1, FBIS3-2531=1}
     * @param firstCurrentLine
     * @param secondCurrentLine
     * @return - concat lines as needed
     */
    private String createLine(String firstCurrentLine, String secondCurrentLine) {
        String[] cut1 = firstCurrentLine.split(">"); // [ 0.256K| DF: 2 , {FBIS3-2314=1, FBIS3-2531=1} ]
        String[] cut2 = secondCurrentLine.split(">");
        cut2[1].replaceFirst("}",", "); //{FBIS3-2314=1, FBIS3-2531=1,
        cut2[1].concat(cut1[1].substring(1));//{FBIS3-2314=1, FBIS3-2531=1, FBIS2-2531=1}
        return "";
    }

    /**
     * Getter
     * @return BufferedWriter object of the first Half Writer
     */
    public BufferedWriter getFirstHalfWriter() {
        return firstHalfWriter;
    }

    /**
     * Getter
     * @return BufferedWriter object of the second Half Writer
     */
    public BufferedWriter getSecondHalfWriter() {
        return secondHalfWriter;
    }

    /**
     * Getter
     * @return the path of the posting files
     */
    public String getRootPath() {
        return rootPath;
    }

    /**
     * Getter
     * @return the posting file counter
     */
    public static int getPostingFilecounter() {
        return postingFilecounter;
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

