package com.mrgames13.jimdo.bsbz_app.CommonObjects;

public class Account {
    //Konstanten
    public static final int RIGHTS_DEFAULT = 1; // normal_user
    public static final int RIGHTS_STUDENT = 1; // student
    public static final int RIGHTS_CLASSSPEAKER = 2; // classspeaker
    public static final int RIGHTS_TEACHER = 3; // teacher
    public static final int RIGHTS_ADMIN = 4; // administrator
    public static final int RIGHTS_TEAM = 5; // team

    //Variablen als Objekte

    //Variablen
    private String username;
    private String password;
    private String form;
    private int rights;
    private String lastLogin;

    public Account(String username, String password, String form, int rights, String lastLogin) {
        this.username = username;
        this.password = password;
        this.form = form;
        this.rights = rights;
        this.lastLogin = lastLogin;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getForm() {
        return form;
    }

    public int getRights() {
        return rights;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public void setRights(int rights) {
        this.rights = rights;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }
}