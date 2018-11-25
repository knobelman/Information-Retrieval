package View;
import Controller.Controller;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

/**
 * This class represents the GUI of the engine
 */
public class GUI {
    @FXML
    public javafx.scene.control.Button LOAD;
    public javafx.scene.control.Button POSTING;
    public javafx.scene.control.Button START;
    public javafx.scene.control.CheckBox STEMM;
    public javafx.scene.control.TextField CorpusPath;
    public javafx.scene.control.TextField PostingPath;

    /**
     * Fields
     * @myController - the controller
     * @corpusePathSelected - boolean flag for indicate if corpus path selected
     * @postingPathSelected - boolean flag for indicate if posting path selected
     * @pathOfCorpus - the path of the corpus
     * @pathOfPosting - the path of the posting
     */
    Controller myController = new Controller();
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
        double before = System.currentTimeMillis();
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
                    myController.startIndexing(pathOfCorpus, pathOfPosting + "\\withStem", true);
                }
                else{
                    //send to controller without stemming
                    myController.startIndexing(pathOfCorpus, pathOfPosting, false);
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
                myController.startIndexing(pathOfCorpus, pathOfPosting + "\\withStem", true);
            }
            //if stemming not required
            else{
                myController.startIndexing(pathOfCorpus, pathOfPosting, false);
            }
            System.out.println((System.currentTimeMillis() - before) / 1000 / 60 + " Minutes");
        }
    }

    public void openLanguageList(ActionEvent actionEvent) {
        FXMLLoader fxmlLoader=new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/Language.fxml"));
        Scene scene=null;
        try{
            scene=new Scene(fxmlLoader.load(), 500, 400);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage=new Stage();
        stage.setTitle("Language List");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public void reset(ActionEvent actionEvent) {
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
            for (File file : postingDirecory.listFiles())
                if (!file.isDirectory())
                    file.delete();
        }
    }
}
