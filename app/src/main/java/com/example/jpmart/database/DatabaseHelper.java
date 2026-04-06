package com.example.jpmart.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.jpmart.models.Category;
import com.example.jpmart.models.Customer;
import com.example.jpmart.models.Invoice;
import com.example.jpmart.models.InvoiceDetail;
import com.example.jpmart.models.Product;
import com.example.jpmart.models.User;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "jpmart.db";
    private static final int DB_VERSION = 1;

    // Tables
    public static final String TABLE_USERS = "users";
    public static final String TABLE_CATEGORIES = "categories";
    public static final String TABLE_PRODUCTS = "products";
    public static final String TABLE_CUSTOMERS = "customers";
    public static final String TABLE_INVOICES = "invoices";
    public static final String TABLE_INVOICE_DETAILS = "invoice_details";

    private static DatabaseHelper instance;

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Users table
        db.execSQL("CREATE TABLE " + TABLE_USERS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username TEXT UNIQUE NOT NULL," +
                "password TEXT NOT NULL," +
                "fullname TEXT," +
                "role TEXT NOT NULL" + // 'manager' or 'staff'
                ")");

        // Categories table
        db.execSQL("CREATE TABLE " + TABLE_CATEGORIES + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "code TEXT UNIQUE NOT NULL," +
                "name TEXT NOT NULL" +
                ")");

        // Products table
        db.execSQL("CREATE TABLE " + TABLE_PRODUCTS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "code TEXT UNIQUE NOT NULL," +
                "name TEXT NOT NULL," +
                "price REAL NOT NULL," +
                "quantity INTEGER NOT NULL," +
                "unit TEXT," +
                "import_date TEXT," +
                "category_id INTEGER," +
                "FOREIGN KEY(category_id) REFERENCES " + TABLE_CATEGORIES + "(id)" +
                ")");

        // Customers table
        db.execSQL("CREATE TABLE " + TABLE_CUSTOMERS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "code TEXT UNIQUE NOT NULL," +
                "name TEXT NOT NULL," +
                "phone TEXT," +
                "address TEXT" +
                ")");

        // Invoices table
        db.execSQL("CREATE TABLE " + TABLE_INVOICES + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "code TEXT UNIQUE NOT NULL," +
                "staff_id INTEGER," +
                "customer_id INTEGER," +
                "date TEXT NOT NULL," +
                "total REAL NOT NULL," +
                "FOREIGN KEY(staff_id) REFERENCES " + TABLE_USERS + "(id)," +
                "FOREIGN KEY(customer_id) REFERENCES " + TABLE_CUSTOMERS + "(id)" +
                ")");

        // Invoice details table
        db.execSQL("CREATE TABLE " + TABLE_INVOICE_DETAILS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "invoice_id INTEGER NOT NULL," +
                "product_id INTEGER NOT NULL," +
                "quantity INTEGER NOT NULL," +
                "price REAL NOT NULL," +
                "FOREIGN KEY(invoice_id) REFERENCES " + TABLE_INVOICES + "(id)," +
                "FOREIGN KEY(product_id) REFERENCES " + TABLE_PRODUCTS + "(id)" +
                ")");

        // Insert default data
        insertDefaultData(db);
    }

    private void insertDefaultData(SQLiteDatabase db) {
        // Default users
        ContentValues cv = new ContentValues();
        cv.put("username", "admin");
        cv.put("password", "admin123");
        cv.put("fullname", "Nguyễn Văn An");
        cv.put("role", "manager");
        db.insert(TABLE_USERS, null, cv);

        cv = new ContentValues();
        cv.put("username", "staff1");
        cv.put("password", "staff123");
        cv.put("fullname", "Trần Thị Cúc");
        cv.put("role", "staff");
        db.insert(TABLE_USERS, null, cv);

        cv = new ContentValues();
        cv.put("username", "staff2");
        cv.put("password", "staff123");
        cv.put("fullname", "Lê Minh Hùng");
        cv.put("role", "staff");
        db.insert(TABLE_USERS, null, cv);

        // Default categories
        String[][] cats = {
                {"DM001", "Đồ uống"}, {"DM002", "Bánh kẹo"}, {"DM003", "Sữa và chế phẩm từ sữa"},
                {"DM004", "Mì - Cháo - Phở"}, {"DM005", "Thực phẩm khô"}, {"DM006", "Gia vị"},
                {"DM007", "Đồ hộp"}, {"DM008", "Rau củ quả"}, {"DM009", "Thịt - Hải sản"},
                {"DM010", "Sản phẩm đông lạnh"}
        };
        for (String[] cat : cats) {
            cv = new ContentValues();
            cv.put("code", cat[0]);
            cv.put("name", cat[1]);
            db.insert(TABLE_CATEGORIES, null, cv);
        }

        // Default products
        Object[][] prods = {
                {"SP001", "Nước ngọt Calpis", 15000.0, 55, "Lon", "2024-02-08", 1},
                {"SP002", "Trà xanh Ito En", 10000.0, 40, "Chai", "2024-02-08", 1},
                {"SP003", "Bánh Pocky", 25000.0, 30, "Hộp", "2024-02-08", 2},
                {"SP004", "Sữa Meiji", 15000.0, 20, "Hộp", "2024-02-08", 3},
                {"SP005", "Mì Udon", 5000.0, 100, "Gói", "2024-02-08", 4},
                {"SP006", "Nước khoáng Suntory", 8000.0, 60, "Chai", "2024-02-08", 1},
                {"SP007", "Snack Jagabee", 12000.0, 45, "Gói", "2024-02-08", 2},
                {"SP008", "Trà gạo lứt Genmaicha", 30000.0, 25, "Hộp", "2024-02-08", 1},
                {"SP009", "Nước tương Kikkoman", 45000.0, 30, "Chai", "2024-02-08", 6},
                {"SP010", "Wasabi S&B", 35000.0, 20, "Tuýp", "2024-02-08", 6}
        };
        for (Object[] p : prods) {
            cv = new ContentValues();
            cv.put("code", (String) p[0]);
            cv.put("name", (String) p[1]);
            cv.put("price", (Double) p[2]);
            cv.put("quantity", (Integer) p[3]);
            cv.put("unit", (String) p[4]);
            cv.put("import_date", (String) p[5]);
            cv.put("category_id", (Integer) p[6]);
            db.insert(TABLE_PRODUCTS, null, cv);
        }

        // Default customers
        cv = new ContentValues();
        cv.put("code", "KH000");
        cv.put("name", "Nặc danh");
        cv.put("phone", "0000000000");
        cv.put("address", "");
        db.insert(TABLE_CUSTOMERS, null, cv);

        String[][] custs = {
                {"KH001", "Trần Thị Bích", "0901234567", "Hà Nội"},
                {"KH002", "Lê Hoàng Nam", "0912345678", "Hà Nội"},
                {"KH003", "Phạm Minh Khang", "0923456789", "Hà Nội"},
                {"KH004", "Võ Hồng Phúc", "0934567890", "Hà Nội"},
                {"KH005", "Bùi Thị Lan", "0945678901", "Hà Nội"}
        };
        for (String[] c : custs) {
            cv = new ContentValues();
            cv.put("code", c[0]);
            cv.put("name", c[1]);
            cv.put("phone", c[2]);
            cv.put("address", c[3]);
            db.insert(TABLE_CUSTOMERS, null, cv);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INVOICE_DETAILS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INVOICES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CUSTOMERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // ===== USER METHODS =====
    public User login(String username, String password) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_USERS, null,
                "username=? AND password=?",
                new String[]{username, password}, null, null, null);
        User user = null;
        if (c.moveToFirst()) {
            user = new User(
                    c.getInt(c.getColumnIndexOrThrow("id")),
                    c.getString(c.getColumnIndexOrThrow("username")),
                    c.getString(c.getColumnIndexOrThrow("password")),
                    c.getString(c.getColumnIndexOrThrow("fullname")),
                    c.getString(c.getColumnIndexOrThrow("role"))
            );
        }
        c.close();
        return user;
    }

    public boolean changePassword(int userId, String newPassword) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("password", newPassword);
        return db.update(TABLE_USERS, cv, "id=?", new String[]{String.valueOf(userId)}) > 0;
    }

    public List<User> getAllStaff() {
        List<User> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_USERS, null, "role=?", new String[]{"staff"}, null, null, null);
        while (c.moveToNext()) {
            list.add(new User(
                    c.getInt(c.getColumnIndexOrThrow("id")),
                    c.getString(c.getColumnIndexOrThrow("username")),
                    c.getString(c.getColumnIndexOrThrow("password")),
                    c.getString(c.getColumnIndexOrThrow("fullname")),
                    c.getString(c.getColumnIndexOrThrow("role"))
            ));
        }
        c.close();
        return list;
    }

    public long addUser(User user) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("username", user.getUsername());
        cv.put("password", user.getPassword());
        cv.put("fullname", user.getFullname());
        cv.put("role", user.getRole());
        return db.insert(TABLE_USERS, null, cv);
    }

    public boolean deleteUser(int id) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_INVOICES + " WHERE staff_id=?",
                new String[]{String.valueOf(id)});
        int count = 0;
        if (c.moveToFirst()) count = c.getInt(0);
        c.close();
        if (count > 0) return false;
        return db.delete(TABLE_USERS, "id=?", new String[]{String.valueOf(id)}) > 0;
    }

    // ===== CATEGORY METHODS =====
    public List<Category> getAllCategories() {
        List<Category> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_CATEGORIES, null, null, null, null, null, "code ASC");
        while (c.moveToNext()) {
            list.add(new Category(
                    c.getInt(c.getColumnIndexOrThrow("id")),
                    c.getString(c.getColumnIndexOrThrow("code")),
                    c.getString(c.getColumnIndexOrThrow("name"))
            ));
        }
        c.close();
        return list;
    }

    public long addCategory(String name) {
        SQLiteDatabase db = getWritableDatabase();
        String code = generateCategoryCode(db);
        ContentValues cv = new ContentValues();
        cv.put("code", code);
        cv.put("name", name);
        return db.insert(TABLE_CATEGORIES, null, cv);
    }

    private String generateCategoryCode(SQLiteDatabase db) {
        Cursor c = db.rawQuery("SELECT MAX(CAST(SUBSTR(code,3) AS INTEGER)) FROM " + TABLE_CATEGORIES, null);
        int max = 0;
        if (c.moveToFirst() && !c.isNull(0)) max = c.getInt(0);
        c.close();
        return String.format("DM%03d", max + 1);
    }

    public boolean updateCategory(int id, String name) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        return db.update(TABLE_CATEGORIES, cv, "id=?", new String[]{String.valueOf(id)}) > 0;
    }

    public boolean deleteCategory(int id) {
        SQLiteDatabase db = getWritableDatabase();
        // Check if any product is using this category
        Cursor c = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_PRODUCTS + " WHERE category_id=?",
                new String[]{String.valueOf(id)});
        int count = 0;
        if (c.moveToFirst()) count = c.getInt(0);
        c.close();
        if (count > 0) return false; // Cannot delete - in use
        return db.delete(TABLE_CATEGORIES, "id=?", new String[]{String.valueOf(id)}) > 0;
    }

    public Category getCategoryById(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_CATEGORIES, null, "id=?", new String[]{String.valueOf(id)}, null, null, null);
        Category cat = null;
        if (c.moveToFirst()) {
            cat = new Category(c.getInt(0), c.getString(1), c.getString(2));
        }
        c.close();
        return cat;
    }

    // ===== PRODUCT METHODS =====
    public List<Product> getAllProducts() {
        List<Product> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT p.*, c.name as cat_name FROM " + TABLE_PRODUCTS + " p " +
                "LEFT JOIN " + TABLE_CATEGORIES + " c ON p.category_id = c.id ORDER BY p.code ASC";
        Cursor c = db.rawQuery(query, null);
        while (c.moveToNext()) {
            Product p = new Product();
            p.setId(c.getInt(c.getColumnIndexOrThrow("id")));
            p.setCode(c.getString(c.getColumnIndexOrThrow("code")));
            p.setName(c.getString(c.getColumnIndexOrThrow("name")));
            p.setPrice(c.getDouble(c.getColumnIndexOrThrow("price")));
            p.setQuantity(c.getInt(c.getColumnIndexOrThrow("quantity")));
            p.setUnit(c.getString(c.getColumnIndexOrThrow("unit")));
            p.setImportDate(c.getString(c.getColumnIndexOrThrow("import_date")));
            p.setCategoryId(c.getInt(c.getColumnIndexOrThrow("category_id")));
            p.setCategoryName(c.getString(c.getColumnIndexOrThrow("cat_name")));
            list.add(p);
        }
        c.close();
        return list;
    }

    public List<Product> searchProducts(String keyword) {
        List<Product> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT p.*, c.name as cat_name FROM " + TABLE_PRODUCTS + " p " +
                "LEFT JOIN " + TABLE_CATEGORIES + " c ON p.category_id = c.id " +
                "WHERE p.name LIKE ? ORDER BY p.code ASC";
        Cursor c = db.rawQuery(query, new String[]{"%" + keyword + "%"});
        while (c.moveToNext()) {
            Product p = new Product();
            p.setId(c.getInt(c.getColumnIndexOrThrow("id")));
            p.setCode(c.getString(c.getColumnIndexOrThrow("code")));
            p.setName(c.getString(c.getColumnIndexOrThrow("name")));
            p.setPrice(c.getDouble(c.getColumnIndexOrThrow("price")));
            p.setQuantity(c.getInt(c.getColumnIndexOrThrow("quantity")));
            p.setUnit(c.getString(c.getColumnIndexOrThrow("unit")));
            p.setImportDate(c.getString(c.getColumnIndexOrThrow("import_date")));
            p.setCategoryId(c.getInt(c.getColumnIndexOrThrow("category_id")));
            p.setCategoryName(c.getString(c.getColumnIndexOrThrow("cat_name")));
            list.add(p);
        }
        c.close();
        return list;
    }

    public long addProduct(Product product) {
        SQLiteDatabase db = getWritableDatabase();
        String code = generateProductCode(db);
        ContentValues cv = new ContentValues();
        cv.put("code", code);
        cv.put("name", product.getName());
        cv.put("price", product.getPrice());
        cv.put("quantity", product.getQuantity());
        cv.put("unit", product.getUnit());
        cv.put("import_date", product.getImportDate());
        cv.put("category_id", product.getCategoryId());
        return db.insert(TABLE_PRODUCTS, null, cv);
    }

    private String generateProductCode(SQLiteDatabase db) {
        Cursor c = db.rawQuery("SELECT MAX(CAST(SUBSTR(code,3) AS INTEGER)) FROM " + TABLE_PRODUCTS, null);
        int max = 0;
        if (c.moveToFirst() && !c.isNull(0)) max = c.getInt(0);
        c.close();
        return String.format("SP%03d", max + 1);
    }

    public boolean updateProduct(Product product) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", product.getName());
        cv.put("price", product.getPrice());
        cv.put("quantity", product.getQuantity());
        cv.put("unit", product.getUnit());
        cv.put("import_date", product.getImportDate());
        cv.put("category_id", product.getCategoryId());
        return db.update(TABLE_PRODUCTS, cv, "id=?", new String[]{String.valueOf(product.getId())}) > 0;
    }

    public boolean deleteProduct(int id) {
        SQLiteDatabase db = getWritableDatabase();
        // Check if product has been used in any invoice
        Cursor c = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_INVOICE_DETAILS + " WHERE product_id=?",
                new String[]{String.valueOf(id)});
        int count = 0;
        if (c.moveToFirst()) count = c.getInt(0);
        c.close();
        if (count > 0) return false; // Cannot delete - used in invoices
        return db.delete(TABLE_PRODUCTS, "id=?", new String[]{String.valueOf(id)}) > 0;
    }

    public boolean updateProductQuantity(int productId, int delta) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        // Use rawQuery workaround: update via execSQL (void), check with update()
        db.execSQL("UPDATE " + TABLE_PRODUCTS + " SET quantity = quantity + " + delta +
                " WHERE id = " + productId);
        return true;
    }

    // ===== CUSTOMER METHODS =====
    public List<Customer> getAllCustomers() {
        List<Customer> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_CUSTOMERS, null, null, null, null, null, "code ASC");
        while (c.moveToNext()) {
            list.add(new Customer(
                    c.getInt(c.getColumnIndexOrThrow("id")),
                    c.getString(c.getColumnIndexOrThrow("code")),
                    c.getString(c.getColumnIndexOrThrow("name")),
                    c.getString(c.getColumnIndexOrThrow("phone")),
                    c.getString(c.getColumnIndexOrThrow("address"))
            ));
        }
        c.close();
        return list;
    }

    public long addCustomer(Customer customer) {
        SQLiteDatabase db = getWritableDatabase();
        String code = generateCustomerCode(db);
        ContentValues cv = new ContentValues();
        cv.put("code", code);
        cv.put("name", customer.getName());
        cv.put("phone", customer.getPhone());
        cv.put("address", customer.getAddress());
        return db.insert(TABLE_CUSTOMERS, null, cv);
    }

    private String generateCustomerCode(SQLiteDatabase db) {
        Cursor c = db.rawQuery("SELECT MAX(CAST(SUBSTR(code,3) AS INTEGER)) FROM " + TABLE_CUSTOMERS, null);
        int max = 0;
        if (c.moveToFirst() && !c.isNull(0)) max = c.getInt(0);
        c.close();
        return String.format("KH%03d", max + 1);
    }

    public boolean updateCustomer(Customer customer) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", customer.getName());
        cv.put("phone", customer.getPhone());
        cv.put("address", customer.getAddress());
        return db.update(TABLE_CUSTOMERS, cv, "id=?", new String[]{String.valueOf(customer.getId())}) > 0;
    }

    public boolean deleteCustomer(int id) {
        SQLiteDatabase db = getWritableDatabase();
        // Check if customer has invoices
        Cursor c = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_INVOICES + " WHERE customer_id=?",
                new String[]{String.valueOf(id)});
        int count = 0;
        if (c.moveToFirst()) count = c.getInt(0);
        c.close();
        if (count > 0) return false; // Cannot delete - has invoices
        return db.delete(TABLE_CUSTOMERS, "id=?", new String[]{String.valueOf(id)}) > 0;
    }

    // ===== INVOICE METHODS =====
    public List<Invoice> getAllInvoices() {
        List<Invoice> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT i.*, u.fullname as staff_name, c.name as customer_name " +
                "FROM " + TABLE_INVOICES + " i " +
                "LEFT JOIN " + TABLE_USERS + " u ON i.staff_id = u.id " +
                "LEFT JOIN " + TABLE_CUSTOMERS + " c ON i.customer_id = c.id " +
                "ORDER BY i.code DESC";
        Cursor cur = db.rawQuery(query, null);
        while (cur.moveToNext()) {
            Invoice inv = new Invoice();
            inv.setId(cur.getInt(cur.getColumnIndexOrThrow("id")));
            inv.setCode(cur.getString(cur.getColumnIndexOrThrow("code")));
            inv.setStaffId(cur.getInt(cur.getColumnIndexOrThrow("staff_id")));
            inv.setCustomerId(cur.getInt(cur.getColumnIndexOrThrow("customer_id")));
            inv.setDate(cur.getString(cur.getColumnIndexOrThrow("date")));
            inv.setTotal(cur.getDouble(cur.getColumnIndexOrThrow("total")));
            inv.setStaffName(cur.getString(cur.getColumnIndexOrThrow("staff_name")));
            inv.setCustomerName(cur.getString(cur.getColumnIndexOrThrow("customer_name")));
            list.add(inv);
        }
        cur.close();
        return list;
    }

    public long createInvoice(Invoice invoice, List<InvoiceDetail> details) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            String code = generateInvoiceCode(db);
            ContentValues cv = new ContentValues();
            cv.put("code", code);
            cv.put("staff_id", invoice.getStaffId());
            cv.put("customer_id", invoice.getCustomerId());
            cv.put("date", invoice.getDate());
            cv.put("total", invoice.getTotal());
            long invId = db.insert(TABLE_INVOICES, null, cv);

            for (InvoiceDetail d : details) {
                ContentValues dcv = new ContentValues();
                dcv.put("invoice_id", invId);
                dcv.put("product_id", d.getProductId());
                dcv.put("quantity", d.getQuantity());
                dcv.put("price", d.getPrice());
                db.insert(TABLE_INVOICE_DETAILS, null, dcv);
                // Update product quantity
                db.execSQL("UPDATE " + TABLE_PRODUCTS + " SET quantity = quantity - " + d.getQuantity() +
                        " WHERE id = " + d.getProductId());
            }
            db.setTransactionSuccessful();
            return invId;
        } finally {
            db.endTransaction();
        }
    }

    private String generateInvoiceCode(SQLiteDatabase db) {
        Cursor c = db.rawQuery("SELECT MAX(CAST(SUBSTR(code,3) AS INTEGER)) FROM " + TABLE_INVOICES, null);
        int max = 0;
        if (c.moveToFirst() && !c.isNull(0)) max = c.getInt(0);
        c.close();
        return String.format("HD%03d", max + 1);
    }

    public boolean deleteInvoice(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(TABLE_INVOICE_DETAILS, "invoice_id=?", new String[]{String.valueOf(id)});
            db.delete(TABLE_INVOICES, "id=?", new String[]{String.valueOf(id)});
            db.setTransactionSuccessful();
            return true;
        } finally {
            db.endTransaction();
        }
    }

    public List<InvoiceDetail> getInvoiceDetails(int invoiceId) {
        List<InvoiceDetail> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT d.*, p.name as product_name FROM " + TABLE_INVOICE_DETAILS + " d " +
                "LEFT JOIN " + TABLE_PRODUCTS + " p ON d.product_id = p.id " +
                "WHERE d.invoice_id = ?";
        Cursor c = db.rawQuery(query, new String[]{String.valueOf(invoiceId)});
        while (c.moveToNext()) {
            InvoiceDetail d = new InvoiceDetail();
            d.setId(c.getInt(c.getColumnIndexOrThrow("id")));
            d.setInvoiceId(c.getInt(c.getColumnIndexOrThrow("invoice_id")));
            d.setProductId(c.getInt(c.getColumnIndexOrThrow("product_id")));
            d.setQuantity(c.getInt(c.getColumnIndexOrThrow("quantity")));
            d.setPrice(c.getDouble(c.getColumnIndexOrThrow("price")));
            d.setProductName(c.getString(c.getColumnIndexOrThrow("product_name")));
            list.add(d);
        }
        c.close();
        return list;
    }

    // ===== STATISTICS =====
    public double getRevenue(String fromDate, String toDate) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT SUM(total) FROM " + TABLE_INVOICES +
                " WHERE date >= ? AND date <= ?", new String[]{fromDate, toDate});
        double total = 0;
        if (c.moveToFirst() && !c.isNull(0)) total = c.getDouble(0);
        c.close();
        return total;
    }

    public List<Product> getTopProducts(String fromDate, String toDate, int limit) {
        List<Product> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT p.id, p.code, p.name, p.price, p.quantity, p.unit, p.import_date, p.category_id, " +
                "SUM(d.quantity) as total_sold FROM " + TABLE_INVOICE_DETAILS + " d " +
                "JOIN " + TABLE_PRODUCTS + " p ON d.product_id = p.id " +
                "JOIN " + TABLE_INVOICES + " i ON d.invoice_id = i.id " +
                "WHERE i.date >= ? AND i.date <= ? " +
                "GROUP BY p.id ORDER BY total_sold DESC LIMIT ?";
        Cursor c = db.rawQuery(query, new String[]{fromDate, toDate, String.valueOf(limit)});
        while (c.moveToNext()) {
            Product p = new Product();
            p.setId(c.getInt(c.getColumnIndexOrThrow("id")));
            p.setCode(c.getString(c.getColumnIndexOrThrow("code")));
            p.setName(c.getString(c.getColumnIndexOrThrow("name")));
            p.setPrice(c.getDouble(c.getColumnIndexOrThrow("price")));
            p.setQuantity(c.getInt(c.getColumnIndexOrThrow("quantity")));
            p.setUnit(c.getString(c.getColumnIndexOrThrow("unit")));
            p.setImportDate(c.getString(c.getColumnIndexOrThrow("import_date")));
            p.setCategoryId(c.getInt(c.getColumnIndexOrThrow("category_id")));
            p.setTotalSold(c.getInt(c.getColumnIndexOrThrow("total_sold")));
            list.add(p);
        }
        c.close();
        return list;
    }
}
