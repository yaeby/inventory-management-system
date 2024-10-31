package builder;

import model.Product;

public class ProductBuilder implements Builder<Product> {
    private Long prodId;
    private String productCode;
    private String productName;
    private String brand;
    private int quantity;
    private Double costPrice;
    private Double sellPrice;
    private Double totalCost;
    private Double totalRevenue;

    public ProductBuilder setProdId(Long prodId) {
        this.prodId = prodId;
        return this;
    }

    public ProductBuilder setProductCode(String productCode) {
        this.productCode = productCode;
        return this;
    }

    public ProductBuilder setProductName(String productName) {
        this.productName = productName;
        return this;
    }

    public ProductBuilder setBrand(String brand) {
        this.brand = brand;
        return this;
    }

    public ProductBuilder setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public ProductBuilder setCostPrice(Double costPrice) {
        this.costPrice = costPrice;
        return this;
    }

    public ProductBuilder setSellPrice(Double sellPrice) {
        this.sellPrice = sellPrice;
        return this;
    }

    public ProductBuilder setTotalCost() {
        this.totalCost = this.costPrice * this.quantity;
        return this;
    }

    public ProductBuilder setTotalRevenue() {
        this.totalRevenue = this.sellPrice * this.quantity - this.totalCost;
        return this;
    }

    @Override
    public Product build() {
//        return new Product(prodId, productCode, productName, brand, quantity, costPrice, sellPrice, totalCost, totalRevenue);
        return null;
    }
}