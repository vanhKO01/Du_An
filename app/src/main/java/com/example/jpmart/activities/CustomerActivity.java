package com.example.jpmart.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jpmart.R;
import com.example.jpmart.adapters.CustomerAdapter;
import com.example.jpmart.database.DatabaseHelper;
import com.example.jpmart.models.Customer;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class CustomerActivity extends AppCompatActivity implements CustomerAdapter.OnCustomerClickListener {

    private RecyclerView recyclerView;
    private CustomerAdapter adapter;
    private DatabaseHelper db;
    private List<Customer> customers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        db = DatabaseHelper.getInstance(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Quản lý khách hàng");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton fab = findViewById(R.id.fab_add);
        fab.setOnClickListener(v -> showCustomerDialog(null));

        loadData();
    }

    private void loadData() {
        customers = db.getAllCustomers();
        adapter = new CustomerAdapter(customers, this);
        recyclerView.setAdapter(adapter);
    }

    private void showCustomerDialog(Customer customer) {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_customer, null);
        TextView tvCode = view.findViewById(R.id.tv_code);
        EditText etName = view.findViewById(R.id.et_name);
        EditText etPhone = view.findViewById(R.id.et_phone);
        EditText etAddress = view.findViewById(R.id.et_address);
        Button btnSave = view.findViewById(R.id.btn_save);
        Button btnCancel = view.findViewById(R.id.btn_cancel);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view).setCancelable(false).create();

        if (customer != null) {
            tvCode.setText("Mã KH: " + customer.getCode());
            tvCode.setVisibility(View.VISIBLE);
            etName.setText(customer.getName());
            etPhone.setText(customer.getPhone());
            etAddress.setText(customer.getAddress());
        } else {
            tvCode.setVisibility(View.GONE);
        }

        btnSave.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String address = etAddress.getText().toString().trim();

            if (name.isEmpty()) {
                etName.setError("Vui lòng nhập tên khách hàng");
                return;
            }

            if (customer == null) {
                Customer c = new Customer();
                c.setName(name); c.setPhone(phone); c.setAddress(address);
                db.addCustomer(c);
                Toast.makeText(this, "Thêm khách hàng thành công", Toast.LENGTH_SHORT).show();
            } else {
                customer.setName(name); customer.setPhone(phone); customer.setAddress(address);
                db.updateCustomer(customer);
                Toast.makeText(this, "Cập nhật khách hàng thành công", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
            loadData();
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    @Override
    public void onEditClick(Customer customer) {
        showCustomerDialog(customer);
    }

    @Override
    public void onDeleteClick(Customer customer) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc muốn xóa khách hàng \"" + customer.getName() + "\"?")
                .setPositiveButton("Xóa", (d, w) -> {
                    if (db.deleteCustomer(customer.getId())) {
                        Toast.makeText(this, "Đã xóa khách hàng", Toast.LENGTH_SHORT).show();
                        loadData();
                    } else {
                        Toast.makeText(this, "Không thể xóa, khách hàng đang có hóa đơn", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
