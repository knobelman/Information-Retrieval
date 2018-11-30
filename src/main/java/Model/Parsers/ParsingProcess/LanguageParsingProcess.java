package Model.Parsers.ParsingProcess;
import Model.DataObjects.ParseableObjects.JasonObject;
import Model.DataObjects.ParseableObjects.IParseableObject;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;


/**
 * Created by Maor on 11/28/2018.
 */
public class LanguageParsingProcess implements IParsingProcess {

    /**
     * Fields
     */

    private ArrayList<String> languageCollection;

    /**
     * C'tor
     */
    public LanguageParsingProcess(){
        this.languageCollection = new ArrayList<>();
        JSONParser parser = new JSONParser();
        File directory = new File("./");
        try {
            Object obj = parser.parse(new FileReader(directory.getAbsoluteFile()+"\\src\\main\\resources\\languages.json"));
            JSONArray jsonArray = (JSONArray)obj;
            JasonObject JasonObject = new JasonObject(jsonArray);
            parsing(JasonObject);
        } catch (IOException e) {
            //e.printStackTrace();
        } catch (ParseException e) {
            //e.printStackTrace();
        }
    }

    @Override
    public void parsing(IParseableObject parseObject) {
        JSONArray jsonArray = ((JasonObject) parseObject).getJasonArray();
        JSONObject s = (JSONObject) jsonArray.get(0);
        for (Object currentValue : s.values()) {
            String languageName = (String) ((JSONObject) currentValue).get("name");
            languageCollection.add(languageName);
        }
        languageCollection.sort(Comparator.naturalOrder());
    }

    public ArrayList<String> getLanguageCollection() {
        return languageCollection;
    }
}
