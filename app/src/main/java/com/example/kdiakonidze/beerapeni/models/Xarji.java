package com.example.kdiakonidze.beerapeni.models;

public class Xarji {
    private String comment;
    private float amount;

    public Xarji(String comment, float amount) {
        this.comment = comment;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Xarji{" +
                "comment='" + comment + '\'' +
                ", amount=" + amount +
                '}';
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
