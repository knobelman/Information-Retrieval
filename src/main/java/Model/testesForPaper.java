package Model;

import Model.DataObjects.Term;
import Model.DataObjects.TermData;

import java.util.*;

public class testesForPaper {
    ArrayList<String> allLines;
    public void countNumbers(HashMap<String, TermData> corpusDictionary) {
        allLines = new ArrayList<>();
        Iterator it = corpusDictionary.entrySet().iterator();
        int counter = 0;
        String term = "";
        String tmp = "";
        System.out.println("Checking starts");
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            allLines.add(pair.getKey() + "|" + ((TermData)pair.getValue()).getTotalTF());
            term = (String)pair.getKey();
            if(term.charAt(term.length()-1)=='K' || term.charAt(term.length()-1)=='M' || term.charAt(term.length()-1)=='B')
                tmp = term.substring(0,term.length()-1);
            else
                tmp = term;
            if(isValidNum(tmp)) {
                counter++;
            }
            it.remove();
        }
        System.out.println("3. Number of numbers: "+counter);
    }

    protected boolean isValidNum(String current){
        if(current.equals(""))
            return false;
        if(current.contains("/") && !moreThenOne(current,'/'))//fraction
            return isValidFrac(current);
        String[] currSplit;
        if(isNumeric(current))//just number
            return true;
        else if(current.contains(".") && current.contains(",")){//10,456.6
            currSplit = current.split("[\\,\\.]");//[10,456,6] - check that all parts are numeric
            for(String tmp:currSplit){
                if(!isNumeric(tmp))
                    return false;
            }
            return true;
        }
        else if(current.contains(",") && !current.equals(",")){//number with ,
            currSplit = current.split(",");//[10,123] - check that all parts are numeric
            for(String s : currSplit){
                if(!isNumeric(s))
                    return false;
            }
            return true;
        }
        else if(current.contains(".")  && !moreThenOne(current,'.')){//number with .
            currSplit = current.split("\\.");//[10,6] - check that all parts are numeric
            for(String s : currSplit){
                if(!isNumeric(s))
                    return false;
            }
            return true;
        }
        return false;
    }

    protected static boolean isNumeric(String str)
    {
        if(str.length()==0)
            return false;
        for (char c : str.toCharArray())
        {
            if (!Character.isDigit(c))
                return false;
        }
        return true;
    }

    protected boolean isValidFrac(String s1){
        String[]split = s1.split("/");
        if(split.length<2)
            return false;
        if(isNumeric(split[0]) && isNumeric(split[1]))
            return true;
        return false;
    }

    public boolean moreThenOne(String str,char c){
        int count = 0;
        for (char curr : str.toCharArray())
        {
            if (curr == c)
                count++;
        }
        return count>1;
    }

    public void maxMinTotalTF(HashMap<String, TermData> corpusDictionary) {
        sort();
        List<String> first = allLines.subList(0, 11);
        List<String> last = allLines.subList(allLines.size()-11,allLines.size()-1);

        System.out.println("7. ");
        System.out.println("Smallest: ");
        for(String s:first)//smallest
            System.out.println(s);
        System.out.println("Biggest: ");
        for(String s:last)//biggest
            System.out.println(s);
    }

    /**
     * This method sort the lines in a temp posting file before writing to disk
     */
    private void sort(){
        allLines.sort((o1, o2) -> {
            String[]first = o1.split("\\|");
            String[]second = o2.split("\\|");
            String tf1 = first[1];
            String tf2 = second[1];
            int int1 = Integer.parseInt(tf1);
            int int2 = Integer.parseInt(tf2);
            if(int1<int2)
                return -1;
            else if(int1>int2)
                return 1;
            else
                return 0;
        });
    }
}
