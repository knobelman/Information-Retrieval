package Model.Parsers.ParsingProcess;
import Model.DataObjects.ParseableObjects.Doc;
import Model.DataObjects.ParseableObjects.IParseableObject;
import Model.Parsers.ParserTypes.AParser;
import Model.Parsers.ParserTypes.ParserClassifier;
import Model.Parsers.ParserTypes.Stemmer;
import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Stack;

/**
 * This class represents the parser object
 * hmNum -
 * hmSign -
 * hmDate -
 * hsDot -
 * stop_words -
 * moreThenOneWord -
 * allTermsInCorpus -
 */
public class DocParsingProcess implements IParsingProcess {
    //    private HashMap hmNum = new HashMap<String, String>();
//    private HashMap hmSign = new HashMap<String, String>();
//    private HashMap hmDate = new HashMap<String, String>();
//    private HashSet hsDot = new HashSet<String>();
    private HashSet stop_words = new HashSet<String>();
    private Stack moreThenOneWord = new Stack<String>();
    private HashSet allTermsInCorpus = new HashSet<String>();
    private AParser parserClassifier;
    int i;
    boolean stem;

    /**
     * C'tor
     *
     * @param path - path of the corpus
     */
    public DocParsingProcess(String path, boolean stem) {
        this.stem = stem;
        readStopWords(path + "\\STOPWORDS");//todo take care of "WORD
        parserClassifier = new ParserClassifier();
    }

    /**
     * this method read all the stop words from a given file
     *
     * @param fileName
     */
    public void readStopWords(String fileName) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(fileName));
        } catch (FileNotFoundException e) {
        }
        try {
            String line = br.readLine();
            while (line != null) {
                stop_words.add(line);
                line = br.readLine();
            }
        } catch (IOException e) {
        } finally {
            try {
                br.close();
            } catch (IOException e) {
            }
        }
    }

    /**
     * this method parsing the document
     *
     * @param documentToParse - to parse
     * @return - document with parsed tokens
     */

    public void parsing(IParseableObject documentToParse) {
        Doc document = (Doc) documentToParse;
        parsing(document, stem);
    }

    public Doc parsing(Doc document, boolean stem) {
        String text = document.getDoc_content();//("[: () -- ]");
        String[] tokenz = text.split("[\\*\\\n\\ \\:\\;\\?\\!\\#\\]\\|\\}\\{\\~\\�\\[\\_\\+\\'\"'\\(\\)\\>\\=\\⪕\\⪖\\Ω]+");//("[: ()|]");
        for (i = 0; i < tokenz.length; i++) {//for to go over all tokenz
            String current = tokenz[i];
            String currValue;
            if (current.contains("--")) {//fill stack with all words to work on
                for (String curr : current.split("--")) {
                    moreThenOneWord.push(curr);
                }
            }
            else if(current.contains("...")) {//fill stack with all words to work on
                for (String curr : current.split("...")) {
                    moreThenOneWord.push(curr);
                }
            }else {
                moreThenOneWord.push(current);
            }
            if (moreThenOneWord.empty())
                continue;
            do {
                current = (String) moreThenOneWord.pop();
                //current = trimming(current);
                if (current.length() == 0 || current.equals("")) {//check if empty
                    continue;
                }
                if ((stop_words.contains(current) && (!current.equals("between") || current.equals("BETWEEN") || current.equals("Between"))) || current.equals("%")) {//if the raw token is a stop word
                    continue;
                }
                Pair<Integer, String> currPair;
                if (i + 1 >= tokenz.length)//last token
                    currPair = parserClassifier.parse(current, "", "", "");
                else if (i + 2 >= tokenz.length)//second before last token
                    currPair = parserClassifier.parse(current, tokenz[i + 1], "", "");
                else if (i + 3 >= tokenz.length)//third before last token
                    currPair = parserClassifier.parse(current, tokenz[i + 1], tokenz[i + 2], "");
                else//not close to last token
                    currPair = parserClassifier.parse(current, tokenz[i + 1], tokenz[i + 2], tokenz[i + 3]);
                if (currPair == null)
                    continue;
                else {
                    currValue = currPair.getValue();//get current term after processing
                    i += currPair.getKey().intValue();//get i to raise by amount of tokenz used
                }

                if (stem) {//if stem == true-> stem the term
                    Stemmer stemmer = new Stemmer();
                    stemmer.add(currValue.toCharArray(), currValue.length());
                    stemmer.stem();
                    currValue = stemmer.toString();
                }
                addToDoc(currValue, document);
            } while (!moreThenOneWord.empty());
        }
        return document;
    }


    /**
     * this method adds the words Upper\Lower like needed
     *
     * @param currValue - Value to add to terms
     */
    private void addToDoc(String currValue, Doc document) {
        if (stop_words.contains(currValue.toLowerCase()))
            return;
        document.addTermToDoc(currValue);
        allTermsInCorpus.add(currValue);//todo
    }
}
