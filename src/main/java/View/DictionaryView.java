package View;

import Controller.DictionaryController;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.util.Pair;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by Maor on 11/29/2018.
 */
public class DictionaryView implements Initializable {

    public javafx.scene.control.TableView DictionaryView;
    DictionaryController myDictionaryController = new DictionaryController();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(myDictionaryController.showDictionary() != null) {
            HashMap<String, Pair<Integer, Integer>> ourCorpusDictionary = myDictionaryController.showDictionary();

            TableColumn<Map.Entry<String, Pair<Integer, Integer>>, String> column1 = new TableColumn<>("TERM");
            column1.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getKey()));

            TableColumn<Map.Entry<String, Pair<Integer, Integer>>, String> column2 = new TableColumn<>("DF");
            column2.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getValue().getKey().toString()));

            ObservableList<Map.Entry<String, Pair<Integer, Integer>>> items = FXCollections.observableArrayList(ourCorpusDictionary.entrySet());
            DictionaryView.setItems(items.sorted());
            DictionaryView.getColumns().setAll(column1, column2);
        }
    }
}
