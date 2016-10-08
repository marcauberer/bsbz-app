package com.mrgames13.jimdo.bsbz_app.CommonObjects;

public class Account {
    //Konstanten
    public static final int RIGHTS_DEFAULT = 1; // student
    public static final int RIGHTS_STUDENT = 1; // student
    public static final int RIGHTS_CLASSSPEAKER = 2; // classspeaker
    public static final int RIGHTS_PARENT = 3; // parent
    public static final int RIGHTS_TEACHER = 4; // teacher
    public static final int RIGHTS_ADMIN = 5; // administrator
    public static final int RIGHTS_TEAM = 6; // team

    //Variablen als Objekte

    //Variablen
    private String username;
    private String password;
    private String form;
    private int rights = RIGHTS_STUDENT;

    public Account(String username, String password, String form, int rights) {
        this.username = username;
        this.password = password;
        this.form = form;
        this.rights = rights;
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

    public String getRightsString() {
        if(rights == RIGHTS_TEAM) return "team";
        if(rights == RIGHTS_ADMIN) return "administrator";
        if(rights == RIGHTS_TEACHER) return "teacher";
        if(rights == RIGHTS_PARENT) return "parent";
        if(rights == RIGHTS_CLASSSPEAKER) return "classspeaker";
        return "student";
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
}