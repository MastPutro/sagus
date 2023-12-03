package com.example.sagu.pesanan;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sagu.R;

public class MyViewHolder extends RecyclerView.ViewHolder{
    TextView tvFname, tvFid, tvFamount;
    ImageButton btRemove;
    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        tvFname = itemView.findViewById(R.id.tv_foodName);
        tvFid = itemView.findViewById(R.id.tv_idFood);
        tvFamount = itemView.findViewById(R.id.tv_jumlah);
        btRemove = itemView.findViewById(R.id.bt_remove);


    }
}
