package com.example.sagu.pembayaran;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sagu.R;

public class PemViewHolder extends RecyclerView.ViewHolder{
    TextView barang, jumlah;
    public PemViewHolder(@NonNull View itemView) {
        super(itemView);
        barang = itemView.findViewById(R.id.barang);
        jumlah = itemView.findViewById(R.id.jumlah);
    }
}
