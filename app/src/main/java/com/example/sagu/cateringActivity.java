package com.example.sagu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.sagu.katering.CatAdapter;
import com.example.sagu.katering.itemCat;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class cateringActivity extends AppCompatActivity {
    Button toko;
    ImageButton refresh;
    RecyclerView rv;
    FirebaseFirestore db;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catering);
        toko = findViewById(R.id.cat_onstore);
        refresh = findViewById(R.id.cat_refresh);
        rv = findViewById(R.id.cat_rv);
        db = FirebaseFirestore.getInstance();

        getCatData();
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCatData();
            }
        });
        toko.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(cateringActivity.this, List_pesanan.class);
                startActivity(intent);
            }
        });
    }
    public void getCatData(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fecthing Data....");
        progressDialog.show();
        db.collection("katering").whereEqualTo("status", true)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<itemCat> dataCat = new ArrayList<>();
                    for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                        itemCat data =  documentSnapshot.toObject(itemCat.class);
                        dataCat.add(data);
                    }
                    displayData(dataCat);
                }).addOnFailureListener(e -> {
                    Log.e("getCatData", "errorFechingdata"+e);
                    Toast.makeText(this, "error fecthing data", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                });
    }
    private void displayData(List<itemCat> dataCat){
        rv.setLayoutManager(new LinearLayoutManager(this));
        CatAdapter adapter = new CatAdapter(cateringActivity.this, dataCat);
        rv.setAdapter(adapter);
        progressDialog.dismiss();

    }
}