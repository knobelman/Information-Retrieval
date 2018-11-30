package Model.Parsers.ParsingProcess;
import Model.DataObjects.CityData;
import Model.DataObjects.ParseableObjects.JasonObject;
import Model.DataObjects.ParseableObjects.IParseableObject;
import Model.Parsers.ParserTypes.NumberParser;
import javafx.util.Pair;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * This class represents the jason parser
 * responsible for parsing the json file and save the required data about each city
 * @cityData - contains the required data about each city
 */
public class CityParsingProcess implements IParsingProcess {
    private HashMap<String, CityData> cityData;
    private HashMap<String, Pair<String,String>> countryData;//country -> population, currency
    private JSONParser parser;
    private File directory;
    /**
     * C'tor
     * Get json file and call init
     */
    public CityParsingProcess(){
        this.cityData = new HashMap<>();
        this.countryData = new HashMap<>();
        this.parser = new JSONParser();
        this.directory = new File("./");
        try {
            Object obj = parser.parse(new FileReader(directory.getAbsoluteFile()+"\\src\\main\\resources\\cities.json"));
            JSONArray jsonArray = (JSONArray)obj;
            JasonObject JasonObject = new JasonObject(jsonArray);
            parsing(JasonObject);

            //cityAndCountry
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initialize cityData HashMap
     */
    public void parsing(IParseableObject parseObject){
        JSONArray jsonArray = ((JasonObject)parseObject).getJasonArray();
        for(Object s: jsonArray){
            String cityName = (String)((JSONObject)s).get("capital");
            String countryName = (String)((JSONObject)s).get("name");
            Long population = (Long)((JSONObject)s).get("population");
            String populationToString = population.toString();
            NumberParser numberParser = new NumberParser();
            String correctPopulation = numberParser.parsing(populationToString,"","","");
            String fixRound;
            if(correctPopulation.contains(".") && correctPopulation.length() > 4) {
                fixRound = roundby2digits(correctPopulation);
            }
            else{
                fixRound = correctPopulation;
            }
            //correctPopulation = String.format(correctPopulation,"%.2f");
            JSONArray currencyArray = (JSONArray)((JSONObject)s).get("currencies");
            String currency = (String)(((JSONObject)currencyArray.get(0)).get("code"));
            //add data about country
            countryData.put(countryName,new Pair<>(fixRound,currency));
            CityData data = new CityData(countryName,fixRound,currency);
            cityData.put(cityName,data);
        }

        JSONObject obj2 = null;
        try {
            obj2 = (JSONObject) parser.parse(new FileReader(directory.getAbsoluteFile()+"\\src\\main\\resources\\countriesAndCities.json"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Iterator it = obj2.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry current = (Map.Entry)it.next(); //current entry in a loop
            String country = (String) current.getKey();
            ArrayList<String> cities = (ArrayList<String>) current.getValue();
            for(String city: cities){
                if(countryData.get(country)!=null){
                    String populationData = countryData.get(country).getKey();
                    String currencyData = countryData.get(country).getValue();
                    cityData.put(city,new CityData(country,populationData,currencyData));
                }

            }
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

    /**
     * This method rounding number by 2 digits
     * @param s - to round
     * @return rounded number
     */
    private String roundby2digits(String s){
        char lastChar = s.charAt(s.length()-1);
        String withoutLastChar = s.substring(0,s.length()-1);
        String[] spllited = withoutLastChar.split("\\.");
        String leftSide = spllited[0];
        String rightSide = spllited[1];
        int secondNumber;
        String fixed ="";
        if(rightSide.length() > 2) {
            secondNumber = Integer.parseInt(String.valueOf(rightSide.charAt(1)));
            int thirdNumber = Integer.parseInt(String.valueOf(rightSide.charAt(2)));
            if (thirdNumber >= 5) {
                secondNumber += 1;
            }
            fixed = leftSide + "." + rightSide.charAt(0) + (secondNumber + "") + lastChar;
        }else{
            fixed = leftSide + "." + rightSide + lastChar;
        }
        return fixed;
    }
}
