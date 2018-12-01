package Model.Indexers;
import Model.DataObjects.ParseableObjects.Doc;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.*;
import java.util.HashSet;

/**
 * This class read all files in a given path
 * @path - the path of the corpus
 * @root - root object
 */
public class ReadFile {

    /**
     * C'tor
     */
    public ReadFile() {
    }

    /**
     * This method take a file and separate each document to doc number, doc content, doc city.
     * after that, create a document object
     * @param file - file to separate to documents
     * return hash set of documents for the next station - parsing
     */
    public HashSet<Doc> fromFileToDoc(File file) {
        String path = file.toString();
        Document doc = Jsoup.parse(readFile(path));
        Elements docs = doc.select("DOC");
        Doc document;
        HashSet<Doc> documents = new HashSet<>();
        //loop throughout all the documents
        for (Element d: docs){
            String doc_num = d.select("DOCNO").text();
            String doc_content = d.select("TEXT").text();
            String doc_city = d.select("F[P=104]").text().split(" ")[0];
            String language = d.select("F[P=105").text();
            String doc_city_to_upper = doc_city.toUpperCase();
            int position;
            HashSet<Integer> positions = new HashSet<>();
            position = doc_content.indexOf(doc_city);
            if (!(doc_city.equals(""))) {
                while (position >= 0) {
                    positions.add(new Integer(position));
                    position = doc_content.indexOf(doc_city, position + 1);
                }
            }
            document = new Doc(path,doc_num,doc_content,doc_city_to_upper,positions,language);
            documents.add(document);
        }
        return documents;
    }

    /**
     * C'tor
     * read file using bufferedReader
     * @param fileName - file to read
     * @return the content of the file
     * @throws IOException
     */
    String readFile(String fileName) {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        try {
            br = new BufferedReader(new FileReader(fileName));
        } catch (FileNotFoundException e) {
        }
        try {
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}