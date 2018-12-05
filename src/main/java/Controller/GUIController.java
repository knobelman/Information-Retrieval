package Controller;

import Model.DataObjects.TermData;
import Model.Model;
import javafx.util.Pair;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This controller is responsible for the GUI
 */
public class GUIController extends Acontroller {

    //call to model to start indexing
    public void startIndexing(String pathOfCorpus, String pathOfPosting, boolean stem){
        myModel.startIndexing(pathOfCorpus,pathOfPosting,stem);//
    }

    public void saveDictionry(boolean stem) {
        myModel.saveDictionary(stem);
    }////

    public void loadDictionary(String path,boolean stem){
        myModel.loadDictionary(path, stem);
    }

    public void getLine(long position) {
        myModel.getLine(position);
    }

    public HashMap<String, TermData> showDictionary() {
        return myModel.showDictionary();
    }

    public void reset() {
        myModel.reset();
    }
}
