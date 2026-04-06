package com.example.jpmart.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jpmart.R;
import com.example.jpmart.database.DatabaseHelper;
import com.example.jpmart.models.Product;

import java.util.List;

public class TopProductStatActivity extends AppCompatActivity {

    private EditText etFromDate, etToDate, etLimit;
    private RecyclerView recyclerView;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_product_stat);

        db = DatabaseHelper.getInstance(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Thống kê sản phẩm");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        etFromDate = findViewById(R.id.et_from_date);
        etToDate = findViewById(R.id.et_to_date);
        etLimit = findViewById(R.id.et_limit);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Button btnStat = findViewById(R.id.btn_stat);
        btnStat.setOnClickListener(v -> {
            String from = etFromDate.getText().toString().trim();
            String to = etToDate.getText().toString().trim();
            String limitStr = etLimit.getText().toString().trim();

            if (from.isEmpty() || to.isEmpty() || limitStr.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin.", Toast.LENGTH_SHORT).show();
                return;
            }

            int limit = Integer.parseInt(limitStr);
            List<Product> topProducts = db.getTopProducts(from, to, limit);
            recyclerView.setAdapter(new TopProductAdapter(topProducts));
        });
    }

    @Override
    public boolean onSupportNavigateUp() { onBackPressed(); return true; }

    // Inner adapter for top products
    private static class TopProductAdapter extends RecyclerView.Adapter<TopProductAdapter.VH> {
        private List<Product> list;
        TopProductAdapter(List<Product> list) { this.list = list; }

        @Override
        public VH onCreateViewHolder(android.view.ViewGroup parent, int viewType) {
            android.view.View v = android.view.LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_top_product, parent, false);
            return new VH(v);
        }

        @Override
        public void onBindViewHolder(VH holder, int position) {
            Product p = list.get(position);
            holder.tvName.setText(p.getName());
            holder.tvSold.setText("Tổng số lượng: " + p.getTotalSold());
        }

        @Override
        public int getItemCount() { return list.size(); }

        static class VH extends RecyclerView.ViewHolder {
            TextView tvName, tvSold;
            VH(android.view.View v) {
                super(v);
                tvName = v.findViewById(R.id.tv_name);
                tvSold = v.findViewById(R.id.tv_sold);
            }
        }
    }
}
