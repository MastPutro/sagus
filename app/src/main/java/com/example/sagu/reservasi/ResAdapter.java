package com.example.sagu.reservasi;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.sagu.R;
import com.example.sagu.pembayaran.Pembayaran;
import com.example.sagu.pesanan.MyViewHolder;

import java.util.List;


public class ResAdapter extends RecyclerView.Adapter<ResViewHolder>{
    List<ItemRes> dataRes;
    private Context context;
    public ResAdapter (Context context, List<ItemRes> dataRes ){
        this.dataRes = dataRes;
        this.context = context;
    }
    @NonNull
    @Override
    public ResViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ResViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_res, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ResViewHolder holder, int position) {
        holder.resHarga.setText(dataRes.get(position).getHarga());
        holder.resMeja.setText(dataRes.get(position).getMeja());
        holder.resTgl.setText(dataRes.get(position).getTanggal());
        holder.resCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Pembayaran.class);
                intent.putExtra("meja", dataRes.get(holder.getAdapterPosition()).getMeja());
                intent.putExtra("harga", dataRes.get(holder.getAdapterPosition()).getHarga());
                intent.putExtra("mejaid", dataRes.get(holder.getAdapterPosition()).getMejaid());
                intent.putExtra("status", dataRes.get(holder.getAdapterPosition()).getStatus());
                intent.putExtra("tanggal", dataRes.get(holder.getAdapterPosition()).getTanggal());
                intent.putExtra("reservasiId", dataRes.get(holder.getAdapterPosition()).getReservasiId());
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return dataRes.size();
    }
}
