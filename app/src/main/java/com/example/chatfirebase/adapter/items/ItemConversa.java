package com.example.chatfirebase.adapter.items;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatfirebase.R;

public class ItemConversa extends RecyclerView.ViewHolder {
    public TextView nome_contato;
    public TextView ultimamensagem;

    public ItemConversa(@NonNull View itemView) {
        super(itemView);
        initView(itemView);
    }

    private void initView(View view) {
        nome_contato = view.findViewById(R.id.nome_contato);
        ultimamensagem = view.findViewById(R.id.ultimaMensagem);
    }
}
