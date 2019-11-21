package com.example.chatfirebase.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Conversas {

    private ListaMensagens lista_mensagens;

    public Conversas() {
    }

    public ListaMensagens getListaMensagens() {
        return lista_mensagens;
    }

    public void setListaMensagens(ListaMensagens listaMensagens) {
        this.lista_mensagens = listaMensagens;
    }


    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("lista_mensagens", lista_mensagens);
        return result;
    }

    public void addMensagem(Mensagem mensagem) {
        if (lista_mensagens == null) {
            lista_mensagens = new ListaMensagens();
        }
        getListaMensagens().addMensagem(mensagem);
    }
}
