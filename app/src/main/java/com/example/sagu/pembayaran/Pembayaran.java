package com.example.sagu.pembayaran;

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
import android.widget.TextView;

import com.example.sagu.List_pesanan;
import com.example.sagu.R;
import com.example.sagu.metodepem.TunaiActivity;
import com.example.sagu.reservasi.ItemRes;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class Pembayaran extends AppCompatActivity {
    TextView pemMeja, pemTgl, pemHarga;
    RecyclerView pemRv;
    ImageButton pemBack;
    Button tunai, transfer;
    FirebaseFirestore db;
    String reservasiId, meja, tgl, harga, mejaid;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pembayaran);
        pemMeja = findViewById(R.id.pem_meja);
        pemTgl = findViewById(R.id.pem_tgl);
        pemHarga = findViewById(R.id.pem_harga);
        pemRv = findViewById(R.id.pem_rvlist);
        pemBack = findViewById(R.id.pem_back);
        tunai = findViewById(R.id.pem_btTunai);
        transfer = findViewById(R.id.pem_btTransfer);
        db = FirebaseFirestore.getInstance();
        setText();
        listBarang();
        Log.d("Pembayaran", "Reservasi ID: " + reservasiId);
        pemBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Pembayaran.this, List_pesanan.class);
                startActivity(intent);
            }
        });
        tunai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Pembayaran.this, TunaiActivity.class);
                intent.putExtra("tmeja", meja);
                intent.putExtra("ttanggal", tgl);
                intent.putExtra("tharga", harga);
                intent.putExtra("treservasi", reservasiId);
                intent.putExtra("tmejaid", mejaid);
                startActivity(intent);
            }
        });

    }
    public void setText(){
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            reservasiId = bundle.getString("reservasiId");
            meja = bundle.getString("meja");
            tgl = bundle.getString("tanggal");
            harga = bundle.getString("harga");
            mejaid = bundle.getString("mejaid");

            pemMeja.setText(meja);
            pemTgl.setText(tgl);
            pemHarga.setText(harga);
//            pemMeja.setText(bundle.getString("meja"));
//            pemTgl.setText(bundle.getString("tanggal"));
//            pemHarga.setText(bundle.getString("harga"));
        }
    }
    public void listBarang(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fecthing Data....");
        progressDialog.show();
        db.collection("barangReservasi")
                .whereEqualTo("reservasiId", reservasiId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<ItemPem> dataPem = new ArrayList<>();
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                        ItemPem data = documentSnapshot.toObject(ItemPem.class);
                        dataPem.add(data);
                    }
                    displayDataInRecyclerView(dataPem);
                }).addOnFailureListener(e -> {
                    Log.e("listBarang", "galat "+e);
                    progressDialog.dismiss();
                });
    }
    public void displayDataInRecyclerView(List<ItemPem> dataPem){
        pemRv.setLayoutManager(new LinearLayoutManager(this));
        PemAdapter adapter = new PemAdapter(dataPem);
        pemRv.setAdapter(adapter);
        progressDialog.dismiss();
    }
}