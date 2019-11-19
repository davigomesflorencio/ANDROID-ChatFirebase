package com.example.chatfirebase.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatfirebase.R;
import com.example.chatfirebase.adapter.items.ItemContato;
import com.example.chatfirebase.model.Contato;
import com.example.chatfirebase.model.ListaContatos;
import com.example.chatfirebase.ui.contato.DetalhesContato;

public class AdapterContatos extends RecyclerView.Adapter<ItemContato> {

    private Context context;
    private ListaContatos listaContatos;

    public AdapterContatos(Context context, ListaContatos listaContatos) {
        this.context = context;
        this.listaContatos = listaContatos;
    }

    @NonNull
    @Override
    public ItemContato onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_contato, parent, false);
        ItemContato itemContato = new ItemContato(view);
        return itemContato;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemContato holder, final int position) {
        if (listaContatos.getListaContatos().size() > 0) {
            Contato itemContato = listaContatos.getListaContatos().get(position);
            holder.nomeContato.setText(itemContato.getNome());
            holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, DetalhesContato.class);
                    Bundle args = new Bundle();
                    args.putString("nome", listaContatos.getListaContatos().get(position).getNome());
                    args.putString("email", listaContatos.getListaContatos().get(position).getEmail());
                    i.putExtras(args);
                    context.startActivity(i);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return listaContatos.getListaContatos().size();
    }
}
