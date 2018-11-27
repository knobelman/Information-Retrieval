package Model.Parsers.ParserTypes;

import Model.Parsers.ParserTypes.MoneyParser;

/**
 * Created by Maor on 11/26/2018.
 */
public class DollarParser extends MoneyParser {

    @Override
    protected String parsing(String s1, String s2, String s3, String s4) {
        String price;
        if(s1.charAt(0)=='$')
            price = s1.substring(1);//get the price out of $price
        else
            price = s1;
        if(!isValidNum(price))
            return "";
        if(s2.equals("") || !hmPriceSize.containsKey(s2))
            return priceOnly(price) + " " + "Dollars";
        else if(s3.equals("")){
            i=1;
        }
        else if(s4.equals("")){
            i=2;
        }
        else{
            i=3;
        }
        return priceAndSize(price, s2) + " " + "Dollars";
    }

    /**
     *
     * @param s1 - valid number
     * @param s2 - the word Dollars
     * @return
     */
    public String PriceDollars(String s1, String s2){
        return priceOnly(s1) + " " + "Dollars";
    }

    /**
     *
     * @param s1 valid number
     * @param s2 fraction to check
     * @param s3 the word Dollars
     * @return
     */
    public String PriceFractionDollars(String s1, String s2, String s3){
        if(isValidFrac(s2)) {
            i=2;
            return priceOnly(s1) + " " + s2 + " " + "Dollars";
        }
        return s1;
    }

    /**
     *
     * @param s1 fraction to check
     * @return - is top is valid num and bottom is valid num
     */
    private boolean isValidFrac(String s1){
        String[]split = s1.split("/");
        if(isValidNum(split[0]) && isValidNum(split[0]))
            return true;
        return false;
    }

}
