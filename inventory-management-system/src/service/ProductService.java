package service;

import model.Product;
import repository.IProductRepository;

import java.util.List;
import java.util.Scanner;

public class ProductService implements IProductService{

    private final IProductRepository productRepository;

    public ProductService(IProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductByCode(String productCode) {
        Product product = productRepository.findByProductCode(productCode);
        if(product == null) {
            System.out.println("No such product with code " + productCode);
            return null;
        }
        return product;
    }

    @Override
    public Product addProduct() {
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
        return productRepository.addProduct(product);
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
        Product product = productRepository.deleteProduct(productCode);
        if(product == null) {
            System.out.println("No such product with code " + productCode);
        } else {
            System.out.println("Product successfully deleted");
        }
    }
}
