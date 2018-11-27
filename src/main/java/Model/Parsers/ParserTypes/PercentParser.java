package Model.Parsers.ParserTypes;

import Model.Parsers.ParserTypes.AParser;

public class PercentParser extends AParser {
    @Override
    /**
     * s1- valid num
     * s2- percent\percentage OR empty
     */
    protected String parsing(String s1, String s2, String s3, String s4) {
        if(s2.equals(""))
            return percentSignCheck(s1);//%6
        else {
            i=1;
            return s1 + "%";//6 percent
        }
    }

    /**
     * Check if after % there is number
     * @param s1 - the %num
     * @return - "" if not a good percent or term if is good
     */
    private String percentSignCheck(String s1) {
        if(s1.charAt(s1.length()-1)=='%' && isValidNum(s1.substring(0,s1.length()-1))) {
            i = 0;
            return s1;
        }
        i=0;
        return "";
    }

    /**
     * Check if s1 is a number and s2 is percent\percentage
     * @param s1 - valid num
     * @param s2 - the percent string - percent\percentage
     * @return - 10.8% or "" if not valid percent
     */
//    private String percentWordCheck(String s1, String s2) {
//        if(isValidNum(s1)){
//            if(s2.equals("percent") || s2.equals("percentage")){
//                i = 1;
//                return s1 + "%";
//            }
//        }
//        i=0;
//        return s1;
//    }
}
