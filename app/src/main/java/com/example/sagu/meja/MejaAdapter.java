package com.example.sagu.meja;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sagu.R;

import java.util.List;

public class MejaAdapter extends RecyclerView.Adapter<MejaAdapter.ViewHolder> {

    public List<Meja> mejas;

    public MejaAdapter(List<Meja> mejas) {
        this.mejas = mejas;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meja, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MejaAdapter.ViewHolder holder, int position) {
        Meja meja = mejas.get(position);

        holder.txtnomeja.setText(meja.mejan);
        holder.txtketerangan.setText(meja.keterangan);

    }

    @Override
    public int getItemCount() {
        return mejas.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtnomeja;
        public TextView txtketerangan;

        public ViewHolder(View view) {
            super(view);

            txtnomeja = view.findViewById(R.id.txt_nomeja);
            txtketerangan = view.findViewById(R.id.txt_keterangan);
        }
    }
}
