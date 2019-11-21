package com.example.chatfirebase.model;

import java.util.ArrayList;
import java.util.List;

public class ListaMensagens {

    private List<Mensagem> lista_mensagens;

    public ListaMensagens() {
    }

    public List<Mensagem> getMensagens() {
        return lista_mensagens;
    }

    public void addMensagem(Mensagem mensagem) {
        if (lista_mensagens == null) {
            lista_mensagens = new ArrayList<>();
        }
        this.lista_mensagens.add(mensagem);
    }
}
