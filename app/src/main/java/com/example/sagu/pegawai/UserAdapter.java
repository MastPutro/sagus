package com.example.sagu.pegawai;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.sagu.R;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    public List<User> users;

    public UserAdapter(List<User> users) {
        this.users = users;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User user = users.get(position);

        holder.tvName.setText(user.name);
        holder.tvLevel.setText(user.level);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvName;
        public TextView tvLevel;

        public ViewHolder(View view) {
            super(view);

            tvName = view.findViewById(R.id.tv_name);
            tvLevel = view.findViewById(R.id.tv_level);
        }
    }
}
