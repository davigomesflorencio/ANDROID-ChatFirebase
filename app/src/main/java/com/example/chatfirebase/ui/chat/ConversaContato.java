package com.example.chatfirebase.ui.chat;

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
import com.example.chatfirebase.model.Chat;
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
    private Chat chat;
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
            Log.d("ConversaContato", "email-> " + email_receptor);
        }

        firebaseUtil = new FirebaseUtil();
        getChat();

//        btSend.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (!msg.isEmpty()) {
//                    salvarMensagem(msg);
//                    mensagem.setText("");
//                    mensagem.clearFocus();
//                }
//            }
//        });
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
        firebaseUtil.getFirebase().child("chats").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Chat c = null;
                for (DataSnapshot dt : dataSnapshot.getChildren()) {
                    c = dt.getValue(Chat.class);
                    if (c != null) {
                        if (c.getId_participante01().equals(firebaseUtil.getFirebaseAuth().getCurrentUser().getUid()) &&
                                c.getId_participante02().equals(email_receptor)) {
                            chat = c;
                            id_chat = dt.getKey();
                            Log.d("ConversaContato", "idchat-> " + id_chat);
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

    private void getListaMensagens(String id_chat) {
        firebaseUtil.getFirebase()
                .child("chats")
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
                            chat.setListaMensagens(listaMensagens);
                            for (int i = 0; i < listaMensagens.getMensagens().size(); i++)
                                Log.d("ConversaContato", "LISTA MENSAGENS ->" + listaMensagens.getMensagens().get(i).getConteudo());

                            AdapterMensagens adapterMensagens = new AdapterMensagens(getApplicationContext(), listaMensagens);

                            llm = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                            recyclerViewListaMensagens.setLayoutManager(llm);
                            recyclerViewListaMensagens.setAdapter(adapterMensagens);
                        } else {
                            Log.d("conversaCOntato", "lista nula");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void createChat() {
        chat = new Chat();
        chat.setId_participante01(firebaseUtil.getFirebaseAuth().getCurrentUser().getUid());
        chat.setId_participante02(email_receptor);
        salvarChat(chat);
    }

    private void salvarChat(Chat c) {
        String keychat = firebaseUtil.getFirebase().child("chats").push().getKey();
        Log.d("ConversaContato", "keychat-> " + keychat);
        id_chat = keychat;
        firebaseUtil.getFirebase()
                .child("chats")
                .child(keychat)
                .setValue(c.toMap())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("ConversaContato", "Chat salvo");
                        } else {
                            Log.d("ConversaContato", "Chat não salvo");
                        }
                    }
                });
        ativarChat(keychat);
    }

    private void salvarChat(Chat c, String id_chat) {
        firebaseUtil.getFirebase()
                .child("chats")
                .child(id_chat)
                .setValue(c.toMap())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("ConversaContato", "Chat salvo");
                        } else {
                            Log.d("ConversaContato", "Chat não salvo");
                        }
                    }
                });
    }

    private void salvarMensagem(String msg) {
        chat.addMensagem(createMensagem(chat.getId_participante02(), msg));
        salvarChat(chat, id_chat);
    }

    private void ativarChat(String id_chat) {
        firebaseUtil.getFirebase()
                .child("chats_ativos")
                .child(id_chat)
                .setValue(true)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("ConversaContato", "Chat ativo salvo");
                        } else {
                            Log.d("ConversaContato", "Chat ativo não salvo");
                        }
                    }
                });
    }

    private Mensagem createMensagem(String destino, String msg) {
        Mensagem m = new Mensagem();
        m.setOrigem(firebaseUtil.getFirebaseAuth().getCurrentUser().getUid());
        m.setDestino(destino);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        m.setHora(timestamp.toString());
        m.setConteudo(msg);
        return m;
    }
}

