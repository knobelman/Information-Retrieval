package View;

import Model.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.stage.DirectoryChooser;
import java.io.File;
import java.io.IOException;

/**
 * Created by Maor on 11/1/2018.
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
     */

    Indexer indexer = new Indexer();
    Posting postingObject;
    boolean corpusePathSelected = false;
    boolean postingPathSelected = false;
    String pathOfCorpus;
    String pathOfPosting;

    public void loadCorpus(ActionEvent actionEvent){
        DirectoryChooser fc = new DirectoryChooser();
        fc.setTitle("Set Corpus file Directory");
        File file = fc.showDialog(null);
        if (file != null) {
            corpusePathSelected = true;
            pathOfCorpus = file.getAbsolutePath();
            CorpusPath.setText(pathOfCorpus);
        }
    }

    public void loadPosting(ActionEvent actionEvent) {
        DirectoryChooser fc = new DirectoryChooser();
        fc.setTitle("Set Posting file Directory");
        File file = fc.showDialog(null);
        if (file != null) {
            postingPathSelected = true;
            pathOfPosting = file.getAbsolutePath();
            PostingPath.setText(pathOfPosting);
        }
    }

    public void startIndexing(ActionEvent actionEvent) {
        double before = System.currentTimeMillis();
        if(!corpusePathSelected || !postingPathSelected){
            if(!PostingPath.getText().equals("") && !CorpusPath.getText().equals("")){
                corpusePathSelected = true;
                postingPathSelected = true;
                pathOfCorpus = CorpusPath.getText();
                pathOfPosting = PostingPath.getText();
            }

        }
        if(!corpusePathSelected || !postingPathSelected){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Warning");
            alert.setHeaderText("One of field is empty");
            alert.setContentText("Please make sure all fields filled");
            alert.showAndWait();
        }
        else {
            indexer.setCorpusFilePath(pathOfCorpus);
            postingObject = new Posting(pathOfPosting);
            indexer.setPostingObject(postingObject);
            ReadFile readFileObject = new ReadFile(indexer.getRootPath());
            indexer.setReadFileObject(readFileObject);

            if (STEMM.isSelected()) {
                indexer.setStemming(true);
                File directory = new File(pathOfPosting +"\\withStem");
                if (!directory.exists()){
                    directory.mkdir();
                }
                indexer.setPostingFilePath(pathOfPosting +"\\withStem");
            } else {
                indexer.setStemming(false);
                indexer.setPostingFilePath(pathOfPosting);
            }
            boolean toStemm = indexer.getToStemm();
            try {
                indexer.init(indexer.getReadFileObject(), toStemm);
                indexer.createFinalPosting();
                indexer.splitFinalPosting();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println((System.currentTimeMillis() - before) / 1000 / 60 + " Minutes");
        }
    }

    public void openLanguageList(ActionEvent actionEvent) {
//        ListView<String> list = new ListView<String>();
//        ObservableList<String> items = FXCollections.observableArrayList (
//                "Single", "Double", "Suite", "Family App");
//        list.setItems(items);
    }

    public void reset(ActionEvent actionEvent) {
        if(!postingPathSelected){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Warning");
            alert.setHeaderText("Posting path is empty");
            alert.setContentText("Please make sure the field is filled");
            alert.showAndWait();
        }
        else {
            File postingDirecory = new File(PostingPath.getText());
            for (File file : postingDirecory.listFiles())
                if (!file.isDirectory())
                    file.delete();
        }
    }
}