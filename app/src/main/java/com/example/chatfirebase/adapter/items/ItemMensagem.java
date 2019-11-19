package com.example.chatfirebase.adapter.items;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatfirebase.R;
import com.google.android.material.textview.MaterialTextView;

public class ItemMensagem extends RecyclerView.ViewHolder {

    public MaterialTextView mensagem;
    public MaterialTextView hora;

    public ItemMensagem(@NonNull View itemView) {
        super(itemView);
        initView(itemView);
    }

    private void initView(View view) {
        mensagem = view.findViewById(R.id.textViewMensagem);
        hora = view.findViewById(R.id.textViewhora);
    }
}
