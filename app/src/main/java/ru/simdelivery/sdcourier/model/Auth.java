package ru.simdelivery.sdcourier.model;

public class Auth {

    public String username;
    public String password;
    public String gcmToken;

    public Auth(String username, String password, String gcmToken) {
        this.username = username;
        this.password = password;
        this.gcmToken = gcmToken;
    }

}
