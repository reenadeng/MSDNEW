package com.example.chinmayee.mainactivity;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Swapnil on 4/2/2016.
 */
public class UserObject {
    private int id;
    private String about;
    private List<String> badges;
    private Integer[] dim;
    private String email;
    private String fname;
    private String lname;
    private Integer level;
    private String pic;
    private boolean pubProfile;
    private String totalLevelScore ;

    public UserObject(){

    }

    public String getTotalLevelScore() {
        return totalLevelScore;
    }

    public void setTotalLevelScore(String totalLevelScore) {
        this.totalLevelScore = totalLevelScore;
    }

    public UserObject(int id, String about, List<String> badges, Integer[] dim, String email, String fname, String lname, int level, String pic, boolean pubProfile) {
        this.id = id;
        this.about = about;
        this.badges = badges;
        this.dim = dim;
        this.email = email;
        this.fname = fname;
        this.lname = lname;
        this.level = level;
        this.pic = pic;
        this.pubProfile = pubProfile;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public List<String> getBadges() {
        return badges;
    }

    public void setBadges(List<String> badges) {
        this.badges = badges;
    }

    public Integer[] getDim() {
        return dim;
    }

    public void setDim(Integer[] dim) {
        this.dim = dim;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public boolean isPubProfile() {
        return pubProfile;
    }

    public void setPubProfile(boolean pubProfile) {
        this.pubProfile = pubProfile;
    }

    public int getTotScore(){
        int sum=0;
        for (int i =0; i<5; i++){
            sum += dim[i];
        }
        return sum;
    }

    @Override
    public String toString() {
        return "UserObject{" +
                "id=" + id +
                ", about='" + about + '\'' +
                ", badges=" + badges +
                ", dim=" + Arrays.toString(dim) +
                ", email='" + email + '\'' +
                ", fname='" + fname + '\'' +
                ", lname='" + lname + '\'' +
                ", level=" + level +
                ", pic='" + pic + '\'' +
                ", pubProfile=" + pubProfile +
                ", totalLevelScore='" + totalLevelScore + '\'' +
                '}';
    }
}
