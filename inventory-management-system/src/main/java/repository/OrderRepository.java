package repository;

import builder.GenericBuilder;
import database.ConnectionFactory;
import model.Order;
import service.CustomerService;
import service.ProductService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderRepository implements IRepository<Order, Long> {

    private final Connection connection;
    private final CustomerService customerService;
    private final ProductService productService;

    public OrderRepository() {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        connection = connectionFactory.getConnection();
        customerService = new CustomerService(new CustomerRepository());
        productService = new ProductService(new ProductRepository());
    }

    @Override
    public List<Order> findAll() {
        String query = "SELECT * FROM orders";
        List<Order> orders = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                orders.add(createOrder(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return orders;
    }

    @Override
    public Order findById(Long id) {
        String query = "SELECT * FROM orders WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return createOrder(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private Order createOrder(ResultSet resultSet) throws SQLException {
        return GenericBuilder.of(Order::new)
                .with(Order::setId, resultSet.getLong("id"))
                .with(Order::setCustomer, customerService.findById(resultSet.getLong("customer_id")))
                .with(Order::setProduct, productService.findById(resultSet.getLong("product_id")))
                .with(Order::setQuantity, resultSet.getInt("quantity"))
                .with(Order::setOrderDate, resultSet.getTimestamp("order_date").toLocalDateTime())
                .build();
    }

    @Override
    public void add(Order order) {
        String query = "INSERT INTO orders (customer_id, product_id, quantity, order_date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, order.getCustomer().getId());
            preparedStatement.setLong(2, order.getProduct().getId());
            preparedStatement.setInt(3, order.getQuantity());
            preparedStatement.setTimestamp(4, Timestamp.valueOf(order.getOrderDate()));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Order order) {
        String query = "UPDATE orders SET customer_id = ?, product_id = ?, quantity = ?, order_date = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, order.getCustomer().getId());
            preparedStatement.setLong(2, order.getProduct().getId());
            preparedStatement.setInt(3, order.getQuantity());
            preparedStatement.setTimestamp(4, Timestamp.valueOf(order.getOrderDate()));
            preparedStatement.setLong(5, order.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Long id) {
        String query = "DELETE FROM orders WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Order findByName(String productName) {
        String query = "SELECT * FROM orders WHERE product_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setLong(1, productService.findByName(productName).getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return createOrder(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public int getTotalCount() {
        String query = "SELECT COUNT(*) AS total FROM orders";
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
