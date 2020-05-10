package ru.simdelivery.sdcourier.model;

import com.google.gson.annotations.SerializedName;

public class AuthResponse {

    @SerializedName("username")
    private String email;

    @SerializedName("name")
    private String username;

    private String token;

    public AuthResponse(String username, String email, String token){
        this.username = username;
        this.email = email;
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
