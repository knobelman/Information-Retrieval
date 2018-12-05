package Model;

/**
 * This class represents the model class.
 * responsible for start the indexing process
 */

import Model.DataObjects.TermData;
import Model.Indexers.Indexer;
import Model.Indexers.Posting;
import Model.Indexers.ReadFile;
import Model.Parsers.ParsingProcess.IParsingProcess;
import javafx.scene.control.Alert;
import javafx.util.Pair;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @indexr - indexer object
 * @postingObject - posting object
 * @readFileObject - readFile object
 */
public class Model {
    Indexer indexer;
    /**
     * This method start indexing process
     * @param pathOfCorpus - the path of the corpus file
     * @param pathOfPosting - the path of the posting file
     * @param stem - indicate if stemming required or not
     */
    public void startIndexing(String pathOfCorpus,String pathOfPosting, boolean stem) {
        indexer = new Indexer(pathOfCorpus,pathOfPosting,stem);
        try {
            long startTime = System.currentTimeMillis();
            indexer.init(new File(pathOfCorpus));
            long endTime = System.currentTimeMillis();
            //finish indexing
            int numberOfDocs = indexer.getNumberOfDocs();
            int termsCount = indexer.getUniqueTermsCount();
            Thread.sleep(2000);
            long totalTime = (endTime - startTime)/1000;
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

    public void loadDictionary(String path,boolean stem){
        indexer = new Indexer();
        try {
            indexer.loadDictionary(path,stem);
        }catch (Exception e){
//            e.printStackTrace();
        }
    }

    public void getLine(long position) {
        indexer.getLine(position);
    }

    public HashMap<String, TermData> showDictionary() {
        if(indexer == null){
            return null;
        }
        else {
            return indexer.getCorpusDictionary();
        }
    }

    public void reset() {
        indexer.reset();
    }

    public HashMap<String,String> getLanguageDictionary(){
        if(indexer == null){
            return null;
        }
        return indexer.getLanguageDictionary();
    }
}
