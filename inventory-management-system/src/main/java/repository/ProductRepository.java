package repository;

import builder.GenericBuilder;
import database.ConnectionFactory;
import model.Product;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

public class ProductRepository implements IRepository<Product, Long> {

    private final Connection connection;

    public ProductRepository() {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        connection = connectionFactory.getConnection();
    }

    @Override
    public List<Product> findAll() {
        String query = "SELECT * FROM product";
        List<Product> products = new ArrayList<>();
        try (Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                products.add(createProduct(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return products;
    }

    @Override
    public Product findById(Long id) {
        String query = "SELECT * FROM product WHERE id = ?";
        try(PreparedStatement preparedStatement= connection.prepareStatement(query)){
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return createProduct(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private Product createProduct(ResultSet resultSet) throws SQLException {
        return GenericBuilder.of(Product::new)
                .with(Product::setId, resultSet.getLong("id"))
                .with(Product::setProductCode, resultSet.getString("product_code"))
                .with(Product::setProductName, resultSet.getString("product_name"))
                .with(Product::setBrand, resultSet.getString("brand"))
                .with(Product::setQuantity, resultSet.getInt("quantity"))
                .with(Product::setCostPrice, resultSet.getDouble("cost_price"))
                .with(Product::setSellPrice, resultSet.getDouble("sell_price"))
                .with(Product::setTotalCost, resultSet.getDouble("total_cost"))
                .with(Product::setTotalRevenue, resultSet.getDouble("total_revenue"))
                .build();
    }

    @Override
    public void add(Product product) {
        String query = "INSERT INTO product (product_code, product_name, brand, quantity, cost_price, sell_price, total_cost, total_revenue) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, product.getProductCode());
            preparedStatement.setString(2, product.getProductName());
            preparedStatement.setString(3, product.getBrand());
            preparedStatement.setInt(4, product.getQuantity());
            preparedStatement.setDouble(5, product.getCostPrice());
            preparedStatement.setDouble(6, product.getSellPrice());
            preparedStatement.setDouble(7, product.getTotalCost());
            preparedStatement.setDouble(8, product.getTotalRevenue());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Long id) {
        String query = "DELETE FROM product WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void update(Product product) {
        String query = "UPDATE product SET product_name = ?, brand = ?, quantity = ?, cost_price = ?, sell_price = ?, total_cost = ?, total_revenue = ? WHERE product_code = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, product.getProductName());
            preparedStatement.setString(2, product.getBrand());
            preparedStatement.setInt(3, product.getQuantity());
            preparedStatement.setDouble(4, product.getCostPrice());
            preparedStatement.setDouble(5, product.getSellPrice());
            preparedStatement.setDouble(6, product.getTotalCost());
            preparedStatement.setDouble(7, product.getTotalRevenue());
            preparedStatement.setString(8, product.getProductCode());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Product findByName(String username) {
        return null;
    }
}

