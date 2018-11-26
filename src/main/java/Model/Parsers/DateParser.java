package Model.Parsers;

import javafx.util.Pair;

/**
 * Created by Maor on 11/26/2018.
 */
public class DateParser extends AParser {


    @Override
    protected String parsing(String s1, String s2, String s3, String s4) {
        String value = monthFirst(s1,s2);
        return value;
    }

    /**
     * Done - May 1994, May 14, May
     *
     * @param s1 - first token (month)
     * @param s2 - second token (year,day)
     * @return
     */
    private String monthFirst(String s1, String s2) {
        String result = "";
        if (isNumeric(s2)) {
            if (hmDate.containsKey(s1)) {//its a Date
                if (s2.length() >= 4)//the number is a year
                    result = s2 + "-" + hmDate.get(s1);
                else {//the number is a day in a month
                    if (Integer.parseInt(s2) < 10)
                        result = hmDate.get(s1) + "-0" + s2;
                    else
                        result = hmDate.get(s1) + "-" + s2;
                }
            }
        } else {
            result = s1;
        }
        return result;
    }
}
