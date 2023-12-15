package com.example.sagu.metodepem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sagu.List_pesanan;
import com.example.sagu.R;
import com.example.sagu.printerAdapter.PrintBluetooth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TunaiActivity extends AppCompatActivity {
    TextView tunaiMeja, tunaiTgl, tunaiHarga, kembalian;
    EditText dibayar, device;
    RadioButton struk;
    Button konfirm;
    ImageButton back;
    String meja, tgl, harga, reservasi, mejaid, barang, jumlah, printer;
    PrintBluetooth printBT;
    int kembali;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tunai);
        tunaiMeja = findViewById(R.id.tunai_meja);
        tunaiHarga = findViewById(R.id.tunai_harga);
        tunaiTgl = findViewById(R.id.tunai_tgl);
        kembalian = findViewById(R.id.tunai_kembalian);
        dibayar = findViewById(R.id.tunai_dibayar);
        konfirm = findViewById(R.id.tunai_konfirm);
        back = findViewById(R.id.tunai_back);
        db = FirebaseFirestore.getInstance();
        getPrinter();
        printBT = new PrintBluetooth();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            meja = bundle.getString("tmeja");
            tgl = bundle.getString("ttanggal");
            harga = bundle.getString("tharga");
            reservasi = bundle.getString("treservasi");
            mejaid = bundle.getString("tmejaid");
            tunaiMeja.setText(meja);
            tunaiTgl.setText(tgl);
            tunaiHarga.setText(harga);
            dibayar.setText(harga);
//            tunaiMeja.setText(bundle.getString("tmeja"));
//            tunaiTgl.setText(bundle.getString("ttanggal"));
//            tunaiHarga.setText(bundle.getString("tharga"));
        }


        dibayar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    // Convert Editable to String and then parse it to int
                    int dibayarValue = Integer.parseInt(editable.toString());

                    // Assuming harga is a String, convert it to int
                    int hargaValue = Integer.parseInt(harga);

                    // Perform subtraction
//                        count = hargaValue - dibayarValue;
                    kembali = dibayarValue - hargaValue;

                    // Set the result in your TextView
                    kembalian.setText(String.valueOf(kembali));
                } catch (NumberFormatException e) {
                    // Handle the case where parsing fails (e.g., non-numeric input)
                    kembalian.setText("Invalid input");
                }
            }
        });
        konfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int bayar = Integer.parseInt(dibayar.getText().toString());
                int price = Integer.parseInt(harga);
                if (bayar >= price){
                    sendResConfirmation();
                }else {
                    Toast.makeText(getApplicationContext(), "Pembayaran Kurang", Toast.LENGTH_SHORT).show();
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TunaiActivity.this, List_pesanan.class);
                startActivity(intent);
            }
        });
    }
    public void sendResConfirmation(){
        Map<String, Object> data = new HashMap<>();
        data.put("status", false);
        db.collection("reservasi")
                .document(reservasi)
                .update(data)
                .addOnSuccessListener(aVoid -> {
            sendReciptOnDatabase();
            sendTableConfirmation();
            printRecipt();
            Log.d("sendResConfirmation", "konfirm berhasil "+reservasi);
        }).addOnFailureListener(e -> {
            Log.w("sendResConfirmation", "konfirm gagal "+e);
        });
    }
    public void sendTableConfirmation(){
        Map<String, Object> data = new HashMap<>();
        data.put("status", true);
        db.collection("meja")
                .document(mejaid)
                .update(data)
                .addOnSuccessListener(aVoid -> {
                    sendReciptOnDatabase();
                    Log.d("sendTableConfirmation", "konfirm berhasil "+mejaid);
                }).addOnFailureListener(e -> {
                    Log.w("sendTableConfirmation", "konfirm gagal "+e);
                });
    }
    public void sendReciptOnDatabase(){
        Map<String, Object> dataDoc = new HashMap<>();
        dataDoc.put("dibayar", dibayar.getText().toString());
        dataDoc.put("kembalian", kembali);
        dataDoc.put("reservasi", reservasi);
        db.collection("struk")
                .add(dataDoc)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d("TunaiActivity", "send database berhasil "+ documentReference.getId());
                Toast.makeText(getApplicationContext(), "data terkirim", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(TunaiActivity.this, List_pesanan.class);
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
        PrintBluetooth.printer_id = printer;
        try {
            printBT.findBT();
            printBT.openBT();
            printBT.printStruk(meja, harga, tgl);
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