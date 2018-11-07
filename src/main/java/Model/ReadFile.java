package Model;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.*;
import java.util.HashSet;

/**
 * This class read all files in a given path
 */
public class ReadFile {
    private String path;
    private File root;
    static int N = 472574;

    public ReadFile(String path) {
        this.path = path;
        this.root = new File(path);
    }

    /**
     * parsing documents
     * @param file - file to parse
     */
    public HashSet<Doc> fromFileToDoc(File file) {
        String path = file.toString();
        Document doc = Jsoup.parse(readFile(path));
        Elements docs = doc.select("DOC");
        Doc document = null;
        HashSet<Doc> documents = new HashSet<Doc>();
        //loop throughout all the documents
        for (Element d: docs){
            String doc_num = d.select("DOCNO").text();
            String doc_content = d.select("TEXT").text();
            String doc_city = d.select("F[P=104]").text().split(" ")[0];
            String doc_city_to_upper = doc_city.toUpperCase();
            int position;
            String positions ="";
            position = doc_content.indexOf(doc_city);
            if (!(doc_city.equals(""))) {
                while (position >= 0) {
                    positions = positions + position +", ";
                    position = doc_content.indexOf(doc_city, position + 1);
                }
            }
            document = new Doc(path,doc_num,doc_content, doc_city_to_upper);
            documents.add(document);
        }
        return documents;
    }

    /**
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
        } finally {
            try {
                br.close();
            } catch (IOException e) {
            }
        }
        return sb.toString();
    }
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public File getRoot() {
        return root;
    }

    public void setRoot(File root) {
        this.root = root;
    }
}
