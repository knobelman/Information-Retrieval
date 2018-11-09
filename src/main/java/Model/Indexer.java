package Model;
import java.io.*;
import java.util.*;

//external sort - אלגוריתם לאינקדסינג

public class Indexer {
    private String rootPath;
    private static ReadFile readFileObject;
    private HashSet<Doc> DocumentsToParse;
    private ArrayList<Thread> DocsThread;
    private Posting postingObject;
    private HashMap<String,HashMap<String,Integer>> dictionary = new LinkedHashMap<>();
    private Parse Parser;


    public Indexer(String rootPath, boolean stemm) {
        this.rootPath = rootPath;
        this.Parser = new Parse(rootPath);
        this.readFileObject = new ReadFile(rootPath);
        this.DocumentsToParse = new HashSet<>();
        this.DocsThread = new ArrayList<>();
        this.postingObject = new Posting(this.rootPath);
        try {
            init(readFileObject.getRoot(),stemm);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param file
     * @param stemm
     */
    public void init(final File file, boolean stemm) throws IOException {
        for (final File fileEntry : file.listFiles()) {
            if (fileEntry.isDirectory()) {
                init(fileEntry, stemm);
            } else {
                DocumentsToParse = readFileObject.fromFileToDoc(fileEntry);
                for (Doc d : DocumentsToParse) {
                    Parser.parsing(d,stemm);
                    for(Map.Entry<String,Term> entry : d.getTermsInDoc().entrySet()) {
                        String termname = entry.getKey();
                        Term value = entry.getValue();
                        String doc_name = d.getDoc_num();
                        if(termname.equals("")){
                            continue;
                        }
                        if(dictionary.containsKey(termname)){
                            Integer newint =  new Integer(d.getTermsInDoc().get(termname).getTf(doc_name));
                            dictionary.get(termname).put(d.getDoc_num(),newint);
                        }else {
                            HashMap<String, Integer> current = new HashMap();
                            current.put(doc_name, new Integer(value.getTf(doc_name)));
                            dictionary.put(termname, current);
                        }
                    }
                }
                postingObject.createPostingFile(this.dictionary);
                dictionary = new LinkedHashMap<>();

//                Thread t = new Thread(() -> DocumentsToParse = readFileObject.fromFileToDoc(fileEntry));
//                DocsThread.add(t);
//                t.start();
//                try {
//                    t.join();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }

//                for (Thread CurrentDoc : DocsThread) {
//                    try {
//                        CurrentDoc.join();
//                    } catch (InterruptedException e) {
//                        //e.printStackTrace();
//                    }
//                }
            }
        }
    }
}



