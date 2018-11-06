package Model;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by Maor on 10/30/2018.
 */
public class Parse {

    private HashMap hmNum = new HashMap<String, String>();
    private HashMap hmSign = new HashMap<String, String>();
    private HashMap hmDate = new HashMap<String, String>();
    private HashSet stop_words = new HashSet<String>();
    int i;

    public Parse() {
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

        //initialize stop_words
        readStopWords("C:/Users/Maor/Desktop/corpus/STOPWORDS");
    }

    public void readStopWords(String fileName) {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
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

    public void parsing(Doc document) {
        String text = document.getDoc_content();
        String[]tokenz = text.split("[: ()]");
        for(i=0; i < tokenz.length; i++){//for to go over all tokenz
            String current = tokenz[i];
            String currValue = "";
            if(current.equals("") || current.equals(",") || current.equals("\\.") || stop_words.contains(current)){
                continue;
            }
            else if(current.charAt(current.length()-1)=='.' || current.charAt(current.length()-1)==',')
                tokenz[i] = current.substring(0, current.length()-1);
            if(current.contains("-") || current.equals("Between") || current.equals("between")){//10-part,6-7 etc'
                if(current.contains("-"))
                    currValue = current;
                else if(i + 1 < tokenz.length && isValidNum(tokenz[i+1])){
                    if(i + 2 < tokenz.length && tokenz[i+2].equals("and"))
                        if(i + 3 < tokenz.length && isValidNum(tokenz[i+3]))
                            currValue = tokenz[i] +" "+ tokenz[i+1] +" "+ tokenz[i+2] +" "+ tokenz[i+3];
                }
            }
            else if(current.charAt(0) == '$'){//if first char is '$' V
                if(i + 1 >= tokenz.length)
                    currValue = dollarFirst(tokenz[i], "");
                else
                    currValue = dollarFirst(tokenz[i], tokenz[i + 1]);
            }
            else if(isValidNum(current)) {//check if token is a valid number
                if(i + 1 >= tokenz.length)
                    currValue = numberFirst(tokenz[i], "", "", "");
                else if(i + 2 >= tokenz.length)
                    currValue = numberFirst(tokenz[i], tokenz[i + 1], "", "");
                else if(i + 3 >= tokenz.length)
                    currValue = numberFirst(tokenz[i], tokenz[i + 1], tokenz[i + 2], "");
                else
                    currValue = numberFirst(tokenz[i], tokenz[i + 1], tokenz[i + 2], tokenz[i + 3]);
            }
            else if(current.contains("%")){//%6 etc'
                currValue = current;
            }
            else if(hmDate.containsKey(current)){//if first token is month
                if(i + 1 >= tokenz.length)//if month comes alone
                    currValue = tokenz[i];
                else
                    currValue = dateFirst(tokenz[i], tokenz[i + 1]);
                if(currValue.contains("-"))//if is a date- ignore next token alone
                    i++;
            }
            else{
                currValue = tokenz[i];
            }
            //System.out.println(currValue);
            document.addTermToDoc(currValue);
        }
    }

    private boolean isValidNum(String current){
        if(current.matches("-?(0|[1-9]\\d*)"))//regular number
            return true;
        else if(current.contains(".") && current.charAt(current.length()-1)!='.') {//number with .
            String[] currSplit = current.split("\\.");
            if (currSplit[0].matches("-?(0|[1-9]\\d*)") && currSplit[1].matches("-?(0|[1-9]\\d*)"))
                return true;
        }
        else if(current.contains(",")){//number with ,
            String[] currSplit = current.split(",");
            for(String s : currSplit){
                if(!s.matches("-?(0|[0-9]\\d*)"))
                    return false;
            }
            return true;
        }
        return false;
    }

    /**
     * Done: $100 million, $100
     * @param s1 - first token with the $
     * @param s2 - second token
     * @return
     */
    private String dollarFirst(String s1, String s2) {
        String curr = s1.substring(1);//lose the $
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
        if(s2.matches("-?(0|[1-9]\\d*)")) {
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
        if(hmDate.containsKey(s2)){//the second token is a month V
            i++;
            return wordAndNumNumeric(s1 + " " + s2);
        }
        else if(hmNum.containsKey(s2)){//the token is a number/word V
            if((s3.equals("U.S.") && s4.equals("dollars"))){//320 million U.S. dollars etc'
                i+=3;
                return toMillionMoney(numberFirstMoney(s1,s2)) + " " + s4;
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
        else if(s1.contains(",")){//450,000
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
        String[] num = current.split(",");
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
        for (int j = num[1].length() - 1; j >= 0; j--) { //for after the dot
            if (num[1].charAt(j) != '0' && !done)
                done = true;
            if (done) {
                result = num[1].charAt(j) + result;
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
                if(Integer.parseInt(curr[numIndex])<10)
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

    public static void main(String[] args){
        /**
         * NUMBERS
         */
        int pass = 0;
        int count = 0;

        System.out.println("~*~ Numbers Tests ~*~");
        pass += test("1","10,123","10.123K");
        count +=1;
        pass += test("2", "123 Thousand","123K");
        count +=1;
        pass += test("3","1010.56","1.01056K");
        count +=1;
        pass += test("4","10,123,000","10.123M");
        count +=1;
        pass += test("6","55 Million","55M");
        count +=1;
        pass += test("7","1010.56","1.01056K");
        count +=1;
        pass += test("8","10,123,000,000","10.123B");
        count +=1;
        pass += test("9","55 Billion","55B");
        count +=1;
        pass += test("10","7 Trillion","7000B");
        count +=1;
        pass += test("11","204","204");
        count +=1;
        pass += test("12","-500","-500");
        count +=1;

        /**
         * Percentage
         */
        System.out.println("~*~ Percentage Tests ~*~");
        pass += test("1","6%","6%");
        count +=1;
        pass += test("2","10.6 percent","10.6%");
        count +=1;
        pass += test("3","6%","6%");
        count +=1;
        pass += test("4","10.6 percentage","10.6%");
        count +=1;
        pass += test("5","1000%","1000%");
        count +=1;

        /**
         * Prices
         */
        System.out.println("~*~ Prices Tests ~*~");
        pass += test("1","1.7320 Dollars","1.7320 Dollars");
        count +=1;
        pass += test("2","22 3/4 Dollars","22 3/4 Dollars");
        count +=1;
        pass += test("3","$450,000","450,000 Dollars");
        count +=1;
        pass += test("4","1,000,000 Dollars","1 M Dollars");
        count +=1;
        pass += test("5","$450,000,000","450 M Dollars");
        count +=1;
        pass += test("6","$100 million","100 M Dollars");
        count +=1;
        pass += test("7","20.6m Dollars","20.6 M Dollars");
        count +=1;
        pass += test("8","$100 billion","100000 M Dollars");
        count +=1;
        pass += test("9","100bn Dollars","100000 M Dollars");
        count +=1;
        pass += test("10","100 billion U.S. dollars","100000 M Dollars");
        count +=1;
        pass += test("11","320 million U.S. dollars","320 M Dollars");
        count +=1;
        pass += test("12","1 trillion U.S. dollars","1000000 M Dollars");
        count +=1;

        /**
         * Date
         */
        System.out.println("~*~ Date Tests ~*~");
        pass += test("1","14 MAY","05-14");
        count +=1;
        pass += test("2","14 May","05-14");
        count +=1;
        pass += test("3","JUNE 4","06-04");
        count +=1;
        pass += test("4","June 4","06-04");
        count +=1;
        pass += test("5","May 1994","1994-05");
        count +=1;
        pass += test("6","MAY 1994","1994-05");
        count +=1;

        /**
         * Hyphen
         */
        System.out.println("~*~ Hyphen Tests ~*~");
        pass += test("1","step-by-step","step-by-step");
        pass += test("2","1-1","1-1");
        count +=1;
        System.out.println("~*~ SUMMERY: PASS " + pass + "/" + count +" ~*~");
    }
//
    public static int test(String number, String input,String output) {
        Parse p = new Parse();
        Doc doc1 = new Doc();
        doc1.setDoc_content(input);
        p.parsing(doc1);
        ArrayList<Term> termsReturn = doc1.getTermsInDoc();
        String result = termsReturn.get(0).toString();
//        for(Term t: termsReturn){
//            result = result + t.toString()+" ";
//        }
        //result = result.substring(0,result.length()-1);
        if (result.equals(output)) {
            System.out.println("TEST " + number + " PASS");
            return 1;
        } else {
            System.out.println("TEST " + number + " FAILED| INPUT: " + input + "| OUTPUT: " + result + "| EXCEPTED: " + output);
            return 0;
        }
    }
}
