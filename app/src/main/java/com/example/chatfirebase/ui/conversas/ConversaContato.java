package com.example.chatfirebase.ui.conversas;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatfirebase.R;
import com.example.chatfirebase.adapter.AdapterMensagens;
import com.example.chatfirebase.adapter.decoration.Divider;
import com.example.chatfirebase.model.Conversas;
import com.example.chatfirebase.model.InfoConversa;
import com.example.chatfirebase.model.ListaMensagens;
import com.example.chatfirebase.model.Mensagem;
import com.example.chatfirebase.util.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;

public class ConversaContato extends AppCompatActivity {

    private RecyclerView recyclerViewListaMensagens;
    private TextInputEditText mensagem;
    private Conversas conversas;
    private static String id_chat = null;
    private String email_emissor;


    private String email_receptor;
    private String nome_receptor;

    private LinearLayoutManager llm;

    private FirebaseUtil firebaseUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversa_contato);
        initView();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
            nome_receptor = i.getStringExtra("nome");
        }

        conversas = new Conversas();
        firebaseUtil = new FirebaseUtil();
        email_emissor = firebaseUtil.getFirebaseAuth().getCurrentUser().getEmail();
        getChat();

        mensagem.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == EditorInfo.IME_ACTION_DONE)
                        || ((event.getKeyCode() == KeyEvent.KEYCODE_ENTER) && (event.getAction() == KeyEvent.ACTION_DOWN))) {
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    if (id_chat != null) {
                        String msg = mensagem.getText().toString();
                        salvarMensagem(msg);
                        mensagem.setText("");
                    } else {
                        Snackbar.make(mensagem, "Espere um momento por favor", BaseTransientBottomBar.LENGTH_LONG).show();
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private void getChat() {
        firebaseUtil.getFirebase()
                .child("conversas_ativas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                InfoConversa c = null;
                for (DataSnapshot dt : dataSnapshot.getChildren()) {
                    c = dt.getValue(InfoConversa.class);
                    if (c != null) {
                        if ((c.getId_participante01().equals(email_receptor) &&
                                c.getId_participante02().equals(email_emissor))
                                || (c.getId_participante02().equals(email_receptor) &&
                                c.getId_participante01().equals(email_emissor))) {
                            id_chat = dt.getKey();
                            getListaMensagens(id_chat);
                            Log.d("ConversaContato", "idchat" + id_chat);
                        }
                    }
                }
                if (id_chat == null)
                    createConversa();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void createConversa() {
        String keychat = firebaseUtil.getFirebase()
                .child("conversas_ativas").push().getKey();
        ativarChat(keychat);
    }

    private void ativarChat(String id_chat) {
        InfoConversa infoConversa = new InfoConversa();
        infoConversa.setId_participante01(email_emissor);
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

        firebaseUtil.getFirebase()
                .child("usuarios")
                .child(firebaseUtil.getFirebaseAuth().getCurrentUser().getUid())
                .child("conversas_ativas")
                .child(id_chat)
                .setValue(nome_receptor)
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
                            Log.d("ConversaContato", "ConversaContato salvo");
                        } else {
                            Log.d("ConversaContato", "ConversaContato não salvo");
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

                            recyclerViewListaMensagens.addItemDecoration(new Divider(getApplicationContext()));

                            llm = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, true);
                            recyclerViewListaMensagens.setLayoutManager(llm);
                            recyclerViewListaMensagens.setAdapter(adapterMensagens);
                        } else {
                            Log.d("ConversaContato", "lista nula");
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
        m.setOrigem(email_emissor);
        m.setDestino(email_receptor);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        m.setHora(timestamp.toString());
        m.setConteudo(msg);
        conversas.setUltima_mensagem(msg);
        return m;
    }
}

