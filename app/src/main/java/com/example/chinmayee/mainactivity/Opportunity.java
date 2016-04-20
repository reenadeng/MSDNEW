package com.example.chinmayee.mainactivity;;

/**
 *
 * Chinmayee Nitin Vaidya, Bhumitra Nagar, Swapnil Mahajan, Xinyan Deng
 * This is a helper class that stores the opportunity infomation.
 *
 */
public class Opportunity {
    private int id;
    private String name;
    private String img_loc;
    private String date;
    private int level;
    private String longDecs;
    private String shortDesc;
    private String location;
    private Integer[] dimScore;
    private String catagory;

    public Opportunity(int id, String name, String img_loc, String date, int level, String longDecs,
                       String shortDesc, Integer[] dimScore, String location, String catagory) {
        this.id = id;
        this.name = name;
        this.img_loc = img_loc;
        this.date = date;
        this.level = level;
        this.longDecs = longDecs;
        this.shortDesc = shortDesc;
        this.dimScore = dimScore;
        this.location= location;
        this.catagory = catagory;
    }

    public Opportunity() {}

    public String getCatagory() {
        return catagory;
    }

    public void setCatagory(String catagory) {
        this.catagory = catagory;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg_loc() {
        return img_loc;
    }

    public void setImg_loc(String img_loc) {
        this.img_loc = img_loc;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getLongDecs() {
        return longDecs;
    }

    public void setLongDecs(String longDecs) {
        this.longDecs = longDecs;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public Integer[] getDimScore() {
        return dimScore;
    }

    public void setDimScore(Integer[] dimScore) {
        this.dimScore = dimScore;
    }

    public int getTotScore(){
        int sum=0;
        for (int i =0; i<5; i++){
            sum += dimScore[i];
        }
        return sum;
    }
}
