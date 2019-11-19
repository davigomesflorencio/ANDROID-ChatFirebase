package com.example.chatfirebase.model;

import java.util.ArrayList;
import java.util.List;

public class ListaMensagens {

    private List<Mensagem> mensagens;

    public ListaMensagens() {
    }

    public List<Mensagem> getMensagens() {
        return mensagens;
    }

    public void addMensagem(Mensagem mensagem) {
        if (mensagens == null) {
            mensagens = new ArrayList<>();
        }
        this.mensagens.add(mensagem);
    }
}
