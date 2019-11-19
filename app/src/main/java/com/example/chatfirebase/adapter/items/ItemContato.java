package com.example.chatfirebase.adapter.items;


import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatfirebase.R;

public class ItemContato extends RecyclerView.ViewHolder {

    public ConstraintLayout constraintLayout;
    public TextView nomeContato;

    public ItemContato(@NonNull View itemView) {
        super(itemView);
        initView(itemView);
    }

    private void initView(View v) {
        nomeContato = v.findViewById(R.id.nome_contato);
        constraintLayout = v.findViewById(R.id.item_contato);
    }
}
