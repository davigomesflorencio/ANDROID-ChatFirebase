package com.example.chatfirebase.fragments;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatfirebase.R;
import com.example.chatfirebase.adapter.AdapterContatos;
import com.example.chatfirebase.adapter.decoration.Divider;
import com.example.chatfirebase.model.Contato;
import com.example.chatfirebase.model.ListaContatos;
import com.example.chatfirebase.ui.contato.AdicionarContato;
import com.example.chatfirebase.util.FirebaseUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;


public class FragmentContatos extends Fragment {

    private static final String ARG_PARAM1 = "id_contato";
    private String id_contato;

    private FirebaseUtil firebaseUtil;
    private RecyclerView listaContatos;
    private FloatingActionButton addcontato;
    private ProgressBar progressBar;

    private LinearLayoutManager llm;

    public FragmentContatos() {

    }

    public static FragmentContatos newInstance(String id_contato) {
        FragmentContatos fragment = new FragmentContatos();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, id_contato);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id_contato = getArguments().getString(ARG_PARAM1);
            Log.d("FragmentContatos", "id_contato " + id_contato);
        }
        firebaseUtil = new FirebaseUtil();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contatos, container, false);
        initView(view);
        controlView(view);
        return view;
    }

    private void initView(View view) {
        listaContatos = view.findViewById(R.id.ListaContatos);
        addcontato = view.findViewById(R.id.AddContato);
        progressBar = view.findViewById(R.id.progressbar);
    }

    private void controlView(View view) {
        addcontato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), AdicionarContato.class);
                startActivity(i);
            }
        });
        progressBar.setVisibility(View.VISIBLE);
        getListaContatosUsuarioFirebase();
    }

    private void getListaContatosUsuarioFirebase() {
        final ListaContatos lista_ct_usuario = new ListaContatos();
        firebaseUtil.getFirebase()
                .child("usuarios")
                .child(id_contato)
                .child("lista_contatos")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        lista_ct_usuario.getListaContatos().clear();
                        for (DataSnapshot noteSnapshot : dataSnapshot.getChildren()) {
                            Contato ct = noteSnapshot.getValue(Contato.class);
                            lista_ct_usuario.addContato(ct);
                        }
                        getContatos(lista_ct_usuario);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void getContatos(final ListaContatos lista_ct_usuario) {

        firebaseUtil.getFirebase()
                .child("contatos")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final ListaContatos aux = new ListaContatos();
                        for (DataSnapshot noteSnapshot : dataSnapshot.getChildren()) {
                            Contato ct = noteSnapshot.getValue(Contato.class);
                            for (int i = 0; i < lista_ct_usuario.getListaContatos().size(); i++) {
                                if (lista_ct_usuario.getListaContatos().get(i).equals(ct)) {
                                    ct.setNome(lista_ct_usuario.getListaContatos().get(i).getNome());
                                    aux.addContato(ct);
                                }
                            }
                        }
                        AdapterContatos adapterContatos = new AdapterContatos(getContext(), aux);

                        listaContatos.addItemDecoration(new Divider(getContext()));

                        llm = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                        listaContatos.setLayoutManager(llm);
                        listaContatos.setAdapter(adapterContatos);
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }


}
