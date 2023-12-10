package com.example.sagu.katering;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sagu.R;
import com.example.sagu.reservasi.ItemRes;
import com.example.sagu.reservasi.ResViewHolder;

import java.util.List;

public class CatAdapter extends RecyclerView.Adapter<CatViewHolder>{
    List<itemCat> dataCat;
    private Context context;

    public CatAdapter(Context context ,List<itemCat> dataCat) {
        this.dataCat = dataCat;
        this.context = context;
    }

    @NonNull
    @Override
    public CatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CatViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cat, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CatViewHolder holder, int position) {
        holder.namacust.setText(dataCat.get(position).getNamacust());
        holder.tanggal.setText(dataCat.get(position).getTanggalcat());
        holder.jumlah.setText(dataCat.get(position).getAmount());
        holder.catCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, detailCatering.class);
                intent.putExtra("namacust", dataCat.get(holder.getAdapterPosition()).getNamacust());
                intent.putExtra("jumlah", dataCat.get(holder.getAdapterPosition()).getAmount());
                intent.putExtra("tanggal", dataCat.get(holder.getAdapterPosition()).getTanggalcat());
                intent.putExtra("phone", dataCat.get(holder.getAdapterPosition()).getPhone());
                intent.putExtra("foods", dataCat.get(holder.getAdapterPosition()).getFoods());
                intent.putExtra("drinks", dataCat.get(holder.getAdapterPosition()).getDrinks());
                intent.putExtra("rice", dataCat.get(holder.getAdapterPosition()).getRice());
                intent.putExtra("alamat", dataCat.get(holder.getAdapterPosition()).getAddress());
                intent.putExtra("status", dataCat.get(holder.getAdapterPosition()).getStatus());
                intent.putExtra("idcat", dataCat.get(holder.getAdapterPosition()).getIdcat());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataCat.size();
    }
}
