package com.example.jpmart.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jpmart.R;
import com.example.jpmart.adapters.ProductAdapter;
import com.example.jpmart.database.DatabaseHelper;
import com.example.jpmart.models.CartItem;
import com.example.jpmart.models.Category;
import com.example.jpmart.models.Product;
import com.example.jpmart.utils.CartManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ProductActivity extends AppCompatActivity implements ProductAdapter.OnProductClickListener {

    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private DatabaseHelper db;
    private List<Product> products;
    private EditText etSearch;
    private TextView tvCartBadge;
    private CartManager cartManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        db = DatabaseHelper.getInstance(this);
        cartManager = CartManager.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Quản lý sản phẩm");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        etSearch = findViewById(R.id.et_search);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterProducts(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        FloatingActionButton fab = findViewById(R.id.fab_add);
        fab.setOnClickListener(v -> showProductDialog(null));

        // Nút giỏ hàng → mở CartActivity (CartManager lưu chung)
        View cartBtn = findViewById(R.id.btn_cart);
        tvCartBadge = findViewById(R.id.tv_cart_badge);
        if (cartBtn != null) {
            cartBtn.setOnClickListener(v -> startActivity(new Intent(this, CartActivity.class)));
        }

        loadData();
        updateCartBadge();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCartBadge();
    }

    private void updateCartBadge() {
        if (tvCartBadge == null) return;
        int count = cartManager.getItemCount();
        if (count > 0) {
            tvCartBadge.setVisibility(View.VISIBLE);
            tvCartBadge.setText(String.valueOf(count));
        } else {
            tvCartBadge.setVisibility(View.GONE);
        }
    }

    private void loadData() {
        products = db.getAllProducts();
        adapter = new ProductAdapter(products, this, cartManager.getCartItems());
        recyclerView.setAdapter(adapter);
    }

    private void filterProducts(String query) {
        List<Product> filtered = db.searchProducts(query);
        adapter.updateData(filtered);
    }

    private void showProductDialog(Product product) {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_product, null);
        TextView tvCode = view.findViewById(R.id.tv_code);
        Spinner spinnerCategory = view.findViewById(R.id.spinner_category);
        EditText etName = view.findViewById(R.id.et_name);
        EditText etPrice = view.findViewById(R.id.et_price);
        EditText etQty = view.findViewById(R.id.et_quantity);
        EditText etUnit = view.findViewById(R.id.et_unit);
        EditText etDate = view.findViewById(R.id.et_date);
        Button btnSave = view.findViewById(R.id.btn_save);
        Button btnCancel = view.findViewById(R.id.btn_cancel);

        List<Category> categories = db.getAllCategories();
        ArrayAdapter<Category> catAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, categories);
        catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(catAdapter);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view).setCancelable(false).create();

        if (product != null) {
            tvCode.setText("Mã SP: " + product.getCode());
            tvCode.setVisibility(View.VISIBLE);
            etName.setText(product.getName());
            etPrice.setText(String.valueOf((long) product.getPrice()));
            etQty.setText(String.valueOf(product.getQuantity()));
            etUnit.setText(product.getUnit());
            etDate.setText(product.getImportDate());
            for (int i = 0; i < categories.size(); i++) {
                if (categories.get(i).getId() == product.getCategoryId()) {
                    spinnerCategory.setSelection(i);
                    break;
                }
            }
        } else {
            tvCode.setVisibility(View.GONE);
        }

        btnSave.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String priceStr = etPrice.getText().toString().trim();
            String qtyStr = etQty.getText().toString().trim();
            String unit = etUnit.getText().toString().trim();
            String date = etDate.getText().toString().trim();

            if (name.isEmpty() || priceStr.isEmpty() || qtyStr.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            Category selectedCat = (Category) spinnerCategory.getSelectedItem();
            Product p = product != null ? product : new Product();
            p.setName(name);
            p.setPrice(Double.parseDouble(priceStr));
            p.setQuantity(Integer.parseInt(qtyStr));
            p.setUnit(unit);
            p.setImportDate(date);
            p.setCategoryId(selectedCat.getId());

            if (product == null) {
                db.addProduct(p);
                Toast.makeText(this, "Thêm sản phẩm thành công", Toast.LENGTH_SHORT).show();
            } else {
                db.updateProduct(p);
                Toast.makeText(this, "Cập nhật sản phẩm thành công", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
            loadData();
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    @Override
    public void onEditClick(Product product) { showProductDialog(product); }

    @Override
    public void onDeleteClick(Product product) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc muốn xóa sản phẩm \"" + product.getName() + "\"?")
                .setPositiveButton("Xóa", (d, w) -> {
                    if (db.deleteProduct(product.getId())) {
                        Toast.makeText(this, "Đã xóa sản phẩm", Toast.LENGTH_SHORT).show();
                        loadData();
                    } else {
                        Toast.makeText(this, "Không thể xóa, sản phẩm đã có trong hóa đơn", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null).show();
    }

    @Override
    public void onAddToCart(Product product) {
        // Kiểm tra hết hàng
        if (product.getQuantity() <= 0) {
            Toast.makeText(this, "Sản phẩm đã hết hàng", Toast.LENGTH_SHORT).show();
            return;
        }
        // Kiểm tra giỏ không vượt tồn kho
        for (CartItem item : cartManager.getCartItems()) {
            if (item.getProduct().getId() == product.getId()) {
                if (item.getQuantity() >= product.getQuantity()) {
                    Toast.makeText(this,
                            "Không đủ hàng trong kho (tồn: " + product.getQuantity() + ")",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                break;
            }
        }
        // Thêm vào CartManager singleton — CartActivity sẽ đọc được
        cartManager.addProduct(product);
        updateCartBadge();
        Toast.makeText(this, "\"" + product.getName() + "\" đã thêm vào giỏ", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onSupportNavigateUp() { onBackPressed(); return true; }
}
