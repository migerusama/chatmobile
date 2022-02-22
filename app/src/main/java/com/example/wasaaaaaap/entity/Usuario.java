package com.example.wasaaaaaap.entity;

import androidx.room.*;

@Entity
public class Usuario {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String user;

    private String password;


    public Usuario() {
    }

    public Usuario(String user, String password) {
        this.user = user;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
