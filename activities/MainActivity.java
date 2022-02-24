package com.example.wasaaaaaap.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wasaaaaaap.R;
import com.example.wasaaaaaap.UserDB;
import com.example.wasaaaaaap.dao.UserDao;
import com.example.wasaaaaaap.entity.Usuario;

public class MainActivity extends AppCompatActivity {

    private Button login, registro;
    private EditText password, username;
    private UserDao userDao;
    private String sUsername, sPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inti();
        onClick();

    }

    private void inti() {
        login = findViewById(R.id.bLogin);
        registro = findViewById(R.id.bRegistrarse);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        userDao = UserDB.getInstance(getApplicationContext()).userDao();
    }

    private void onClick() {

        //Login
        login.setOnClickListener(v -> {
            try {
                check();
                if (userDao.getUser(sUsername, sPassword) == null)
                    throw new Exception(getString(R.string.incorrectUserPass));
                else {
                    Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                    startActivity(intent);
                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        //Registro
        registro.setOnClickListener(v -> {
            try {
                check();
                if (userDao.getUser(sUsername) != null)
                    throw new Exception(getString(R.string.userTaken));
                else {
                    AsyncTask.execute(() -> userDao.insert(new Usuario(sUsername, sPassword)));
                    Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                    intent.putExtra("username",sUsername);
                    startActivity(intent);
                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void check() throws Exception {
        sUsername = username.getText().toString();
        sPassword = password.getText().toString();
        if (sUsername.isEmpty()) username.setError(getText(R.string.campoObligatorio));
        if (sPassword.isEmpty()) password.setError(getText(R.string.campoObligatorio));
        if (sUsername.isEmpty() || sPassword.isEmpty())
            throw new Exception();//TODO mensaje exception
        if (sUsername.length() < 3 || sUsername.length() > 20) {
            username.setError(getString(R.string.userIncorrect));
            throw new Exception();//TODO mensaje exception
        }
        if (sPassword.length() < 6 || sPassword.length() > 18) {
            password.setError(getString(R.string.passIncorrect));
            throw new Exception();//TODO mensaje exception
        }
    }
}