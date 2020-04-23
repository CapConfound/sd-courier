package ru.simdelivery.sdcourier.model;

public class AuthResponse {
    String username;
//    String email;
    String token;

    public AuthResponse(String username, String token){
        this.username = username;
//        this.email = email;
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
