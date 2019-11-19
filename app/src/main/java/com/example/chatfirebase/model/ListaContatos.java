package com.example.chatfirebase.model;

import java.util.ArrayList;
import java.util.List;

public class ListaContatos {
    private List<Contato> listaContatos;

    public ListaContatos() {
        this.listaContatos = new ArrayList<>();
    }

    public List<Contato> getListaContatos() {
        return listaContatos;
    }

    public void addContato(Contato ct) {
        this.listaContatos.add(ct);
    }

}
