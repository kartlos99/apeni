package com.example.kdiakonidze.beerapeni.models;

/**
 * Created by k.diakonidze on 11/22/2017.
 */

public class BeerModel {
    private int id;
    private String dasaxeleba, displayColor;
    private Double fasi;

    public String getDisplayColor() {
        return displayColor;
    }

    public void setDisplayColor(String displayColor) {
        this.displayColor = displayColor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDasaxeleba() {
        return dasaxeleba;
    }

    public void setDasaxeleba(String dasaxeleba) {
        this.dasaxeleba = dasaxeleba;
    }

    public Double getFasi() {
        return fasi;
    }

    public void setFasi(Double fasi) {
        this.fasi = fasi;
    }
}
