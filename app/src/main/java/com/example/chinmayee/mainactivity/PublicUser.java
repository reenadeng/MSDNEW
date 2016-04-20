package com.example.chinmayee.mainactivity;

/**
 *
 * Chinmayee Nitin Vaidya, Bhumitra Nagar, Swapnil Mahajan, Xinyan Deng
 * This is a helper class that stores the other user's infomation.
 *
 */
public class PublicUser {
    private String id;
    private String fname;
    private String lname;
    private String pic;

    public PublicUser(String id, String fname, String lname, String pic) {
        this.id = id;
        this.fname = fname;
        this.lname = lname;
        this.pic = pic;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
