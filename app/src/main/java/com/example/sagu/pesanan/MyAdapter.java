package com.example.sagu.pesanan;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sagu.R;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder>{
    List<ItemP> itemPS;
    public MyAdapter(List<ItemP> itemPS){
        this.itemPS = itemPS;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pesanbaru, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvFname.setText(itemPS.get(position).getFname());
        holder.tvFid.setText(itemPS.get(position).getFid());
        holder.tvFamount.setText(itemPS.get(position).getFamount());
    }

    @Override
    public int getItemCount() {
        return itemPS.size();
    }

}
