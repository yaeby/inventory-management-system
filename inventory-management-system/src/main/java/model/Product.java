    package model;

    public class Product {
        private Long id;
        private String productCode;
        private String productName;
        private String brand;
        private int quantity;
        private Double costPrice;
        private Double sellPrice;
        private Double totalCost;
        private Double totalRevenue;
        private Long categoryId;
        private Category category;

        public Product() {
        }

        public Long getId() {
            return id;
        }

        public void setId(Long productId) {
            this.id = productId;
        }

        public String getProductCode() {
            return productCode;
        }

        public void setProductCode(String prodCode) {
            this.productCode = prodCode;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String prodName) {
            this.productName = prodName;
        }

        public Double getCostPrice() {
            return costPrice;
        }

        public void setCostPrice(Double costPrice) {
            this.costPrice = costPrice;
        }

        public Double getSellPrice() {
            return sellPrice;
        }

        public void setSellPrice(Double sellPrice) {
            this.sellPrice = sellPrice;
        }

        public Double getTotalCost() {
            return totalCost;
        }

        public void setTotalCost(Double totalCost) {
            this.totalCost = totalCost;
        }

        public Double getTotalRevenue() {
            return totalRevenue;
        }

        public void setTotalRevenue(Double totalRevenue) {
            this.totalRevenue = totalRevenue;
        }

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public Long getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(Long categoryId) {
            this.categoryId = categoryId;
        }

        public Category getCategory() {
            return category;
        }

        public void setCategory(Category category) {
            this.category = category;
            this.categoryId = category.getId();
        }

        @Override
        public String toString() {
            return getProductCode();
        }
    }
