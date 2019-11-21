package com.example.chatfirebase.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Conversas {

    private ListaMensagens lista_mensagens;
    private String ultima_mensagem;

    public Conversas() {
    }

    public ListaMensagens getListaMensagens() {
        return lista_mensagens;
    }

    public void setListaMensagens(ListaMensagens listaMensagens) {
        this.lista_mensagens = listaMensagens;
    }

    public void addMensagem(Mensagem mensagem) {
        if (lista_mensagens == null) {
            lista_mensagens = new ListaMensagens();
        }
        getListaMensagens().addMensagem(mensagem);
    }


    public String getUltima_mensagem() {
        return ultima_mensagem;
    }

    public void setUltima_mensagem(String ultima_mensagem) {
        this.ultima_mensagem = ultima_mensagem;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("lista_mensagens", lista_mensagens);
        result.put("ultima_mensagem", ultima_mensagem);
        return result;
    }

}
