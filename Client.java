package com.example.wasaaaaaap.bean;

import android.os.AsyncTask;
import android.os.Looper;

import com.example.wasaaaaaap.bean.Mensaje;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class Client extends AsyncTask<Mensaje, Void, Void> {

    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Socket socket;
    private String nickname;
    private Mensaje mensaje;
    private List<Mensaje> mensajeList;
    private boolean done;

    public Client(String nickname, List<Mensaje> mensajeList) {
        this.nickname = nickname;
        this.mensajeList = mensajeList;
        this.done = false;
    }

    @Override
    protected Void doInBackground(Mensaje... mensajes) {
        mensaje = mensajes[0];
        try {
            socket = new Socket("192.168.1.24", 6666);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            new Thread(() -> {
                while (!done) {
                    try {
                        out.writeObject(mensaje);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            Mensaje inMessage;
            while ((inMessage = (Mensaje) in.readObject()) != null) {
                mensajeList.add(inMessage);
            }
        } catch (Exception e) {
            shutdown();
        }
        return null;
    }

    private void shutdown() {
        done = true;
        try {
            in.close();
            out.close();
            if (!socket.isClosed()) socket.close();
        } catch (IOException e) {
            // ignore
        }
    }

    public List<Mensaje> getMensajeList() {
        return mensajeList;
    }
}
