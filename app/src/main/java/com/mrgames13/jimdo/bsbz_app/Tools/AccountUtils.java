package com.mrgames13.jimdo.bsbz_app.Tools;

import com.mrgames13.jimdo.bsbz_app.CommonObjects.Account;

import java.util.ArrayList;

public class AccountUtils {
    //Konstanten

    //Variablen als Objekte
    private StorageUtils su;

    //Variablen
    private Account activeAccount;

    public AccountUtils(StorageUtils su) {
        this.su = su;
    }

    public void LogIn(String username, String password, String form, String rights_string) {
        int rights = Account.RIGHTS_STUDENT;
        if(rights_string.equals("student")) rights = Account.RIGHTS_STUDENT;
        if(rights_string.equals("classspeaker")) rights = Account.RIGHTS_CLASSSPEAKER;
        if(rights_string.equals("teacher")) rights = Account.RIGHTS_TEACHER;
        if(rights_string.equals("administrator")) rights = Account.RIGHTS_ADMIN;
        if(rights_string.equals("team")) rights = Account.RIGHTS_TEAM;
        //Klasse berichtigen
        if(form.equals("no_class")) form = getLastUser().getForm();
        //Account in den SharedPreferences anmelden
        su.addAccountWhenNotExisting(username, password, form, rights);
        su.setLastLoggedInAccount(username, password, form, rights);
        //ActiveAccount erstellen
        activeAccount = new Account(username, password, form, rights);
    }

    public Account getLastUser() {
        return su.getLastUser();
    }

    public ArrayList<Account> getAllUsers() {
        return su.getAllAccounts();
    }

    public Account getActiveAccount() {
        return activeAccount;
    }
}