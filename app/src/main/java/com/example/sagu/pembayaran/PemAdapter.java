package com.example.sagu.pembayaran;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sagu.R;

import java.util.List;


public class PemAdapter extends RecyclerView.Adapter<PemViewHolder>{
    List<ItemPem> dataPem;
    public PemAdapter (List<ItemPem> dataPem){
        this.dataPem = dataPem;
    }

    @NonNull
    @Override
    public PemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pem, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PemViewHolder holder, int position) {
        holder.barang.setText(dataPem.get(position).getBarang());
        holder.jumlah.setText(dataPem.get(position).getJumlah());
    }

    @Override
    public int getItemCount() {
        return dataPem.size();
    }
}
