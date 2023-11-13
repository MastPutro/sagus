package com.example.sagu;

import androidx.appcompat.app.AppCompatActivity;

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
    Button btnLogin;
    TextView txtDaftar;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        editEmail = findViewById(R.id.edit_email);
        editPasword = findViewById(R.id.edit_password);
        btnLogin = findViewById(R.id.btn_login);
        txtDaftar = findViewById(R.id.txt_daftar);
        mAuth = FirebaseAuth.getInstance();


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verify();

            }
        });


    }
    public void verify (){
        String email = editEmail.getText().toString();
        String password = editPasword.getText().toString();

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
                                            startActivity(new Intent(this, ManagerEmployee.class));
                                        } else if (document.get("level").equals("user")) {
                                            startActivity(new Intent(this, List_pesanan.class));
                                        } else {
                                            Toast.makeText(this, "User level tidak valid", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Log.d("LoginActivity", "Error getting user document", task1.getException());
                                    }
                                });
                    } else {
                        // Sign in failed
                        Log.d("LoginActivity", "Sign in failed", task.getException());
                        Toast.makeText(this, "Email atau password salah", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}