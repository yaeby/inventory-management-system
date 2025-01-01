package service;

import model.Product;
import repository.IRepository;
import repository.ProductRepository;

import java.util.List;

public class ProductService extends Service<Product, Long>{

    public final ProductRepository repository;

    public ProductService(ProductRepository productRepository) {
        super(productRepository);
        this.repository = productRepository;
    }

    @Override
    public List<Product> findAll(){
        return super.findAll();
    }

    @Override
    public Product findById(Long id){
        return super.findById(id);
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

    @Override
    public int getTotalCount() {
        return super.getTotalCount();
    }

    public int getTotalCountByCategory(Long categoryId){
        return repository.getTotalCountByCategory(categoryId);
    }

    public int getLowStockCount(){
        return repository.getLowStockCount();
    }
}
