package service;

import model.Order;
import repository.IRepository;

import java.util.List;

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
}
