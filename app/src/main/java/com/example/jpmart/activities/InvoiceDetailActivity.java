package com.example.jpmart.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jpmart.R;
import com.example.jpmart.adapters.InvoiceDetailAdapter;
import com.example.jpmart.database.DatabaseHelper;
import com.example.jpmart.models.InvoiceDetail;

import java.util.List;

public class InvoiceDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Hóa Đơn Chi Tiết");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        int invoiceId = getIntent().getIntExtra("invoice_id", -1);
        if (invoiceId == -1) { finish(); return; }

        DatabaseHelper db = DatabaseHelper.getInstance(this);
        List<InvoiceDetail> details = db.getInvoiceDetails(invoiceId);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new InvoiceDetailAdapter(details));
    }

    @Override
    public boolean onSupportNavigateUp() { onBackPressed(); return true; }
}
