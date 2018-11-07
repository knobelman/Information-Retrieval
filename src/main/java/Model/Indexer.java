package Model;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;

//external sort - אלגוריתם לאינקדסינג

public class Indexer {
    private String rootPath;
    private static ReadFile ourCorpus;
    private HashSet<Doc> DocumentsToParse;
    private Parse parser = new Parse();
    private LinkedHashMap<Term, ArrayList> linkedHashMap = new LinkedHashMap<Term, ArrayList>();

    public Indexer(String rootPath) {
        this.rootPath = rootPath;
        this.ourCorpus = new ReadFile(rootPath);
        this.DocumentsToParse = new HashSet<Doc>();
        try {
            init(ourCorpus.getRoot());
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
                DocumentsToParse = ourCorpus.fromFileToDoc(fileEntry);
                System.out.println(fileEntry.toString());

                for(Doc d: DocumentsToParse){
//                    ArrayList<Term> d_terms = new ArrayList<Term>(20);
//                    d_terms.add(new Term("hello"));
//                    d_terms.add(new Term("hey"));
//                    d_terms.add(new Term("bey"));
//                    d_terms.add(new Term("good"));
                    parser.parsing(d);
                    System.out.println();
                    //ArrayList<Term> d_terms = d.getTermsInDoc();
//                    linkedHashMap.put(d_terms.get(0),new ArrayList());
//                    for(Term t: d_terms) {
//                        //linkedHashMap.get(t).add(d);
//                    }

                    //writeToTempPostingFile();



                    //clean linkedhashmap



                    //parser.parsing(d);
                        //IndexWriter w = new IndexWriter(index, config);
                        //addDoc(w, d.getDoc_content(), d.getDoc_num());
                }

            }
        }
    }

    public void writeToTempPostingFile(){
        //write linkedhashmap content to posting file
        Writer writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(new File("example")), StandardCharsets.UTF_8));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Writer finalWriter = writer;
        linkedHashMap.forEach((key, value) -> {
            try {
                finalWriter.write(key + ", " + ((Term)value.get(0)).getTerm() + System.lineSeparator());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        try {
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void addDoc(IndexWriter w, String title, String isbn) throws IOException {
        Document doc = new Document();
        doc.add(new TextField("title", title, Field.Store.YES));
        doc.add(new StringField("isbn", isbn, Field.Store.YES));
        w.addDocument(doc);
    }
}



