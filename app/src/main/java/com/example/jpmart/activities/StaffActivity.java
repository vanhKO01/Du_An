package com.example.jpmart.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jpmart.R;
import com.example.jpmart.database.DatabaseHelper;
import com.example.jpmart.models.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class StaffActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private StaffAdapter adapter;
    private DatabaseHelper db;
    private List<User> staffList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff);

        db = DatabaseHelper.getInstance(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Quản lý nhân viên");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton fab = findViewById(R.id.fab_add);
        fab.setOnClickListener(v -> showAddDialog());

        loadData();
    }

    private void loadData() {
        staffList = db.getAllStaff();
        adapter = new StaffAdapter(staffList);
        recyclerView.setAdapter(adapter);
    }

    private void showAddDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_staff, null);
        EditText etUsername = view.findViewById(R.id.et_username);
        EditText etPassword = view.findViewById(R.id.et_password);
        EditText etFullname = view.findViewById(R.id.et_fullname);
        Button btnSave = view.findViewById(R.id.btn_save);
        Button btnCancel = view.findViewById(R.id.btn_cancel);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view).setCancelable(false).create();

        btnSave.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String fullname = etFullname.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty() || fullname.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setFullname(fullname);
            user.setRole("staff");

            long result = db.addUser(user);
            if (result > 0) {
                Toast.makeText(this, "Thêm nhân viên thành công", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                loadData();
            } else {
                Toast.makeText(this, "Tên đăng nhập đã tồn tại", Toast.LENGTH_SHORT).show();
            }
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    @Override
    public boolean onSupportNavigateUp() { onBackPressed(); return true; }

    private class StaffAdapter extends RecyclerView.Adapter<StaffAdapter.VH> {
        private List<User> list;
        StaffAdapter(List<User> list) { this.list = list; }

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_staff, parent, false);
            return new VH(v);
        }

        @Override
        public void onBindViewHolder(VH holder, int position) {
            User u = list.get(position);
            holder.tvFullname.setText(u.getFullname());
            holder.tvUsername.setText("@" + u.getUsername());
            holder.btnDelete.setOnClickListener(v -> {
                new AlertDialog.Builder(StaffActivity.this)
                        .setTitle("Xác nhận xóa")
                        .setMessage("Xóa nhân viên " + u.getFullname() + "?")
                        .setPositiveButton("Xóa", (d, w) -> {
                            if (db.deleteUser(u.getId())) {
                                loadData();
                            } else {
                                Toast.makeText(StaffActivity.this,
                                        "Không thể xóa, nhân viên đang có hóa đơn",
                                        Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Hủy", null).show();
            });
        }

        @Override
        public int getItemCount() { return list.size(); }

        class VH extends RecyclerView.ViewHolder {
            TextView tvFullname, tvUsername;
            ImageButton btnDelete;
            VH(View v) {
                super(v);
                tvFullname = v.findViewById(R.id.tv_fullname);
                tvUsername = v.findViewById(R.id.tv_username);
                btnDelete = v.findViewById(R.id.btn_delete);
            }
        }
    }
}
