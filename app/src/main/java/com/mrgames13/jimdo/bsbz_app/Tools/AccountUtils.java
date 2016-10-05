package com.mrgames13.jimdo.bsbz_app.Tools;

import com.mrgames13.jimdo.bsbz_app.CommonObjects.Account;

import java.util.ArrayList;

public class AccountUtils {
    //Konstanten

    //Variablen als Objekte
    private StorageUtils su;

    //Variablen

    public AccountUtils(StorageUtils su) {
        this.su = su;
    }

    public Account getLastUser() {
        ArrayList<Account> allUsers = getAllUsers();

        return null;
    }

    public ArrayList<Account> getAllUsers() {
        return su.getAllAccounts();
    }
}