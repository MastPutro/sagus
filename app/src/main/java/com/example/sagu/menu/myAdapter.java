package com.example.sagu.menu;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sagu.ManagerMenu;
import com.example.sagu.ManagerTable;
import com.example.sagu.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class myAdapter extends RecyclerView.Adapter<myAdapter.ViewHolder> {
    Context context;
    public List <Item> menus;
    public myAdapter(Context context, List<Item> menus){
        this.context = context;
        this.menus = menus;
    }
    @NonNull
    @Override
    public myAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myAdapter.ViewHolder holder, int position) {

        holder.txtMenu.setText(menus.get(position).getDataMenu());
        holder.txtHarga.setText(menus.get(position).getDataHarga());
        String key = menus.get(position).getKey();
        Glide.with(context).load(menus.get(position).getDataImage()).into(holder.vImage);
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.builder.setTitle("Alert!").setMessage("Apakah anda ingin hapus")
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                holder.db.collection("menu").document(key)
                                        .delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d("remove", "DocumentSnapshot successfully deleted!");
                                                Intent intent = new Intent(context, ManagerMenu.class);
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
        return menus.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtMenu;
        public TextView txtHarga;
        public ImageView vImage;
        CardView card;
        AlertDialog.Builder builder;
        FirebaseFirestore db;

        public ViewHolder(View view) {
            super(view);

            txtMenu = view.findViewById(R.id.recNamaMakanan);
            txtHarga = view.findViewById(R.id.recHarga);
            vImage = view.findViewById(R.id.recImage);
            card = view.findViewById(R.id.recCard);
            builder = new AlertDialog.Builder(context);
            db = FirebaseFirestore.getInstance();

        }
    }
}
