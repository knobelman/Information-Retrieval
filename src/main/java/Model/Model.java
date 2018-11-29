package Model;

/**
 * This class represents the model class.
 * responsible for start the indexing process
 */

import Model.Indexers.Indexer;
import Model.Indexers.Posting;
import Model.Indexers.ReadFile;
import Model.Parsers.ParsingProcess.IParsingProcess;
import Model.Parsers.ParsingProcess.LanguageParsingProcess;
import javafx.scene.control.Alert;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * @indexr - indexer object
 * @postingObject - posting object
 * @readFileObject - readFile object
 */
public class Model {
    Indexer indexer;
    IParsingProcess languageParsingProcess;
    Posting postingObject;
    ReadFile readFileObject;

    /**
     * This method start indexing process
     * @param pathOfCorpus - the path of the corpus file
     * @param pathOfPosting - the path of the posting file
     * @param stem - indicate if stemming required or not
     */
    public void startIndexing(String pathOfCorpus,String pathOfPosting, boolean stem) {
        indexer = new Indexer(pathOfCorpus,stem);
        postingObject = new Posting(pathOfPosting);
        postingObject.resetPostingCounter();
        readFileObject = new ReadFile(indexer.getRootPath());
        indexer.setPostingObject(postingObject);
        indexer.setReadFileObject(readFileObject);
        indexer.setStemming(true);
        try {
            float startTime = System.currentTimeMillis();
            indexer.init(indexer.getReadFileObject());
            indexer.createFinalPosting();
            indexer.writeCityDictionaryToDisk();
            float endTime = System.currentTimeMillis();

            //Read seek
            indexer.getLine(69809);

            //finish indexing
            int numberOfDocs = indexer.getNumberOfDocs();
            int termsCount = indexer.getUniqueTermsCount();
            float totalTime = (endTime - startTime)/1000;

            //show alert
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Finished!");
            alert.setHeaderText("Indexing process finished");
            alert.setContentText("Number of Docs: " + numberOfDocs + "\n"
            +"Unique Terms count: " + termsCount + "\n"
            +"Total Run time: " + totalTime +" Seconds");
            alert.showAndWait();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void saveDictionary(boolean stem) {
        indexer.saveDictionary(stem);
    }

    public void loadDictionary(File file){
        indexer = new Indexer();
        try {
            indexer.loadDictionary(file);
        }catch (Exception e){
        }
    }

    public ArrayList<String> openLanguageList(){
        languageParsingProcess = new LanguageParsingProcess();
        return ((LanguageParsingProcess)languageParsingProcess).getLanguageCollection();
    }
}
