package com.example.chatfirebase.model;

import androidx.annotation.Nullable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Contato {

    private String nome;
    private String email;

    public Contato() {

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("nome", nome);
        result.put("email", email);
        return result;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof Contato) {
            if (((Contato) obj).getEmail().equals(getEmail())) {
                return true;
            }
            return false;
        }
        return false;
    }
}
