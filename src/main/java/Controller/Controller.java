package Controller;

import Model.Model;

/**
 * This class represents the controller class
 */
public class Controller {

    /**
     * Fields
     */
    private Model myModel;

    public Controller(){
        myModel = new Model();
    }

    //call to model to start indexing
    public void startIndexing(String pathOfCorpus, String pathOfPosting, boolean stem){
        myModel.startIndexing(pathOfCorpus,pathOfPosting,stem);
    }
}
