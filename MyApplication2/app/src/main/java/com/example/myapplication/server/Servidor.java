package com.example.myapplication.server;

import com.example.myapplication.bean.Mensaje;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Servidor implements Runnable {

    private ArrayList<ConnectionHandler> connections;
    private ServerSocket server;
    private boolean done;
    private ExecutorService pool;

    public Servidor() {
        connections = new ArrayList<>();
        done = false;
    }

    @Override
    public void run() {
        try {
            server = new ServerSocket(9999);
            pool = Executors.newCachedThreadPool();
            while (!done) {
                Socket client = server.accept();
                ConnectionHandler handler = new ConnectionHandler(client);
                connections.add(handler);
                pool.execute(handler);
            }
        } catch (Exception e) {
            shutdown();
        }
    }

    public void broadcast(Mensaje message) {
        for (ConnectionHandler ch : connections) {
            if (ch != null) ch.sendMessage(message);
        }
    }

    public void shutdown() {
        try {
            done = true;
            pool.shutdown();
            if (!server.isClosed()) {
                server.close();
            }
            for (ConnectionHandler ch : connections) {
                ch.shutdown();
            }
        } catch (Exception e) {
            // ignore
        }
    }

    class ConnectionHandler implements Runnable {

        private Socket client;
        private ObjectInputStream in;
        private ObjectOutputStream out;

        public ConnectionHandler(Socket client) {
            this.client = client;
        }

        @Override
        public void run() {
            try {
                out = new ObjectOutputStream(client.getOutputStream());
                in = new ObjectInputStream(client.getInputStream());
                Mensaje message;
                while ((message = (Mensaje) in.readObject()) != null) {
                    broadcast(message);
                }
            } catch (Exception e) {
                shutdown();
            }
        }

        public void sendMessage(Mensaje message) {
            try {
                out.writeObject(message);
            } catch (IOException e) {
                shutdown();
            }
        }

        public void shutdown() {
            try {
                in.close();
                out.close();
                if (!client.isClosed()) client.close();
            } catch (IOException e) {
                // ignore
            }
        }
    }

    public static void main(String[] args) {
        Servidor servidor = new Servidor();
        servidor.run();
    }











    /*public static void main(String[] args) throws Exception {
        byte[] bytes = new byte[1024];
        DatagramSocket socket = new DatagramSocket(8888);
        DatagramPacket dp = new DatagramPacket(bytes, bytes.length);
        socket.receive(dp);
        String texto = new String(dp.getData());
        InetAddress ipOrigen = dp.getAddress();
        int puerto = dp.getPort();
        byte[] mensaje = new byte[1024];
        String Saludo="Devuelvo Saludos !!";
        mensaje = Saludo.getBytes();
        DatagramPacket envio = new DatagramPacket(mensaje, mensaje.length, ipOrigen, puerto);
        socket.send(envio);
        socket.close();

    }*/
}
