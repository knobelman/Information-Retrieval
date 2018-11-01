package View;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.Arrays;
import java.util.StringTokenizer;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../../resources/sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }

    public static void main(String[] args) {
        String doc_for_test = "<DOC>\n" + "<DOCNO> FBIS3-1 </DOCNO>\n" + "<HT>  \"cr00000011094001\" </HT>\n" + "<HEADER>\n" +
                "<H2>   March Reports </H2>\n" +
                "<DATE1>  1 March 1994 </DATE1>\n" +
                "Article Type:FBIS " + "</HEADER>\n" +
                "<TEXT>\n" + "POLITICIANS,  PARTY PREFERENCES \n" +
                "   The 22-23 January edition of the Skopje newspaper VECER in \n" +
                "   November 1993    May 1993 \n" +
                "Kiro Gligorov, President of the Republic      76/15           78/13 \n" +
                "Vasil Tupurkovski, former Macedonian          50/36           43/37 \n" +
                "   official in Federal Yugoslavia \n" +
                "Ljubomir Frckovski, Interior Minister        1020.56          42/43 \n" +
                "Stojan Andov, Parliamentary Chairman          48/41           48/39 \n" +
                " March       June      September    December \n" +
                " Deputy Prime Minister    39          39         50              37 \n" +
                " Yes           No       Do Not Know \n" +
                "SDSM           28           51          21 \n" +
                "VMRO-DPMNE     15           72          14 \n" +
                "LP             19           59          22 \n" +
                "PDP-NDP*       20           73           7 \n" +
                "*Party for Democratic Prosperity-People's Democratic Party \n" +
                "<TEXT>\n" + "</DOC>";
        String[] tags = {"DOCNO", "HEADER", "H2", "DATE1", "H3", "TI", "TEXT"};
        Document doc = Jsoup.parse(doc_for_test);
        String text = doc.select(tags[6]).text();
        StringTokenizer st = new StringTokenizer(text, " /:\"()");//all the tokens in <TEXT>
        String regex = "[0-9, /,]+";
        String num;
        while (st.hasMoreElements()) {
            String current = st.nextToken();
            if (current.matches(regex)) {
                if (current.contains(",")) {
                    num = comaToWord(current);
                    System.out.println(num);
                } else
                    System.out.println(Integer.parseInt(current));
            }

        }
    }

    /**
     * Func that turns numbers with coma into the right representative
     *
     * @param current - the number with the coma (1,234 etc)
     * @return The correct representative of the number (1,234 -> 1.234K)
     */
    public static String comaToWord(String current) {
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
            if (i == 1)
                result = '.' + result;
        }
        char letter = ' ';
        if(num.length == 2)
            letter = 'K';
        else if(num.length == 3)
            letter = 'M';
        else if(num.length == 4)
            letter = 'B';
        else
            letter = ' ';
        result = num[0] + result + letter;
        return result;
    }
    //launch(args);
}
