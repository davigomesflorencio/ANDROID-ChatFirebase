package com.example.chatfirebase.ui.conversas;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatfirebase.R;
import com.example.chatfirebase.adapter.AdapterMensagens;
import com.example.chatfirebase.model.Conversas;
import com.example.chatfirebase.model.InfoConversa;
import com.example.chatfirebase.model.ListaMensagens;
import com.example.chatfirebase.model.Mensagem;
import com.example.chatfirebase.util.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;

public class ConversaContato extends AppCompatActivity {

    private RecyclerView recyclerViewListaMensagens;
    private TextInputEditText mensagem;
    private Conversas conversas;
    private String id_chat;
    private String email_receptor;

    private LinearLayoutManager llm;

    private FirebaseUtil firebaseUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversa_contato);
        initView();
    }

    private void initView() {
        recyclerViewListaMensagens = findViewById(R.id.listaMensagens);
        mensagem = findViewById(R.id.textInputMensagem);

        controlView();
    }

    private void controlView() {
        Intent i = getIntent();
        if (i.getExtras() != null) {
            email_receptor = i.getStringExtra("email");
//            Log.d("ConversaContato", "email-> " + email_receptor);
        }

        firebaseUtil = new FirebaseUtil();
        getChat();

        mensagem.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == EditorInfo.IME_ACTION_DONE)
                        || ((event.getKeyCode() == KeyEvent.KEYCODE_ENTER) && (event.getAction() == KeyEvent.ACTION_DOWN))) {
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    String msg = mensagem.getText().toString();
                    salvarMensagem(msg);
                    mensagem.setText("");
                    return true;
                }
                return false;
            }
        });
    }

    private void getChat() {
        firebaseUtil.getFirebase().child("conversas_ativas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                InfoConversa c = null;
                for (DataSnapshot dt : dataSnapshot.getChildren()) {
                    c = dt.getValue(InfoConversa.class);
                    if (c != null) {
                        if ((c.getId_participante01().equals(firebaseUtil.getFirebaseAuth().getCurrentUser().getUid()) &&
                                c.getId_participante02().equals(email_receptor))
                                || (c.getId_participante02().equals(firebaseUtil.getFirebaseAuth().getCurrentUser().getEmail()))) {
                            id_chat = dt.getKey();
                            conversas = new Conversas();
                            getListaMensagens(id_chat);
                        }
                    }
                }
                if (c == null)
                    createChat();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void createChat() {
        conversas = new Conversas();
        salvarConversa(conversas);
    }

    private void salvarConversa(Conversas c) {
        String keychat = firebaseUtil.getFirebase().child("conversas_ativas").push().getKey();
        ativarChat(keychat);
    }

    private void ativarChat(String id_chat) {
        firebaseUtil.getFirebase()
                .child("usuarios")
                .child(firebaseUtil.getFirebaseAuth().getCurrentUser().getUid())
                .child("conversas")
                .child(id_chat)
                .setValue(true)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("ConversaContato", "ConversaContato ativo salvo");
                        } else {
                            Log.d("ConversaContato", "ConversaContato ativo não salvo");
                        }
                    }
                });

        InfoConversa infoConversa = new InfoConversa();
        infoConversa.setId_participante01(firebaseUtil.getFirebaseAuth().getCurrentUser().getUid());
        infoConversa.setId_participante02(email_receptor);

        firebaseUtil.getFirebase()
                .child("conversas_ativas")
                .child(id_chat)
                .setValue(infoConversa)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("ConversaContato", "ConversaContato ativo salvo");
                        } else {
                            Log.d("ConversaContato", "ConversaContato ativo não salvo");
                        }
                    }
                });
    }

    private void salvarConversa(Conversas c, String id_chat) {
        firebaseUtil.getFirebase()
                .child("conversas")
                .child(id_chat)
                .setValue(c.toMap())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("ConversaContato", "FragmentConversas salvo");
                        } else {
                            Log.d("ConversaContato", "FragmentConversas não salvo");
                        }
                    }
                });
    }

    private void getListaMensagens(String id_chat) {
        firebaseUtil.getFirebase()
                .child("conversas")
                .child(id_chat)
                .child("lista_mensagens")
                .child("mensagens")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ListaMensagens listaMensagens = new ListaMensagens();
                        for (DataSnapshot dt : dataSnapshot.getChildren()) {
                            Mensagem m = dt.getValue(Mensagem.class);
                            listaMensagens.addMensagem(m);
                        }
                        if (listaMensagens.getMensagens() != null) {
                            conversas.setListaMensagens(listaMensagens);

                            AdapterMensagens adapterMensagens = new AdapterMensagens(getApplicationContext(), listaMensagens);

                            llm = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, true);
                            recyclerViewListaMensagens.setLayoutManager(llm);
                            recyclerViewListaMensagens.setAdapter(adapterMensagens);
                        } else {
                            Log.d("conversaContato", "lista nula");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void salvarMensagem(String msg) {
        conversas.addMensagem(createMensagem(msg));
        salvarConversa(conversas, id_chat);
    }


    private Mensagem createMensagem(String msg) {
        Mensagem m = new Mensagem();
        m.setOrigem(firebaseUtil.getFirebaseAuth().getCurrentUser().getUid());
        m.setDestino(email_receptor);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        m.setHora(timestamp.toString());
        m.setConteudo(msg);
        return m;
    }
}

