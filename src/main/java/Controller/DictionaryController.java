package Controller;

import javafx.util.Pair;

import java.util.HashMap;

/**
 * Created by Maor on 11/29/2018.
 */
public class DictionaryController extends Acontroller {
    public HashMap<String, Pair<Integer, Integer>> showDictionary() {
        return myModel.showDictionary();
    }
}
