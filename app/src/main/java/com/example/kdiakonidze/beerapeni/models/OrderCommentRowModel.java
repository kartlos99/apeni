package com.example.kdiakonidze.beerapeni.models;

public class OrderCommentRowModel {
    private String objName, comment, comment_Of;

    public String getComment_Of() {
        return comment_Of;
    }

    public void setComment_Of(String comment_Of) {
        this.comment_Of = comment_Of;
    }

    public OrderCommentRowModel(String objName, String comment, String comment_Of) {
        this.objName = objName;
        this.comment = comment;
        this.comment_Of = comment_Of;
    }

    public String getObjName() {
        return objName;
    }

    public void setObjName(String objName) {
        this.objName = objName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}
