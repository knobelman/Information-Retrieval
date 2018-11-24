package Model;

/**
 * This class represents the model class.
 * responsible for start the indexing process
 */

import Model.Indexers.Indexer;
import Model.Indexers.Posting;
import Model.Indexers.ReadFile;

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
    public void startIndexing(String pathOfCorpus, String pathOfPosting, boolean stem) {
        indexer = new Indexer(pathOfCorpus,stem);
        postingObject = new Posting(pathOfPosting);
        readFileObject = new ReadFile(indexer.getRootPath());
        indexer.setPostingObject(postingObject);
        indexer.setReadFileObject(readFileObject);
        indexer.setStemming(true);
        try {
            indexer.init(indexer.getReadFileObject());
            indexer.createFinalPosting();
            indexer.splitFinalPosting();
            indexer.writeDictionaryToDisk();
        }catch (Exception e){
        }
    }
}
