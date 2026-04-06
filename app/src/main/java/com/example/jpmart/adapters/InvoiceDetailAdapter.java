package com.example.jpmart.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jpmart.R;
import com.example.jpmart.models.InvoiceDetail;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class InvoiceDetailAdapter extends RecyclerView.Adapter<InvoiceDetailAdapter.ViewHolder> {

    private List<InvoiceDetail> list;

    public InvoiceDetailAdapter(List<InvoiceDetail> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_invoice_detail, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        InvoiceDetail d = list.get(position);
        NumberFormat fmt = NumberFormat.getInstance(new Locale("vi", "VN"));
        holder.tvName.setText(d.getProductName());
        holder.tvPrice.setText(fmt.format(d.getPrice()) + " đ");
        holder.tvQty.setText("Số lượng: " + d.getQuantity());
    }

    @Override
    public int getItemCount() { return list.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice, tvQty;

        ViewHolder(View v) {
            super(v);
            tvName = v.findViewById(R.id.tv_name);
            tvPrice = v.findViewById(R.id.tv_price);
            tvQty = v.findViewById(R.id.tv_qty);
        }
    }
}
