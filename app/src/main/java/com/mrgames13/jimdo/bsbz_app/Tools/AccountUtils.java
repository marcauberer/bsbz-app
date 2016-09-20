package com.mrgames13.jimdo.bsbz_app.Tools;

public class AccountUtils {
    //Konstanten
    public int RIGHTS_STUDENT = 1;
    public int RIGHTS_CLASSSPEAKER = 2;
    public int RIGHTS_TEACHER = 3;
    public int RIGHTS_ADMINISTRATOR = 4;
    public int RIGHTS_TEAM = 5;

    //Variablen als Objekte
    private StorageUtils su;

    //Variablen
    public String acc_username;
    public String acc_password;
    public String acc_class;
    public int acc_rights;

    public AccountUtils(StorageUtils su) {
        this.su = su;
    }

    public String getAccUsername() {
        return acc_username;
    }

    public String getAccPassword() {
        return acc_password;
    }

    public String getAccClass() {
        return acc_class;
    }

    public int getAccRights() {
        return acc_rights;
    }
}