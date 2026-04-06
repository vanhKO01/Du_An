package com.example.jpmart.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jpmart.R;
import com.example.jpmart.models.Invoice;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class InvoiceAdapter extends RecyclerView.Adapter<InvoiceAdapter.ViewHolder> {

    private List<Invoice> list;
    private OnInvoiceClickListener listener;

    public interface OnInvoiceClickListener {
        void onInvoiceClick(Invoice invoice);
        void onDeleteClick(Invoice invoice);
    }

    public InvoiceAdapter(List<Invoice> list, OnInvoiceClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_invoice, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Invoice inv = list.get(position);
        NumberFormat fmt = NumberFormat.getInstance(new Locale("vi", "VN"));
        holder.tvCode.setText("Mã hóa đơn: " + inv.getCode());
        holder.tvStaff.setText("Tên nhân viên: " + inv.getStaffName());
        holder.tvCustomer.setText("Tên khách hàng: " + inv.getCustomerName());
        holder.tvDate.setText("Ngày lập: " + inv.getDate());
        holder.tvTotal.setText("Tổng tiền: " + fmt.format(inv.getTotal()));
        holder.itemView.setOnClickListener(v -> listener.onInvoiceClick(inv));
        holder.btnDelete.setOnClickListener(v -> listener.onDeleteClick(inv));
    }

    @Override
    public int getItemCount() { return list.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCode, tvStaff, tvCustomer, tvDate, tvTotal;
        ImageButton btnDelete;

        ViewHolder(View v) {
            super(v);
            tvCode = v.findViewById(R.id.tv_code);
            tvStaff = v.findViewById(R.id.tv_staff);
            tvCustomer = v.findViewById(R.id.tv_customer);
            tvDate = v.findViewById(R.id.tv_date);
            tvTotal = v.findViewById(R.id.tv_total);
            btnDelete = v.findViewById(R.id.btn_delete);
        }
    }
}
