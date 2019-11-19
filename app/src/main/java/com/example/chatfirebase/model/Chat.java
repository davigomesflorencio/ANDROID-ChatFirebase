package com.example.chatfirebase.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Chat {

    private String id_participante01;
    private String id_participante02;
    private ListaMensagens listaMensagens;

    public Chat() {
    }

    public ListaMensagens getListaMensagens() {
        return listaMensagens;
    }

    public void setListaMensagens(ListaMensagens listaMensagens) {
        this.listaMensagens = listaMensagens;
    }


    public String getId_participante01() {
        return id_participante01;
    }

    public void setId_participante01(String id_participante01) {
        this.id_participante01 = id_participante01;
    }

    public String getId_participante02() {
        return id_participante02;
    }

    public void setId_participante02(String id_participante02) {
        this.id_participante02 = id_participante02;
    }


    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id_participante01", id_participante01);
        result.put("id_participante02", id_participante02);
        result.put("lista_mensagens", listaMensagens);
        return result;
    }

    public void addMensagem(Mensagem mensagem) {
        if (listaMensagens == null) {
            listaMensagens = new ListaMensagens();
        }
        getListaMensagens().addMensagem(mensagem);
    }
}
