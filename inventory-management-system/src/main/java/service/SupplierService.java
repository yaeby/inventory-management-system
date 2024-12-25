package service;

import model.Supplier;
import repository.IRepository;

import java.util.List;

public class SupplierService extends Service<Supplier, Long>{

    public SupplierService(IRepository<Supplier, Long> repository) {
        super(repository);
    }

    @Override
    public List<Supplier> findAll() {
        return super.findAll();
    }

    @Override
    public Supplier findById(Long aLong) {
        return super.findById(aLong);
    }

    @Override
    public void add(Supplier entity) {
        super.add(entity);
    }

    @Override
    public void update(Supplier entity) {
        super.update(entity);
    }

    @Override
    public void delete(Long aLong) {
        super.delete(aLong);
    }
}
