package com.example.chatfirebase.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatfirebase.R;
import com.example.chatfirebase.adapter.items.ItemConversa;
import com.example.chatfirebase.util.FirebaseUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdapterConversas extends RecyclerView.Adapter<ItemConversa> {

    private Context context;
    private HashMap<String, String> hashMap;
    private List<String> nomes;
    private List<String> id_conversas;
    private FirebaseUtil firebaseUtil;

    public AdapterConversas(Context context, HashMap<String, String> hashMap) {
        this.context = context;
        this.hashMap = hashMap;
        control();
    }

    private void control() {
        nomes = new ArrayList<>();
        id_conversas = new ArrayList<>();
        firebaseUtil = new FirebaseUtil();
        for (Map.Entry<String, String> entry : hashMap.entrySet()) {
            nomes.add(entry.getValue());
            id_conversas.add(entry.getKey());
        }
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
        setUltimaMensagem(holder, position);
    }

    @Override
    public int getItemCount() {
        return hashMap.size();
    }

    private void setUltimaMensagem(final ItemConversa holder, final int position) {
        firebaseUtil.getFirebase()
                .child("conversas")
                .child(id_conversas.get(position))
                .child("ultima_mensagem")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        holder.ultimamensagem.setText("Ultima mensagem: "+dataSnapshot.getValue(String.class));
                        holder.nome_contato.setText(nomes.get(position));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}
