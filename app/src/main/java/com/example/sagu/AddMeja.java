package com.example.sagu;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddMeja extends AppCompatActivity {

    ImageButton save, back;
    EditText txtmeja, txtket;
    Button btsave;
    FirebaseFirestore fstore;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meja);
        save = findViewById(R.id.save);
        back = findViewById(R.id.back);
        txtmeja = findViewById(R.id.txt_mejaname);
        txtket = findViewById(R.id.txt_mejaket);
        btsave = findViewById(R.id.bt_savemeja);
        fstore = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                saveMeja();
            }
        });
        btsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                saveMeja();
            }
        });
    }

    public void back(){
        Intent intent = new Intent(AddMeja.this, ManagerTable.class);
        startActivity(intent);
    }
    public void saveMeja(){
        String meja_name = txtmeja.getText().toString();
        String meja_ket = txtket.getText().toString();
        DocumentReference documentReference = fstore.collection("meja").document();
        Map<String, Object> user = new HashMap<>();
        user.put("mejan",meja_name);
        user.put("keterangan",meja_ket);
        user.put("status", true);
        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                progressDialog.dismiss();
                Log.d(TAG, "onSuccess: meja is created" + meja_name);
                Toast.makeText(getApplicationContext(), "Meja ditambahkan!", Toast.LENGTH_SHORT);
                back();
            }
        });
    }
}