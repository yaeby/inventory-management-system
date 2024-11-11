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
    public void add() {
        Scanner scanner = new Scanner(System.in);
        Product product = new Product();
        System.out.print("Enter the product code: ");
        product.setProductCode(scanner.nextLine());
        System.out.print("Enter the product name: ");
        product.setProductName(scanner.nextLine());
        System.out.print("Enter the quantity: ");
        product.setQuantity(Integer.parseInt(scanner.nextLine()));
        System.out.print("Enter the product brand: ");
        product.setBrand(scanner.nextLine());
        System.out.print("Enter the product price: ");
        product.setCostPrice(Double.parseDouble(scanner.nextLine()));
        System.out.print("Enter the sell price: ");
        product.setSellPrice(Double.parseDouble(scanner.nextLine()));
        product.setTotalCost(product.getCostPrice() * product.getQuantity());
        product.setTotalRevenue(product.getSellPrice() * product.getQuantity() - product.getTotalCost());
        repository.add(product);
        System.out.println("Product added successfully");
    }

    @Override
    public void update(Long id) {
        Product product = findById(id);
        if(product != null){
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter the product name: ");
            String productName = scanner.nextLine();
            if(!productName.isEmpty()){
                product.setProductName(productName);
            }

            System.out.print("Enter the quantity: ");
            String quantityString = scanner.nextLine();
            if(!quantityString.isEmpty()){
                product.setQuantity(Integer.parseInt(quantityString));
            }

            System.out.print("Enter the product brand: ");
            String brand = scanner.nextLine();
            if(!brand.isEmpty()){
                product.setBrand(brand);
            }

            System.out.print("Enter the product price: ");
            String priceString = scanner.nextLine();
            if(!priceString.isEmpty()){
                product.setCostPrice(Double.parseDouble(priceString));
            }

            System.out.print("Enter the sell price: ");
            String sellPriceString = scanner.nextLine();
            if(!sellPriceString.isEmpty()){
                product.setSellPrice(Double.parseDouble(sellPriceString));
            }
            product.setTotalCost(product.getCostPrice() * product.getQuantity());
            product.setTotalRevenue(product.getSellPrice() * product.getQuantity() - product.getTotalCost());

            repository.update(product);
            System.out.println("Product updated successfully");
        }
    }

    @Override
    public void delete(Long id) {
        repository.delete(id);
        System.out.println("Product deleted successfully");
    }
}
