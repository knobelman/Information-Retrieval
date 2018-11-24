import Model.Doc;
import Model.Parse;
import Model.Term;

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
//        pass += test("1","19,986,897.65","19.98689765M");
//        count +=1;
//        pass += test("2","\"Man","Man");
//        count +=1;
//        pass += test("3","10,123","10.123K");
//        count +=1;
//        pass += test("4", "123 Thousand","123K");
//        count +=1;
//        pass += test("5","1010.56","1.01056K");
//        count +=1;
//        pass += test("6","10,123,000","10.123M");
//        count +=1;
//        pass += test("7","55 Million","55M");
//        count +=1;
//        pass += test("8","1010.56","1.01056K");
//        count +=1;
//        pass += test("9","10,123,000,000","10.123B");
//        count +=1;
//        pass += test("10","55 Billion","55B");
//        count +=1;
//        pass += test("11","7 Trillion","7000B");
//        count +=1;
//        pass += test("12","204","204");
//        count +=1;
//        pass += test("13","1000000","1M");
//        count +=1;
//        pass += test("14","1,993","1.993K");
//        count +=1;
//
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
//
//        /**
//         * Prices
//         */
//        System.out.println("~*~ Prices Tests ~*~");
//        pass += test("1","1.7320 Dollars","1.7320 Dollars");
//        count +=1;
//        pass += test("2","22 3/4 Dollars","22 3/4 Dollars");
//        count +=1;
//        pass += test("3","$450,000","450,000 Dollars");
//        count +=1;
//        pass += test("4","1,000,000 Dollars","1 M Dollars");
//        count +=1;
//        pass += test("5","$450,000,000","450 M Dollars");
//        count +=1;
//        pass += test("6","$100 million","100 M Dollars");
//        count +=1;
//        pass += test("7","20.6 m Dollars","20.6 M Dollars");
//        count +=1;
//        pass += test("8","$100 billion","100000 M Dollars");
//        count +=1;
//        pass += test("9","100 bn Dollars","100000 M Dollars");
//        count +=1;
//        pass += test("10","100 billion U.S. dollars","100000 M Dollars");
//        count +=1;
//        pass += test("11","320 million U.S. dollars","320 M Dollars");
//        count +=1;
//        pass += test("12","1 trillion U.S. dollars","1000000 M Dollars");
//        count +=1;
//        pass += test("13","4 March","03-04"); //check
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
//////
//      /**
//       * Hyphen
//       */
//        System.out.println("~*~ Hyphen Tests ~*~");
//        pass += test("1","step-by-step","step-by-step");
//        count +=1;
//        pass += test("2","1-1","1-1");
//        count +=1;
//        pass += test("3","Xinhua,","Xinhua");
//        count +=1;
//        pass += test("4","[Xinhua]","Xinhua");
//        count +=1;
//        pass += test("5","(Xinhua)","Xinhua");
//        count +=1;
//        pass += test("6","Xinhua--","Xinhua");
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
//
//    /**
//     * More
//       */
//      System.out.println("~*~ More ~*~");
//        pass += test("0","Europe's","Europe");
//        count +=1;
//        pass += test("1","\"Europe's","Europe");
//        count +=1;
//        pass += test("2","...and",""); //retun null todo all good!
//        count +=1;
//        pass += test("3","-","");
//        count +=1;
//        pass += test("4","DASA'","DASA");
//        count +=1;
//        pass += test("5","\"\"","");
//        count +=1;
//        pass += test("6","Bosnia-Herzegovina'","Bosnia-Herzegovina");
//        count +=1;
//        pass += test("7","Coalition!","Coalition");
//        count +=1;
//        pass += test("8","+14","14");
//        count +=1;
//        pass += test("9","\"2020,\"","2.02K");
//        count +=1;
//        pass += test("10","/[Gao","Gao");
//        count +=1;
//        pass += test("11","year./","year");
//        count +=1;
//        pass += test("12","|1-12/93","1-12/93");
//        count +=1;
//        pass += test("13","-5","-5");
//        count +=1;
        pass += test("14","-,","WORD");
        count +=1;
        pass += test("1",",5","1994-01");
        count +=1;
        System.out.println("~*~ SUMMERY: PASS " + pass + "/" + (count) +" ~*~");


    }

    public static int test(String number, String input,String output) {
        Parse p = new Parse("C:\\Users\\Yaniv\\Desktop\\corpus");
        Doc doc1 = new Doc();
        doc1.setDoc_content(input);
        p.parsing(doc1, false);
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
