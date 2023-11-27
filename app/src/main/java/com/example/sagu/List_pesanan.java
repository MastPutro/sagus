package com.example.sagu;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.session.MediaSessionManager;
import android.os.Bundle;

import com.example.sagu.pesanan.AddPesan;
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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class List_pesanan extends AppCompatActivity {
    TextView tvTanggal;
    RecyclerView recyclerView;
    ImageButton bt_add;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference itemsCollection = db.collection("items");

    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_pesanan);

        bt_add = findViewById(R.id.bt_addpesan);

        tvTanggal = findViewById(R.id.tv_tanggal);
        String currentDate = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
        String date = DateFormat.getDateInstance().format(Calendar.getInstance().getTime());
        tvTanggal.setText(date);
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
    }
}