package com.example.kdiakonidze.beerapeni.models;

public class Xarji {
    private String comment, distrID;
    private float amount;

    public Xarji(String comment, String distrID, float amount) {
        this.comment = comment;
        this.distrID = distrID;
        this.amount = amount;
    }

//    public Xarji(String comment, float amount) {
//        this.comment = comment;
//        this.amount = amount;
//    }

    @Override
    public String toString() {
        return "Xarji{" +
                "comment='" + comment + '\'' +
                ", distrID='" + distrID + '\'' +
                ", amount=" + amount +
                '}';
    }

    public String getDistrID() {
        return distrID;
    }

    public void setDistrID(String distrID) {
        this.distrID = distrID;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }
}
