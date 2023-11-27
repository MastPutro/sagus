package com.example.sagu.pesanan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.sagu.R;
import com.example.sagu.menu.Item;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddPesan extends AppCompatActivity {
    Spinner spin_menu;
    RecyclerView pesanananBaru;
    ImageButton addBarang;
    FirebaseFirestore db;
    ProgressDialog progressDialog;
    ArrayAdapter<String> adapter;
    ArrayList<String> arrayMeja;
    QuerySnapshot liMeja;
    pesanAdapter pAdapter;
    String idMeja;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pesan);
        db = FirebaseFirestore.getInstance();
        spin_menu = findViewById(R.id.spin_meja);
        addBarang = findViewById(R.id.bt_addBarang);
        pesanananBaru = findViewById(R.id.rv_pesananbaru);
        pAdapter = new pesanAdapter(new ArrayList<>());

        arrayMeja = new ArrayList<>();
        adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayMeja);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_menu.setAdapter(adapter);
        getDataSpin();

        spin_menu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(), "" +adapter.getItem(i), Toast.LENGTH_SHORT).show();
                idMeja = liMeja.getDocuments().get(i).getId();
                Log.e("ID Nama Meja", liMeja.getDocuments().get(i).getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        addBarang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddPesan.this, popupAddMenu.class);
                startActivity(intent);
            }
        });
    }
    public void getDataSpin(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        db.collection("meja").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                liMeja = queryDocumentSnapshots;
                if (queryDocumentSnapshots.size()>0){
                    arrayMeja.clear();
                    for (DocumentSnapshot document : queryDocumentSnapshots){
                        arrayMeja.add("Meja " + document.getString("mejan"));
                    }
                    adapter.notifyDataSetChanged();
                    progressDialog.dismiss();
                }else {
                    Toast.makeText(getApplicationContext(), "data idak ada", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.show();
                Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void getListMenu(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Retieve data...");
        progressDialog.show();
        String date = DateFormat.getDateInstance().format(Calendar.getInstance().getTime());
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("pesan").orderBy(date).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                List<pesanItem> liItem = new ArrayList<>();
                for (DocumentSnapshot document : task.getResult()) {
                    pesanItem itemLi = document.toObject(pesanItem.class);
                    liItem.add(itemLi);

                }
                pAdapter.pesanL = liItem;
                pAdapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }else {
                Log.d("AddPesan", "Error getting menu", task.getException());
                Toast.makeText(this, "Retrive data failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

}