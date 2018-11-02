package View;

import Model.Doc;
import Model.Parse;
import Model.ReadFile;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;
import java.util.Iterator;

/**
 * Created by Maor on 11/1/2018.
 */
public class HomeView {
    @FXML

    public javafx.scene.control.Button LOAD;
    public javafx.scene.control.TextField path;

    public void load(ActionEvent actionEvent) throws IOException {
        ReadFile rd = new ReadFile(path.getText());
//        while (documentCollection.hasNext()) {
//            System.out.println(documentCollection.next().getDoc_num());
//            System.out.println(documentCollection.next().getDoc_content());
            //Parse Parse_doc = new Parse(documentCollection.next());
        }
}
