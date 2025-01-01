package service;

import model.Order;
import repository.IRepository;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class OrderService extends Service<Order, Long>{

    public OrderService(IRepository<Order, Long> repository) {
        super(repository);
    }

    @Override
    public List<Order> findAll() {
        return super.findAll();
    }

    @Override
    public Order findById(Long aLong) {
        return super.findById(aLong);
    }

    @Override
    public void add(Order entity) {
        super.add(entity);
    }

    @Override
    public void update(Order entity) {
        super.update(entity);
    }

    @Override
    public void delete(Long aLong) {
        super.delete(aLong);
    }

    @Override
    public int getTotalCount() {
        return super.getTotalCount();
    }

    public double getTotalRevenue() {
        return findAll().stream()
                .mapToDouble(order ->
                        order.getProduct().getSellPrice() * order.getQuantity())
                .sum();
    }

    public List<Order> getRecentOrders(int limit) {
        return findAll().stream()
                .sorted(Comparator.comparing(Order::getOrderDate).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    public Map<String, Double> getMonthlySales() {
        Map<String, Double> monthlySales = new TreeMap<>();
        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMMM");

        findAll().stream()
                .collect(Collectors.groupingBy(
                        order -> order.getOrderDate().format(monthFormatter),
                        Collectors.summingDouble(order ->
                                order.getProduct().getSellPrice() * order.getQuantity())
                ))
                .forEach(monthlySales   ::put);

        return monthlySales;
    }
}
