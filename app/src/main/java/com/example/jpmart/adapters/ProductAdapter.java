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
import com.example.jpmart.models.Product;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private List<Product> list;
    private OnProductClickListener listener;
    private List<CartItem> cart;

    public interface OnProductClickListener {
        void onEditClick(Product product);
        void onDeleteClick(Product product);
        void onAddToCart(Product product);
    }

    public ProductAdapter(List<Product> list, OnProductClickListener listener, List<CartItem> cart) {
        this.list = list;
        this.listener = listener;
        this.cart = cart;
    }

    public void updateData(List<Product> newList) {
        this.list = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product p = list.get(position);
        NumberFormat fmt = NumberFormat.getInstance(new Locale("vi", "VN"));
        holder.tvName.setText(p.getName());
        holder.tvPrice.setText(fmt.format(p.getPrice()) + " đ");
        holder.tvStock.setText("Tồn kho: " + p.getQuantity());
        holder.btnEdit.setOnClickListener(v -> listener.onEditClick(p));
        holder.btnDelete.setOnClickListener(v -> listener.onDeleteClick(p));
        holder.btnCart.setOnClickListener(v -> listener.onAddToCart(p));
    }

    @Override
    public int getItemCount() { return list.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice, tvStock;
        ImageButton btnEdit, btnDelete, btnCart;

        ViewHolder(View v) {
            super(v);
            tvName = v.findViewById(R.id.tv_name);
            tvPrice = v.findViewById(R.id.tv_price);
            tvStock = v.findViewById(R.id.tv_stock);
            btnEdit = v.findViewById(R.id.btn_edit);
            btnDelete = v.findViewById(R.id.btn_delete);
            btnCart = v.findViewById(R.id.btn_cart);
        }
    }
}
