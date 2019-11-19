package com.example.chatfirebase.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatfirebase.R;
import com.example.chatfirebase.adapter.items.ItemConversa;
import com.example.chatfirebase.model.Contato;
import com.example.chatfirebase.model.ListaContatos;

public class AdapterConversas extends RecyclerView.Adapter<ItemConversa> {

    private Context context;
    private ListaContatos listaContatos;

    public AdapterConversas(Context context, ListaContatos listaContatos) {
        this.context = context;
        this.listaContatos = listaContatos;
    }

    @NonNull
    @Override
    public ItemConversa onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_conversa, parent, false);
        ItemConversa itemConversa = new ItemConversa(view);
        return itemConversa;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemConversa holder, int position) {
        Contato contato = listaContatos.getListaContatos().get(position);
        holder.nome_contato.setText(contato.getNome());

    }

    @Override
    public int getItemCount() {
        return listaContatos.getListaContatos().size();
    }
}
