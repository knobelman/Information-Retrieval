package Model;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

//external sort - אלגוריתם לאינקדסינג

public class Indexer {
    private String rootPath;
    private static ReadFile readFileObject;
    private HashSet<Doc> DocumentsToParse;
    private ArrayList<Thread> DocsThread;
    private LinkedHashMap<Term, ArrayList> linkedHashMap = new LinkedHashMap<Term, ArrayList>();
    private Parse Parser = new Parse();


    public Indexer(String rootPath) {
        this.rootPath = rootPath;
        this.readFileObject = new ReadFile(rootPath);
        this.DocumentsToParse = new HashSet<>();
        this.DocsThread = new ArrayList<>();
        try {
            init(readFileObject.getRoot());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param file
     */
    public void init(final File file) throws IOException {
        for (final File fileEntry : file.listFiles()) {
            if (fileEntry.isDirectory()) {
                init(fileEntry);
            } else {
//                DocumentsToParse = readFileObject.fromFileToDoc(fileEntry);
//                for (Doc d : DocumentsToParse) {
//                    Parser.parsing(d);
//            }
                Thread t = new Thread(() -> DocumentsToParse = readFileObject.fromFileToDoc(fileEntry));
                DocsThread.add(t);
                t.start();

                for (Doc d : DocumentsToParse) {
                    //System.out.println(d.getPath());
                    Parser.parsing(d);
                }

                for (Thread CurrentDoc : DocsThread) {
                    try {
                        CurrentDoc.join();
                    } catch (InterruptedException e) {
                        //e.printStackTrace();
                    }
                }
            }
        }
    }


//
//    public void writeToTempPostingFile(){
//        //write linkedhashmap content to posting file
//        Writer writer = null;
//        try {
//            writer = new BufferedWriter(new OutputStreamWriter(
//                    new FileOutputStream(new File("example")), StandardCharsets.UTF_8));
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        Writer finalWriter = writer;
//        linkedHashMap.forEach((key, value) -> {
//            try {
//                finalWriter.write(key + ", " + ((Term)value.get(0)).getTerm() + System.lineSeparator());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        });
//        try {
//            writer.flush();
//            writer.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

//    private static void addDoc(IndexWriter w, String title, String isbn) throws IOException {
//        Document doc = new Document();
//        doc.add(new TextField("title", title, Field.Store.YES));
//        doc.add(new StringField("isbn", isbn, Field.Store.YES));
//        w.addDocument(doc);
//    }
}



