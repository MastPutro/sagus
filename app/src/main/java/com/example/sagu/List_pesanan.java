package com.example.sagu;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.session.MediaSessionManager;
import android.nfc.Tag;
import android.os.Bundle;

import com.example.sagu.pesanan.AddPesan;
import com.example.sagu.reservasi.ItemRes;
import com.example.sagu.reservasi.ResAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class List_pesanan extends AppCompatActivity {
    TextView tvTanggal;
    RecyclerView rvPesanList;
    ImageButton bt_add, btRefresh, history;
    FirebaseFirestore db;
    Button catering;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_pesanan);
        bt_add = findViewById(R.id.bt_addpesan);
        tvTanggal = findViewById(R.id.tv_tanggal);
        rvPesanList = findViewById(R.id.rv_pesanlist);
        btRefresh = findViewById(R.id.bt_refresh);
        history = findViewById(R.id.btn_riwayatpsn);
        catering = findViewById(R.id.btn_cat);

        db = FirebaseFirestore.getInstance();
        String currentDate = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
        String date = DateFormat.getDateInstance().format(Calendar.getInstance().getTime());
        getResData();
//        progressDialog = new ProgressDialog(this);
//        progressDialog.setCancelable(false);
//        progressDialog.setMessage("Fecthing Data....");
//        progressDialog.show();

        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(List_pesanan.this, AddPesan.class);
                startActivity(intent);
            }
        });
        btRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getResData();
            }
        });
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(List_pesanan.this, HistoryActivity.class);
                startActivity(intent);
            }
        });
        catering.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(List_pesanan.this, cateringActivity.class);
                startActivity(intent);
            }
        });
    }
    public void getResData(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fecthing Data....");
        progressDialog.show();
        db.collection("reservasi").whereEqualTo("status", true)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    // Proses data yang diterima di sini
                    List<ItemRes> dataList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        ItemRes data = document.toObject(ItemRes.class);
                        dataList.add(data);
                    }
                    // Panggil method untuk menampilkan data ke RecyclerView
                    displayDataInRecyclerView(dataList);
                })
                .addOnFailureListener(e -> {
                    // Handle error jika terjadi
                    Log.e("getResdata", "error Fecthingdata "+e );
                    Toast.makeText(this, "error fecthing data", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                });
    }
    private void displayDataInRecyclerView(List<ItemRes> dataList){
        rvPesanList.setLayoutManager(new LinearLayoutManager(this));
        ResAdapter adapter = new ResAdapter(List_pesanan.this, dataList);
        rvPesanList.setAdapter(adapter);
        progressDialog.dismiss();
    }
}