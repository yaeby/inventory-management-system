package commands.product;

import commands.Command;
import model.Product;
import exceptions.ResourceNotFoundException;
import service.IProductService;
import service.ProductService;

import java.util.Scanner;

public class GetProductByCodeCommand implements Command {

    @Override
    public void execute() {
        IProductService productService = ProductService.getInstance();
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the product code: ");
        String productCode = scanner.nextLine();
        try {
            Product product = productService.getProductByCode(productCode);
            System.out.println(product);
        } catch (ResourceNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }

}
