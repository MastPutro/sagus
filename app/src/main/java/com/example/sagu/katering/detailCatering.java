package com.example.sagu.katering;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sagu.List_pesanan;
import com.example.sagu.R;
import com.example.sagu.cateringActivity;
import com.example.sagu.metodepem.TunaiActivity;
import com.example.sagu.printerAdapter.PrintBluetooth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class detailCatering extends AppCompatActivity {
    String namacus, phone, foods, drinks, rice, alamat, tanggal, jumlah, idcat, printer;
    Boolean status;
    TextView tvnama, tvphone, tvfood, tvdrink, tvrice, tvjumlah, tvalamat;
    EditText rego;
    ImageButton back, remove;
    Button konfirm;
    FirebaseFirestore db;
    AlertDialog.Builder builder;
    PrintBluetooth printBT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_catering);
        tvnama = findViewById(R.id.cat_namac);
        tvphone = findViewById(R.id.cat_phone);
        tvfood = findViewById(R.id.cat_foods);
        tvdrink = findViewById(R.id.cat_drinks);
        tvrice = findViewById(R.id.cat_rice);
        tvjumlah = findViewById(R.id.cat_jumlah);
        tvalamat = findViewById(R.id.cat_alamat);
        back = findViewById(R.id.cat_back);
        remove = findViewById(R.id.cat_remove);
        konfirm = findViewById(R.id.cat_konfirm);
        rego = findViewById(R.id.cat_harga);
        db = FirebaseFirestore.getInstance();
        getPrinter();
        builder = new AlertDialog.Builder(this);
        printBT = new PrintBluetooth();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            namacus = bundle.getString("namacust");
            jumlah = bundle.getString("jumlah");
            tanggal = bundle.getString("tanggal");
            phone = bundle.getString("phone");
            foods = bundle.getString("foods");
            drinks = bundle.getString("drinks");
            rice = bundle.getString("rice");
            alamat = bundle.getString("alamat");
            status = bundle.getBoolean("status");
            idcat = bundle.getString("idcat");
        }
        setText();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(detailCatering.this, cateringActivity.class);
                startActivity(intent);
            }
        });
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.setTitle("Alert!!").setMessage("Apakah anda ingin menghapus data?").setCancelable(true)
                        .setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                db.collection("katering").document(idcat)
                                        .delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d("detailCat", "DocumentSnapshot successfully deleted!");
                                            }
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.e("detailCatering", "gagal delete" + e);
                                        });
                                finish();
                            }
                        }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        }).show();

            }
        });
        konfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.setTitle("Katering").setMessage("Terima Transaksi?").setCancelable(true)
                        .setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                sendCaterinConfirmation();
                                finish();
                            }
                        }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        }).show();
            }
        });
    }
    private void setText(){
        tvnama.setText(namacus);
        tvphone.setText(phone);
        tvfood.setText(foods);
        tvdrink.setText(drinks);
        tvrice.setText(rice);
        tvjumlah.setText(jumlah);
        tvalamat.setText(alamat);
    }
    public void sendCaterinConfirmation(){
        Map<String, Object> data = new HashMap<>();
        data.put("status", false);
        db.collection("katering")
                .document(idcat)
                .update(data)
                .addOnSuccessListener(aVoid -> {
                    sendReciptOnDatabase();
                    Log.d("sendTableConfirmation", "konfirm berhasil "+idcat);
                }).addOnFailureListener(e -> {
                    Log.w("sendTableConfirmation", "konfirm gagal "+e);
                });
    }
    public void sendReciptOnDatabase(){
        Map<String, Object> dataDoc = new HashMap<>();
        dataDoc.put("nama", namacus);
        dataDoc.put("food", foods);
        dataDoc.put("drinks", drinks);
        dataDoc.put("nasi", rice);
        dataDoc.put("jumlah", jumlah);
        dataDoc.put("harga", rego.getText().toString());

        db.collection("strukKatering")
                .add(dataDoc)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("detailActivity", "send database berhasil "+ documentReference.getId());
                        Toast.makeText(getApplicationContext(), "data terkirim", Toast.LENGTH_SHORT).show();
                        printRecipt();
                        Intent intent = new Intent(detailCatering.this, cateringActivity.class);
                        startActivity(intent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TunaiActivity", "send database gagal "+ e);
                    }
                });
    }
    public void printRecipt(){
        PrintBluetooth.printer_id = "MTP-2";
        try {
            printBT.findBT();
            printBT.openBT();
            printBT.printCatering(namacus, foods, drinks, rice, jumlah, rego.getText().toString());
            printBT.closeBT();
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }
    public void getPrinter(){
        DocumentReference docRef = db.collection("printer").document("1");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("getPrinter", "DocumentSnapshot data: " + document.getData());
                        printer = document.getString("nama");
                    } else {
                        Log.d("getPrinter", "No such document");
                    }
                } else {
                    Log.d("getPrinter", "get failed with ", task.getException());
                }
            }
        });
    }
}