package com.example.wasaaaaaap.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.wasaaaaaap.entity.Usuario;

@Dao
public interface UserDao {

    @Insert
    void insert(Usuario user);

    @Update
    void update(Usuario user);

    @Delete
    void delete(Usuario user);

    @Query("SELECT * FROM Usuario AS u WHERE u.user=:usuario AND u.password=:pass")
    Usuario getUser(String usuario, String pass);

    @Query("SELECT * FROM Usuario AS u WHERE u.user=:usuario")
    Usuario getUser(String usuario);

}
