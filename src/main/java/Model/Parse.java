package Model;
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
        stop_words.add("a"); stop_words.add("a's"); stop_words.add("able"); stop_words.add("about"); stop_words.add("above"); stop_words.add("according");
        stop_words.add("accordingly"); stop_words.add("across"); stop_words.add("actually");
        stop_words.add("after"); stop_words.add("afterwards"); stop_words.add("again"); stop_words.add("against"); stop_words.add("ain't");
        stop_words.add("all"); stop_words.add("allow"); stop_words.add("allows");
        stop_words.add("almost"); stop_words.add("alone"); stop_words.add("along"); stop_words.add("already"); stop_words.add("also");
        stop_words.add("although"); stop_words.add("always"); stop_words.add("am"); stop_words.add("among");
        stop_words.add("amongst"); stop_words.add("an"); stop_words.add("and"); stop_words.add("another"); stop_words.add("any");
        stop_words.add("anybody"); stop_words.add("anyhow"); stop_words.add("anyone"); stop_words.add("anything"); stop_words.add("anyway");
        stop_words.add("anyways"); stop_words.add("anywhere"); stop_words.add("apart"); stop_words.add("appear"); stop_words.add("appreciate");
        stop_words.add("appropriate"); stop_words.add("are"); stop_words.add("aren't"); stop_words.add("around"); stop_words.add("as");
        stop_words.add("aside"); stop_words.add("ask"); stop_words.add("asking"); stop_words.add("associated"); stop_words.add("at");
        stop_words.add("available"); stop_words.add("away"); stop_words.add("awfully"); stop_words.add("b"); stop_words.add("be");
        stop_words.add("became"); stop_words.add("because"); stop_words.add("become"); stop_words.add("becomes"); stop_words.add("becoming");
        stop_words.add("been"); stop_words.add("before"); stop_words.add("beforehand"); stop_words.add("behind"); stop_words.add("being");
        stop_words.add("believe"); stop_words.add("below"); stop_words.add("beside"); stop_words.add("besides"); stop_words.add("best");
        stop_words.add("better"); stop_words.add("between"); stop_words.add("beyond"); stop_words.add("both"); stop_words.add("brief");
        stop_words.add("but"); stop_words.add("by"); stop_words.add("c"); stop_words.add("c'mon"); stop_words.add("c's");
        stop_words.add("came"); stop_words.add("can"); stop_words.add("can't"); stop_words.add("cannot"); stop_words.add("cant");
        stop_words.add("cause"); stop_words.add("causes"); stop_words.add("certain"); stop_words.add("certainly"); stop_words.add("changes");
        stop_words.add("clearly"); stop_words.add("co"); stop_words.add("com"); stop_words.add("come"); stop_words.add("comes");
        stop_words.add("concerning"); stop_words.add("consequently"); stop_words.add("consider"); stop_words.add("considering"); stop_words.add("contain");
        stop_words.add("containing"); stop_words.add("contains"); stop_words.add("corresponding"); stop_words.add("could"); stop_words.add("couldn't");
        stop_words.add("course"); stop_words.add("currently"); stop_words.add("d"); stop_words.add("definitely"); stop_words.add("described");
        stop_words.add("despite"); stop_words.add("did"); stop_words.add("didn't"); stop_words.add("different"); stop_words.add("do");
        stop_words.add("does"); stop_words.add("doesn't"); stop_words.add("doing"); stop_words.add("don't"); stop_words.add("done");
        stop_words.add("down"); stop_words.add("downwards"); stop_words.add("during"); stop_words.add("e"); stop_words.add("each");
        stop_words.add("edu"); stop_words.add("eg"); stop_words.add("eight"); stop_words.add("either"); stop_words.add("else");
        stop_words.add("elsewhere"); stop_words.add("enough"); stop_words.add("entirely"); stop_words.add("especially"); stop_words.add("et");
        stop_words.add("etc"); stop_words.add("even"); stop_words.add("ever"); stop_words.add("every"); stop_words.add("everybody");
        stop_words.add("everyone"); stop_words.add("everything"); stop_words.add("everywhere"); stop_words.add("ex"); stop_words.add("exactly");
        stop_words.add("example"); stop_words.add("except"); stop_words.add("f"); stop_words.add("far"); stop_words.add("few");
        stop_words.add("fifth"); stop_words.add("first"); stop_words.add("five"); stop_words.add("followed"); stop_words.add("following");
        stop_words.add("follows"); stop_words.add("for"); stop_words.add("former"); stop_words.add("formerly"); stop_words.add("forth");
        stop_words.add("four"); stop_words.add("from"); stop_words.add("further"); stop_words.add("furthermore"); stop_words.add("g");
        stop_words.add("get"); stop_words.add("gets"); stop_words.add("getting"); stop_words.add("given"); stop_words.add("gives");
        stop_words.add("go"); stop_words.add("goes"); stop_words.add("going"); stop_words.add("gone"); stop_words.add("got");
        stop_words.add("gotten"); stop_words.add("greetings"); stop_words.add("h"); stop_words.add("had"); stop_words.add("hadn't");
        stop_words.add("happens"); stop_words.add("hardly"); stop_words.add("has"); stop_words.add("hasn't"); stop_words.add("have");
        stop_words.add("haven't"); stop_words.add("having"); stop_words.add("he"); stop_words.add("he's"); stop_words.add("hello");
        stop_words.add("help"); stop_words.add("hence"); stop_words.add("her"); stop_words.add("here"); stop_words.add("here's");
        stop_words.add("hereafter"); stop_words.add("hereby"); stop_words.add("herein"); stop_words.add("hereupon"); stop_words.add("hers");
        stop_words.add("herself"); stop_words.add("hi"); stop_words.add("him"); stop_words.add("himself"); stop_words.add("his");
        stop_words.add("hither"); stop_words.add("hopefully"); stop_words.add("how"); stop_words.add("howbeit"); stop_words.add("however");
        stop_words.add("i"); stop_words.add("i'd"); stop_words.add("i'll"); stop_words.add("i'm"); stop_words.add("i've");
        stop_words.add("ie"); stop_words.add("if"); stop_words.add("ignored");
    }

    public void parsing(Doc document) {
        String text = document.getDoc_content();
        String[]tokenz = text.split("[: ()]");
        for(int i=0; i < tokenz.length; i++){//for to go over all tokenz
            String current = tokenz[i];
            String currValue = "";
            if(current.matches("-?(0|[1-9]\\d*)")) {//check if token is made of only digits
                currValue = numberFirst(tokenz[i], tokenz[i + 1], tokenz[i + 2], tokenz[i + 3]);
            }
            else if(current.charAt(0) == '$'){//if first char is '$'
                currValue = dollarFirst(tokenz[i], tokenz[i + 1]);
            }
            else if(hmDate.containsKey(current)){//if first token is month
                currValue = dateFirst(tokenz[i], tokenz[i + 1]);
                if(currValue.contains("-"))//if the second token is a year\month, ignore that token
                    i++;
            }
            System.out.print(currValue);
        }
    }

    /**
     * Done: $100 million, $100,
     * @param s1 - first token with the $
     * @param s2 - second token
     * @return
     */
    private String dollarFirst(String s1, String s2) {
        String curr = s1.substring(1,s1.length());
        if(!hmNum.containsKey(s2)){//second token isn't from hmNum
            curr = numberFirst(curr,"","","") + " Dollars";
        }
        else{//second token is from hmNum
            curr = numberFirst(curr,s2,"","") + " Dollars";
        }
        System.out.print(curr);
        return "";
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
            result = wordAndNum(s1 + " " + s2);
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
            return wordAndNum(s1 + " " + s2);
        }
        else if(hmNum.containsKey(s2)){//the token is a number/word
            if(s3.equals("U.S.") && s4.equals("dollars")){
                return toMillion(wordAndNum(s1 + " " + s2)) + " " + s4;
            }
            else
                return wordAndNum(s1 + " " + s2);
        }
        else if(hmSign.containsKey(s2)){//the second token is a sign V
            return wordAndNum(s1 + " " + s2);
        }
        else if(s2.contains("/")){//the second token is a fraction V
            if(s3.equals("Dollars")){//the third word is "Dollars" - num fraction Dollars
                return wordAndNum(s1 + " " + s2 + " " + s3);
            }
            else{//num fraction
                return wordAndNum(s1 + " " + s2);
            }
        }
        else
            return s1;
    }

    private String toMillion(String s) {
        String c = s.substring(s.length()-1,s.length());
        String result = "";
        if(c.equals("M") || c.equals("m"))
            result = s.substring(0,s.length()-1) + " " + c;
        else if(c.equals("B") || c.equals("b") || c.equals("bn"))//todo - fix bn in wordAndNum
            result = s.substring(0,s.length()-1) + "000 " + "M";
        else if(c.equals("T") || c.equals("t"))
            result = s.substring(0,s.length()-1) + "000000 " + "M";
        return result;
    }


    /**
     * Func that turns numbers with coma into the right representative
     * @param current - the number with the coma (1,234 etc)
     * @return The correct representative of the number (1,234 -> 1.234K, 10,340 -> 10.34K)
     */
    public String comaToWord(String current) {
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
        result = num[0] + result + letter;
        return result;
    }

    /**
     * Func that turns numbers with dot into the right representative
     * @param current - the number with the dot (1045.54 etc)
     * @return The correct representative of the number (1045.56 -> 1.04556K, 1034.1 -> 1.034K)
     */
    public String dotToWord(String current) {
        String[] num = current.split("\\."); //[1020,400] -> 1.0204K
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
     * Func that turns numbers with words into the right representative
     * @param current - string made of a word and number (100 Million, 14 May ...)
     * @return he correct representative of the number (100 Million -> 100M)
     */
    public String wordAndNum(String current){
        String[] curr = current.split(" "); //[100,Million] -> 100M
        int numIndex;
        if(isNumeric(curr[0]))
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

    private boolean isNumeric(String strNum)
    {
        if(!strNum.contains(","))
            return strNum.matches("-?\\d+(\\.\\d+)?");
        else{
            return true;
        }
    }

    public String numberAndSign(String current){
        String curr =  current.substring(1);
        String result = wordAndNum(curr) + hmNum.get("$");
        return result;
    }
}
