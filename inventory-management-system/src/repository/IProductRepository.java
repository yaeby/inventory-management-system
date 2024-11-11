package repository;

import model.Product;

import java.util.List;

public interface IProductRepository {
    List<Product> findAll();
    Product findByProductCode(String productCode);
    void addProduct(Product product);
    void deleteProduct(String productCode);
    void updateProduct(Product product);
}
