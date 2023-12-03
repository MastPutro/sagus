package com.example.sagu.pesanan;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.sagu.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class PopDialog extends AppCompatDialogFragment {
    private Spinner spinBarang;
    private TextView tvCount;
    private ImageButton btMinus, btPlus;
    private ExampleDialogListener listener;
    QuerySnapshot listMenu;
    ProgressDialog progressDialog;
    FirebaseFirestore dba;
    ArrayAdapter<String> barangAdapter;
    ArrayList<String> arrayBarang;
    String idMenuN;
    int counter = 0;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog, null);

        builder.setView(view)
                .setTitle("Login")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        String username = editTextUsername.getText().toString();
//                        String password = editTextPassword.getText().toString();
//                        listener.applyTexts(username, password);
                        String barang = spinBarang.getSelectedItem().toString();
                        String foodId = idMenuN;
                        String amount = tvCount.getText().toString();
                        if (Integer.valueOf(amount)>0){
                            listener.applyTexts(barang, amount, foodId);
                        }else {
                            Toast.makeText(getContext(), "Masukkan Jumlah Dengan Benar", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        spinBarang = view.findViewById(R.id.spin_barangN);
        tvCount = view.findViewById(R.id.tv_countN);
        btMinus = view.findViewById(R.id.bt_minusN);
        btPlus = view.findViewById(R.id.bt_plusN);


        arrayBarang = new ArrayList<>();
        barangAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, arrayBarang);
        barangAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinBarang.setAdapter(barangAdapter);
        dba = FirebaseFirestore.getInstance();
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


        return  builder.create();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (ExampleDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement ExampleDialogListener");
        }
    }
    public interface ExampleDialogListener {
        void applyTexts(String barang, String amount, String foodId);
    }


//    Fungsi Laen
    public void getMenuSpin(){
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        dba.collection("menu").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                listMenu = queryDocumentSnapshots;
                if (queryDocumentSnapshots.size()>0){
                    arrayBarang.clear();
                    for (DocumentSnapshot document : queryDocumentSnapshots){
                        arrayBarang.add(document.getString("dataMenu"));
                    }
                    barangAdapter.notifyDataSetChanged();
                    progressDialog.dismiss();
                }else {
                    Toast.makeText(getContext(), "data tidak ada", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.show();
                Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        spinBarang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(getContext(), "" +barangAdapter.getItem(i), Toast.LENGTH_SHORT).show();
                idMenuN = listMenu.getDocuments().get(i).getId();
                Log.e("ID Nama Menu", listMenu.getDocuments().get(i).getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}
