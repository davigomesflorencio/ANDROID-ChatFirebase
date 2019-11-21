package com.example.chatfirebase.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatfirebase.R;
import com.example.chatfirebase.adapter.AdapterConversas;
import com.example.chatfirebase.adapter.decoration.Divider;
import com.example.chatfirebase.model.Conversas;
import com.example.chatfirebase.model.InfoConversa;
import com.example.chatfirebase.util.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


public class FragmentConversas extends Fragment {

    private static final String ARG_PARAM1 = "id_contato";

    private FirebaseUtil firebaseUtil;
    private String email_emissor;

    private RecyclerView listaConversas;

    private LinearLayoutManager llm;

    public FragmentConversas() {
    }

    public static FragmentConversas newInstance(String id_contato) {
        FragmentConversas fragment = new FragmentConversas();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, id_contato);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.email_emissor = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conversas, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        listaConversas = view.findViewById(R.id.ListaConversas);

        controlView();
    }

    private void controlView() {
        firebaseUtil = new FirebaseUtil();
        getConversasExistentes();
        getIDConversasUsuario();
    }

    private void getConversasExistentes() {
        firebaseUtil.getFirebase()
                .child("conversas_ativas")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        InfoConversa c = new InfoConversa();
                        for (DataSnapshot dt : dataSnapshot.getChildren()) {
                            c = dt.getValue(InfoConversa.class);
                            if (c != null) {
                                if ((c.getId_participante02().equals(email_emissor))) {
                                    updateConversas(c.getId_participante01(), dt.getKey());
                                } else if (c.getId_participante01().equals(email_emissor)) {
                                    updateConversas(c.getId_participante02(), dt.getKey());
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void updateConversas(String nome, String id_chat) {
        firebaseUtil.getFirebase().
                child("usuarios")
                .child(firebaseUtil.getFirebaseAuth().getCurrentUser().getUid())
                .child("conversas_ativas")
                .child(id_chat)
                .setValue(nome)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
    }


    private void getIDConversasUsuario() {
        firebaseUtil.getFirebase()
                .child("usuarios")
                .child(firebaseUtil.getFirebaseAuth().getCurrentUser().getUid())
                .child("conversas_ativas")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        HashMap<String, String> hashMap = new HashMap<>();
                        for (DataSnapshot dt : dataSnapshot.getChildren()) {
                            hashMap.put(dt.getKey(), dt.getValue(String.class));
                        }
                        getUltimaMensagem(hashMap);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void getUltimaMensagem(HashMap<String, String> hashMap) {
        AdapterConversas adapterConversas = new AdapterConversas(getContext(), hashMap);

        listaConversas.addItemDecoration(new Divider(getContext()));

        llm = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        listaConversas.setLayoutManager(llm);
//        listaConversas.setdi
        listaConversas.setAdapter(adapterConversas);
    }

}
