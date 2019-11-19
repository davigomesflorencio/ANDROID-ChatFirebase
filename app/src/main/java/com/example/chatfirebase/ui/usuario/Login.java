package com.example.chatfirebase.ui.usuario;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.chatfirebase.R;
import com.example.chatfirebase.ui.usuario.Cadastro;
import com.example.chatfirebase.ui.usuario.Principal;
import com.example.chatfirebase.util.FirebaseUtil;
import com.example.chatfirebase.util.LibraryClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private FirebaseUtil firebaseUtil = new FirebaseUtil();

    private TextInputEditText email;
    private TextInputEditText senha;
    private TextView cadastro;
    private ProgressBar progressBar;
    private MaterialButton button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        controlView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        verifica_IF_LOGADO();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (firebaseUtil.getAuthStateListener() != null) {
            firebaseUtil.getFirebaseAuth().removeAuthStateListener(firebaseUtil.getAuthStateListener());
        }
    }

    private void initView() {
        email = findViewById(R.id.email);
        senha = findViewById(R.id.senha);
        cadastro = findViewById(R.id.txtCadastrar);
        button = findViewById(R.id.btLogar);
        progressBar = findViewById(R.id.progressbar);
    }

    private void controlView() {
        firebaseUtil.setAuthStateListener(getFirebaseAuthResultHandler());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String EMAIL = email.getText().toString();
                String SENHA = senha.getText().toString();
                boolean ok = true;
                if (EMAIL.isEmpty()) {
                    email.setError("E-mail não informado!");
                    ok = false;
                }
                if (SENHA.isEmpty()) {
                    senha.setError("Por favor digite uma senha!");
                    ok = false;
                } else if (SENHA.length() <= 5) {
                    senha.setError("A senha deve ser maior que 5 caracteres");
                    ok = false;
                }
                if (ok) {
                    button.setEnabled(false);
                    progressBar.setFocusable(true);
                    progressBar.setVisibility(View.VISIBLE);
                    LOGIN();
                } else {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
        cadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_cadastro();
            }
        });

    }


    private void verifica_IF_LOGADO() {
        if (firebaseUtil.getFirebaseAuth().getCurrentUser() != null) {
            open_principal();
        } else {
            firebaseUtil.getFirebaseAuth().addAuthStateListener(getFirebaseAuthResultHandler());
        }
    }

    private void LOGIN() {
        if (LibraryClass.isOnline(getApplicationContext()) == true) {
            firebaseUtil.getFirebaseAuth().signInWithEmailAndPassword(email.getText().toString(), senha.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            button.setEnabled(true);
                            progressBar.setVisibility(View.GONE);

                            if (!task.isSuccessful()) {
                                Snackbar.make(button, "Não foi possivel realizar o login!!", Snackbar.LENGTH_LONG).show();
                            }
                            return;
                        }
                    });
        } else {
            Snackbar.make(button, "Erro de conexão -> Sem acesso a internet!!", Snackbar.LENGTH_LONG).show();
        }
    }

    private FirebaseAuth.AuthStateListener getFirebaseAuthResultHandler() {
        FirebaseAuth.AuthStateListener callback = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser userFirebase = firebaseAuth.getCurrentUser();
                if (userFirebase == null) {
                    return;
                }
                open_principal();
            }
        };
        return (callback);
    }


    private void open_principal() {
        Intent intent = new Intent(this, Principal.class);
        Intent i = getIntent();
        if (i.getExtras() != null) {
            Bundle args = new Bundle();
            args.putString("nome", i.getStringExtra("nome"));
            args.putString("endereco", i.getStringExtra("endereco"));
            args.putString("telefone", i.getStringExtra("telefone"));
            args.putString("email", i.getStringExtra("email"));
            intent.putExtras(args);
        }
        startActivity(intent);
        finish();
    }

    public void open_cadastro() {
        Intent intent = new Intent(this, Cadastro.class);
        startActivity(intent);
    }
}
