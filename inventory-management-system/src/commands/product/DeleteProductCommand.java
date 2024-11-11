package commands.product;

import commands.Command;
import repository.ProductRepository;
import service.ProductService;

import java.util.Scanner;

public class DeleteProductCommand implements Command {

    @Override
    public void execute() {
        ProductService productService = new ProductService(new ProductRepository());
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the product ID: ");
        Long id = scanner.nextLong();
        productService.delete(id);
    }
}
