package View;

import Model.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Iterator;

/**
 * Created by Maor on 11/1/2018.
 */
public class HomeView {
    @FXML

    public javafx.scene.control.Button LOAD;
    public javafx.scene.control.Button POSTING;
    public javafx.scene.control.Button START;
    public javafx.scene.control.CheckBox STEMM;

    /**
     * Fields
     */

    Indexer indexer = new Indexer();
    Posting postingObject;


    public void loadCorpus(ActionEvent actionEvent){
        DirectoryChooser fc = new DirectoryChooser();
        fc.setTitle("Load");
        File file = fc.showDialog(null);
        if (file != null) {
            String path = file.getAbsolutePath();
            indexer.setCorpusFilePath(path);
//            if(STEMM.isSelected()){
//                indexer.setStemming(true);
//            }else {
//                indexer.setStemming(false);
//            }
        }
    }

    public void loadPosting(ActionEvent actionEvent) {
        DirectoryChooser fc = new DirectoryChooser();
        fc.setTitle("Set Posting file Directory");
        File file = fc.showDialog(null);
        if (file != null) {
            String path = file.getAbsolutePath();
            postingObject = new Posting(path);
            indexer.setPostingObject(postingObject);
            }
        }

    public void startIndexing(ActionEvent actionEvent) {
        double before = System.currentTimeMillis();
        ReadFile readFileObject = new ReadFile(indexer.getRootPath());
        indexer.setReadFileObject(readFileObject);
        if(STEMM.isSelected()){
            indexer.setStemming(true);
        }else {
            indexer.setStemming(false);
        }
        boolean toStemm = indexer.getToStemm();
        try {
            indexer.init(indexer.getReadFileObject(),toStemm);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println((System.currentTimeMillis()-before)/1000/60 +" Minutes");
    }
}
