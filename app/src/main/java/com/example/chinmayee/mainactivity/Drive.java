package com.example.chinmayee.mainactivity;

import android.app.Application;

public class Drive extends Application {

    private String userId;
    private String userEmail;
    private String level;
    private String pic;


    private String firebaseURL;


    public String getFirebaseURL() {
        return "https://flickering-inferno-293.firebaseio.com/";
    }

    public void setFirebaseURL(String firebaseURL) {
        this.firebaseURL = firebaseURL;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}