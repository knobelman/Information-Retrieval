package Model;

import Model.DataObjects.TermData;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class testesForPaper {
    public void countNumbers(HashMap<String, TermData> corpusDictionary) {
        Iterator it = corpusDictionary.entrySet().iterator();
        int counter = 0;
        String term = "";
        String tmp = "";
        System.out.println("Checking starts");
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            term = (String)pair.getKey();
            if(term.charAt(term.length()-1)=='K' || term.charAt(term.length()-1)=='M' || term.charAt(term.length()-1)=='B')
                tmp = term.substring(0,term.length()-1);
            else
                tmp = term;
            if(isValidNum(tmp)) {
                counter++;
                System.out.println(term);
            }
            it.remove();
        }
        System.out.println(counter);
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

    public void countCountries() {//
    }
}
