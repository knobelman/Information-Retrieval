import Model.DataObjects.ParseableObjects.Doc;
import Model.DataObjects.Term;
import Model.Parsers.ParsingProcess.DocParsingProcess;
import Model.Parsers.ParsingProcess.IParsingProcess;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Maor on 11/7/2018.
 */
public class ParseTests {
    public static void main(String[] args){
        int pass = 0;
        int count = 0;
//
        /**
         * NUMBERS
         */
//        System.out.println("~*~ Numbers Tests ~*~");
        pass += test("0","Mr Tomiichi Murayama, Japan's new Socialist prime minister, yesterday\n" +
                "attempted to win confidence in his much-criticised government by pledging to\n" +
                "pursue moderate conservative policies.\n" +
                "He acknowledged that there was anxiety over his appointment and pledged to\n" +
                "'make further efforts to make my cabinet more trustworthy and reliable'.\n" +
                "The three-party alliance of the conservative Liberal Democratic party with\n" +
                "its traditional enemy the Social Democratic party, plus the New Harbinger\n" +
                "party, an LDP splinter group, has achieved instant unpopularity after\n" +
                "seizing power in a parliamentary coup on Wednesday.\n" +
                "Only 33 per cent of the electorate supports the new government, according to\n" +
                "a poll yesterday by the Mainichi Shimbun newspaper. The figure rose to 40\n" +
                "per cent in a survey conducted by Television Tokyo.\n" +
                "In his first public address since taking office, Mr Murayama outlined","");
        count +=1;
        pass += test("1","1,009.5 m Dollars","1.0095K");
        count +=1;

//        pass += test("0","7.5 Trillion","7500B");
//        count +=1;
//        pass += test("1","55 Million","55M");
//        count +=1;
//        pass += test("2", "23 3/4","23 3/4");
//        count +=1;
//        pass += test("3", "123 Thousand","123K");
//        count +=1;
//        pass += test("4","10,123,000,000","10.123B");
//        count +=1;
//        pass += test("5","19,986,897.65","19.98689765M");
//        count +=1;
//        pass += test("6","\"Man","MAN");
//        count +=1;
//        pass += test("7","10,123","10.123K");
//        count +=1;
//        pass += test("8","1010.56","1.01056K");
//        count +=1;
//        pass += test("9","10,123,000","10.123M");
//        count +=1;
//        pass += test("10","1010.56","1.01056K");
//        count +=1;
//        pass += test("11","55 Billion","55B");
//        count +=1;
//        pass += test("12","7 Trillion","7000B");
//        count +=1;
//        pass += test("13","204","204");
//        count +=1;
//        pass += test("14","1000000","1M");
//        count +=1;
//        pass += test("15","1,993","1.993K");
//        count +=1;

//        /**
//         * Percentage
//         */
//        System.out.println("~*~ Percentage Tests ~*~");
//        pass += test("1","6%","6%");
//        count +=1;
//        pass += test("2","10.6 percent","10.6%");
//        count +=1;
//        pass += test("3","6%","6%");
//        count +=1;
//        pass += test("4","10.6 percentage","10.6%");
//        count +=1;
//        pass += test("5","1000%","1000%");
//        count +=1;

//        /**
//         * Prices
//         */
//        System.out.println("~*~ Price Dollars ~*~");
//        pass += test("1","1.7320 Dollars","1.7320 Dollars");
//        count +=1;
//        pass += test("2","22.26 Dollars","22.26 Dollars");
//        count +=1;
//        pass += test("3","395.7 Dollars","395.7 Dollars");
//        count +=1;
//        pass += test("4","10,100 Dollars","10,100 Dollars"); //todo - fail
//        count +=1;
//        pass += test("5","10,100.2 Dollars","10,100.2 Dollars"); //todo - fail
//        count +=1;
//        pass += test("6","250,100 Dollars","250,100 Dollars"); //todo - fail
//        count +=1;
//        pass += test("7","$1,856","1,856 Dollars");
//        count +=1;
//
        //Price fraction Dollars
//        System.out.println("~*~*~* Price fraction Dollars ~*~*~*");
//        pass += test("1","22 3/4 Dollars","22 3/4 Dollars");
//        count +=1;
//        pass += test("2","252 3/4 Dollars","252 3/4 Dollars");
//        count +=1;
//        pass += test("3","2,520 3/4 Dollars","2,520 3/4 Dollars");
//        count +=1;
//        pass += test("4","122,520 3/4 Dollars","122,520 3/4 Dollars");
//        count +=1;
//        pass += test("5","999999 1/8 Dollars","999999 1/8 Dollars"); //todo -fail
//        count +=1;
//        pass += test("6","999,999 1/8 Dollars","999,999 1/8 Dollars");
//        count +=1;
//        pass += test("7","1/8 Dollars","1/8 Dollars"); //todo - fail
//        count +=1;
//
//        System.out.println("~*~*~* $price ~*~*~*");
//        pass += test("1","$450,000","450,000 Dollars");
//        count +=1;
//        pass += test("2","$780,000","780,000 Dollars");
//        count +=1;
//        pass += test("3","$780000","780000 Dollars");
//        count +=1;
//        pass += test("4","$10.5","10.5 Dollars");
//        count +=1;
//        pass += test("5","$1","1 Dollars");
//        count +=1;
//
//        System.out.println("~*~*~* $above million price ~*~*~*");
//        pass += test("1","1,000,000 Dollars","1 M Dollars");
//        count +=1;
//        pass += test("2","$450,000,000","450 M Dollars");
//        count +=1;
//        pass += test("3","$100 million","100 M Dollars");
//        count +=1;
//        pass += test("4","20.6 m Dollars","20.6 M Dollars");
//        count +=1;
//        pass += test("5","$200 billion","200000 M Dollars");
//        count +=1;
//        pass += test("6","100 bn Dollars","100000 M Dollars");
//        count +=1;
//        pass += test("7","100 billion U.S. dollars","100000 M Dollars");
//        count +=1;
//        pass += test("8","320 million U.S. dollars","320 M Dollars");
//        count +=1;
//        pass += test("9","1 trillion U.S. dollars","1000000 M Dollars");
//        count +=1;
//
//        /**
//         * Date
//         */
//        System.out.println("~*~ Date Tests ~*~");
//        pass += test("1","14 MAY","05-14");
//        count +=1;
//        pass += test("2","14 May","05-14");
//        count +=1;
//        pass += test("3","JUNE 4","06-04");
//        count +=1;
//        pass += test("4","June 4","06-04");
//        count +=1;
//        pass += test("5","May 1994","1994-05");
//        count +=1;
//        pass += test("6","MAY 1994","1994-05");
//        count +=1;
//        pass += test("7","4 March","03-04"); //check
//        count +=1;
//      /**
//       * Hyphen
//       */
//        System.out.println("~*~ Hyphen Tests ~*~");
//        pass += test("1","step-by-step","step-by-step");
//        count +=1;
//        pass += test("2","1-1","1-1");
//        count +=1;
//        pass += test("3","Xinhua,","XINHUA");
//        count +=1;
//        pass += test("4","[Xinhua]","XINHUA");
//        count +=1;
//        pass += test("5","(Xinhua)","XINHUA");
//        count +=1;
//        pass += test("6","Xinhua--","XINHUA");
//        count +=1;
//        pass += test("7","the yaniv","yaniv");
//        count +=1;
//        pass += test("8","Value-added","Value-added");
//        count +=1;
//        pass += test("9","10-part","10-part");
//        count +=1;
//        pass += test("10","6-7","6-7");
//        count +=1;
//        pass += test("10","and","");
//        count +=1;
//        pass += test("11","between moshe and dana","moshe"); //todo not working
//        count +=1;
//        pass += test("12","between 30 and 40","between 30 and 40");
//        count +=1;
//        pass += test("13","between 18 and 24","between 18 and 24");
//        count +=1;
//        pass += test("14","between","");
//        count +=1;
//
//
//    /**
//     * More
//       */
//      System.out.println("~*~ More ~*~");
//        pass += test("0","Europe's","EUROPE");
//        count +=1;
//        pass += test("1","\"Europe's","EUROPE");
//        count +=1;
//        pass += test("2","...and",""); //retun null todo all good!
//        count +=1;
//        pass += test("3","-","");
//        count +=1;
//        pass += test("4","DASA'","DASA");
//        count +=1;
//        pass += test("5","\"\"","");
//        count +=1;
//        pass += test("6","Bosnia-Herzegovina'","BOSNIA-HERZEGOVINA");
//        count +=1;
//        pass += test("7","Coalition!","COALITION");
//        count +=1;
//        pass += test("8","+14","14");
//        count +=1;
//        pass += test("9","\"2020,\"","2.02K");
//        count +=1;
//        pass += test("10","/[Gao","GAO");
//        count +=1;
//        pass += test("11","year./","year");
//        count +=1;
//        pass += test("12","|1-12/93","1-12/93");
//        count +=1;
//        pass += test("13","-5","-5");
//        count +=1;
//        pass += test("14","-,","");
//        count +=1;
        System.out.println("~*~ SUMMERY: PASS " + pass + "/" + (count) +" ~*~");
    }

    public static int test(String number, String input,String output) {
        IParsingProcess p = new DocParsingProcess("C:\\Users\\Maor\\Desktop\\corpus",false);
        Doc doc1 = new Doc();
        doc1.setDoc_content(input);
        p.parsing(doc1);
        HashMap<String,Term> termsReturn = doc1.getTermsInDoc();
        if(termsReturn.size() == 0 && output.length() !=0){
            System.out.println("TEST " + number + " FAILED| INPUT: " + input + "| OUTPUT: " + "| EXCEPTED: " + output);
            return 0;
        }
        else if(termsReturn.size() == 0 && output.length() ==0){
            System.out.println("TEST " + number + " PASS");
            return 1;
        }

        Map.Entry<String, Term> entry = termsReturn.entrySet().iterator().next();
        String result = entry.getKey();
        if (result.equals(output)) {
            System.out.println("TEST " + number + " PASS");
            return 1;
        } else {
            System.out.println("TEST " + number + " FAILED| INPUT: " + input + "| OUTPUT: " + result + "| EXCEPTED: " + output);
            return 0;
        }
    }
}
