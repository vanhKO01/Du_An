package com.example.jpmart.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.jpmart.R;
import com.example.jpmart.utils.SessionManager;

public class HomeManagerActivity extends AppCompatActivity {

    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_manager);

        session = new SessionManager(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Home");
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }

        // Statistics cards
        findViewById(R.id.card_revenue).setOnClickListener(v ->
                startActivity(new Intent(this, RevenueStatActivity.class)));
        findViewById(R.id.card_top_products).setOnClickListener(v ->
                startActivity(new Intent(this, TopProductStatActivity.class)));
        findViewById(R.id.card_top_customers).setOnClickListener(v ->
                startActivity(new Intent(this, CustomerActivity.class)));

        // Management cards
        findViewById(R.id.card_products).setOnClickListener(v ->
                startActivity(new Intent(this, ProductActivity.class)));
        findViewById(R.id.card_customers).setOnClickListener(v ->
                startActivity(new Intent(this, CustomerActivity.class)));
        findViewById(R.id.card_invoices).setOnClickListener(v ->
                startActivity(new Intent(this, InvoiceActivity.class)));
        findViewById(R.id.card_categories).setOnClickListener(v ->
                startActivity(new Intent(this, CategoryActivity.class)));

        // User cards
        findViewById(R.id.card_staff).setOnClickListener(v ->
                startActivity(new Intent(this, StaffActivity.class)));
        findViewById(R.id.card_change_password).setOnClickListener(v ->
                startActivity(new Intent(this, ChangePasswordActivity.class)));
        findViewById(R.id.card_logout).setOnClickListener(v -> logout());
    }

    private void logout() {
        session.logout();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        // Prevent going back to prevent re-login issues
    }
}
