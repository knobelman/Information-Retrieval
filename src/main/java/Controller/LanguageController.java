package Controller;

import Model.Model;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Maor on 11/28/2018.
 */
public class LanguageController extends Acontroller {

    public ArrayList<String> openLanguageList() {
        return myModel.openLanguageList();
    }
}
