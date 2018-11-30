package Model.DataObjects.ParseableObjects;

import org.json.simple.JSONArray;

/**
 * This class represents a jason object
 */
public class JasonObjectI implements IParseableObject {
    private JSONArray jsonArray;
    public JasonObjectI(JSONArray jsonArray){
        this.jsonArray = jsonArray;
    }
    public JSONArray getJasonArray(){
        return this.jsonArray;
    }
}
