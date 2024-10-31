package service;

import model.Product;

import java.sql.SQLException;
import java.util.List;

public interface IProductService {
    List<Product> getAllProducts() throws SQLException;
    Product getProductByCode(String productCode);
    void addProduct();
    void updateProduct(String productCode);
    void deleteProduct(String productCode);
}
