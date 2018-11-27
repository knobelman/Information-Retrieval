package View;

import Controller.GUIController;
import Controller.LanguageController;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.ResourceBundle;

/**
 * Created by Maor on 11/25/2018.
 */
public class LanguageView implements Initializable{
    @FXML
    public javafx.scene.control.ListView langugageView;

    //fields
    LanguageController myLanguageController = new LanguageController();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ArrayList<String> languageCollection = myLanguageController.openLanguageList();
        ObservableList languageObservableList = FXCollections.observableArrayList();
        languageObservableList.addAll(languageCollection);
        langugageView.setItems(languageObservableList);
        langugageView.setOnMouseClicked(event -> System.out.println("clicked on " + langugageView.getSelectionModel().getSelectedItem()));
    }
}
