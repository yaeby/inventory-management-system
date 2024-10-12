package repository;

import model.Product;

import java.util.List;

public interface IProductRepository {
    List<Product> findAll();

    Product findByProductCode(String productCode);

    Product addProduct(Product product);

    Product deleteProduct(String productCode);

    void updateProduct(Product product);
}
