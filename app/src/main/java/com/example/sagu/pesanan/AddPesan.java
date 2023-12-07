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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sagu.List_pesanan;
import com.example.sagu.R;
import com.example.sagu.menu.Item;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddPesan extends AppCompatActivity implements PopDialog.ExampleDialogListener {
    Spinner spin_menu;
    RecyclerView pesanananBaru;
    Button btSimpan;
    ImageButton addBarang, back;
    FirebaseFirestore db, dba, dbd;
    ProgressDialog progressDialog;
    ArrayAdapter<String> adapter;
    ArrayList<String> arrayMeja;
    QuerySnapshot liMeja;
    String idMeja;
    TextView tvHarga;
    MyAdapter myAdapter;
    String reservasiId;
    List<ItemP> itemPS = new ArrayList<ItemP>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pesan);
        db = FirebaseFirestore.getInstance();
        dba = FirebaseFirestore.getInstance();
        dbd = FirebaseFirestore.getInstance();
        spin_menu = findViewById(R.id.spin_meja);
        addBarang = findViewById(R.id.bt_addBarang);
        tvHarga = findViewById(R.id.tv_harga);
        pesanananBaru = findViewById(R.id.rv_pesananbaru);
        btSimpan = findViewById(R.id.bt_simpan);
        back = findViewById(R.id.bt_backPesan);

        arrayMeja = new ArrayList<>();
        adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayMeja);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_menu.setAdapter(adapter);

        myAdapter = new MyAdapter(itemPS);
        pesanananBaru.setAdapter(myAdapter);

        getDataSpin();

        pesanananBaru.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), pesanananBaru, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                // Tambahkan logika penghapusan item di sini
                myAdapter.removeItem(position);
                calculateAndDisplayTotal();
            }

            @Override
            public void onLongClick(View view, int position) {
                // Handle long clicks if needed
            }
        }));

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
        btSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendData();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddPesan.this, List_pesanan.class);
                startActivity(intent);
            }
        });
    }
    public void getDataSpin(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        db.collection("meja").whereEqualTo("status", true).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
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
        pesanananBaru.setAdapter(myAdapter);
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
        tvHarga.setText(String.valueOf(total));
    }
    public void sendData(){
        String harga = tvHarga.getText().toString();
        String meja = spin_menu.getSelectedItem().toString();
        String date = DateFormat.getDateInstance().format(Calendar.getInstance().getTime());
        Map<String, Object> data = new HashMap<>();
        data.put("harga", harga);
        data.put("meja",meja);
        data.put("mejaid", idMeja);
        data.put("tanggal", date);
        data.put("status", true);

//            Map<String, Object> nested = new HashMap<>();
//            nested.put("barang", barang);
//            nested.put("foodid", foodid);
//            nested.put("jumlah", jumlah);
//            data.put("menulist", nested);
        CollectionReference collectionRef = db.collection("reservasi");
        collectionRef
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        reservasiId = documentReference.getId();
                        collectionRef.document(reservasiId).update("reservasiId", reservasiId);
                        sendBarang();
                        Log.d("AddPesan", "DocumentSnapshot written with ID: " + reservasiId);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("AddPesan", "Error adding document", e);
                    }
                });
    }
    public void sendBarang(){
        for (ItemP itemP : itemPS) {
            String barang = itemP.getFname();
            String foodid = itemP.getFid();
            String jumlah = itemP.getFamount();
            Map<String, Object> docData = new HashMap<>();
            docData.put("barang", barang);
            docData.put("foodid", foodid);
            docData.put("jumlah", jumlah);
            docData.put("reservasiId", reservasiId);
            db.collection("barangReservasi")
                    .add(docData)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d("AddPesan", "DocumentSnapshot written with ID: " + documentReference.getId());
                            Toast.makeText(AddPesan.this, "Pesanan berhasil dibuat", Toast.LENGTH_SHORT).show();
                            sendTableCom();
                            Intent intent = new Intent(AddPesan.this, List_pesanan.class);
                            startActivity(intent);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("AddPesan", "Error adding document", e);
                        }
                    });
        }
    }
    public void sendTableCom(){
        Map<String, Object> data = new HashMap<>();
        data.put("status", false);
        db.collection("meja")
                .document(idMeja)
                .update(data)
                .addOnSuccessListener(aVoid -> {
                    Log.d("sendTableConfirmation", "konfirm berhasil "+idMeja);
                }).addOnFailureListener(e -> {
                    Log.w("sendTableConfirmation", "konfirm gagal "+e);
                });
    }
}