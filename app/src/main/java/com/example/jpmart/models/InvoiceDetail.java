package com.example.jpmart.models;

public class InvoiceDetail {
    private int id, invoiceId, productId, quantity;
    private double price;
    private String productName;

    public InvoiceDetail() {}
    public InvoiceDetail(int productId, String productName, int quantity, double price) {
        this.productId = productId; this.productName = productName;
        this.quantity = quantity; this.price = price;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getInvoiceId() { return invoiceId; }
    public void setInvoiceId(int invoiceId) { this.invoiceId = invoiceId; }
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public double getSubtotal() { return price * quantity; }
}
