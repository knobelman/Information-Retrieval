package Model;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class read all files in a given path
 */
public class ReadFile {
    private String path;
    private File root;

    public ReadFile(String path) throws IOException {
        this.path = path;
        this.root = new File(path);
        listFilesForFolder(this.root);
    }

    /**
     * send to parse
     * @param file - file to insert
     * @throws IOException
     */
    public void sendToParse(File file) throws IOException {
        Document doc = Jsoup.parse(readFile(file.toString()));
        String[] tags = {"DOCNO", "HEADER", "H2", "DATE1", "H3", "TI", "TEXT"};
        Elements doc_num = doc.select(tags[0]);
        Elements doc_content = doc.select(tags[6]);
        Iterator<Element> doc_num_iterator = doc_num.iterator();
        Iterator<Element> doc_content_iterator = doc_content.iterator();
        Parse parse = new Parse();
        while (doc_num_iterator.hasNext()) {
            Doc document;
            if (doc_content_iterator.hasNext()){
                document = new Doc(doc_num_iterator.next().text(), doc_content_iterator.next().text());
            }else{
                document = new Doc(doc_num_iterator.next().text(), "");
            }
            System.out.println(document.getDoc_num());
            //parse.parsing(document);
        }
    }

    /**
     * read file using bufferedReader
     * @param fileName - file to read
     * @return the content of the file
     * @throws IOException
     */
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

    /**
     * recursive function to get all the files in a directory
     * @param root - the root of the files
     * @throws IOException
     */
    public void listFilesForFolder(final File root) throws IOException {
        for (final File fileEntry : root.listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry);
            } else {
                sendToParse(fileEntry);
            }
        }
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
