package Model.DataObjects;

import java.io.Serializable;

public class TermData implements Serializable{
    private int df;
    private int totalTF;
    private int position;

    public TermData(int df, int totalTF, int position){
        this.df = df;
        this.totalTF = totalTF;
        this.position = position;
    }

    public void setDf(int df) {
        this.df = df;
    }

    public void setTotalTF(int totalTF) {
        this.totalTF = totalTF;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getDf() {
        return df;
    }

    public int getTotalTF() {
        return totalTF;
    }

    public int getPosition() {
        return position;
    }

    public void incDF(int plusDF){
        this.df += plusDF;
    }

    public void incTotalTF(int plusTF){
        this.totalTF += plusTF;
    }
}
