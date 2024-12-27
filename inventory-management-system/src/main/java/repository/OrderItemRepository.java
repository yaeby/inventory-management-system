package repository;

import builder.GenericBuilder;
import database.ConnectionFactory;
import model.OrderItem;
import service.ProductService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderItemRepository implements IRepository<OrderItem, Long> {

    private final Connection connection;
    private final ProductService productService;

    public OrderItemRepository() {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        connection = connectionFactory.getConnection();
        productService = new ProductService(new ProductRepository());
    }

    @Override
    public List<OrderItem> findAll() {
        String query = "SELECT * FROM order_items";
        List<OrderItem> orderItems = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                orderItems.add(createOrderItem(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return orderItems;
    }

    @Override
    public OrderItem findById(Long id) {
        String query = "SELECT * FROM order_items WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return createOrderItem(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private OrderItem createOrderItem(ResultSet resultSet) throws SQLException {
        return GenericBuilder.of(OrderItem::new)
                .with(OrderItem::setId, resultSet.getLong("id"))
                .with(OrderItem::setQuantity, resultSet.getInt("quantity"))
                .with(OrderItem::setOrderId, resultSet.getLong("order_id"))
                .with(OrderItem::setProduct, productService.findById(resultSet.getLong("product_id")))
                .build();
    }

    @Override
    public void add(OrderItem orderItem) {
        String query = "INSERT INTO order_items (quantity, order_id, product_id) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, orderItem.getQuantity());
            preparedStatement.setLong(2, orderItem.getOrderId());
            preparedStatement.setLong(3, orderItem.getProduct().getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Long id) {
        String query = "DELETE FROM order_items WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(OrderItem orderItem) {
        String query = "UPDATE order_items SET quantity = ?, order_id = ?, product_id = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, orderItem.getQuantity());
            preparedStatement.setLong(2, orderItem.getOrderId());
            preparedStatement.setLong(3, orderItem.getProduct().getId());
            preparedStatement.setLong(4, orderItem.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public OrderItem findByName(String name) {
        throw new UnsupportedOperationException("findByName is not supported for OrderItem.");
    }
}
