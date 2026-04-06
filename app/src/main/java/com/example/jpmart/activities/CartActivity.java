package com.example.jpmart.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jpmart.R;
import com.example.jpmart.adapters.CartAdapter;
import com.example.jpmart.database.DatabaseHelper;
import com.example.jpmart.models.Customer;
import com.example.jpmart.models.Invoice;
import com.example.jpmart.models.InvoiceDetail;
import com.example.jpmart.utils.CartManager;
import com.example.jpmart.utils.SessionManager;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CartActivity extends AppCompatActivity implements CartAdapter.OnCartItemListener {

    private RecyclerView recyclerView;
    private CartAdapter adapter;
    private TextView tvTotal;
    private Spinner spinnerCustomer;
    private DatabaseHelper db;
    private SessionManager session;
    private CartManager cartManager;
    private List<Customer> customers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        db = DatabaseHelper.getInstance(this);
        session = new SessionManager(this);
        cartManager = CartManager.getInstance(); // Lấy singleton — chứa đúng giỏ hàng

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Giỏ hàng");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        tvTotal = findViewById(R.id.tv_total);
        spinnerCustomer = findViewById(R.id.spinner_customer);
        Button btnCheckout = findViewById(R.id.btn_checkout);

        // Load danh sách khách hàng
        customers = db.getAllCustomers();
        ArrayAdapter<Customer> custAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, customers);
        custAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCustomer.setAdapter(custAdapter);

        // Adapter đọc trực tiếp từ CartManager
        adapter = new CartAdapter(cartManager.getCartItems(), this);
        recyclerView.setAdapter(adapter);

        btnCheckout.setOnClickListener(v -> checkout());
        updateTotal();
    }

    private void updateTotal() {
        NumberFormat fmt = NumberFormat.getInstance(new Locale("vi", "VN"));
        tvTotal.setText("Tổng tiền: " + fmt.format(cartManager.getTotal()) + " đ");
    }

    private void checkout() {
        if (cartManager.getItemCount() == 0) {
            Toast.makeText(this, "Giỏ hàng trống", Toast.LENGTH_SHORT).show();
            return;
        }

        Customer selectedCustomer = (Customer) spinnerCustomer.getSelectedItem();
        if (selectedCustomer == null) {
            Toast.makeText(this, "Vui lòng chọn khách hàng", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Xác nhận thanh toán")
                .setMessage("Tổng tiền: " + NumberFormat.getInstance(new Locale("vi", "VN"))
                        .format(cartManager.getTotal()) + " đ\nBạn có chắc muốn tạo hóa đơn?")
                .setPositiveButton("Thanh toán", (d, w) -> {
                    Invoice invoice = new Invoice();
                    invoice.setStaffId(session.getUserId());
                    invoice.setCustomerId(selectedCustomer.getId());
                    invoice.setDate(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));
                    invoice.setTotal(cartManager.getTotal());

                    List<InvoiceDetail> details = new ArrayList<>();
                    for (com.example.jpmart.models.CartItem item : cartManager.getCartItems()) {
                        details.add(new InvoiceDetail(
                                item.getProduct().getId(),
                                item.getProduct().getName(),
                                item.getQuantity(),
                                item.getProduct().getPrice()
                        ));
                    }

                    long result = db.createInvoice(invoice, details);
                    if (result > 0) {
                        cartManager.clear(); // Xóa giỏ sau khi thanh toán
                        adapter.notifyDataSetChanged();
                        updateTotal();
                        Toast.makeText(this, "Tạo hóa đơn thành công!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(this, "Lỗi khi tạo hóa đơn", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    @Override
    public void onIncrease(int position) {
        if (cartManager.canIncrease(position)) {
            cartManager.increaseQuantity(position);
            adapter.notifyItemChanged(position);
            updateTotal();
        } else {
            Toast.makeText(this, "Không đủ hàng trong kho", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDecrease(int position) {
        cartManager.decreaseQuantity(position);
        adapter.notifyItemChanged(position);
        updateTotal();
    }

    @Override
    public void onRemove(int position) {
        cartManager.removeItem(position);
        adapter.notifyItemRemoved(position);
        adapter.notifyItemRangeChanged(position, cartManager.getItemCount());
        updateTotal();
    }

    @Override
    public boolean onSupportNavigateUp() { onBackPressed(); return true; }
}
