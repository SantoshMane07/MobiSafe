package com.example.mobisafe;


import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "contacts")
public class ContactModel {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String userName = "";
    private String userPhone = "";
    @Ignore
    public ContactModel(String userName, String userPhone){
        this.userName = userName;
        this.userPhone = userPhone;
    }
    public ContactModel(int id,String userName, String userPhone){
        this.id = id;
        this.userName = userName;
        this.userPhone = userPhone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }
}
