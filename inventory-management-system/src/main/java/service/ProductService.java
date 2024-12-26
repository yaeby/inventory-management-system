package service;

import model.Product;
import repository.IRepository;
import java.util.List;

public class ProductService extends Service<Product, Long>{

    public ProductService(IRepository<Product, Long> repository) {
        super(repository);
    }

    @Override
    public List<Product> findAll(){
        return repository.findAll();
    }

    @Override
    public Product findById(Long id){
        return repository.findById(id);
    }

    @Override
    public void add(Product product) {
        product.setTotalCost(product.getQuantity() * product.getSellPrice());
        product.setTotalRevenue((product.getQuantity() * product.getSellPrice()) - product.getTotalCost());
        repository.add(product);
    }

    @Override
    public void update(Product entity) {
        super.update(entity);
    }

    @Override
    public void delete(Long id) {
        super.delete(id);
    }
}
