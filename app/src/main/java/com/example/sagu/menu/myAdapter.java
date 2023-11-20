package com.example.sagu.menu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sagu.R;

import java.util.List;

public class myAdapter extends RecyclerView.Adapter<myAdapter.ViewHolder> {
    Context context;
    public List <Item> menus;
    public myAdapter(Context context, List<Item> menus){
        this.context = context;
        this.menus = menus;
    }
    @NonNull
    @Override
    public myAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myAdapter.ViewHolder holder, int position) {
        Item menu = menus.get(position);

        holder.txtMenu.setText(menu.dataMenu);
        holder.txtHarga.setText(menu.dataHarga);
        Glide.with(context).load(menu.dataImage).into(holder.vImage);


    }

    @Override
    public int getItemCount() {
        return menus.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtMenu;
        public TextView txtHarga;
        public ImageView vImage;

        public ViewHolder(View view) {
            super(view);

            txtMenu = view.findViewById(R.id.recNamaMakanan);
            txtHarga = view.findViewById(R.id.recHarga);
            vImage = view.findViewById(R.id.recImage);

        }
    }
}
