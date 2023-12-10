package com.example.sagu.pembayaran;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sagu.List_pesanan;
import com.example.sagu.R;
import com.example.sagu.metodepem.TransferActivity;
import com.example.sagu.metodepem.TunaiActivity;
import com.example.sagu.reservasi.ItemRes;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Pembayaran extends AppCompatActivity {
    TextView pemMeja, pemTgl, pemHarga;
    RecyclerView pemRv;
    ImageButton pemBack, remove;
    Button tunai, transfer;
    FirebaseFirestore db;
    String reservasiId, meja, tgl, harga, mejaid;
    ProgressDialog progressDialog;
    AlertDialog.Builder builder;

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
        remove = findViewById(R.id.pem_remove);
        builder = new AlertDialog.Builder(this);
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
        transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Pembayaran.this, TransferActivity.class);
                intent.putExtra("tmeja", meja);
                intent.putExtra("ttanggal", tgl);
                intent.putExtra("tharga", harga);
                intent.putExtra("treservasi", reservasiId);
                intent.putExtra("tmejaid", mejaid);
                startActivity(intent);
            }
        });
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                remove();
            }
        });

    }

    public void setText() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
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

    public void listBarang() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fecthing Data....");
        progressDialog.show();
        db.collection("barangReservasi")
                .whereEqualTo("reservasiId", reservasiId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<ItemPem> dataPem = new ArrayList<>();
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        ItemPem data = documentSnapshot.toObject(ItemPem.class);
                        dataPem.add(data);
                    }
                    displayDataInRecyclerView(dataPem);
                }).addOnFailureListener(e -> {
                    Log.e("listBarang", "galat " + e);
                    progressDialog.dismiss();
                });
    }

    public void displayDataInRecyclerView(List<ItemPem> dataPem) {
        pemRv.setLayoutManager(new LinearLayoutManager(this));
        PemAdapter adapter = new PemAdapter(dataPem);
        pemRv.setAdapter(adapter);
        progressDialog.dismiss();
    }

    public void remove() {
        builder.setTitle("Alert!").setMessage("Anda Ingin Meghapus data?").setCancelable(true).setPositiveButton("Iya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                db.collection("reservasi").document(reservasiId)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                deleteDocumentsByField("ReservasiId", reservasiId);
                                sendTableConfirmation();
                                Log.d("remove", "DocumentSnapshot successfully deleted!");
                                Intent intent = new Intent(Pembayaran.this, List_pesanan.class);
                                startActivity(intent);
                                Toast.makeText(getApplicationContext(), "data berhasil dihapus", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("remove", "Error deleting document", e);
                            }
                        });
            }
        }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        }).show();

    }

    public void deleteDocumentsByField(String fieldName, String fieldValue) {
        CollectionReference usersCollection = db.collection("barangReservasi");
        // Membuat query untuk menemukan dokumen dengan field tertentu
        Query query = usersCollection.whereEqualTo(fieldName, fieldValue);

        // Menjalankan query untuk mendapatkan hasil
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Iterasi melalui dokumen yang sesuai dengan kriteria
                for (QueryDocumentSnapshot document : task.getResult()) {
                    // Mendapatkan ID dokumen
                    String documentId = document.getId();

                    // Menghapus dokumen berdasarkan ID
                    usersCollection.document(documentId).delete()
                            .addOnSuccessListener(aVoid -> {
                                // Dokumen berhasil dihapus
                                System.out.println("Dokumen berhasil dihapus: " + documentId);
                            })
                            .addOnFailureListener(e -> {
                                // Gagal menghapus dokumen
                                System.err.println("Gagal menghapus dokumen: " + documentId);
                            });
                }
            } else {
                // Gagal mengeksekusi query
                System.err.println("Gagal mengeksekusi query: " + task.getException());
            }
        });
    }
    public void sendTableConfirmation(){
        Map<String, Object> data = new HashMap<>();
        data.put("status", true);
        db.collection("meja")
                .document(mejaid)
                .update(data)
                .addOnSuccessListener(aVoid -> {
                    Log.d("sendTableConfirmation", "konfirm berhasil "+mejaid);
                }).addOnFailureListener(e -> {
                    Log.w("sendTableConfirmation", "konfirm gagal "+e);
                });
    }
}