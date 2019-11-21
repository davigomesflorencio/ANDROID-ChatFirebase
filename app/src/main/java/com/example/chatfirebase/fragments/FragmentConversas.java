package com.example.chatfirebase.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatfirebase.R;
import com.example.chatfirebase.util.FirebaseUtil;


public class FragmentConversas extends Fragment {

    private static final String ARG_PARAM1 = "id_contato";
    private String id_contato;

    private FirebaseUtil firebaseUtil;


    private RecyclerView listaConversas;

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
            id_contato = getArguments().getString(ARG_PARAM1);
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

    }

    private void ListaConversas() {

    }
}
