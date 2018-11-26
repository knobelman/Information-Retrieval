package Model.Parsers;

import javafx.util.Pair;

/**
 * Created by Maor on 11/26/2018.
 */

public class ParserClassifier extends AParser{
    private Pair<Integer,String>doneTerm;
    private NumberParser numberParser;
    private DollarParser dollarParser;
    private DateParser dateParser;

    public ParserClassifier() {
        numberParser = new NumberParser();
        dollarParser = new DollarParser();
        dateParser = new DateParser();
    }

    public Pair<Integer, String> parse(String s1, String s2, String s3, String s4){
        String term = parsing(s1, s2, s3, s4);
        return new Pair<>(i,term);
    }

    protected String parsing(String s1, String s2, String s3, String s4){
        String current = s1;
        current = trimming(current);
        String currValue = "";
        if (current.contains("-") || current.equals("BETWEEN") || current.equals("Between") || current.equals("between")) {//10-part,6-7 etc'
            if (current.contains("-"))
                currValue = current;
            else if (!s2.equals("") && !isValidNum(s2)) {
                //continue;
            }
            else if (!s2.equals("") && isValidNum(s2)) {
                if (!s3.equals("") && s3.equals("and"))
                    if (!s4.equals("") && isValidNum(s4)) {
                        currValue = current + " " + s2 + " " + s3 + " " + s4;
                        i += 3;
                    }
            }
        }

        else if (current.charAt(0) == '$') {//if first char is '$' V
            if (s2.equals(""))
                currValue = dollarParser.parsing(current, "","","");
            else
                currValue = dollarParser.parsing(current, s2,"","");
        }

        else if (isValidNum(current)) {//check if token is a valid number
//            if (!current.contains(",") && !current.contains(".") && current.length() >= 4)
//                current = toComa(current);
//            if (current.contains(",") && current.contains(".") && current.length() >= 4)
//                current = vanishDot(current);
            if (s2.equals(""))
                currValue = numberParser.parsing(current, "", "", "");
            else if (s3.equals(""))
                currValue = numberParser.parsing(current, s2, "", "");
            else if (s4.equals(""))
                currValue = numberParser.parsing(current, s2, s3, "");
            else
                currValue = numberParser.parsing(current, s2, s3, s4);
        }

        else if (current.contains("%")) {//%6 etc'
            currValue = current;
        }

        else if (hmDate.containsKey(current)) {//if first token is month
            if (s2.equals(""))//if month comes alone
                currValue = current;
            else {
                currValue = dateParser.parsing(current, trimming(s2),"","");
            }
            if (currValue.contains("-"))//if is a date- ignore next token alone
                i++;
        }

        else {
            current = apostropheS(current);
            currValue = current;
        }
        return currValue;
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
}
