package View;

import Model.ReadFile;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
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

    public static void main(String[] args) throws IOException {
        ReadFile rd = new ReadFile("C:/Users/Maor/Desktop/corpus");


//        String doc_for_test = "\n" +
//                "<DOC>\n" +
//                "<DOCNO> FBIS3-11991 </DOCNO>\n" +
//                "<HT>    \"drlat060_m_94007\" </HT>\n" +
//                "\n" +
//                "\n" +
//                "<HEADER>\n" +
//                "<AU>   FBIS-LAT-94-060 </AU>\n" +
//                "Document Type:Daily Report \n" +
//                "<DATE1>  28 Mar 1994 </DATE1>\n" +
//                "\n" +
//                "</HEADER>\n" +
//                "\n" +
//                "<F P=100> Brazil </F>\n" +
//                "<H3> <TI>   Relations With Cambodia Normalized </TI></H3>\n" +
//                "<F P=102>  PY2903032594 Sao Paulo GAZETA MERCANTIL in Portuguese 28 Mar \n" +
//                "94 p 3 </F>\n" +
//                "\n" +
//                "<F P=103> PY2903032594 </F>\n" +
//                "<F P=104>  Sao Paulo GAZETA MERCANTIL </F>\n" +
//                "\n" +
//                "\n" +
//                "<TEXT>\n" +
//                "Language: <F P=105> Portuguese </F>\n" +
//                "Article Type:BFN \n" +
//                "\n" +
//                "<F P=106> [Article by Maria Helena Tachinardi] </F>\n" +
//                "  [Text] Brasilia -- On 25 March, Brazil and Cambodia \n" +
//                "normalized relations when representatives from the two countries \n" +
//                "met at United Nations headquarters in New York and signed a \n" +
//                "joint communique. \n" +
//                "  Brazil first established diplomatic relations with the \n" +
//                "Kingdom of Cambodia in 1961, establishing representation in \n" +
//                "Phnom Penh concurrent with the Brazilian Embassy in New Delhi, \n" +
//                "India. Afterward that representation became concurrent with the \n" +
//                "Brazilian Embassy in Bangkok. According to the Brazilian \n" +
//                "Foreign Ministry, the representation in Phnom Penh was closed \n" +
//                "down in 1966, but not because relations had been broken between \n" +
//                "the two countries. Following the internal pacification of \n" +
//                "Cambodia under the leadership of Prince Norodom Sihanouk, which \n" +
//                "was endorsed by Brazilian diplomacy, the Brazilian Government \n" +
//                "suggested the normalization of bilateral relations, which was \n" +
//                "confirmed on 25 March with the joint communique. Brazil will \n" +
//                "open an embassy in Phnom Penh concurrent with the embassy in \n" +
//                "Bangkok. \n" +
//                "\n" +
//                "</TEXT>\n" +
//                "\n" +
//                "</DOC>";
//        String[] tags = {"DOCNO", "HEADER", "H2", "DATE1", "H3", "TI", "TEXT","F[P]"};
//        Document doc = Jsoup.parse(doc_for_test);
//        String text = doc.select(tags[6]).text();
//        StringTokenizer st = new StringTokenizer(text, " /:\"()");//all the tokens in <TEXT>
//        String regex = "[0-9, /.]+[0-9, /,]+";
//        String num;
//        while (st.hasMoreElements()) {
//            String current = st.nextToken();
//            if (current.matches(regex)) {
//                if (current.contains(",")) {
//                    num = comaToWord(current);
//                    System.out.println(num);
//                } else{
//                    System.out.println(current);
//                }
//            }
//        }
//        while (st.hasMoreElements()){
//            System.out.println(st.nextElement().toString());
//        }
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
