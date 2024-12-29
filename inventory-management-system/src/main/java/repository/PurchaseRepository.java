package repository;

import builder.GenericBuilder;
import database.ConnectionFactory;
import model.Purchase;
import service.ProductService;
import service.SupplierService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PurchaseRepository implements IRepository<Purchase, Long> {

    private final Connection connection;
    private final ProductService productService;
    private final SupplierService supplierService;

    public PurchaseRepository() {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        connection = connectionFactory.getConnection();
        productService = new ProductService(new ProductRepository());
        supplierService = new SupplierService(new SupplierRepository());
    }

    @Override
    public List<Purchase> findAll() {
        String query = "SELECT * FROM purchase";
        List<Purchase> purchases = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)){
            while(resultSet.next()) {
                purchases.add(createPurchase(resultSet));
            }
            return purchases;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Purchase createPurchase(ResultSet resultSet) throws SQLException {
        return GenericBuilder.of(Purchase::new)
                .with(Purchase::setId, resultSet.getLong("id"))
                .with(Purchase::setSupplier, supplierService.findById(resultSet.getLong("supplier_id")))
                .with(Purchase::setProduct, productService.findById(resultSet.getLong("product_id")))
                .with(Purchase::setQuantity, resultSet.getInt("quantity"))
                .with(Purchase::setPurchaseDate, resultSet.getTimestamp("purchase_date").toLocalDateTime())
                .build();
    }

    @Override
    public Purchase findById(Long id) {
        String query = "SELECT * FROM purchase WHERE id = ?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                return createPurchase(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void add(Purchase purchase) {
        String query = "INSERT INTO purchase (supplier_id, product_id, quantity, purchase_date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, purchase.getSupplier().getId());
            preparedStatement.setLong(2, purchase.getProduct().getId());
            preparedStatement.setInt(3, purchase.getQuantity());
            preparedStatement.setTimestamp(4, Timestamp.valueOf(purchase.getPurchaseDate()));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Purchase purchase) {
        String query = "UPDATE purchase SET supplier_id = ?, product_id = ?, quantity = ?, purchase_date = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, purchase.getSupplier().getId());
            preparedStatement.setLong(2, purchase.getProduct().getId());
            preparedStatement.setInt(3, purchase.getQuantity());
            preparedStatement.setTimestamp(4, Timestamp.valueOf(purchase.getPurchaseDate()));
            preparedStatement.setLong(5, purchase.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Long id) {
        String query = "DELETE FROM purchase WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Purchase findByName(String productName) {
        String query = "SELECT * FROM purchase WHERE id = ?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setLong(1, productService.findByName(productName).getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                return createPurchase(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public int getTotalCount() {
        String query = "SELECT COUNT(*) AS total FROM purchase";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            if (resultSet.next()) {
                return resultSet.getInt("total");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }
}
