package View;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.awt.*;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/Home.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 500, 500));
        primaryStage.show();
    }

    public static void main(String[] args) throws IOException {
        launch(args);
//        HashMap hmDate = new HashMap<String, String>();
//        hmDate.put("01","Jan");
//        String dataset = "<DOC>\n" +
//                "<DOCNO> FBIS3-2 </DOCNO>\n" +
//                "<HT>    \"cr00000011194001\" </HT>\n" +
//                "\n" +
//                "\n" +
//                "<HEADER>\n" +
//                "<DATE1>   2 March 1994 </DATE1>\n" +
//                "Article Type:FBIS \n" +
//                "Document Type:MEDIA GUIDE--FB WMR 94-001--FORMER YUGOSLAVIA \n" +
//                "<H3> <TI>      FBIS MEDIA GUIDE:  THE FORMER YUGOSLAVIA </TI></H3>\n" +
//                "</HEADER>\n " +
//                "<TEXT> This guide is 10.6percent intended to $900 help $200 users of Foreign Jan 1994 Broadcast Information Service (FBIS) translations assess the value and reliability of media sources in the countries or regions of the former Yugoslavia.  It provides key information about the media of Bosnia-Herzegovina, Croatia, The Former Yugoslav Republic of </TEXT></DOC>";
//        String[] tags = {"DOCNO","HEADER","H2","DATE1","H3","TI","TEXT"};
//        Document doc = Jsoup.parse(dataset);
//        String text = doc.select(tags[6]).text();
//        //org.apache.lucene.document.Document doc1 = new org.apache.lucene.document.Document();
//        //doc1.add(new Field("word", new FileReader("sds")));
//        String regex = ("[$][0-9]*"); //$number
//        String regex2 = ("[0-9]*"); //$number
//        Pattern r = Pattern.compile(regex);
//        // Now create matcher object.
//        Matcher m = r.matcher(text);
//        while (m.find()) {
//            System.out.println(m.group());
//        }
    }

//    /**
//     * Func that turns numbers with coma into the right representative
//     *
//     * @param current - the number with the coma (1,234 etc)
//     * @return The correct representative of the number (1,234 -> 1.234K)
//     */
//    public static String comaToWord(String current) {
//        String[] num = current.split(",");
//        String result = "";
//        boolean done = false;
//        for (int i = num.length - 1; i > 0; i--) {
//            for (int j = num[i].length() - 1; j >= 0; j--) {
//                if (num[i].charAt(j) != '0' && !done)
//                    done = true;
//                if (done) {
//                    result = num[i].charAt(j) + result;
//                }
//            }
//            if (i == 1)
//                result = '.' + result;
//        }
//        char letter = ' ';
//        if(num.length == 2)
//            letter = 'K';
//        else if(num.length == 3)
//            letter = 'M';
//        else if(num.length == 4)
//            letter = 'B';
//        else
//            letter = ' ';
//        result = num[0] + result + letter;
//        return result;
//    }
}
