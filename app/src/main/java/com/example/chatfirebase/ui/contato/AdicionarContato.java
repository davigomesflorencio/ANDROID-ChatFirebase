package com.example.chatfirebase.ui.contato;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.chatfirebase.R;
import com.example.chatfirebase.model.Contato;
import com.example.chatfirebase.util.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

public class AdicionarContato extends AppCompatActivity {

    private FirebaseUtil firebaseUtil;

    private TextInputEditText nome;
    private TextInputEditText email;
    private MaterialButton btAddContato;

    private ProgressBar progressBar;


    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_contato);
        initView();
        controlView();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void initView() {
        nome = findViewById(R.id.nome);
        email = findViewById(R.id.email);
        btAddContato = findViewById(R.id.btAddContato);
        progressBar = findViewById(R.id.progressbar);

        toolbar = findViewById(R.id.toolbar);
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

    private void controlView() {
        firebaseUtil = new FirebaseUtil();

        toolbar.setTitle("Adicionar Contato");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        email.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == EditorInfo.IME_ACTION_DONE)
                        || ((event.getKeyCode() == KeyEvent.KEYCODE_ENTER) && (event.getAction() == KeyEvent.ACTION_DOWN))) {
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });
        btAddContato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String NOME = nome.getText().toString();
                String EMAIL = email.getText().toString();
                boolean ok = true;
                if (NOME.isEmpty()) {
                    nome.setError("Digite algo, nome invalido!");
                    nome.requestFocus();
                    ok = false;
                }
                if (EMAIL.isEmpty()) {
                    email.setError("Digite algo, email invalido!");
                    email.requestFocus();
                    ok = false;
                }
                if (ok) {
                    btAddContato.setEnabled(false);
                    progressBar.setFocusable(false);
                    progressBar.setVisibility(View.VISIBLE);
                    addContatoFirebase();
                } else {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }


    private void addContatoFirebase() {
        Contato ct = new Contato();
        ct.setNome(nome.getText().toString());
        ct.setEmail(email.getText().toString());
        if (!email.getText().equals(firebaseUtil.getFirebaseAuth().getCurrentUser().getEmail())) {
            String key = firebaseUtil.getFirebase()
                    .child("usuarios")
                    .child(firebaseUtil.getFirebaseAuth().getCurrentUser().getUid())
                    .child("lista_contatos").push().getKey();

            firebaseUtil.getFirebase()
                    .child("usuarios")
                    .child(firebaseUtil.getFirebaseAuth().getCurrentUser().getUid())
                    .child("lista_contatos")
                    .child(key)
                    .setValue(ct.toMap())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            btAddContato.setEnabled(true);
                            progressBar.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                Snackbar.make(nome, "Contato adicionado com sucesso!!!", Snackbar.LENGTH_LONG).show();

                            } else {
                                Snackbar.make(nome, "Não foi possivel realizar cadastro do contato!!!", Snackbar.LENGTH_LONG).show();
                            }
                        }
                    });
        } else {
            Snackbar.make(nome, "Contato com seu email não pode ser adicionado!!!", Snackbar.LENGTH_LONG).show();
        }
    }

}
