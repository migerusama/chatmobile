package com.example.myapplication;

import android.os.AsyncTask;
import android.os.Looper;

import com.example.myapplication.bean.Mensaje;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class Client implements Runnable {

    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Socket socket;
    private String nickname;
    private Mensaje mensaje;
    private boolean done;
    private List<Mensaje> mensajeList;

    public Client(String nickname, Mensaje mensaje, List<Mensaje> mensajeList) {
        this.nickname = nickname;
        this.mensaje = mensaje;
        this.done = false;
        this.mensajeList = mensajeList;
    }

    @Override
    public void run() {
        AsyncTask.execute(() -> {
            if (Looper.myLooper() != null) Looper.prepare();
            try {
                socket = new Socket("localhost", 666);
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

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                shutdown();
            }
        });
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
