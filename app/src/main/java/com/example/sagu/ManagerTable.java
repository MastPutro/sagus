package com.example.sagu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sagu.meja.Meja;
import com.example.sagu.meja.MejaAdapter;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ManagerTable extends AppCompatActivity {
    Button btpegawai, btmenu;
    MejaAdapter mejaAdapter;
    RecyclerView rvmejalist;
    ImageView btaddmeja;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_table);

//        Inisialisasi nilai
        rvmejalist = findViewById(R.id.rv_mejalist);
        btpegawai = findViewById(R.id.bt_pegawai);
        btaddmeja = findViewById(R.id.bt_addmeja);
        btmenu = findViewById(R.id.bt_menu);


//        Set adapter meja
        mejaAdapter = new MejaAdapter(new ArrayList<>());
        rvmejalist.setAdapter(mejaAdapter);


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

    }


//    Method
    public void mejalist(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("meja").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Meja> mejas = new ArrayList<>();
                for (DocumentSnapshot document : task.getResult()) {
                    Meja meja = document.toObject(Meja.class);
                    mejas.add(meja);
                }
                // Set data ke adapter
                mejaAdapter.mejas = mejas;
                mejaAdapter.notifyDataSetChanged();
            } else {
                Log.d("ManagerTable", "Error getting meja", task.getException());
                Toast.makeText(this, "Retrive data failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}