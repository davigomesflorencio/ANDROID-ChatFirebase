package com.example.chatfirebase.model;

import java.util.ArrayList;
import java.util.List;

public class ListaContatos {
    private List<Contato> lista_contatos;

    public ListaContatos() {
        this.lista_contatos = new ArrayList<>();
    }

    public List<Contato> getListaContatos() {
        return lista_contatos;
    }

    public void addContato(Contato ct) {
        this.lista_contatos.add(ct);
    }

}
