package com.example.sagu.history;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sagu.R;

public class HisViewHolder extends RecyclerView.ViewHolder{
    TextView meja, harga, tanggal;
    public HisViewHolder(@NonNull View itemView) {
        super(itemView);
        meja = itemView.findViewById(R.id.item_hismeja);
        harga = itemView.findViewById(R.id.item_hisharga);
        tanggal = itemView.findViewById(R.id.item_histanggal);
    }
}
