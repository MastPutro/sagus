package com.example.sagu.katering;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sagu.R;

public class CatViewHolder extends RecyclerView.ViewHolder{
    TextView namacust, tanggal, jumlah;
    CardView catCard;
    public CatViewHolder(@NonNull View itemView) {
        super(itemView);
        namacust = itemView.findViewById(R.id.cat_namacust);
        tanggal = itemView.findViewById(R.id.cat_tgl);
        jumlah = itemView.findViewById(R.id.cat_jumlah);
        catCard = itemView.findViewById(R.id.catCard);
    }
}
