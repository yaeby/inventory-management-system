package service;

import model.OrderItem;
import repository.IRepository;

import java.util.List;

public class OrderItemService extends Service<OrderItem, Long> {

    public OrderItemService(IRepository<OrderItem, Long> repository) {
        super(repository);
    }

    @Override
    public List<OrderItem> findAll() {
        return super.findAll();
    }

    @Override
    public OrderItem findById(Long aLong) {
        return super.findById(aLong);
    }

    @Override
    public void add(OrderItem entity) {
        super.add(entity);
    }

    @Override
    public void update(OrderItem entity) {
        super.update(entity);
    }

    @Override
    public void delete(Long aLong) {
        super.delete(aLong);
    }
}
