package service;

import model.Customer;
import repository.IRepository;

import java.util.List;

public class CustomerService extends Service<Customer, Long>{

    public CustomerService(IRepository<Customer, Long> repository) {
        super(repository);
    }

    @Override
    public List<Customer> findAll() {
        return super.findAll();
    }

    @Override
    public Customer findById(Long aLong) {
        return super.findById(aLong);
    }

    @Override
    public void add(Customer entity) {
        super.add(entity);
    }

    @Override
    public void update(Customer entity) {
        super.update(entity);
    }

    @Override
    public void delete(Long aLong) {
        super.delete(aLong);
    }
}
