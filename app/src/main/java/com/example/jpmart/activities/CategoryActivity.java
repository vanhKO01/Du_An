package com.example.jpmart.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.example.jpmart.adapters.CategoryAdapter;
import com.example.jpmart.database.DatabaseHelper;
import com.example.jpmart.models.Category;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class CategoryActivity extends AppCompatActivity implements CategoryAdapter.OnCategoryClickListener {

    private RecyclerView recyclerView;
    private CategoryAdapter adapter;
    private DatabaseHelper db;
    private List<Category> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        db = DatabaseHelper.getInstance(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Quản lý danh mục");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton fab = findViewById(R.id.fab_add);
        fab.setOnClickListener(v -> showAddDialog());

        loadData();
    }

    private void loadData() {
        categories = db.getAllCategories();
        adapter = new CategoryAdapter(categories, this);
        recyclerView.setAdapter(adapter);
    }

    private void showAddDialog() {
        showCategoryDialog(null);
    }

    private void showCategoryDialog(Category category) {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_category, null);
        TextView tvCode = view.findViewById(R.id.tv_code);
        EditText etName = view.findViewById(R.id.et_name);
        Button btnSave = view.findViewById(R.id.btn_save);
        Button btnCancel = view.findViewById(R.id.btn_cancel);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view)
                .setCancelable(false)
                .create();

        if (category != null) {
            tvCode.setText("Mã Danh Mục: " + category.getCode());
            tvCode.setVisibility(View.VISIBLE);
            etName.setText(category.getName());
        } else {
            tvCode.setVisibility(View.GONE);
        }

        btnSave.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            if (TextUtils.isEmpty(name)) {
                etName.setError("Vui lòng nhập tên danh mục");
                return;
            }
            if (category == null) {
                db.addCategory(name);
                Toast.makeText(this, "Thêm danh mục thành công", Toast.LENGTH_SHORT).show();
            } else {
                db.updateCategory(category.getId(), name);
                Toast.makeText(this, "Cập nhật danh mục thành công", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
            loadData();
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    @Override
    public void onEditClick(Category category) {
        showCategoryDialog(category);
    }

    @Override
    public void onDeleteClick(Category category) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc muốn xóa danh mục \"" + category.getName() + "\"?")
                .setPositiveButton("Xóa", (d, w) -> {
                    if (db.deleteCategory(category.getId())) {
                        Toast.makeText(this, "Đã xóa danh mục", Toast.LENGTH_SHORT).show();
                        loadData();
                    } else {
                        Toast.makeText(this, "Không thể xóa, danh mục đang được sử dụng", Toast.LENGTH_SHORT).show();
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
