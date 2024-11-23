package service;

import exceptions.ResourceNotFoundException;
import model.Product;
import repository.IRepository;
import java.util.List;
import java.util.Scanner;

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
        Product product = repository.findById(id);
        if (product == null) {
            throw new ResourceNotFoundException("Product with id: " + id + " not found");
        }
        return product;
    }

    @Override
    public void add(Product product) {
        product.setTotalCost(product.getQuantity() * product.getSellPrice());
        product.setTotalRevenue((product.getQuantity() * product.getSellPrice()) - product.getTotalCost());
        repository.add(product);
    }

    @Override
    public void update(Product product) {
        repository.update(product);
    }

    @Override
    public void delete(Long id) {
        repository.delete(id);
        System.out.println("Product deleted successfully");
    }
}
