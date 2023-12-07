package com.example.sagu.reservasi;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sagu.R;

public class ResViewHolder extends RecyclerView.ViewHolder{
    TextView resMeja, resHarga, resTgl;
    CardView resCard;
    public ResViewHolder(@NonNull View itemView) {
        super(itemView);
        resHarga = itemView.findViewById(R.id.res_harga);
        resMeja = itemView.findViewById(R.id.res_nomeja);
        resTgl = itemView.findViewById(R.id.res_tgl);
        resCard = itemView.findViewById(R.id.res_Card);
    }
}
