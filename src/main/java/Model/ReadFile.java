package Model;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.*;
import java.util.Iterator;

/**
 * This class read all files in a given path
 */
public class ReadFile {
    private String path;
    private File root;

    public ReadFile(String path) {
        this.path = path;
        this.root = new File(path);
        try {
            listFilesForFolder(this.root);
        } catch (IOException e) {
        }
    }

    /**
     * parsing documents
     * @param file - file to parse
     */
    public void sendToParse(File file) {
        Document doc = Jsoup.parse(readFile(file.toString()));
        Elements docs = doc.select("DOC");
        Doc document;
        //loop throughout all the documents
        for (Element d: docs){
            String doc_num = d.select("DOCNO").text();
            String doc_content = d.select("TEXT").text();
            String doc_city = d.select("F[P=104]").text().split(" ")[0];
            document = new Doc(doc_num,doc_content, doc_city);
//            System.out.println("DOCNUM: " +document.getDoc_num());
//            System.out.println("DOCTEXT: " + document.getDoc_content());
//            System.out.println("DOCCITY: " + document.getCity());
        }

        Parse parse = new Parse();
    }

    /**
     * read file using bufferedReader
     *
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
