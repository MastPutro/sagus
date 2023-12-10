package com.example.sagu.history;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sagu.R;
import com.example.sagu.reservasi.ResViewHolder;

import java.util.List;

public class HisAdapter extends RecyclerView.Adapter<HisViewHolder>{
    List<itemHistory> dataHis;
    private Context context;

    public HisAdapter(List<itemHistory> dataHis, Context context) {
        this.dataHis = dataHis;
        this.context = context;
    }

    @NonNull
    @Override
    public HisViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HisViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HisViewHolder holder, int position) {
        holder.meja.setText(dataHis.get(position).getMeja());
        holder.harga.setText(dataHis.get(position).getHarga());
        holder.tanggal.setText(dataHis.get(position).getTanggal());

    }

    @Override
    public int getItemCount() {
        return dataHis.size();
    }
}
