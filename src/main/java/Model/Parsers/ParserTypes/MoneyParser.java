package Model.Parsers.ParserTypes;

public class MoneyParser extends AParser {
    @Override
    protected String parsing(String s1, String s2, String s3, String s4) {
        return null;
    }

    /**
     * s1 = 1.7320, 450,000,
     *
     * @param s1 - price alone
     * @return - the correct way to show that price
     */
    protected String priceOnly(String s1) {
        s1 = addComa(s1);
        s1 = toMillionPrice(s1);
//        if(s1.contains(","))
//            s1 = delComa(s1);
        return s1;
    }

    /**
     * s1 = 1.7320, 450,000
     * s2 = m,bn,tn,million,billion,trillion
     *
     * @param s1 - price alone
     * @param s2 - m,bn,tn,million,billion,trillion
     * @return - the correct way to show that price in M
     */
    protected String priceAndSize(String s1, String s2) {
        s1 = addComa(s1);
        s1 = fixSize(s1,s2);
        //s1 = addComa(s1);
        //s1 = toMillionPrice(s1);
        if(s1.contains(","))
            s1 = delComa(s1);
        return s1 + " " + "M";
    }

    /**
     * Make price the right size 100 million -> 100000000, 100.5 million -> 100000000.5
     * @param s1 - price
     * @param s2 - size
     * @return
     */
    private String fixSize(String s1, String s2) {
        String[] s1SplitDot=null;
        String tmpS1 = s1;//number without dot
        boolean containsDot = false;
        if(s1.contains(".")){
            containsDot = true;
            s1SplitDot = s1.split("\\.");
            tmpS1 = s1SplitDot[0];
        }
        tmpS1 += hmPriceSize.get(s2);
        if(containsDot)
            tmpS1 += "." + s1SplitDot[1];
        return tmpS1;
    }

//    private String toMillionPrice(String s1) {
//        String res = toMillion(s1);
//        if(s1.split(",").length>=3)//if more then million
//            return res + " " + "M";
//        return res;
//    }

    /**
     * 450,000,000, 1,000,000
     *
     * @param s1 - price with comas
     * @return - the correct representation
     */
    private String toMillionPrice(String s1) {
        boolean done = false;
        String tmpS1 = s1;
        String[] s1SplitDot=null;
        boolean containsDot = false;
        if(s1.contains(".")){
            containsDot = true;
            done = true;
            s1SplitDot = s1.split("\\.");
            tmpS1 = s1SplitDot[0];
        }
        String[] tmp = tmpS1.split(",");
        if (tmp.length < 3)//if less then 1,000,000
        {
            return s1;
        }
        else {
            String s1Million = "";
            int count = 0;
            int j = tmp.length - 1;//the last array cell
            while (tmp[j].equals("000") && !done) {
                count++;
                j--;
                if (count == 2) {
                    done = true;
                    break;
                }
            }
            if (count != 2) {
                while (count != 2) {
                    s1Million = tmp[j] + s1Million;
                    count++;
                    j--;
                }
                s1Million = "." + s1Million;
            }
            for (; j >= 0; j--) {
                s1Million = tmp[j] + s1Million;
            }
            if(containsDot){
                s1Million += s1SplitDot[1];
            }

            return s1Million + " " + "M";
        }
    }

//    /**
//     * Check if number is with comas
//     *
//     * @param s1 - price
//     * @return - price with comas
//     */
//    private String addComa(String s1) {
//        if (s1.length() > 3 && !s1.contains(",")) {//if price>1,000 and without ','
//            if (s1.contains(".")) {//if number with .
//                String[] tmp = s1.split("\\.");
//                s1 = toComa(tmp[0]) + "." + tmp[1];
//            } else//number without .
//                s1 = toComa(s1);
//        }
//        return s1;
//    }

//    /**
//     * @param current - number without coma
//     * @return - the number with comas
//     */
//    private String toComa(String current) {
//        String temp = "";
//        int count = 0;
//        for (int i = current.length() - 1; i >= 0; i--) {
//            if (count == 3) {
//                temp = ',' + temp;
//                count = 0;
//            }
//            temp = current.charAt(i) + temp;
//            count++;
//        }
//        return temp;
//    }

    private String delComa(String s1) {
        String[] tmp = s1.split(",");
        String res = "";
        for(String cell: tmp){
            res += cell;
        }
        return res;
    }
}
