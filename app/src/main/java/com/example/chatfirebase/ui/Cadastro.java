package com.example.chatfirebase.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chatfirebase.R;
import com.example.chatfirebase.ui.Login;
import com.example.chatfirebase.util.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;

public class Cadastro extends AppCompatActivity {

    private FirebaseUtil firebaseUtil = new FirebaseUtil();

    private TextInputEditText nome;
    private TextInputEditText endereco;
    private TextInputEditText telefone;
    private TextInputEditText email;
    private TextInputEditText senha;
    private TextInputEditText senha_repeat;
    private ProgressBar progressBar;
    private MaterialButton salvar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        initView();
        controlView();
    }

    @Override
    public void onBackPressed() {
        finish();
        return;
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
        nome = findViewById(R.id.nome);
        endereco = findViewById(R.id.endereco);
        telefone = findViewById(R.id.telefone);
        email = findViewById(R.id.email);
        senha = findViewById(R.id.senha);
        senha_repeat = findViewById(R.id.senha_repeat);
        salvar = findViewById(R.id.btCadastro);
        progressBar = findViewById(R.id.progressbar);
    }

    private void controlView() {
        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String EMAIL = email.getText().toString();
                String ENDERECO = endereco.getText().toString();
                String TELEFONE = telefone.getText().toString();
                String SENHA = senha.getText().toString();
                String senha_r = senha_repeat.getText().toString();
                boolean ok = true;
                if (EMAIL.isEmpty()) {
                    email.setError("Digite algo, email invalido!");
                    email.requestFocus();
                    ok = false;
                }
                if (ENDERECO.isEmpty()) {
                    endereco.setError("Digite algo, endereco invalido!");
                    endereco.requestFocus();
                    ok = false;
                }
                if (TELEFONE.isEmpty()) {
                    telefone.setError("Digite algo, telefone invalido!");
                    telefone.requestFocus();
                    ok = false;
                }
                if (SENHA.isEmpty()) {
                    senha.setError("Digite algo, senha vazia é invalida");
                    senha.requestFocus();
                    ok = false;
                } else if (SENHA.length() <= 5) {
                    senha.setError("A senha deve ser maior que 5 caracteres");
                    senha.requestFocus();
                    ok = false;
                } else if (!SENHA.equals(senha_r)) {
                    senha_repeat.setError("As senhas devem ser iguais");
                    senha_repeat.requestFocus();
                    ok = false;
                }
                if (ok) {
                    salvar.setEnabled(false);
                    progressBar.setFocusable(false);
                    progressBar.setVisibility(View.VISIBLE);
                    salvarUsuario();
                } else {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    private void salvarUsuario() {

        firebaseUtil.getFirebaseAuth().createUserWithEmailAndPassword(email.getText().toString(), senha.getText().toString())
                .addOnCompleteListener(
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                salvar.setEnabled(true);
                                progressBar.setVisibility(View.GONE);

                                if (task.isSuccessful()) {
                                    Snackbar.make(salvar, "Sucesso!! Sua conta foi cadastrada!", Snackbar.LENGTH_LONG).show();

                                    Intent i = new Intent(getApplicationContext(), Login.class);
                                    Bundle args = new Bundle();
                                    args.putString("nome", nome.getText().toString());
                                    args.putString("endereco", endereco.getText().toString());
                                    args.putString("telefone", telefone.getText().toString());
                                    args.putString("email", email.getText().toString());
                                    i.putExtras(args);
                                    startActivity(i);
                                    finish();
                                } else {
                                    Snackbar.make(salvar, "Não foi possivel realizar cadastro de sua conta!!!", Snackbar.LENGTH_LONG).show();
                                }
                            }
                        }
                );

    }

}
