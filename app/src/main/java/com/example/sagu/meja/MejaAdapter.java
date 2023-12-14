package com.example.sagu.meja;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sagu.ManagerTable;
import com.example.sagu.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class MejaAdapter extends RecyclerView.Adapter<MejaAdapter.ViewHolder> {

    public List<Meja> mejas;
    Context context;

    public MejaAdapter(List<Meja> mejas, Context context) {
        this.mejas = mejas;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meja, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MejaAdapter.ViewHolder holder, int position) {
        String meja = mejas.get(position).getMejan();
        String ket = mejas.get(position).getKeterangan();
        String key = mejas.get(position).getKey();

        holder.txtnomeja.setText(meja);
        holder.txtketerangan.setText(ket);
        holder.itmeja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.builder.setTitle("Alert!").setMessage("Apakah anda ingin menghapus?")
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        holder.db.collection("meja").document(key)
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("remove", "DocumentSnapshot successfully deleted!");
                                        Intent intent = new Intent(context, ManagerTable.class);
                                        context.startActivity(intent);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("Remove", "Error deleting document", e);
                                    }
                                });

                    }
                })
                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        }).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mejas.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtnomeja;
        public TextView txtketerangan;
        CardView itmeja;
        AlertDialog.Builder builder;
        FirebaseFirestore db;

        public ViewHolder(View view) {
            super(view);

            txtnomeja = view.findViewById(R.id.txt_nomeja);
            txtketerangan = view.findViewById(R.id.txt_keterangan);
            itmeja = view.findViewById(R.id.meja_item);
            builder = new AlertDialog.Builder(context);
            db = FirebaseFirestore.getInstance();
        }
    }
}
