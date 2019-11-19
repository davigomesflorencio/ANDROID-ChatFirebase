package com.example.chatfirebase.model;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatAtivos {

    private List<String> ChatsAtivos;

    public ChatAtivos() {
        ChatsAtivos = new ArrayList<>();
    }

    public List<String> getChatsAtivos() {
        return ChatsAtivos;
    }

    public void adicionarChat(String id) {
        getChatsAtivos().add(id);
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("chat_ativos", ChatsAtivos);
        return result;
    }
}
