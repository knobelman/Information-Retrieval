package Model.Parsers;

import javafx.util.Pair;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by Maor on 11/26/2018.
 */
public abstract class AParser {
    protected HashMap hmNum ;
    protected HashMap hmSign;
    protected HashMap hmDate;
    protected HashSet hsDot ;
    protected int i;

    public AParser() {
        hmNum = new HashMap<String, String>();
        hmSign = new HashMap<String, String>();
        hmDate = new HashMap<String, String>();
        hsDot = new HashSet<String>();
        //initialize hm
        hmNum.put("Thousand","K"); hmNum.put("Million","M"); hmNum.put("Billion","B"); hmNum.put("Trillion","000B");
        hmNum.put("thousand","K"); hmNum.put("million","M"); hmNum.put("billion","B"); hmNum.put("trillion","000B");
        hmNum.put("k","K"); hmNum.put("m","M"); hmNum.put("b","B"); hmNum.put("bn","B"); hmNum.put("t","000B"); hmNum.put("tn","000B");
        hmSign.put("percent", "%"); hmSign.put("percentage","%"); hmSign.put("$"," Dollars"); hmSign.put("Dollars"," Dollars");
        hmDate.put("Jan","01"); hmDate.put("JAN","01"); hmDate.put("January","01");  hmDate.put("JANUARY","01");
        hmDate.put("Feb","02"); hmDate.put("FEB","02"); hmDate.put("February","02"); hmDate.put("FEBRUARY","02");
        hmDate.put("Mar","03"); hmDate.put("MAR","03"); hmDate.put("March","03");    hmDate.put("MARCH","03");
        hmDate.put("Apr","04"); hmDate.put("APR","04"); hmDate.put("April","04");    hmDate.put("APRIL","04");
        hmDate.put("May","05"); hmDate.put("MAY","05");
        hmDate.put("Jun","06"); hmDate.put("JUN","06"); hmDate.put("June","06");     hmDate.put("JUNE","06");
        hmDate.put("Jul","07"); hmDate.put("JUL","07"); hmDate.put("July","07");     hmDate.put("JULY","07");
        hmDate.put("Aug","08"); hmDate.put("AUG","08"); hmDate.put("August","08");   hmDate.put("AUGUST","08");
        hmDate.put("Sep","09"); hmDate.put("SEP","09"); hmDate.put("September","09");hmDate.put("SEPTEMBER","09");
        hmDate.put("Oct","10"); hmDate.put("OCT","10"); hmDate.put("October","10");  hmDate.put("OCTOBER","10");
        hmDate.put("Nov","11"); hmDate.put("NOV","11"); hmDate.put("November","11"); hmDate.put("NOVEMBER","11");
        hmDate.put("Dec","12"); hmDate.put("DEC","12"); hmDate.put("December","12"); hmDate.put("DECEMBER","12");
        hsDot.add(','); hsDot.add('.'); hsDot.add(':'); hsDot.add(';'); hsDot.add('|'); hsDot.add(' '); hsDot.add('"');
        hsDot.add('['); hsDot.add(']'); hsDot.add('*'); hsDot.add('\'');hsDot.add('+'); hsDot.add('"'); hsDot.add('\\');
        hsDot.add('?'); hsDot.add('-'); hsDot.add('&'); hsDot.add('`'); hsDot.add('!'); hsDot.add('/'); hsDot.add('#');
        int i;
    }

    protected abstract String parsing(String s1, String s2, String s3, String s4);

    public Pair<Integer, String> parse(String s1, String s2, String s3, String s4){return null;}

    protected boolean isValidNum(String current){
        if(isNumeric(current))
            return true;
        else if(current.contains(".") && current.charAt(current.length()-1)!='.') {//number with .
            String[] currSplit = current.split("\\.");
            if(isValidNum(currSplit[0]) && isValidNum(currSplit[1]))
                return true;
        }
        else if(current.contains(",") && !current.equals(",")){//number with ,
            String[] currSplit = current.split(",");
            for(String s : currSplit){
                if(!isNumeric(s))
                    return false;
            }
            if(currSplit[currSplit.length-1].length()<3)//if after ',' there are less then 3 digits
                return false;
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

    /**
     *
     * @param current - the current token to deal with
     * @return - the current token without clutter
     */
    protected String trimming(String current) {
        if (current.equals("") || current.equals(" ") || current.equals("$") || current.equals("-") || current.equals(",")
                || current.equals(".") || current.equals("%"))//if empty token
            return "";
        if(current.charAt(0)=='-' && !isValidNum(current.substring(1, current.length())))// if -word and not -number
            return trimming(current.substring(1, current.length()));
        if (hsDot.contains(current.charAt(current.length() - 1))) {//if there is a sign at the end
            do {
                current = current.substring(0, current.length() - 1);
            } while (current.length() > 0 && hsDot.contains(current.charAt(current.length() - 1)));
            if (current.length() == 0) {
                return "";
            }
        }
        if ((hsDot.contains(current.charAt(0)) && current.charAt(0)!='-') || current.charAt(0)=='%') {//if there is a sign in the beginning
            do {
                current = current.substring(1, current.length());
            } while (current.length() > 0 && hsDot.contains(current.charAt(0)));
            if (current.length() == 0) {
                return "";
            }
        }
        return current;
    }

    /**
     * Func that turns numbers with words into the right representative - no money
     * @param current - string made of a word and number (100 Million, 14 May ...)
     * @return he correct representative of the number (100 Million -> 100M)
     */
    protected String wordAndNumNumeric(String current){
        String[] curr = current.split(" "); //[100,Million] -> 100M
        int numIndex;
        if(isValidNum(curr[0]))
            numIndex = 0;
        else
            numIndex = 1;
        String result = "";
        if(curr.length >= 3 || curr[1].contains("/")){// num fraction Dollars / num fraction
            result = current;
        }
        else if(hmNum.containsKey(curr[1-numIndex])){//it's a number
            result = comaToWord(curr[numIndex]) + hmNum.get(curr[1 - numIndex]);
        }
        else if(hmSign.containsKey(curr[1-numIndex])) {//it's a sign
            result = comaToWord(curr[numIndex]) + hmSign.get(curr[1 - numIndex]);
        }
        return result;
    }

    /**
     * Func that turns numbers with coma into the right representative
     * @param current - the number with the coma (1,234 etc)
     * @return The correct representative of the number (1,234 -> 1.234K, 10,340 -> 10.34K)
     */
    private String comaToWord(String current) {
        String[]num = current.split(",");
        String result = "";
        boolean done = false;
        for (int i = num.length - 1; i > 0; i--) {
            for (int j = num[i].length() - 1; j >= 0; j--) {
                if (num[i].charAt(j) != '0' && !done)
                    done = true;
                if (done) {
                    result = num[i].charAt(j) + result;
                }
            }
            if (i == 1 && done)
                result = '.' + result;
        }
        char letter = ' ';
        if (num.length == 2)
            letter = 'K';
        else if (num.length == 3)
            letter = 'M';
        else if (num.length == 4)
            letter = 'B';
        else
            letter = 0;
        if (letter == 0){
            result = num[0] + result;
        }else {
            result = num[0] + result + letter;
        }
        return result;
    }

    /**
     *  Done: 123 Thousand, 55 Million, 55 Billion, 7 Trillion, percent, percentage, 2 3/4, 2 3/4 Dollars,
     *        100 billion/million/billion U.S. dollars, 4 MAY, 14 JUNE,
     *
     *  Todo: , . % 100 bn Dollars
     * @param s1 - first token
     * @param s2 - second token
     * @param s3 - third token
     * @param s4 - fourth token
     * @return
     */
    protected String numberFirst(String s1, String s2, String s3, String s4) {
        if(s1.length()<=2 & hmDate.containsKey(s2)){//the second token is a month V
            i++;
            return wordAndNumNumeric(s1 + " " + s2);
        }
        else if(hmNum.containsKey(s2)){//the token is a number/word V
            if((s3.equals("U.S.") && s4.equals("dollars"))){//320 million U.S. dollars etc'
                i+=3;
                return toMillionMoney(numberFirstMoney(s1,s2)) + " " + "Dollars";
            }
            else if(s3.equals("Dollars")) {//320 million Dollars etc'
                i+=2;
                return toMillionMoney(numberFirstMoney(s1, s2)) + " " + s3;
            }
            else {
                i+=1;
                return wordAndNumNumeric(s1 + " " + s2);
            }
        }
        else if(hmSign.containsKey(s2)){//the second token is a sign from hmSign V
            i+=1;
            if(s2.equals("Dollars")){//price Dollar
                return numberFirstMoney(s1,"") + " Dollars";
            }
            else
                return wordAndNumNumeric(s1 + " " + s2);//number percent
        }
        else if(s2.contains("/")){//the second token is a fraction V
            if(s3.equals("Dollars")){//the third word is "Dollars" - num fraction Dollars
                i+=2;
                return wordAndNumNumeric(s1 + " " + s2 + " " + s3);
            }
            else{//num fraction
                String[] tmp = s2.split("/");
                if(tmp.length >=2 && isValidNum(tmp[0]) && isValidNum(tmp[1])) {
                    i++;
                    return wordAndNumNumeric(s1 + " " + s2);
                }
                else
                    return s1;
            }
        }
        else if(s1.contains(",")){// || s1.length()>=4){//450,000
            i++;
            return comaToWord(s1);
        }
        else if(s1.contains(".")) {//2.5
            return dotToWord(s1);
        }
        else
            return s1;
    }
}
