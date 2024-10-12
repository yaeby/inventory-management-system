package repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Product;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ProductRepository implements IProductRepository {

    private static final String PRODUCTS_JSON_PATH = "src/data/products.json";

    @Override
    public List<Product> findAll() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(new File(PRODUCTS_JSON_PATH), new TypeReference<List<Product>>() {});
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Product findByProductCode(String productCode) {
        List<Product> products = findAll();

        for (Product product : products) {
            if(product.getProductCode().equals(productCode)) {
                return product;
            }
        }
        return null;
    }

    @Override
    public Product addProduct(Product product) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<Product> products = findAll();

            Long newId = products.stream()
                    .mapToLong(Product::getProdId)
                    .max()
                    .orElse(0L) + 1;
            product.setProdId(newId);

            products.add(product);

            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(PRODUCTS_JSON_PATH), products);
            return product;
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Product deleteProduct(String productCode){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Product product = findByProductCode(productCode);
            if (product == null) {
                return null;
            }
            List<Product> products = findAll();
            products.removeIf(prd -> prd.getProductCode().equals(productCode));
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(PRODUCTS_JSON_PATH), products);
            return product;
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void updateProduct(Product product) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<Product> products = findAll();
            for (int i = 0; i < products.size(); i++) {
                if (products.get(i).getProductCode().equals(product.getProductCode())) {
                    products.set(i, product);
                    break;
                }
            }
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(PRODUCTS_JSON_PATH), products);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
