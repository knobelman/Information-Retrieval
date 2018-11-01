package Model;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.StringTokenizer;

/**
 * Created by Maor on 10/30/2018.
 */
public class Parse {

    private HashMap hm = new HashMap<String, String>();
    private HashSet stop_words = new HashSet<String>();

    public Parse(File[] file) {
        //initialize hm
        hm.put("Thousand","K"); hm.put("Million","M"); hm.put("Billion","B"); hm.put("Trillion","00B");
        hm.put("percent", "%"); hm.put("percentage","%"); hm.put("$","Dollars");
        hm.put("Jan","01"); hm.put("JAN","01"); hm.put("January","01"); hm.put("JANUARY","01");
        hm.put("Feb","02");hm.put("FEB","02"); hm.put("February","02"); hm.put("FEBRUARY","02");
        hm.put("Mar","03"); hm.put("MAR","03"); hm.put("March","03");hm.put("MARCH","03");
        hm.put("Apr","04");hm.put("APR","04"); hm.put("April","04"); hm.put("APRIL","04");
        hm.put("May","05"); hm.put("MAY","05");
        hm.put("Jun","06"); hm.put("JUN","06"); hm.put("June","06");hm.put("JUNE","06");
        hm.put("Jul","07"); hm.put("JUL","07"); hm.put("July","07");hm.put("JULY","07");
        hm.put("Aug","08"); hm.put("AUG","08"); hm.put("August","08");hm.put("AUGUST","08");
        hm.put("Sep","09"); hm.put("SEP","09"); hm.put("September","09");hm.put("SEPTEMBER","09");
        hm.put("Oct","10"); hm.put("OCT","10"); hm.put("October","10");hm.put("OCTOBER","10");
        hm.put("Nov","11"); hm.put("NOV","11"); hm.put("November","11");hm.put("NOVEMBER","11");
        hm.put("Dec","12"); hm.put("DEC","12"); hm.put("December","12");hm.put("DECEMBER","12");

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

        parsing(file);
    }



    public void parsing(File[] folder) {
//        String doc_for_test = "<DOC>\n" + "<DOCNO> FBIS3-1 </DOCNO>\n" + "<HT>  \"cr00000011094001\" </HT>\n" + "<HEADER>\n" +
//                "<H2>   March Reports </H2>\n" +
//                "<DATE1>  1 March 1994 </DATE1>\n" +
//                "Article Type:FBIS " + "</HEADER>\n" +
//                "<TEXT>\n" + "POLITICIANS,  PARTY PREFERENCES \n" +
//                "   The 22-23 January edition of the Skopje newspaper VECER in \n" +
//                "   November 1993    May 1993 \n" +
//                "Kiro Gligorov, President of the Republic      76/15           78/13 \n" +
//                "Vasil Tupurkovski, former Macedonian          50/36           43/37 \n" +
//                "   official in Federal Yugoslavia \n" +
//                "Ljubomir Frckovski, Interior Minister        10,023          42/43 \n" +
//                "Stojan Andov, Parliamentary Chairman          48/41           48/39 \n" +
//                " March       June      September    December \n" +
//                " Deputy Prime Minister    39          39         50              37 \n" +
//                " Yes           No       Do Not Know \n" +
//                "SDSM           28           51          21 \n" +
//                "VMRO-DPMNE     15           72          14 \n" +
//                "LP             19           59          22 \n" +
//                "PDP-NDP*       20           73           7 \n" +
//                "*Party for Democratic Prosperity-People's Democratic Party \n" +
//                "<TEXT>\n" + "</DOC>";
//        Document doc = Jsoup.parse(doc_for_test);
//        String text = doc.select(tags[6]).text();
//        StringTokenizer st = new StringTokenizer(text, " /:\"()");
//        String regex = "[0-9, /,]+";
//        while (st.hasMoreElements()) {
//            String current = st.nextToken();
//            if (current.matches(regex)) {
//                System.out.println(current);
//            }
//        }
    }
}
