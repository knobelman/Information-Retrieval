package Model;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;

/**
 * This class represents the parser object
 * hmNum -
 * hmSign -
 * hmDate -
 * hsDot -
 * stop_words -
 * moreThenOneWord -
 * allTermsInCorpus -
 */
public class Parse {

    private HashMap hmNum = new HashMap<String, String>();
    private HashMap hmSign = new HashMap<String, String>();
    private HashMap hmDate = new HashMap<String, String>();
    private HashSet hsDot = new HashSet<String>();
    private HashSet stop_words = new HashSet<String>();
    private Stack moreThenOneWord = new Stack<String>();
    private HashSet allTermsInCorpus = new HashSet <String>();
    int i;

    /**
     * C'tor
     * @param path - path of the corpus
     */
    public Parse(String path) {
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
        //initialize stop_words
        readStopWords(path+"\\STOPWORDS");//todo take care of "WORD
    }

    /**
     * this method read all the stop words from a given file
     * @param fileName
     */
    public void readStopWords(String fileName) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(fileName));
        } catch (FileNotFoundException e) {
        }
        try {
            String line = br.readLine();
            while (line != null) {
                stop_words.add(line);
                line = br.readLine();
            }
        } catch (IOException e) {
        } finally {
            try {
                br.close();
            } catch (IOException e) {
            }
        }
    }

    /**
     * this method parsing the document
     * @param document - to parse
     * @param stem - if stem or not
     * @return - document with parsed tokens
     */
    public Doc parsing(Doc document, boolean stem) {
        String text = document.getDoc_content();//("[: () -- ]");
        String[]tokenz = text.split("[: ()|]");
        for(i=0; i < tokenz.length; i++){//for to go over all tokenz
            String current = tokenz[i];
            String currValue = "";
            if(current.contains("--")){//fill stack with all words to work on
                for(String curr : current.split("--")){
                    moreThenOneWord.push(curr);
                }
            }
            else{
                moreThenOneWord.push(current);
            }
            if(moreThenOneWord.empty())
                continue;
            do {
                current = (String)moreThenOneWord.pop();
                current = trimming(current);
                if(current.length() == 0 || current.equals("")) {
                    continue;
                }
                if ((stop_words.contains(current) && (!current.equals("between") || current.equals("BETWEEN") || current.equals("Between"))) || hsDot.contains(current) || current.equals("%")) {//if the raw token is a stop word //todo - (stop_words.contains(current)
                    continue;
                }//
                if (current.contains("-") || current.equals("BETWEEN") || current.equals("Between") || current.equals("between")) {//10-part,6-7 etc'
                    if (current.contains("-"))
                        currValue = current;
                    else if(i + 1 < tokenz.length && !isValidNum(tokenz[i + 1])) {
                        continue;
                    }
                    else if (i + 1 < tokenz.length && isValidNum(tokenz[i + 1])) {
                        if (i + 2 < tokenz.length && tokenz[i + 2].equals("and"))
                            if (i + 3 < tokenz.length && isValidNum(tokenz[i + 3])) {
                                currValue = current + " " + tokenz[i + 1] + " " + tokenz[i + 2] + " " + tokenz[i + 3];
                                i += 3;
                            }
                    }
                } else if (current.charAt(0) == '$') {//if first char is '$' V
                    if (i + 1 >= tokenz.length)
                        currValue = dollarFirst(current, "");
                    else
                        currValue = dollarFirst(current, tokenz[i + 1]);
                } else if (isValidNum(current)) {//check if token is a valid number
                    if(!current.contains(",") && !current.contains(".") && current.length()>=4)
                        current = toCome(current);
                    if (i + 1 >= tokenz.length)
                        currValue = numberFirst(current, "", "", "");
                    else if (i + 2 >= tokenz.length)
                        currValue = numberFirst(current, tokenz[i + 1], "", "");
                    else if (i + 3 >= tokenz.length)
                        currValue = numberFirst(current, tokenz[i + 1], tokenz[i + 2], "");
                    else
                        currValue = numberFirst(current, tokenz[i + 1], tokenz[i + 2], tokenz[i + 3]);
                } else if (current.contains("%")) {//%6 etc'
                    currValue = current;
                } else if (hmDate.containsKey(current)) {//if first token is month
                    if (i + 1 >= tokenz.length)//if month comes alone
                        currValue = current;
                    else
                        currValue = dateFirst(current, trimming(tokenz[i + 1]));
                    if (currValue.contains("-"))//if is a date- ignore next token alone
                        i++;
                } else {
                    current = apostropheS(current);
                    currValue = current;
                }

                if (stem) {
                    Stemmer stemmer = new Stemmer();
                    stemmer.add(currValue.toCharArray(), currValue.length());
                    stemmer.stem();
                    currValue = stemmer.toString();
                }
                if(currValue.equals(""))
                    continue;
                addToDoc(currValue,document);
            }while(!moreThenOneWord.empty());
        }
        document.setSpecialWordCount();
        //System.out.println(allTermsInCorpus.size());
        return document;
    }

    /**
     *  this method adds the words Upper\Lower like needed
     * @param currValue - Value to add to terms
     */
    private void addToDoc(String currValue,Doc document) {
        if(stop_words.contains(currValue.toLowerCase()))
            return;
        if(currValue.toLowerCase().equals(currValue)) {//if is Lower case "yaniv"
            if(allTermsInCorpus.contains(currValue.toUpperCase())) {//if allTerm contains Upper case of current word and current word is lower case
                document.removeFromDoc(currValue.toUpperCase());
                allTermsInCorpus.remove(currValue.toUpperCase());
            }
            document.addTermToDoc(currValue);
            allTermsInCorpus.add(currValue);
        }
        else if(currValue.toUpperCase().equals(currValue)){//if is Upper case "YANIV"
            if(allTermsInCorpus.contains(currValue.toLowerCase())) {//if allTerm contains Lower case of current word and current word is Upper case
                return;
            }
            document.addTermToDoc(currValue);
            allTermsInCorpus.add(currValue);
        }
        else if(Character.isUpperCase(currValue.charAt(0))) {//if first char is upper "Yaniv"
            if(allTermsInCorpus.contains(currValue.toLowerCase())) {//if allTerm contains Lower case of current
                return;
            }
            document.addTermToDoc(currValue.toUpperCase());
            allTermsInCorpus.add(currValue.toUpperCase());
        }
        else {
            document.addTermToDoc(currValue);
            allTermsInCorpus.add(currValue);
        }
    }

    /**
     * Return a token without "'s"
     * @param current - the current token to check
     * @return
     */
    private String apostropheS(String current) {
        if(current.length()<2)
            return current;
        if(current.charAt(current.length()-1)=='s' && current.charAt(current.length()-2)=='\'')
            current = current.substring(0,current.length()-2);
        return current;
    }

    /**
     *
     * @param current - the current token to deal with
     * @return - the current token without clutter
     */
    private String trimming(String current) {
        if (current.equals("") || current.equals(" ") || current.equals("$") || current.equals("-") || current.equals(",")
                || current.equals(".") || current.equals("%"))//if empty token
            return "";
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


    private boolean isValidNum(String current){
        if(isNumeric(current))
            return true;
        else if(current.contains(".") && current.charAt(current.length()-1)!='.') {//number with .
            String[] currSplit = current.split("\\.");
            if(isNumeric(currSplit[0]) && isNumeric(currSplit[1]))
                return true;
        }
        else if(current.contains(",")){//number with ,
            String[] currSplit = current.split(",");
            for(String s : currSplit){
                //if(!s.matches("-?(0|[0-9]\\d*)"))
                if(!isNumeric(s))
                    return false;
            }
            if(currSplit[currSplit.length-1].length()<3)//if after ',' there are less then 3 digits
                return false;
            return true;
        }
        return false;
    }

    public static boolean isNumeric(String str)
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
     * Done: $100 million, $100
     * @param s1 - first token with the $
     * @param s2 - second token
     * @return
     */
    private String dollarFirst(String s1, String s2) {
        String curr = s1.substring(1);//lose the $
        if(curr.length()==0)
            return s1;
        if(!hmNum.containsKey(s2)){//second token isn't from hmNum
            curr = numberFirstMoney(curr,"") + " Dollars";
            i++;
        }
        else{//second token is from hmNum
            curr = toMillionMoney(numberFirstMoney(curr,s2)) + " Dollars";
            i+=2;
        }
        return curr;
    }

    /**
     * Takes care of number when it is about money
     * @param s1 - the number
     * @param s2 - the amount (million...)
     * @return - the correct value
     */
    private String numberFirstMoney(String s1, String s2) {
        if(hmNum.containsKey(s2)){//the second token is from hmNum (million, billion, bn, t...)
            if(s2.equals("trillion"))//because is 000B
                return numberFirst(s1,"","","") + hmNum.get(s2);
            else
                return numberFirst(s1,"","","") + " " + hmNum.get(s2);
        }
        else{
            return toMillionMoney(numberFirst(s1,"","",""));
        }
    }

    /**
     *
     * @param s String num + size
     * @return 100 bn -> 100000 M \ 100M -> 100 M
     */
    private String toMillionMoney(String s){
        String[] c = new String[2];
        if(s.contains(" "))
            c = s.split(" ");
        else {
            c[0] = s.substring(0,s.length()-1);
            c[1] = s.substring(s.length() - 1);
        }
        String result = "";
        if(c[1].equals("M") || c[1].equals("m") || c[1].equals("million"))
            result = c[0] + " " + "M";
        else if(c[1].equals("B") || c[1].equals("b") || c[1].equals("bn") || c[1].equals("billion"))
            result = c[0] + "000 " + "M";
        else if(c[1].equals("T") || c[1].equals("t") || c[1].equals("trillion"))
            result = c[0] + "000000 " + "M";
        else if(c[1].equals("K"))
            result = c[0] + ",000";
        else
            result = s;
        return result;
    }

    /**
     * Done - May 1994, May 14, May
     * @param s1 - first token (month)
     * @param s2 - second token (year,day)
     * @return
     */
    private String dateFirst(String s1, String s2) {
        String result = "";
        if(isNumeric(s2)){
            result = wordAndNumNumeric(s1 + " " + s2);
        }
        else{
            result = s1;
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
    private String numberFirst(String s1, String s2, String s3, String s4) {
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
                i++;
                return wordAndNumNumeric(s1 + " " + s2);
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

    /**
     * Func that turns numbers with coma into the right representative
     * @param current - the number with the coma (1,234 etc)
     * @return The correct representative of the number (1,234 -> 1.234K, 10,340 -> 10.34K)
     */
    private String comaToWord(String current) {
//
//        if(!current.contains(","))
//            current = toCome(current);
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
     *
     * @param current - number without coma
     * @return - array as if the number was with coma
     */
    private String toCome(String current) {
        String temp = "";
        int count = 0;
        for(int i =current.length()-1; i>=0; i--){
            if(count==3) {
                temp = ',' + temp;
                count=0;
            }
            temp = current.charAt(i) + temp;
            count++;
        }
        return temp;
    }

    /**
     * Func that turns numbers with dot into the right representative
     * @param current - the number with the dot (1045.54 etc)
     * @return The correct representative of the number (1045.56 -> 1.04556K, 1034.1 -> 1.034K)
     */
    private String dotToWord(String current) {
        String[] num = current.split("\\.");
        if(num[0].length()<4)
            return current;
        String result = "";
        boolean done = false;
        if(num.length>1) {//check that there is something after the dot
            for (int j = num[1].length() - 1; j >= 0; j--) { //for after the dot
                if (num[1].charAt(j) != '0' && !done)
                    done = true;
                if (done) {
                    result = num[1].charAt(j) + result;
                }
            }
        }

        int three = 0;
        for (int j = num[0].length() - 1; j >= 0; j--) {//for before the dot
            if (three == 3) {
                if (j <= 2){
                    result = '.' + result;
                }
                three = 0;
            }
            result = num[0].charAt(j) + result;
            three++;
        }
        result = result + sizeToLetter(num[0].length());
        return result;
    }

    private String sizeToLetter(int length) {
        if(length > 3 && length <= 6)
            return "K";
        else if(length > 6 && length <= 9)
            return "M";
        else if(length > 9 )
            return "B";
        return "";
    }

    /**
     * Func that turns numbers with words into the right representative - no money
     * @param current - string made of a word and number (100 Million, 14 May ...)
     * @return he correct representative of the number (100 Million -> 100M)
     */
    private String wordAndNumNumeric(String current){
        String[] curr = current.split(" "); //[100,Million] -> 100M
        int numIndex;
        if(isValidNum(curr[0]))
            numIndex = 0;
        else
            numIndex = 1;
        String result = "";
        if(curr.length == 3 || curr[1].contains("/")){// num fraction Dollars / num fraction
            result = current;
        }
        else if(hmDate.containsKey(curr[1-numIndex])){//its a Date
            if(curr[numIndex].length() == 4)//the number is a year
                result = comaToWord(curr[numIndex]) + "-" + hmDate.get(curr[1 - numIndex]);
            else {//the number is a day in a month
                if(Integer.parseInt(curr[numIndex])<10) //todo to fix tomorrow!
                    result = hmDate.get(curr[1 - numIndex]) + "-0" + comaToWord(curr[numIndex]);
                else
                    result = hmDate.get(curr[1 - numIndex]) + "-" + comaToWord(curr[numIndex]);
            }
        }
        else if(hmNum.containsKey(curr[1-numIndex])){//it's a number
            result = comaToWord(curr[numIndex]) + hmNum.get(curr[1 - numIndex]);
        }
        else if(hmSign.containsKey(curr[1-numIndex])) {//it's a sign
            result = comaToWord(curr[numIndex]) + hmSign.get(curr[1 - numIndex]);
        }
        return result;
    }
}