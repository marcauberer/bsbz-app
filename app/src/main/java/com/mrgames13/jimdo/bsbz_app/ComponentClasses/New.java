package com.mrgames13.jimdo.bsbz_app.ComponentClasses;

public class New {
    //Konstanten

    //Variablen als Objekte

    //Variablen
    private int new_id;
    private int new_state;
    private String new_subject;
    private String new_description;
    private String new_activation_date;
    private String new_expiration_date;
    private String new_receiver;
    private String new_writer;


    public New(int id, int state, String subject, String description, String receiver, String writer, String activation_date, String expiration_date) {
        this.new_id = id;
        this.new_state = state;
        this.new_subject = subject;
        this.new_description = description;
        this.new_receiver = receiver;
        this.new_writer = writer;
        this.new_activation_date = activation_date;
        this.new_expiration_date = expiration_date;
    }

    public int getID() {
        return new_id;
    }

    public int getState() {
        return new_state;
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

    public String getActivationDate() {
        return new_activation_date;
    }

    public String getExpirationDate() {
        return new_expiration_date;
    }
}