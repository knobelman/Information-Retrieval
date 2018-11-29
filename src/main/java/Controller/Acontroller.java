package Controller;

import Model.Model;

/**
 * Created by Maor on 11/29/2018.
 */
public class Acontroller {
    protected static Model myModel;

    public Acontroller(){
        if(myModel==null)
            myModel = new Model();
    }
}
