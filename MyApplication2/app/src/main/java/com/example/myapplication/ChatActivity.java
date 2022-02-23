package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.widget.Button;
import android.widget.EditText;

import com.example.myapplication.adapter.MsgAdapter;
import com.example.myapplication.bean.Mensaje;
import com.example.myapplication.server.Cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private List<Mensaje> mensajeList = new ArrayList<>();
    private EditText inputText;
    private Button send;
    private RecyclerView recyclerView;
    private MsgAdapter adapter;
    private int type;
    private String nickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chat);
        init();

        crearLayoutManager();

        onClick();
    }

    private void init() {
        inputText = findViewById(R.id.input_text);
        send = findViewById(R.id.send);
        recyclerView = findViewById(R.id.recycler_view);
    }

    private void crearLayoutManager() {
        //Crea un LayoutManager lineal predeterminado
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MsgAdapter(mensajeList);
        recyclerView.setAdapter(adapter);
        type = Mensaje.TYPE_SENT;
    }

    private void onClick() {
        send.setOnClickListener(v -> {
            String content = inputText.getText().toString().trim();
            if (!content.isEmpty()) {
                Mensaje mensaje = new Mensaje(content, type);
                Client cliente = new Client(nickname, mensaje, mensajeList);
                cliente.run();
                mensajeList = cliente.getMensajeList();

                // Cuando haya un mensaje nuevo, actualice la pantalla en RecyclerView
                adapter.notifyItemInserted(mensajeList.size() - 1);
                // Coloque el RecyclerView en la última línea
                recyclerView.scrollToPosition(mensajeList.size() - 1);
                // Vacía el contenido en el cuadro de entrada
                inputText.setText("");
                // Cambia el estado del diálogo y forma un diálogo
                // type = type == Mensaje.TYPE_RECEIVED ? Mensaje.TYPE_SENT : Mensaje.TYPE_RECEIVED;
            }
        });
    }
}