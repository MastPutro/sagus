package com.example.sagu.pesanan;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sagu.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class popupAddMenu extends AppCompatActivity {
    Spinner spin_menu;
    ImageButton btPlus, btMinus;
    Button tambah, cancel;
    TextView tvCount;
    ProgressDialog progressDialog;
    FirebaseFirestore db;
    ArrayAdapter<String> menuAdapter;
    ArrayList<String> arrayMenu;
    QuerySnapshot liMenu;
    String idMenu;
    int counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_add_menu);
        spin_menu = findViewById(R.id.spin_barang);
        btPlus = findViewById(R.id.bt_plus);
        btMinus = findViewById(R.id.bt_minus);
        tvCount = findViewById(R.id.tv_count);
        tambah = findViewById(R.id.bt_tambahBarang);
        cancel = findViewById(R.id.bt_cancelBarang);


        arrayMenu = new ArrayList<>();
        menuAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayMenu);
        menuAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_menu.setAdapter(menuAdapter);
        db = FirebaseFirestore.getInstance();
        counter = 0;

//        DISPLAY POP UP
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*.7), (int)(height*.5));
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;
        getWindow().setAttributes(params);

//        DISPLAY SPINNER
        getMenuSpin();

        btPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter++;
                tvCount.setText(Integer.toString(counter));
            }
        });
        btMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter--;
                tvCount.setText(Integer.toString(counter));
            }
        });
        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePesanPopup();
            }
        });
        spin_menu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(), "" +menuAdapter.getItem(i), Toast.LENGTH_SHORT).show();
                idMenu = liMenu.getDocuments().get(i).getId();
                Log.e("ID Nama Menu", liMenu.getDocuments().get(i).getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
    public void getMenuSpin(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        db.collection("menu").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                liMenu = queryDocumentSnapshots;
                if (queryDocumentSnapshots.size()>0){
                    arrayMenu.clear();
                    for (DocumentSnapshot document : queryDocumentSnapshots){
                        arrayMenu.add(document.getString("dataMenu"));
                    }
                    menuAdapter.notifyDataSetChanged();
                    progressDialog.dismiss();
                }else {
                    Toast.makeText(getApplicationContext(), "data tidak ada", Toast.LENGTH_SHORT).show();
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
    public void savePesanPopup(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        Intent intent = new Intent(popupAddMenu.this, AddPesan.class);
        intent.putExtra("fName", spin_menu.getSelectedItem().toString());
        intent.putExtra("fId", idMenu);
        intent.putExtra("fAmount", tvCount.getText().toString());
        startActivity(intent);
        progressDialog.dismiss();

    }
}