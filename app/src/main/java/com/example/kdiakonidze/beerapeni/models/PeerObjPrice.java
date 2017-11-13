package com.example.kdiakonidze.beerapeni.models;

import java.util.ArrayList;

/**
 * Created by k.diakonidze on 11/11/2017.
 */

public class PeerObjPrice {
    private Integer obj_id;
    private ArrayList<Double> fasebi = new ArrayList<>();

    public PeerObjPrice(Integer obj_id) {
        this.obj_id = obj_id;
    }

    public Integer getObj_id() {
        return obj_id;
    }

    public void setObj_id(Integer obj_id) {
        this.obj_id = obj_id;
    }

    public ArrayList<Double> getFasebi() {
        return fasebi;
    }

    public void setFasebi(ArrayList<Double> fasebi) {
        this.fasebi = fasebi;
    }
}
