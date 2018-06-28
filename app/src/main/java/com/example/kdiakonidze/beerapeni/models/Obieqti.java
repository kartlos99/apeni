package com.example.kdiakonidze.beerapeni.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by k.diakonidze on 11/1/2017.
 */

public class Obieqti implements Serializable {
    private String dasaxeleba, adress, tel, comment, sk, sakpiri, chek;
    private Integer id, valiM, valiK30, valiK50;
    private ArrayList<Double> fasebi = new ArrayList<>();

    public Obieqti(String dasaxeleba) {
        this.dasaxeleba = dasaxeleba;

    }

    public String getChek() {
        return chek;
    }

    public void setChek(String chek) {
        this.chek = chek;
    }

    public Integer getId() {
        return id;
    }

    public ArrayList<Double> getFasebi() {
        return fasebi;
    }

    public void setFasebi(ArrayList<Double> fasebi) {
        this.fasebi = fasebi;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDasaxeleba() {
        return dasaxeleba;
    }

    public void setDasaxeleba(String dasaxeleba) {
        this.dasaxeleba = dasaxeleba;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getSk() {
        return sk;
    }

    public void setSk(String sk) {
        this.sk = sk;
    }

    public String getSakpiri() {
        return sakpiri;
    }

    public void setSakpiri(String sakpiri) {
        this.sakpiri = sakpiri;
    }

    public Integer getValiM() {
        return valiM;
    }

    public void setValiM(Integer valiM) {
        this.valiM = valiM;
    }

    public Integer getValiK30() {
        return valiK30;
    }

    public void setValiK30(Integer valiK30) {
        this.valiK30 = valiK30;
    }

    public Integer getValiK50() {
        return valiK50;
    }

    public void setValiK50(Integer valiK50) {
        this.valiK50 = valiK50;
    }

    @Override
    public String toString() {
        return dasaxeleba;
    }
}
