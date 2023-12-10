package com.example.sagu;

import static android.content.ContentValues.TAG;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.sagu.pegawai.User;
import com.example.sagu.pegawai.UserAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManagerEmployee extends AppCompatActivity {
    Button btEmployee, btMenuList, btTable, btRegister;

    EditText etEmail, etPassword, etName;
    FirebaseAuth auth;
    FirebaseFirestore fstore;
    Spinner etLevel;
    String userID;
    static UserAdapter userAdapter;
    RecyclerView rvUsers;
    ArrayList<String> arrspin;
    ArrayAdapter<String> user;
    static ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manageremployee);
        progressDialog = new ProgressDialog(ManagerEmployee.this);

        btEmployee = findViewById(R.id.bt_pegawai);
        btMenuList = findViewById(R.id.bt_menu);
        btTable = findViewById(R.id.bt_meja);
        etEmail = findViewById(R.id.email_et);
        etPassword = findViewById(R.id.password_et);
        etName = findViewById(R.id.name_et);
        etLevel = findViewById(R.id.level_et);
        btRegister = findViewById(R.id.register_bt);
        rvUsers = findViewById(R.id.rv_users);
        userAdapter = new UserAdapter(new ArrayList<>(), ManagerEmployee.this);
        rvUsers.setAdapter(userAdapter);

        arrspin = new ArrayList<>();
        user = new ArrayAdapter<>(ManagerEmployee.this, android.R.layout.simple_spinner_dropdown_item, arrspin);
        user.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etLevel.setAdapter(user);
        arrspin.clear();
        arrspin.add("admin");
        arrspin.add("user");
        user.notifyDataSetChanged();


        getUsers();

        auth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertReg();

            }
        });

        btEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        btMenuList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManagerEmployee.this, ManagerMenu.class);
                startActivity(intent);
            }
        });

        btTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManagerEmployee.this, ManagerTable.class);
                startActivity(intent);
            }
        });
        etLevel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(getContext(), "" +barangAdapter.getItem(i), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
    private void insertReg(){
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String name = etName.getText().toString();
        String level = etLevel.getSelectedItem().toString();

        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener((task -> {
            if(task.isSuccessful()){
                userID = auth.getCurrentUser().getUid();
                DocumentReference documentReference = fstore.collection("user").document(userID);
                Map<String, Object> user = new HashMap<>();
                user.put("name",name);
                user.put("level",level);
                user.put("userid", userID);
                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "onSuccess: user is create" + userID);
                        getUsers();
                        Toast.makeText(getApplicationContext(), "Registration Success!", Toast.LENGTH_SHORT);
                    }
                });
                Toast.makeText(getApplicationContext(), "Registration Success!", Toast.LENGTH_SHORT);

            }
        }));
    }
    public static void getUsers() {
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("user").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<User> users = new ArrayList<>();
                for (DocumentSnapshot document : task.getResult()) {
                    User user = document.toObject(User.class);
                    users.add(user);
                }
                // Set data ke adapter
                userAdapter.users = users;
                userAdapter.notifyDataSetChanged();
                progressDialog.dismiss();
            } else {
                Log.d("MainActivity", "Error getting users", task.getException());
            }
        });
    }
}