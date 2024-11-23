package commands.product;

import commands.Command;
import model.Product;
import exceptions.ResourceNotFoundException;
import repository.ProductRepository;
import service.ProductService;

import java.util.Scanner;

public class GetProductByIdCommand implements Command {

    @Override
    public void execute() {
        ProductService productService = new ProductService(new ProductRepository());
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the product ID: ");
        Long id = scanner.nextLong();
        try {
            Product product = productService.findById(id);
            System.out.println(product);
        } catch (ResourceNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }

}
