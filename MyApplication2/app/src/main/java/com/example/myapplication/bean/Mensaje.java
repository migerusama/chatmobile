package com.example.myapplication.bean;

public class Mensaje {
    public static final int TYPE_RECEIVED = 0;// Indica que este es un mensaje recibido
    public static final int TYPE_SENT = 1;// Indica que este es un mensaje enviado
    private String content;
    private int type;

    /**
     * Indica el contenido del mensaje y tipo indica el tipo de mensaje. Los dos valores anteriores son opcionales para el tipo de mensaje.
     */
    public Mensaje(String content, int type) {
        this.content = content;
        this.type = type;
    }

    public Mensaje(String content) {
        this.content = content;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public String getContent() {
        return content;
    }
}
