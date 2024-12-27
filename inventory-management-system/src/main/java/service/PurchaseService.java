package service;

import model.Purchase;
import repository.IRepository;

import java.util.List;

public class PurchaseService extends Service<Purchase, Long>{

    public PurchaseService(IRepository<Purchase, Long> repository) {
        super(repository);
    }

    @Override
    public List<Purchase> findAll() {
        return super.findAll();
    }

    @Override
    public Purchase findById(Long aLong) {
        return super.findById(aLong);
    }

    @Override
    public Purchase findByName(String name) {
        return super.findByName(name);
    }

    @Override
    public void add(Purchase entity) {
        super.add(entity);
    }

    @Override
    public void update(Purchase entity) {
        super.update(entity);
    }

    @Override
    public void delete(Long aLong) {
        super.delete(aLong);
    }
}
