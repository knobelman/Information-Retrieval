package Model.Indexers;
import java.io.*;
import java.util.*;

/**
 * This class represents the Posting class
 * creates the posting files
 * @allLines - lines in one file
 * @rootPath - the path of posting file
 * @postingFilecounter - a counter from 0 to number of files in the corpus
 */
public class Posting {
    ArrayList<String> allLines;
    private String rootPath;
    private static int postingFileCounter;


    /**
     * C'tor
     */
    public Posting(String rootPath) {
        allLines = new ArrayList<>();
        this.rootPath = rootPath;
    }

    /**
     * This method create a temporary posting file
     * for each file will be temp posting file
     * @param linkedHashMap String - term, String - doc, Integer - tf
     */
    public void createTempPostingFile(HashMap<String, HashMap<String, Integer>> linkedHashMap) {
        try {
            Iterator it = linkedHashMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                int size = ((HashMap<String, Integer>)pair.getValue()).size();
                allLines.add(((String)pair.getKey()).toLowerCase() + "|DF:" + size + "|" + pair.getValue()+"\n");
                it.remove();
            }
            sort();
            FileWriter fw = new FileWriter(this.rootPath + "\\" + postingFileCounter);
            for(String s: allLines){
                fw.write(s);
            }
            fw.close();
            clearDic();
            postingFileCounter++;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * this function clear the current posting file content for the next one
     */
    public void clearDic() {
        this.allLines = new ArrayList<>();
    }


    /**
     * This function merge between all the temp posting files to create a final temp posting file
     * @param firstFile - the first file
     * @param secondFile - the second file
     * @param bw - bufferedWriter object
     */
    public void mergeBetweenTempPostingFiles(String firstFile, String secondFile, BufferedWriter bw) {
        try {
            String firstCurrentLine;
            String secondCurrentLine;
            File fFILE = new File(firstFile);
            File sFILE = new File(secondFile);
            BufferedReader first = new BufferedReader(new FileReader(this.rootPath+"\\"+fFILE));
            BufferedReader second = new BufferedReader(new FileReader(this.rootPath +"\\"+sFILE));
            firstCurrentLine = first.readLine();
            secondCurrentLine = second.readLine();

            while(firstCurrentLine!=null && secondCurrentLine!=null) {
                String t1 = firstCurrentLine.substring(0, firstCurrentLine.indexOf('|'));
                String t2 = secondCurrentLine.substring(0, secondCurrentLine.indexOf('|'));
                if(t1.compareTo(t2)<0){ //t1 < t2
                    bw.write(firstCurrentLine + "\n");
                    firstCurrentLine = first.readLine();
                }else if(t1.compareTo(t2)>0){ // t1 > t2
                    bw.write(secondCurrentLine + "\n");
                    secondCurrentLine = second.readLine();
                }else {//t1 = t2
                    //firstCurrentLine = firstCurrentLine.substring(firstCurrentLine.indexOf('|') + 1, firstCurrentLine.length());
                    secondCurrentLine = createLine(firstCurrentLine,secondCurrentLine); //secondCurrentLine.concat(firstCurrentLine +"\n");
                    bw.write(secondCurrentLine +"\n");
                    firstCurrentLine = first.readLine();
                    secondCurrentLine = second.readLine();
                }
            }

            if(firstCurrentLine == null){
                while (secondCurrentLine != null){
                    bw.write(secondCurrentLine+"\n");
                    secondCurrentLine = second.readLine();
                }
            }

            if(secondCurrentLine == null){
                while(firstCurrentLine != null){
                    bw.write(firstCurrentLine+"\n");
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
     * This method concat DF's of two lines
     * HEROLD|DF:1|{FBIS3-9996=1}   HEROLD|DF:1|{FBIS3-10419=1}
     * Example for a line: 0.256K|DF:2|{FBIS3-2314=1, FBIS3-2531=1}
     * @param firstCurrentLine
     * @param secondCurrentLine
     * @return - concat lines as needed
     */
    private String createLine(String firstCurrentLine, String secondCurrentLine) {
        String[] cut1 = firstCurrentLine.split("\\|"); // [0.256K,DF:2,{FBIS3-2314=1, FBIS3-2531=1}]
        String[] cut2 = secondCurrentLine.split("\\|");

        //TF
        cut2[2] = cut2[2].replaceAll("}",", ");// {FBIS3-2314=1, FBIS3-2531=1} -> {FBIS3-2314=1, FBIS3-2531=1,
        cut2[2] = cut2[2].concat(cut1[2].substring(1,cut1[2].length()));// {FBIS3-2314=1, FBIS3-2531=1, FBIS2-2531=1}

        //DF
        String[] cut11 = cut1[1].split(":");//[DF,2]
        String[] cut22 = cut2[1].split(":");

        //Create the number
        try {
            int DF1 = Integer.parseInt(cut11[1]);
            int DF2 = Integer.parseInt(cut22[1]);
            int newDF = DF1 + DF2;
            //Create the correct string
            String secondNewLine = cut1[0] + "|DF:" + newDF + "|" + cut2[2];//term + "\DF" + newDF + "|" + TF
            return secondNewLine;
        }catch (Exception e){
        }

      return "";
    }

    public void createEvenPostingFiles(int currPostingNumber){
        int lastPosting = currPostingNumber-1;
        FileWriter mergedWriter;
        BufferedWriter mergedBuffer;
        File lFILE = new File(this.getRootPath() + "\\" + lastPosting);
        File blFILE = new File(this.getRootPath() + "\\" + (lastPosting-1));
        try {
            mergedWriter = new FileWriter(this.getRootPath() + "\\" + "merged");
            mergedBuffer = new BufferedWriter(mergedWriter);
            mergeBetweenTempPostingFiles("" + (lastPosting), "" + (lastPosting-1), mergedBuffer);
            mergedBuffer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        lFILE.delete();
        blFILE.delete();
        File mergedF = new File(this.getRootPath()+"\\"+"merged");
        mergedF.renameTo(blFILE);
    }
    public void createFinalPosting(){
        int postingCounter = postingFileCounter;
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
                File lFILE = new File(this.getRootPath() + "\\" + i);
                File blFILE = new File(this.getRootPath() + "\\" + (i+1));
                try {
                    mergedWriter = new FileWriter(this.getRootPath() + "\\" + "merged");
                    mergedBuffer = new BufferedWriter(mergedWriter);
                    mergeBetweenTempPostingFiles("" + (i), "" + (i+1), mergedBuffer);
                    mergedBuffer.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                lFILE.delete();
                blFILE.delete();
                postingCounter--;
                File mergedF = new File(this.getRootPath()+"\\"+"merged");
                File newFile = new File(this.getRootPath()+"\\"+ newName);
                mergedF.renameTo(newFile);
                newName++;
            }
            newName = 0;
        }
    }

    /**
     * This method sort the lines in a temp posting file before writing to disk
     */
    private void sort(){
        allLines.sort((o1, o2) -> {
            String s1 = o1.substring(0, o1.indexOf('|'));
            String s2 = o2.substring(0, o2.indexOf('|'));
            return s1.compareTo(s2);
        });
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
        return postingFileCounter;
    }
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

