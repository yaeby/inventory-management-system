package repository;

import builder.GenericBuilder;
import database.ConnectionFactory;
import model.Order;
import model.OrderItem;
import service.CustomerService;
import service.OrderItemService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderRepository implements IRepository<Order, Long> {

    private final Connection connection;
    private final CustomerService customerService;
    private final OrderItemService orderItemService;

    public OrderRepository() {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        connection = connectionFactory.getConnection();
        customerService = new CustomerService(new CustomerRepository());
        orderItemService = new OrderItemService(new OrderItemRepository());
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
        Order order = GenericBuilder.of(Order::new)
                .with(Order::setId, resultSet.getLong("id"))
                .with(Order::setOrderNumber, resultSet.getString("order_number"))
                .with(Order::setCustomer, customerService.findById(resultSet.getLong("customer_id")))
                .build();

        List<OrderItem> orderItems = orderItemService.findAll()
                .stream()
                .filter(orderItem -> order.getId().equals(orderItem.getOrderId()))
                .collect(Collectors.toList());
        order.setOrderItems(orderItems);
        return order;
    }

    @Override
    public void add(Order order) {
        String query = "INSERT INTO orders (order_number, customer_id) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, order.getOrderNumber());
            preparedStatement.setLong(2, order.getCustomer().getId());
            preparedStatement.executeUpdate();
            order.getOrderItems().forEach(orderItem -> {
                orderItem.setOrderId(findByName(order.getOrderNumber()).getId());
                System.out.println(orderItem);
                orderItemService.add(orderItem);
            });
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Order order) {
        String query = "UPDATE orders SET order_number = ?, customer_id = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, order.getOrderNumber());
            preparedStatement.setLong(2, order.getCustomer().getId());
            preparedStatement.setLong(3, order.getId());
            preparedStatement.executeUpdate();
            order.getOrderItems().forEach(orderItemService::add);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Long id) {
        String query = "DELETE FROM orders WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            findById(id).getOrderItems().forEach(orderItem -> {orderItemService.delete(orderItem.getId());});

            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Order findByName(String name) {
        String query = "SELECT * FROM orders WHERE order_number = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return createOrder(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
