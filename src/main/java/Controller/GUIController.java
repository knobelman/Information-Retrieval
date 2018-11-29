package Controller;

import Model.Model;
import javafx.util.Pair;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class represents the controller class
 */
public class GUIController extends Acontroller {

    /**
     * Fields
     */

    //call to model to start indexing
    public void startIndexing(String pathOfCorpus, String pathOfPosting, boolean stem){
        myModel.startIndexing(pathOfCorpus,pathOfPosting,stem);
    }

    public void saveDictionry(boolean stem) {
        myModel.saveDictionary(stem);
    }

    public void loadDictionary(File file){
        myModel.loadDictionary(file);
    }

    public void getLine(long position) {
        myModel.getLine(position);
    }

    public HashMap<String, Pair<Integer,Integer>> showDictionary() {
        return myModel.showDictionary();
    }

    public void reset() {
        myModel.reset();
    }
}
