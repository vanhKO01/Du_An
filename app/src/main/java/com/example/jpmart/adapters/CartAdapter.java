package com.example.jpmart.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jpmart.R;
import com.example.jpmart.models.CartItem;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private List<CartItem> list;
    private OnCartItemListener listener;

    public interface OnCartItemListener {
        void onIncrease(int position);
        void onDecrease(int position);
        void onRemove(int position);
    }

    public CartAdapter(List<CartItem> list, OnCartItemListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartItem item = list.get(position);
        NumberFormat fmt = NumberFormat.getInstance(new Locale("vi", "VN"));
        holder.tvName.setText(item.getProduct().getName());
        holder.tvPrice.setText(fmt.format(item.getProduct().getPrice()) + " đ");
        holder.tvQty.setText(String.valueOf(item.getQuantity()));

        holder.btnIncrease.setOnClickListener(v -> {
            int pos = holder.getBindingAdapterPosition();
            if (pos != RecyclerView.NO_ID) listener.onIncrease(pos);
        });
        holder.btnDecrease.setOnClickListener(v -> {
            int pos = holder.getBindingAdapterPosition();
            if (pos != RecyclerView.NO_ID) listener.onDecrease(pos);
        });
        holder.btnRemove.setOnClickListener(v -> {
            int pos = holder.getBindingAdapterPosition();
            if (pos != RecyclerView.NO_ID) listener.onRemove(pos);
        });
    }

    @Override
    public int getItemCount() { return list.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice, tvQty;
        ImageButton btnIncrease, btnDecrease, btnRemove;

        ViewHolder(View v) {
            super(v);
            tvName = v.findViewById(R.id.tv_name);
            tvPrice = v.findViewById(R.id.tv_price);
            tvQty = v.findViewById(R.id.tv_qty);
            btnIncrease = v.findViewById(R.id.btn_increase);
            btnDecrease = v.findViewById(R.id.btn_decrease);
            btnRemove = v.findViewById(R.id.btn_remove);
        }
    }
}
