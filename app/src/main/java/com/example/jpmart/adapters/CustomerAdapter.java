package com.example.jpmart.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jpmart.R;
import com.example.jpmart.models.Customer;

import java.util.List;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.ViewHolder> {

    private List<Customer> list;
    private OnCustomerClickListener listener;

    public interface OnCustomerClickListener {
        void onEditClick(Customer customer);
        void onDeleteClick(Customer customer);
    }

    public CustomerAdapter(List<Customer> list, OnCustomerClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_customer, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Customer c = list.get(position);
        holder.tvCode.setText(c.getCode());
        holder.tvName.setText(c.getName());
        holder.tvPhone.setText(c.getPhone());
        holder.btnEdit.setOnClickListener(v -> listener.onEditClick(c));
        holder.btnDelete.setOnClickListener(v -> listener.onDeleteClick(c));
    }

    @Override
    public int getItemCount() { return list.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCode, tvName, tvPhone;
        ImageButton btnEdit, btnDelete;

        ViewHolder(View v) {
            super(v);
            tvCode = v.findViewById(R.id.tv_code);
            tvName = v.findViewById(R.id.tv_name);
            tvPhone = v.findViewById(R.id.tv_phone);
            btnEdit = v.findViewById(R.id.btn_edit);
            btnDelete = v.findViewById(R.id.btn_delete);
        }
    }
}
