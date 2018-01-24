package com.example.user.login.model;

/**
 * Created by user on 25/07/2017.
 */

public class Token {
    String token, device_id;

    public Token(String token, String device_id, String deviceId) {
        this.token = token;
        this.device_id = device_id;
    }
    public Token (){

    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDevice_id() {
        return device_id;
    }

    public String getToken() {
        return token;
    }
}