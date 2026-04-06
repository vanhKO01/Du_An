package com.example.jpmart.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jpmart.R;
import com.example.jpmart.models.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private List<Category> list;
    private OnCategoryClickListener listener;

    public interface OnCategoryClickListener {
        void onEditClick(Category category);
        void onDeleteClick(Category category);
    }

    public CategoryAdapter(List<Category> list, OnCategoryClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category cat = list.get(position);
        holder.tvCode.setText(cat.getCode());
        holder.tvName.setText(cat.getName());
        holder.btnEdit.setOnClickListener(v -> listener.onEditClick(cat));
        holder.btnDelete.setOnClickListener(v -> listener.onDeleteClick(cat));
    }

    @Override
    public int getItemCount() { return list.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCode, tvName;
        ImageButton btnEdit, btnDelete;

        ViewHolder(View v) {
            super(v);
            tvCode = v.findViewById(R.id.tv_code);
            tvName = v.findViewById(R.id.tv_name);
            btnEdit = v.findViewById(R.id.btn_edit);
            btnDelete = v.findViewById(R.id.btn_delete);
        }
    }
}
