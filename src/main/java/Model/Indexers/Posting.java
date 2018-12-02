package Model.Indexers;
import Model.DataObjects.TermData;
import javafx.util.Pair;

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
    private HashMap<Character, String> letters; // every letter and the name of the file


    /**
     * C'tor
     */
    public Posting(String rootPath) {
        allLines = new ArrayList<>();
        this.rootPath = rootPath;
        this.letters = new HashMap<>();
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
                allLines.add((pair.getKey()) + "|DF:" + size + "|" + pair.getValue()+"\n");
//                it.remove();
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
        this.allLines.clear();
    }


    /**
     * This function merge between all the temp posting files to create a final temp posting file
     * @param firstFile - the first file
     * @param secondFile - the second file
     * @param bw - bufferedWriter object
     * @param corpusDictionary
     */
    public void mergeBetweenTempPostingFiles(String firstFile, String secondFile, BufferedWriter bw, HashMap<String, TermData> corpusDictionary) {
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
                if(t1.toLowerCase().compareTo(t2.toLowerCase())<0){ //t1 < t2
                    bw.write(firstCurrentLine + "\n");
                    firstCurrentLine = first.readLine();
                }else if(t1.toLowerCase().compareTo(t2.toLowerCase())>0){ // t1 > t2
                    bw.write(secondCurrentLine + "\n");
                    secondCurrentLine = second.readLine();
                }else {//t1 = t2
                    //firstCurrentLine = firstCurrentLine.substring(firstCurrentLine.indexOf('|') + 1, firstCurrentLine.length());
                    String correctTerm;
                    if(corpusDictionary.containsKey(t1))
                        correctTerm = t1;
                    else
                        correctTerm = t2;
                    secondCurrentLine = createLine(firstCurrentLine,secondCurrentLine,correctTerm); //secondCurrentLine.concat(firstCurrentLine +"\n");
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
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method concat DF's of two lines
     * HEROLD|DF:1|{FBIS3-9996=1}   HEROLD|DF:1|{FBIS3-10419=1}
     * Example for a line: 0.256K|DF:2|{FBIS3-2314=1, FBIS3-2531=1}
     *
     * @param currentTerm
     * @param firstCurrentLine
     * @param secondCurrentLine
     * @return - concat lines as needed
     */
    private String createLine(String firstCurrentLine, String secondCurrentLine, String currentTerm) {
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
            String secondNewLine = currentTerm + "|DF:" + newDF + "|" + cut2[2];//term + "\DF" + newDF + "|" + TF
            return secondNewLine;
        }catch (Exception e){
            e.printStackTrace();
        }

        return "";
    }

    public void createEvenPostingFiles(int currPostingNumber, HashMap<String, TermData> corpusDictionary){
        int lastPosting = currPostingNumber-1;
        FileWriter mergedWriter;
        BufferedWriter mergedBuffer;
        File lFILE = new File(this.rootPath + "\\" + lastPosting);
        File blFILE = new File(this.rootPath + "\\" + (lastPosting-1));
        try {
            mergedWriter = new FileWriter(this.rootPath + "\\" + "merged");
            mergedBuffer = new BufferedWriter(mergedWriter);
            mergeBetweenTempPostingFiles("" + (lastPosting), "" + (lastPosting-1), mergedBuffer,corpusDictionary);
            mergedBuffer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        lFILE.delete();
        blFILE.delete();
        File mergedF = new File(this.rootPath+"\\"+"merged");
        mergedF.renameTo(blFILE);
    }

    public void createFinalPosting(HashMap<String, TermData> corpusDictionary){
        int postingCounter = postingFileCounter;
        int newName = 0;
        while(postingCounter>=2) {
            if (!(postingCounter % 2 == 0)) {
                createEvenPostingFiles(postingCounter,corpusDictionary);
                postingCounter--;
            }
            int currPostingCounter = postingCounter;
            for (int i = 0; i < currPostingCounter; i+=2) {
                FileWriter mergedWriter;
                BufferedWriter mergedBuffer;
                File lFILE = new File(this.rootPath + "\\" + i);
                File blFILE = new File(this.rootPath + "\\" + (i+1));
                try {
                    mergedWriter = new FileWriter(this.rootPath + "\\" + "merged");
                    mergedBuffer = new BufferedWriter(mergedWriter);
                    mergeBetweenTempPostingFiles("" + (i), "" + (i+1), mergedBuffer, corpusDictionary);
                    mergedBuffer.close();
                    mergedWriter.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                lFILE.delete();
                blFILE.delete();
                postingCounter--;
                File mergedF = new File(this.rootPath+"\\"+"merged");
                File newFile = new File(this.rootPath+"\\"+ newName);
                mergedF.renameTo(newFile);
                newName++;
            }
            newName = 0;
        }
    }

    public void splitFinalPosting(HashMap<String, TermData> corpusDictionary) {
        HashMap<String, BufferedWriter> fileWriters = new HashMap<>();//hashmap for Filewriters
        HashMap<String, Integer> filePosition = new HashMap<>();//hashmap for Filewriters
        try {
            fileWriters.put("ABCD", new BufferedWriter(new FileWriter(this.rootPath + "\\ABCD")));
            fileWriters.put("EFGH", new BufferedWriter(new FileWriter(this.rootPath + "\\EFGH")));
            fileWriters.put("IJKL", new BufferedWriter(new FileWriter(this.rootPath + "\\IJKL")));
            fileWriters.put("MNOP", new BufferedWriter(new FileWriter(this.rootPath + "\\MNOP")));
            fileWriters.put("QRST", new BufferedWriter(new FileWriter(this.rootPath + "\\QRST")));
            fileWriters.put("UVWXYZ", new BufferedWriter(new FileWriter(this.rootPath + "\\UVWXYZ")));
            fileWriters.put("OTHER", new BufferedWriter(new FileWriter(this.rootPath + "\\OTHER")));
            filePosition.put("ABCD", new Integer(0));
            filePosition.put("EFGH", new Integer(0));
            filePosition.put("IJKL", new Integer(0));
            filePosition.put("MNOP", new Integer(0));
            filePosition.put("QRST", new Integer(0));
            filePosition.put("UVWXYZ", new Integer(0));
            filePosition.put("OTHER", new Integer(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
        BufferedReader postingFile;
        BufferedWriter fileBuffer;
        String currTerm, line, fileName, newLine;
        int position;
        try {
            postingFile = new BufferedReader(new FileReader(this.rootPath + "\\0"));//read posting
            line = postingFile.readLine();
            do {
                if (!letters.containsKey(line.toLowerCase().charAt(0))) {//if first char isn't a known letter
                    fileBuffer = fileWriters.get("OTHER");//get hte buffer to write
                    position = filePosition.get("OTHER").intValue();//get position to update Dic
                    fileName = "OTHER";//get name of file to update Dic
                } else {
                    char tmp = line.toLowerCase().charAt(0);
                    fileBuffer = fileWriters.get(letters.get(tmp));
                    position = filePosition.get(letters.get(tmp)).intValue();//todo
                    fileName = letters.get(tmp);
                }
                currTerm = line.substring(0, line.indexOf('|'));//get the term
                newLine = line.split("\\|")[2];//create line to wrtie without term and DF
//                Pair tmpPair = new Pair<>(corpusDictionary.get(currTerm).getKey(), position);//create tmppair to insert into Dic
//                corpusDictionary.replace(currTerm, tmpPair);//change the position for the term in the Dic
                corpusDictionary.get(currTerm).setPosition(position);
                fileBuffer.write(newLine + "\n");
                position += line.length() + 1;//increase the position for next line //todo
                filePosition.replace(fileName, position);//insert new position for next line //todo
                line = postingFile.readLine();
            } while (line != null && !line.equals("") && line.length()!=0);
            File pFile = new File(this.rootPath + "\\" + "0");
            fileWriters.get("ABCD").close();
            fileWriters.get("EFGH").close();
            fileWriters.get("IJKL").close();
            fileWriters.get("MNOP").close();
            fileWriters.get("QRST").close();
            fileWriters.get("UVWXYZ").close();
            fileWriters.get("OTHER").close();
            pFile.delete();
            postingFile.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method sort the lines in a temp posting file before writing to disk
     */
    private void sort(){
        allLines.sort((o1, o2) -> {
            String s1 = o1.substring(0, o1.indexOf('|'));
            String s2 = o2.substring(0, o2.indexOf('|'));
            return s1.toLowerCase().compareTo(s2.toLowerCase());
        });
    }

    public void resetPostingCounter() {
        Posting.postingFileCounter = 0;
    }
}
