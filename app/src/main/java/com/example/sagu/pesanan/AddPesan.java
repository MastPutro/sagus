package com.example.sagu.pesanan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sagu.R;
import com.example.sagu.menu.Item;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddPesan extends AppCompatActivity implements PopDialog.ExampleDialogListener {
    Spinner spin_menu;
    RecyclerView pesanananBaru;
    ImageButton addBarang;
    FirebaseFirestore db, dba;
    ProgressDialog progressDialog;
    ArrayAdapter<String> adapter;
    ArrayList<String> arrayMeja;
    QuerySnapshot liMeja;
    String idMeja;
    TextView tvTotalharga;
    List<ItemP> itemPS = new ArrayList<ItemP>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pesan);
        db = FirebaseFirestore.getInstance();
        dba = FirebaseFirestore.getInstance();
        spin_menu = findViewById(R.id.spin_meja);
        addBarang = findViewById(R.id.bt_addBarang);
        tvTotalharga = findViewById(R.id.tv_hargatotal);
        pesanananBaru = findViewById(R.id.rv_pesananbaru);

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
//                Intent intent = new Intent(AddPesan.this, popupAddMenu.class);
//                startActivity(intent);
                PopDialog exampleDialog = new PopDialog();
                exampleDialog.show(getSupportFragmentManager(), "example dialog");
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
        pesanananBaru.setLayoutManager(new LinearLayoutManager(this));
        pesanananBaru.setAdapter(new MyAdapter(itemPS));

    }

    @Override
    public void applyTexts(String barang, String amount, String foodId) {
        getFoodFromFirestore(barang, amount, foodId);
    }
    public void getFoodFromFirestore(String barang, String amount, String foodId) {
        dba.collection("menu")
                .whereEqualTo("dataMenu", barang)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String harga = document.getString("dataHarga");

                                if (harga != null) {
                                    for (ItemP itemP : itemPS) {
                                        if (itemP.getFname().equals(barang)) {
                                            // Update the amount if the item already exists
                                            int newAmount = Integer.parseInt(itemP.getFamount()) + Integer.parseInt(amount);
                                            itemP.setFamount(String.valueOf(newAmount));
                                            // Update the RecyclerView
                                            getListMenu();
                                            calculateAndDisplayTotal();
                                            return;
                                        }
                                    }
                                    // Add a new item to the list with the price
                                    itemPS.add(new ItemP(barang, foodId, amount, harga));
                                    // Update the RecyclerView
                                    getListMenu();
                                    // Calculate and display the total price
                                    calculateAndDisplayTotal();
                                }
                            }
                        } else {
                            Log.e("Firestore", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
    public void calculateAndDisplayTotal() {
        int total = 0;
        for (ItemP itemP : itemPS) {
            int amount = Integer.parseInt(itemP.getFamount());
            int harga = Integer.parseInt(itemP.getHarga());
            total += amount * harga;
        }
        tvTotalharga.setText(String.valueOf(total));
    }


}