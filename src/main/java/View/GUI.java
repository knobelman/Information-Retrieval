package View;
import Controller.GUIController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * This class represents the GUI of the engine
 */
public class GUI {

    /**
     * Fields
     * @myController - the controller
     * @corpusePathSelected - boolean flag for indicate if corpus path selected
     * @postingPathSelected - boolean flag for indicate if posting path selected
     * @pathOfCorpus - the path of the corpus
     * @pathOfPosting - the path of the posting
     */
    @FXML
    public javafx.scene.control.Button LOAD;
    public javafx.scene.control.Button POSTING;
    public javafx.scene.control.Button START;
    public javafx.scene.control.Button saveDictionary;
    public javafx.scene.control.Button RESET;
    public javafx.scene.control.CheckBox STEMM;
    public javafx.scene.control.TextField CorpusPath;
    public javafx.scene.control.TextField lineValue;
    public javafx.scene.control.TextField PostingPath;

    GUIController myGUIController = new GUIController();
    boolean corpusePathSelected = false;
    boolean postingPathSelected = false;
    String pathOfCorpus;
    String pathOfPosting;

    /**
     * get corpus path from browse button
     */
    public void loadCorpus(){
        DirectoryChooser fc = new DirectoryChooser();
        fc.setTitle("Set Corpus file Directory");
        File file = fc.showDialog(null);
        if (file != null) {
            corpusePathSelected = true;
            pathOfCorpus = file.getAbsolutePath();
            CorpusPath.setText(pathOfCorpus);
        }
    }

    /**
     * get posting path from browse button
     */
    public void loadPosting() {
        DirectoryChooser fc = new DirectoryChooser();
        fc.setTitle("Set Posting file Directory");
        File file = fc.showDialog(null);
        if (file != null) {
            postingPathSelected = true;
            pathOfPosting = file.getAbsolutePath();
            PostingPath.setText(pathOfPosting);
        }
    }

    /**
     * start indexing
     */
    public void startIndexing() {
        //if one of the browser buttons not clicked
        if(!corpusePathSelected || !postingPathSelected){
            //if both of the path text filed we need to start indexing
            if(!PostingPath.getText().equals("") && !CorpusPath.getText().equals("")){
                corpusePathSelected = true;
                postingPathSelected = true;
                pathOfCorpus = CorpusPath.getText();
                pathOfPosting = PostingPath.getText();
                //if stemming required
                if (STEMM.isSelected()) {
                    File directory = new File(pathOfPosting + "\\withStem");
                    if (!directory.exists()) {
                        directory.mkdir();
                    }
                    //send to controller with stemming
                    Alert alert = showWindow();
                    myGUIController.startIndexing(pathOfCorpus, pathOfPosting + "\\withStem", true);
                    alert.close();
                    saveDictionary.setDisable(false);
                    RESET.setDisable(false);
                }
                else{
                    //send to controller without stemming
                    Alert alert = showWindow();
                    myGUIController.startIndexing(pathOfCorpus, pathOfPosting, false);
                    alert.close();
                    saveDictionary.setDisable(false);
                    RESET.setDisable(false);
                }
            }
            //one of fields is empty
            else if(PostingPath.getText().equals("") || CorpusPath.getText().equals("")){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Warning");
                alert.setHeaderText("One of field is empty");
                alert.setContentText("Please make sure all fields filled");
                alert.showAndWait();
            }
        }
        //if both browser buttons clicked
        else {
            //if stemming required
            if (STEMM.isSelected()) {
                File directory = new File(pathOfPosting + "\\withStem");
                //if directory of stemming not exists create one
                if (!directory.exists()) {
                    directory.mkdir();
                }
                //send to controller with stemming
                Alert alert = showWindow();
                myGUIController.startIndexing(pathOfCorpus, pathOfPosting + "\\withStem", true);
                alert.close();
                saveDictionary.setDisable(false);
                RESET.setDisable(false);

            }
            //if stemming not required
            else{
                Alert alert = showWindow();
                myGUIController.startIndexing(pathOfCorpus, pathOfPosting, false);
                alert.close();
                saveDictionary.setDisable(false);
                RESET.setDisable(false);
            }
        }
    }

    public void openLanguageList() {
        FXMLLoader fxmlLoader=new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/Language.fxml"));
        Scene scene=null;
        try{
            scene=new Scene(fxmlLoader.load(), 500, 400);
        } catch (IOException e) {
            //e.printStackTrace();
        }
        Stage stage=new Stage();
        stage.setTitle("Language List");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public void reset() {
        //if posting path not give
        if(!postingPathSelected && pathOfPosting.equals("")){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Warning");
            alert.setHeaderText("Posting path is empty");
            alert.setContentText("Please make sure the field is filled for reset the posting folder");
            alert.showAndWait();
        }
        else {
            //delete all posting files
            File postingDirecory = new File(PostingPath.getText());
            for (File file : postingDirecory.listFiles()) {
                if(file.isDirectory()){
                    File[] files = file.listFiles();
                    for(File inner : files){
                        inner.delete();
                    }
                }
                file.delete();
            }
        }
        myGUIController.reset();
    }

    public void saveDictionary() {
        if (STEMM.isSelected()) {
            myGUIController.saveDictionry(true);
        }else{
            myGUIController.saveDictionry(false);
        }
    }

    public void loadDictionary() {
//        FileChooser chooser = new FileChooser();
//        chooser.setTitle("Load Dictionary");
//        File file = chooser.showOpenDialog(null);
//        if (file != null) {
        if(PostingPath.getText().equals("")){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Path not specified");
            alert.setHeaderText("Path not specified");
            alert.setContentText("Please enter path in Posting path field");
            alert.show();
        }
        else {
            File file;
            if (!STEMM.isSelected()) {
                file = new File(PostingPath.getText() + "\\CorpusDictionaryWithoutStem");
                if(!file.exists()){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Dictionary not exists");
                    alert.setHeaderText("Dictionary not exists");
                    alert.setContentText("Please run and save dictionary before");
                    alert.show();
                }
                else {
                    myGUIController.loadDictionary(file);
                }
            } else {
                file = new File(PostingPath.getText() + "\\withStem\\CorpusDictionaryWithStem");
                if(!file.exists()){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Dictionary not exists");
                    alert.setHeaderText("Dictionary not exists");
                    alert.setContentText("Please run and save dictionary before");
                    alert.show();
                }else {
                    myGUIController.loadDictionary(file);
                }
            }
        }
    }

    private Alert showWindow(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Indexing starting...");
        alert.setHeaderText("Indexing start...");
        alert.setContentText("Indexing process start...");
        alert.setContentText("Indexing process start...\n"+"" +
                "Start indexing...");
        alert.show();
        return alert;
    }

    public void getLine() {
        long position = Long.parseLong(lineValue.getText());
        myGUIController.getLine(position);
    }

    public void showDictionary() {
        FXMLLoader fxmlLoader=new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/Dictionary.fxml"));
        Scene scene=null;
        try{
            scene=new Scene(fxmlLoader.load(), 580, 545);
        } catch (IOException e) {
            //e.printStackTrace();
        }
        Stage stage=new Stage();
        stage.setTitle("Corpus Dictionary");
        stage.setScene(scene);
        stage.show();
    }
}
