package com.example.chatfirebase.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatfirebase.R;
import com.example.chatfirebase.adapter.items.ItemMensagem;
import com.example.chatfirebase.model.ListaMensagens;
import com.example.chatfirebase.model.Mensagem;
import com.example.chatfirebase.util.FirebaseUtil;

public class AdapterMensagens extends RecyclerView.Adapter<ItemMensagem> {
    private FirebaseUtil firebaseUtil;
    private Context context;
    private ListaMensagens listaMensagens;

    private final static int TYPE_SEND = 1, TYPE_RECEIVE = 2;

    public AdapterMensagens(Context context, ListaMensagens listaMensagens) {
        this.context = context;
        this.listaMensagens = listaMensagens;
        firebaseUtil = new FirebaseUtil();
    }

    @NonNull
    @Override
    public ItemMensagem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMensagem i = null;
        View view;
        switch (viewType) {
            case TYPE_SEND:
                view = LayoutInflater.from(context).inflate(R.layout.item_mensagem_send, parent, false);
                i = new ItemMensagem(view);
                break;
            case TYPE_RECEIVE:
                view = LayoutInflater.from(context).inflate(R.layout.item_mensagem_receive, parent, false);
                i = new ItemMensagem(view);
                break;
            default:
        }
        return i;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemMensagem holder, int position) {
        if (listaMensagens.getMensagens().size() > 0) {
            Mensagem aux = listaMensagens.getMensagens().get(position);
            holder.mensagem.setText(aux.getConteudo());
            holder.hora.setText(aux.getHora());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (listaMensagens.getMensagens().size() > 0) {
            if (listaMensagens.getMensagens().get(position).getOrigem().equals(firebaseUtil.getFirebaseAuth().getCurrentUser().getEmail())) {
                return TYPE_SEND;
            } else {
                return TYPE_RECEIVE;
            }
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return listaMensagens.getMensagens().size();
    }
}
