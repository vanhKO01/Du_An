package com.example.jpmart.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.jpmart.R;
import com.example.jpmart.database.DatabaseHelper;

import java.text.NumberFormat;
import java.util.Locale;

public class RevenueStatActivity extends AppCompatActivity {

    private EditText etFromDate, etToDate;
    private TextView tvResult;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revenue_stat);

        db = DatabaseHelper.getInstance(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Thống kê doanh thu");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        etFromDate = findViewById(R.id.et_from_date);
        etToDate = findViewById(R.id.et_to_date);
        tvResult = findViewById(R.id.tv_result);
        Button btnStat = findViewById(R.id.btn_stat);

        btnStat.setOnClickListener(v -> {
            String from = etFromDate.getText().toString().trim();
            String to = etToDate.getText().toString().trim();

            if (from.isEmpty() || to.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            double revenue = db.getRevenue(from, to);
            NumberFormat fmt = NumberFormat.getInstance(new Locale("vi", "VN"));
            tvResult.setText("Doanh thu: " + fmt.format(revenue) + " VND");
        });
    }

    @Override
    public boolean onSupportNavigateUp() { onBackPressed(); return true; }
}
