package com.example.kdiakonidze.beerapeni.models;

/**
 * Created by k.diakonidze on 10/30/2017.
 */

public class Shekvetebi {
    String obieqti, ludi, tarigi;
    int k30in, k50in, k30wont, k50wont;

    public Shekvetebi(String obieqti, String ludi, int k30in, int k50in, int k30wont, int k50wont) {
        this.obieqti = obieqti;
        this.ludi = ludi;
        this.k30in = k30in;
        this.k50in = k50in;
        this.k30wont = k30wont;
        this.k50wont = k50wont;
    }

    public String getObieqti() {
        return obieqti;
    }

    public void setObieqti(String obieqti) {
        this.obieqti = obieqti;
    }

    public String getLudi() {
        return ludi;
    }

    public void setLudi(String ludi) {
        this.ludi = ludi;
    }

    public String getTarigi() {
        return tarigi;
    }

    public void setTarigi(String tarigi) {
        this.tarigi = tarigi;
    }

    public int getK30in() {
        return k30in;
    }

    public void setK30in(int k30in) {
        this.k30in = k30in;
    }

    public int getK50in() {
        return k50in;
    }

    public void setK50in(int k50in) {
        this.k50in = k50in;
    }

    public int getK30wont() {
        return k30wont;
    }

    public void setK30wont(int k30wont) {
        this.k30wont = k30wont;
    }

    public int getK50wont() {
        return k50wont;
    }

    public void setK50wont(int k50wont) {
        this.k50wont = k50wont;
    }
}
