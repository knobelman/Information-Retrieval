package Controller;

import Model.Model;

import java.io.File;

/**
 * This class represents the controller class
 */
public class GUIController {

    /**
     * Fields
     */
    private Model myModel;

    public GUIController(){
        myModel = new Model();
    }

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
}
