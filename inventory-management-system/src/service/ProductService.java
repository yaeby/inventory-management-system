package service;

import exceptions.ResourceNotFoundException;
import model.Product;
import repository.IProductRepository;
import repository.ProductRepository;

import java.util.List;
import java.util.Scanner;

public class ProductService implements IProductService{

    private static volatile ProductService instance;
    private final IProductRepository productRepository;

    private ProductService() {
        this.productRepository = ProductRepository.getInstance();
    }

    public static ProductService getInstance() {
        ProductService result = instance;
        if (result == null) {
            synchronized (ProductService.class) {
                result = instance;
                if (result == null) {
                    instance = result = new ProductService();
                }
            }
        }
        return result;
    }

    @Override
    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    @Override
    public Product getProductByCode(String productCode) {
        Product product = productRepository.findByProductCode(productCode);
        if (product == null) {
            throw new ResourceNotFoundException("Product with code " + productCode + " not found.");
        }
        return product;
    }


    @Override
    public void addProduct() {
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
        productRepository.addProduct(product);
        System.out.println("Product added successfully");
        scanner.close();
    }

    @Override
    public void updateProduct(String productCode) {
        Product product = getProductByCode(productCode);
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

            productRepository.updateProduct(product);
            System.out.println("Product updated successfully");
        }
    }

    @Override
    public void deleteProduct(String productCode) {
        productRepository.deleteProduct(productCode);
        System.out.println("Product deleted successfully");
    }
}
