package ru.simdelivery.sdcourier.model;

public class Auth {

    public String username;
    public String password;
//    public String gcm_token;

    public Auth(String username, String password) {
        this.username = username;
        this.password = password;
//        this.gcm_token = gcm_token;
    }

}
