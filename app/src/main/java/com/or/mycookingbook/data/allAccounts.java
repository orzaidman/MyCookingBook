package com.or.mycookingbook.data;

import com.google.gson.Gson;

import java.util.ArrayList;


public class allAccounts {

     private ArrayList<Account> allAccounts;

        public allAccounts() {
            this.allAccounts = new ArrayList<>();
        }

        public allAccounts(allAccounts other) {
            this.allAccounts = other.allAccounts;
        }


        public ArrayList<Account> getAllAccounts() {
            return allAccounts;
        }

        public allAccounts(String data) {
            this(createPlayersFromString(data));
        }

        private static allAccounts createPlayersFromString(String data) {
            allAccounts tempP;
            if (data == "NA") {
                tempP = new allAccounts();
            }
            else {
                tempP = new Gson().fromJson(data, allAccounts.class);
            }
            return tempP;
        }



        public Account getAccountByPhone(String phoneNumber){
            for (Account a: allAccounts) {
                if(a.getUserPhoneNumber().equals(phoneNumber))
                    return  a;
            }
            return null;
        }

        public Account getAccountByUUid(String uuid){
            for (Account a: allAccounts) {
                if(a.getUuidAccount().equals(uuid))
                    return  a;
            }
            return null;
        }

        public void setAllAccounts(ArrayList<Account> allAccounts) {
            this.allAccounts = allAccounts;
        }

    }
