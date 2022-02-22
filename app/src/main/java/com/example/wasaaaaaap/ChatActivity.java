package com.example.wasaaaaaap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.widget.Button;
import android.widget.EditText;

import com.example.wasaaaaaap.adapter.MsgAdapter;
import com.example.wasaaaaaap.bean.Mensaje;
import com.example.wasaaaaaap.server.Cliente;

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
    private Socket client;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private boolean done;
    private Thread t;

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
        t = new Thread(new InputHandler());
        done = false;
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
                AsyncTask.execute(() -> {
                    if (Looper.myLooper() != null) Looper.prepare();
                    try {
                        Mensaje mensaje = new Mensaje(content, type);
                        client = new Socket("localhost", 666);  //FALLAAAAAA
                        out = new ObjectOutputStream(client.getOutputStream());
                        in = new ObjectInputStream(client.getInputStream());
                        //Mando el mensaje
                        out.writeObject(mensaje);
                        out.flush();
                        t.start();
                        //Recibo los mensajes
                        while ((mensaje = (Mensaje) in.readObject()) != null)
                            mensajeList.add(mensaje);
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                });


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

    private void shutdown() {
        done = true;
        try {
            in.close();
            out.close();
            if (!client.isClosed()) client.close();
            this.finishAffinity();
        } catch (IOException e) {
            // ignore
        }
    }

    class InputHandler implements Runnable {

        @Override
        public void run() {
            try {
                BufferedReader inReader = new BufferedReader(new InputStreamReader(System.in));
                while (!done) {
                    String message = inReader.readLine();
                    out.writeObject(message);
                }
            } catch (Exception e) {
                shutdown();
            }
        }
    }

    class Client extends AsyncTask<String, String, Socket> {

        @Override
        protected Socket doInBackground(String[] objects) {
            try {
                client = new Socket("localhost", 666);
                return null;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

    }
}