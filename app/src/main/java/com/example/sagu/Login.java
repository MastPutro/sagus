package com.example.sagu;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login extends AppCompatActivity {
    EditText editEmail, editPasword;
    ProgressDialog progressDialog;
    Button btnLogin;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        editEmail = findViewById(R.id.edit_email);
        editPasword = findViewById(R.id.edit_password);
        btnLogin = findViewById(R.id.btn_login);
        mAuth = FirebaseAuth.getInstance();


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editEmail.getText().toString().isEmpty()){
                    Toast.makeText(Login.this, "Masukkan Email!", Toast.LENGTH_SHORT).show();
                }else {
                    if (editPasword.getText().toString().isEmpty()){
                        Toast.makeText(Login.this, "Masukkan Password!", Toast.LENGTH_SHORT).show();
                    }else {
                        verify();
                    }
                }
            }
        });


    }
    public void verify (){
        String email = editEmail.getText().toString();
        String password = editPasword.getText().toString();

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success
                        FirebaseUser user = mAuth.getCurrentUser();

                        // Check user level
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.collection("user")
                                .document(user.getUid())
                                .get()
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        DocumentSnapshot document = task1.getResult();

                                        if (document.get("level").equals("admin")) {
                                            progressDialog.dismiss();
                                            startActivity(new Intent(this, ManagerEmployee.class));
                                            Toast.makeText(this, "Login Berhasil", Toast.LENGTH_SHORT).show();
                                        } else if (document.get("level").equals("user")) {
                                            progressDialog.dismiss();
                                            startActivity(new Intent(this, List_pesanan.class));
                                            Toast.makeText(this, "Login Berhasil", Toast.LENGTH_SHORT).show();
                                        } else {
                                            progressDialog.dismiss();
                                            Toast.makeText(this, "User level tidak valid", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        progressDialog.dismiss();
                                        Log.d("LoginActivity", "Error getting user document", task1.getException());
                                    }
                                });
                    } else {
                        // Sign in failed
                        progressDialog.dismiss();
                        Log.d("LoginActivity", "Sign in failed", task.getException());
                        Toast.makeText(this, "Email atau password salah", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}