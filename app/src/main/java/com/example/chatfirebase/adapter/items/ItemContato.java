package com.example.chatfirebase.adapter.items;


import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatfirebase.R;
import com.google.android.material.button.MaterialButton;

public class ItemContato extends RecyclerView.ViewHolder {

    public MaterialButton btdetalhes;
    public TextView nomeContato;
    public TextView emailContato;

    public ItemContato(@NonNull View itemView) {
        super(itemView);
        initView(itemView);
    }

    private void initView(View v) {
        nomeContato = v.findViewById(R.id.nome_contato);
        emailContato = v.findViewById(R.id.email_contato);
        btdetalhes = v.findViewById(R.id.btdetalhes);
    }
}
