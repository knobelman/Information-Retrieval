package Model;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Maor on 10/30/2018.
 */
public class ReadFile {
    private String doc_number;
    private File current;
    private String path;
    private File dir;
    private HashMap docs = new HashMap<String, String>();

    public ReadFile(String path) throws IOException {
        this.path = path;
        this.dir = new File(path);
        listFilesForFolder(this.dir);
    }

    public void insert_into_hash(File file) throws IOException {
        Document doc = Jsoup.parse(readFile(file.toString()));
        String[] tags = {"DOCNO", "HEADER", "H2", "DATE1", "H3", "TI", "TEXT","F[P]"};
        Elements doc_num = doc.select(tags[0]);
        Elements content = doc.select(tags[1]);
        Iterator<Element> doc_nums = doc_num.iterator();
        Iterator<Element> doc_content = content.iterator();
        while (doc_nums.hasNext()) {
            docs.put(doc_nums.next().text(), doc_content.next().text());
        }
    }

    String readFile(String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
            return sb.toString();
        } finally {
            br.close();
        }
    }

    public void listFilesForFolder(final File folder) throws IOException {
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry);
            } else {
                insert_into_hash(fileEntry);
            }
        }
    }

    public String getDoc_number() {
        return doc_number;
    }

    public void setDoc_number(String doc_number) {
        this.doc_number = doc_number;
    }

    public File getCurrent() {
        return current;
    }

    public void setCurrent(File current) {
        this.current = current;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
