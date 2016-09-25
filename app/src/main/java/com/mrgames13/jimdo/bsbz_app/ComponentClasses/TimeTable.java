package com.mrgames13.jimdo.bsbz_app.ComponentClasses;

public class TimeTable {
    //Konstanten

    //Variablen als Objekte

    //Variablen
    private String tt_mo;
    private String tt_di;
    private String tt_mi;
    private String tt_do;
    private String tt_fr;
    private String tt_receiver;


    public TimeTable(String tt_receiver, String tt_mo, String tt_di, String tt_mi, String tt_do, String tt_fr) {
        this.tt_receiver = tt_receiver;
        this.tt_mo = tt_mo;
        this.tt_di = tt_di;
        this.tt_mi = tt_mi;
        this.tt_do = tt_do;
        this.tt_fr = tt_fr;
    }

    public String getReceiver() {
        return tt_receiver;
    }

    public String getMo() {
        return tt_mo;
    }

    public String getDi() {
        return tt_di;
    }

    public String getMi() {
        return tt_mi;
    }

    public String getDo() {
        return tt_do;
    }

    public String getFr() {
        return tt_fr;
    }
}