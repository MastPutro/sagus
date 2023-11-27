package com.example.sagu.pesanan;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sagu.R;
import com.example.sagu.menu.Item;
import com.example.sagu.menu.myAdapter;

import java.util.List;

public class pesanAdapter extends RecyclerView.Adapter<pesanAdapter.ViewHolder> {

    public List<pesanItem> pesanL;

    public pesanAdapter(List<pesanItem> pesanL){
        this.pesanL = pesanL;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pesanbaru, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        pesanItem pItem = pesanL.get(position);
        holder.foodname.setText(pItem.dataBarang);
        holder.jumlah.setText(pItem.dataJumlah);
        holder.idfood.setText(pItem.foodId);

    }

    @Override
    public int getItemCount() {
        return pesanL.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView foodname, idfood, jumlah;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            foodname = itemView.findViewById(R.id.tv_foodName);
            idfood = itemView.findViewById(R.id.tv_idFood);
            jumlah = itemView.findViewById(R.id.tv_jumlah);
        }
    }
}
