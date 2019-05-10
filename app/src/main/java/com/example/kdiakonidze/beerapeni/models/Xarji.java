package com.example.kdiakonidze.beerapeni.models;

public class Xarji {
    private String comment, distrID, id;
    private float amount;

    public Xarji(String comment, String distrID, String id, float amount) {
        this.comment = comment;
        this.distrID = distrID;
        this.id = id;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Xarji{" +
                "comment='" + comment + '\'' +
                ", distrID='" + distrID + '\'' +
                ", id='" + id + '\'' +
                ", amount=" + amount +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
