package service;

import model.Product;

import java.util.List;

public interface IProductService {
    List<Product> getAllProducts();
    Product getProductByCode(String productCode);
    Product addProduct();
    void updateProduct(String productCode);
    void deleteProduct(String productCode);
}
