package View;

import Model.Doc;
import Model.Indexer;
import Model.Parse;
import Model.ReadFile;
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
    public javafx.scene.control.TextField path;
    public javafx.scene.control.CheckBox STEMM;


    public void load(ActionEvent actionEvent){
        DirectoryChooser fc = new DirectoryChooser();
        fc.setTitle("Load");
        File file = fc.showDialog(null);
        if (file != null) {
            String path = file.getAbsolutePath();
            double before = System.currentTimeMillis();
            if(STEMM.isSelected()){
                Indexer indexer = new Indexer(path,true);

            }else {
                Indexer indexer = new Indexer(path,false);
            }
            System.out.println((System.currentTimeMillis()-before)/1000/60 +" Minutes");
        }

//        while (documentCollection.hasNext()) {
//            System.out.println(documentCollection.next().getDoc_num());
//            System.out.println(documentCollection.next().getDoc_content());
            //Parse Parse_doc = new Parse(documentCollection.next());

    }
}
