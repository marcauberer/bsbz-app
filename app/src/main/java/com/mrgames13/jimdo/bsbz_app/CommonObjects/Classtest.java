package com.mrgames13.jimdo.bsbz_app.CommonObjects;

public class Classtest {
    //Konstanten

    //Variablen als Objekte

    //Variablen
    private int new_id;
    private String new_subject;
    private String new_description;
    private String new_date;
    private String new_receiver;
    private String new_writer;


    public Classtest(int id, String subject, String description, String receiver, String writer, String date) {
        this.new_id = id;
        this.new_subject = subject;
        this.new_description = description;
        this.new_receiver = receiver;
        this.new_writer = writer;
        this.new_date = date;
    }

    public int getID() {
        return new_id;
    }

    public String getSubject() {
        return new_subject;
    }

    public String getDescription() {
        return new_description;
    }

    public String getReceiver() {
        return new_receiver;
    }

    public String getWriter() {
        return new_writer;
    }

    public String getDate() {
        return new_date;
    }
}