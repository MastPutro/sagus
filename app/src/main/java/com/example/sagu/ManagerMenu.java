package com.example.sagu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.sagu.menu.AddMenu;
import com.example.sagu.menu.Item;
import com.example.sagu.menu.myAdapter;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ManagerMenu extends AppCompatActivity {
    Button btpegawai, btmeja, btmenu;
    ImageButton btaddmenu;
    RecyclerView rvmenulist;
    myAdapter adapter;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_menu);
        btpegawai = findViewById(R.id.bt_pegawai);
        btmeja = findViewById(R.id.bt_meja);
        btmenu = findViewById(R.id.bt_menu);
        btaddmenu = findViewById(R.id.bt_addmenu);
        rvmenulist = findViewById(R.id.rv_menulist);

        adapter = new myAdapter(ManagerMenu.this, new ArrayList<>());
        rvmenulist.setAdapter(adapter);

        menuList();



//        FUNGSI BUTTON
        btaddmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManagerMenu.this, AddMenu.class);
                startActivity(intent);
            }
        });
        btmeja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManagerMenu.this, ManagerTable.class);
                startActivity(intent);
            }
        });
        btpegawai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManagerMenu.this, ManagerEmployee.class);
                startActivity(intent);
            }
        });
    }

//    FULL METHOD
    public void menuList(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Retieve data...");
        progressDialog.show();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("menu").get().addOnCompleteListener(task -> {
           if (task.isSuccessful()){
               List<Item> menus = new ArrayList<>();
               for (DocumentSnapshot document :task.getResult()) {
                   Item menu = document.toObject(Item.class);
                   menu.setKey(document.getId());
                   menus.add(menu);

               }
               adapter.menus = menus;
               adapter.notifyDataSetChanged();
               progressDialog.dismiss();

           }else {
               Log.d("ManagerMenu", "Error getting menu", task.getException());
               Toast.makeText(this, "Retrive data failed", Toast.LENGTH_SHORT).show();
           }
        });
    }
}