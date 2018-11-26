package Model.Parsers;

import javafx.util.Pair;

/**
 * Created by Maor on 11/26/2018.
 */
public class DollarParser extends AParser{

    @Override
    protected String parsing(String s1, String s2, String s3, String s4) {
        return null;
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

}
