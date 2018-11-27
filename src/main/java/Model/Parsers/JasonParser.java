package Model.Parsers;
import Model.DataObjects.CityData;
import Model.DataObjects.ParseableObjects.JasonObject;
import Model.DataObjects.ParseableObjects.ParseableObject;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * This class represents the jason parser
 * responsible for parsing the json file and save the required data about each city
 * @cityData - contains the required data about each city
 */
public class JasonParser implements IParser {
    private HashMap<String, CityData> cityData;

    /**
     * C'tor
     * Get json file and call init
     */
    public JasonParser(){
        this.cityData = new HashMap<>();
        JSONParser parser = new JSONParser();
        File directory = new File("./");
        try {
            Object obj = parser.parse(new FileReader(directory.getAbsoluteFile()+"\\src\\main\\resources\\cities.json"));
            JSONArray jsonArray = (JSONArray)obj;
            JasonObject JasonObject = new JasonObject(jsonArray);
            parsing(JasonObject);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initialize cityData HashMap
     */
    public void parsing(ParseableObject parseObject){
        JSONArray jsonArray = ((JasonObject)parseObject).getJasonArray();
        for(Object s: jsonArray){
            String cityName = (String)((JSONObject)s).get("capital");
            String countryName = (String)((JSONObject)s).get("name");
            Long population = (Long)((JSONObject)s).get("population");
            JSONArray currencyArray = (JSONArray)((JSONObject)s).get("currencies");
            String currency = (String)(((JSONObject)currencyArray.get(0)).get("code"));
            CityData data = new CityData(countryName,population,currency);
            cityData.put(cityName,data);
        }
    }

    /**
     * Getter
     * @param city - city to get data about
     * @return - CityData object
     */
    public CityData getData(String city){
        String correctCityName;
        String secondParth = city.substring(1,city.length()).toLowerCase();
        correctCityName = Character.toUpperCase(city.charAt(0))+secondParth;
        return this.cityData.get(correctCityName);
    }
}
