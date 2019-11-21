package com.example.chatfirebase.ui.contato;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.chatfirebase.R;
import com.example.chatfirebase.model.DetailsContato;
import com.example.chatfirebase.ui.conversas.ConversaContato;
import com.example.chatfirebase.util.FirebaseUtil;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class DetalhesContato extends AppCompatActivity {

    private FirebaseUtil firebaseUtil;
    private TextView textViewNome;
    private TextView textViewEndereco;
    private TextView textViewTelefone;
    private TextView textViewEmail;
    private MaterialButton startConversa;

    private String email_contato;
    private String nome_contato;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_contato);
        initView();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
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
        textViewNome = findViewById(R.id.nome);
        textViewEndereco = findViewById(R.id.endereco);
        textViewTelefone = findViewById(R.id.telefone);
        textViewEmail = findViewById(R.id.email);
        startConversa = findViewById(R.id.startConversa);

        firebaseUtil = new FirebaseUtil();

        toolbar = findViewById(R.id.toolbar);
        controlView();
    }

    private void controlView() {
        toolbar.setTitle("Detalhes do Contato");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();
        if (i.getExtras() != null) {
            nome_contato = i.getStringExtra("nome");
            email_contato = i.getStringExtra("email");
            GetDetalhesFirebase();
        }

        startConversa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), ConversaContato.class);
                Bundle args = new Bundle();
                args.putString("email", email_contato);
                args.putString("nome", nome_contato);
                i.putExtras(args);
                startActivity(i);
                finish();
            }
        });
    }

    private void GetDetalhesFirebase() {
        firebaseUtil.getFirebase().child("detalhes_contatos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    DetailsContato dtl = data.getValue(DetailsContato.class);
                    if (dtl.getEmail().equals(email_contato)) {
                        dtl.setNome(nome_contato);
                        setDetailsView(dtl);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void setDetailsView(DetailsContato dtl) {
        textViewNome.setText("Nome: " + dtl.getNome());
        textViewEmail.setText("Email: " + dtl.getEmail());
        textViewEndereco.setText("Endereço: " + dtl.getEndereco());
        textViewTelefone.setText("Telefone: " + dtl.getTelefone());
    }
}
