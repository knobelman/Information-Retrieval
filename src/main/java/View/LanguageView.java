package View;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Maor on 11/25/2018.
 */
public class LanguageView implements Initializable{
    @FXML
    public javafx.scene.control.ListView langugageView;

    //fields
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        langugageView.setEditable(true);
        ObservableList names = FXCollections.observableArrayList();
        names.addAll("a", "a", "a","Brenda","a", "a", "a","Brenda","a", "a", "a","Brenda", "Adam", "Williams", "Zach", "Connie", "Donny", "Lynne", "Rose", "Tony", "Derek");
        SortedList<String> sortedList = new SortedList(names);
        langugageView.setItems(sortedList);
    }
}
