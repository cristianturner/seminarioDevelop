package com.example.seminario.seminariodevelop;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class Signup extends AppCompatActivity implements View.OnClickListener{
    ProgressBar progressbar;
    EditText editTextEmail,editTextUsername, editTextPassword;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        findViewById(R.id.cardViewSingup).setOnClickListener(this);
        findViewById(R.id.textAccessLogin).setOnClickListener(this);

        //editTextEmail = (EditText)findViewById(R.id.editTextEmail);
        editTextEmail = findViewById(R.id.editTextEmail);
        //editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        progressbar = findViewById(R.id.progressbar);

        mAuth = FirebaseAuth.getInstance();

    }
    private void registerUser(){
        String email = editTextEmail.getText().toString().trim();
        //String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(email.isEmpty()){
            editTextEmail.setError("El email es requerido");
            editTextEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Por favor ingrese un email valido");
            editTextEmail.requestFocus();
            return;
        }
        /*if(username.isEmpty()){
            editTextUsername.setError("El nombre de usuario es requerido");
            editTextUsername.requestFocus();
            return;
        }*/
        if(password.isEmpty()){
            editTextPassword.setError("El password es obligatorio");
            editTextPassword.requestFocus();
            return;
        }

        if(password.length()<6){
            editTextPassword.setError("El password debe ser mayor a 6 caracteres");
            editTextPassword.requestFocus();
            return;
        }

        progressbar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressbar.setVisibility(View.GONE);
                if (task.isSuccessful()){
                    Intent startSession = new Intent(Signup.this,MainHome.class); //iniciamos una nueva clase gallery
                    startSession.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(startSession);
                    Toast.makeText(getApplicationContext(),"Usuario Registrado", Toast.LENGTH_LONG).show()  ;
                }
                else{
                    if(task.getException() instanceof FirebaseAuthUserCollisionException){
                        Toast.makeText(getApplicationContext(), "Usted ya se encuentra registrado", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    //Toast.makeText(getApplicationContext(),"Ocurrio un error",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case(R.id.cardViewSingup):
                registerUser();
                break;
            case(R.id.textAccessLogin):
                startActivity(new Intent(this,Signin.class));
                break;

        }
    }
}
