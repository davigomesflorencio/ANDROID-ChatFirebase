package com.example.chatfirebase.ui.usuario;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.example.chatfirebase.R;
import com.example.chatfirebase.adapter.TabLayoutAdapter;
import com.example.chatfirebase.fragments.FragmentContatos;
import com.example.chatfirebase.fragments.FragmentConversas;
import com.example.chatfirebase.model.Contato;
import com.example.chatfirebase.model.DetailsContato;
import com.example.chatfirebase.ui.Login;
import com.example.chatfirebase.util.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

public class Principal extends AppCompatActivity {

    //FirebaseUtil
    private FirebaseUtil firebaseUtil = new FirebaseUtil();

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private TabLayoutAdapter tabLayoutAdapter;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        initView();
        controlView();
        initFirebase();
        salvarUsuario();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
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
            case R.id.sair:
                firebaseUtil.getFirebaseAuth().signOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (firebaseUtil.getAuthStateListener() != null) {
            firebaseUtil.getFirebaseAuth().removeAuthStateListener(firebaseUtil.getAuthStateListener());
        }
    }

    private void initFirebase() {
        firebaseUtil.getFirebaseAuth().addAuthStateListener(
                new FirebaseAuth.AuthStateListener() {
                    @Override
                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                        if (firebaseAuth.getCurrentUser() == null) {
                            Intent intent = new Intent(Principal.this, Login.class);
                            startActivity(intent);
                            finish();
                            return;
                        }
                        return;
                    }
                });

    }


    private void initView() {
        tabLayoutAdapter = new TabLayoutAdapter(getSupportFragmentManager());
        tabLayoutAdapter.addFragment(FragmentConversas.newInstance(firebaseUtil.getFirebaseAuth().getCurrentUser().getEmail()), "Conversas");
        tabLayoutAdapter.addFragment(FragmentContatos.newInstance(firebaseUtil.getFirebaseAuth().getCurrentUser().getUid()), "Contatos");
        viewPager = findViewById(R.id.viewpager);

        tabLayout = findViewById(R.id.tablayout);
        toolbar = findViewById(R.id.toolbar);
    }

    private void controlView() {
        viewPager.setAdapter(tabLayoutAdapter);
        tabLayout.setupWithViewPager(viewPager);
        setSupportActionBar(toolbar);
        toolbar.setTitle("ChatFirebase");

    }

    private void salvarUsuario() {
        Intent i = getIntent();
        if (i.getExtras() != null) {
            String nome = i.getStringExtra("nome");
            String endereco = i.getStringExtra("endereco");
            String telefone = i.getStringExtra("telefone");
            String email = i.getStringExtra("email");

            Contato ct = new Contato();
            ct.setNome(nome);
            ct.setEmail(email);
            String key = firebaseUtil.getFirebaseAuth().getCurrentUser().getUid();
            firebaseUtil.getFirebase()
                    .child("contatos")
                    .child(key)
                    .setValue(ct.toMap())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("Principal", "Contato cadastrado com sucesso");
                            }
                        }
                    });

            DetailsContato dtl = new DetailsContato();
            dtl.setNome(nome);
            dtl.setEndereco(endereco);
            dtl.setTelefone(telefone);
            dtl.setEmail(email);

            firebaseUtil.getFirebase()
                    .child("detalhes_contatos")
                    .child(key)
                    .setValue(dtl.toMap())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("Principal", "Detalhes contato cadastrado com sucesso");
                            }
                        }
                    });

        }

    }
}
