package Model;

/**
 * This class represents the model class.
 * responsible for start the indexing process
 */

import Model.Indexers.Indexer;
import Model.Indexers.Posting;
import Model.Indexers.ReadFile;
import javafx.scene.control.Alert;

import java.io.File;

/**
 * @indexr - indexer object
 * @postingObject - posting object
 * @readFileObject - readFile object
 */
public class Model {
    Indexer indexer;
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
        readFileObject = new ReadFile(indexer.getRootPath());
        indexer.setPostingObject(postingObject);
        indexer.setReadFileObject(readFileObject);
        indexer.setStemming(true);
        try {
            long startTime = System.currentTimeMillis();
            indexer.init(indexer.getReadFileObject());
            indexer.createFinalPosting();
            indexer.splitFinalPosting();
            indexer.writeCityDictionaryToDisk();
            long endTime = System.currentTimeMillis();

            //finish indexing
            int numberOfDocs = indexer.getNumberOfDocs();
            int termsCount = indexer.getUniqueTermsCount();
            long totalTime = (endTime - startTime)/1000/60;

            //show alert
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Finished!");
            alert.setHeaderText("Indexing process finished");
            alert.setContentText("Number of Docs: " + numberOfDocs + "\n"
            +"Unique Terms count: " + termsCount + "\n"
            +"Total Run time: " + totalTime +" Minutes");
            alert.showAndWait();
        }catch (Exception e){
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
}
