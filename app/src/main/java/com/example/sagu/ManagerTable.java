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
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sagu.meja.Meja;
import com.example.sagu.meja.MejaAdapter;
import com.example.sagu.reservasi.ItemRes;
import com.example.sagu.reservasi.ResAdapter;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ManagerTable extends AppCompatActivity {
    Button btpegawai, btmenu;
    MejaAdapter mejaAdapter;
    RecyclerView rvmejalist;
    ImageView btaddmeja;
    ProgressDialog progressDialog;
    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_table);

//        Inisialisasi nilai
        rvmejalist = findViewById(R.id.rv_mejalist);
        btpegawai = findViewById(R.id.bt_pegawai);
        btaddmeja = findViewById(R.id.bt_addmeja);
        btmenu = findViewById(R.id.bt_menu);
        db = FirebaseFirestore.getInstance();



//        Load Method
        mejalist();


//        Fungsi Button

        btaddmeja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManagerTable.this, AddMeja.class);
                startActivity(intent);
            }
        });
        btpegawai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManagerTable.this, ManagerEmployee.class);
                startActivity(intent);
            }
        });
        btmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManagerTable.this, ManagerMenu.class);
                startActivity(intent);
            }
        });

    }


//    Method
    public void mejalist(){
        progressDialog = new ProgressDialog(ManagerTable.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fecthing Data....");
        progressDialog.show();
        db.collection("meja")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    // Proses data yang diterima di sini
                    List<Meja> dataList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Meja data = document.toObject(Meja.class);
                        data.setKey(document.getId());
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
    private void displayDataInRecyclerView(List<Meja> dataList){
        rvmejalist.setLayoutManager(new LinearLayoutManager(this));
        MejaAdapter adapter = new MejaAdapter(dataList, ManagerTable.this);
        rvmejalist.setAdapter(adapter);
        progressDialog.dismiss();
    }
}