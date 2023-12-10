package com.example.sagu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.print.PrintHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.PrintManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.sagu.history.DatePickerFragment;
import com.example.sagu.history.HisAdapter;
import com.example.sagu.history.itemHistory;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class HistoryActivity extends AppCompatActivity {
    ImageButton listmenu, refresh;
    ProgressDialog progressDialog;
    FirebaseFirestore db;
    RecyclerView rvhistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        listmenu = findViewById(R.id.his_listmeja);
        refresh = findViewById(R.id.his_refresh);
        rvhistory = findViewById(R.id.his_rv);
        db = FirebaseFirestore.getInstance();
        getDataHistory();
        listmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent  intent = new Intent(HistoryActivity.this, List_pesanan.class);
                startActivity(intent);
            }
        });
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDataHistory();
            }
        });
    }
    public void getDataHistory() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Sabar Bolo....");

        progressDialog.show();
        db.collection("reservasi")
                .whereEqualTo("status", false)
//                .whereEqualTo("tanggal", tanggal.getText().toString())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<itemHistory> dataHis = new ArrayList<>();
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        itemHistory data = documentSnapshot.toObject(itemHistory.class);
                        dataHis.add(data);
                    }
                    displayDataInRecycleView(dataHis);
                })
                .addOnFailureListener(e -> {
                    Log.e("getHisdata", "error Fecthingdata " + e);
                    Toast.makeText(this, "error fetching data", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                });

//        if (tgl != null) {
//
//        } else {
//            db.collection("reservasi")
//                    .whereEqualTo("status", false)
//                    .get()
//                    .addOnSuccessListener(queryDocumentSnapshots -> {
//                        List<itemHistory> dataHis = new ArrayList<>();
//                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
//                            itemHistory data = documentSnapshot.toObject(itemHistory.class);
//                            dataHis.add(data);
//                        }
//                        displayDataInRecycleView(dataHis);
//                    })
//                    .addOnFailureListener(e -> {
//                        Log.e("getHisdata", "error Fecthingdata " + e);
//                        Toast.makeText(this, "error fetching data", Toast.LENGTH_SHORT).show();
//                        progressDialog.dismiss();
//                    });
//        }
    }
    private void displayDataInRecycleView (List<itemHistory> dataHis){
        Log.d("HistoryActivity", "Displaying data in RecyclerView");

        if (dataHis == null) {
            Log.e("HistoryActivity", "Data is null");
            return;
        }

        Log.d("HistoryActivity", "Data size: " + dataHis.size());

        rvhistory.setLayoutManager(new LinearLayoutManager(this));
        HisAdapter adapter = new HisAdapter(dataHis, HistoryActivity.this);
        rvhistory.setAdapter(adapter);
        progressDialog.dismiss();
    }
}