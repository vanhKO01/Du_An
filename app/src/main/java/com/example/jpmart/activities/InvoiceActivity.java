package com.example.jpmart.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jpmart.R;
import com.example.jpmart.adapters.InvoiceAdapter;
import com.example.jpmart.database.DatabaseHelper;
import com.example.jpmart.models.Invoice;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class InvoiceActivity extends AppCompatActivity implements InvoiceAdapter.OnInvoiceClickListener {

    private RecyclerView recyclerView;
    private InvoiceAdapter adapter;
    private DatabaseHelper db;
    private List<Invoice> invoices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);

        db = DatabaseHelper.getInstance(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Quản lý hóa đơn");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton fab = findViewById(R.id.fab_add);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(this, CartActivity.class);
            startActivity(intent);
        });

        loadData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        invoices = db.getAllInvoices();
        adapter = new InvoiceAdapter(invoices, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onInvoiceClick(Invoice invoice) {
        Intent intent = new Intent(this, InvoiceDetailActivity.class);
        intent.putExtra("invoice_id", invoice.getId());
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(Invoice invoice) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc muốn xóa hóa đơn " + invoice.getCode() + "?")
                .setPositiveButton("Xóa", (d, w) -> {
                    if (db.deleteInvoice(invoice.getId())) {
                        Toast.makeText(this, "Đã xóa hóa đơn", Toast.LENGTH_SHORT).show();
                        loadData();
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
