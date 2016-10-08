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
        //Aktiven Account laden
        loadActiveAccount();
    }

    public void LogIn(String username, String password, String form, String rights_string) {
        int rights = Account.RIGHTS_STUDENT;
        if(rights_string.equals("student")) rights = Account.RIGHTS_STUDENT;
        if(rights_string.equals("classspeaker")) rights = Account.RIGHTS_CLASSSPEAKER;
        if(rights_string.equals("parent")) rights = Account.RIGHTS_PARENT;
        if(rights_string.equals("teacher")) rights = Account.RIGHTS_TEACHER;
        if(rights_string.equals("administrator")) rights = Account.RIGHTS_ADMIN;
        if(rights_string.equals("team")) rights = Account.RIGHTS_TEAM;
        //Klasse berichtigen
        if(form.equals("no_class")) form = getLastUser().getForm();
        //Account in den SharedPreferences anmelden
        su.addAccountWhenNotExisting(username, password, form, rights);
        su.setLastLoggedInAccount(username, password, form, rights);
        //ActiveAccount erstellen
        saveActiveAccount(username, password, form, rights);
    }

    public Account getLastUser() {
        return su.getLastUser();
    }

    public ArrayList<Account> getAllUsers() {
        return su.getAllAccounts();
    }

    public Account getActiveAccount() {
        loadActiveAccount();
        return activeAccount;
    }

    private void saveActiveAccount(String username, String password, String form, int rights) {
        su.putString("ActiveAccount", username + "~" + password + "~" + form + "~" + String.valueOf(rights));
        activeAccount = new Account(username, password, form, rights);
    }

    private void loadActiveAccount() {
        String active_account = su.getString("ActiveAccount");
        if(!active_account.equals("")) {
            int index1 = active_account.indexOf("~", 0);
            int index2 = active_account.indexOf("~", index1 +1);
            int index3 = active_account.indexOf("~", index2 +1);
            String username = active_account.substring(0, index1);
            String password = active_account.substring(index1 +1, index2);
            String form = active_account.substring(index2 +1, index3);
            int rights = Integer.parseInt(active_account.substring(index3 +1));
            activeAccount = new Account(username, password, form, rights);
        }
    }
}