package commands.product;

import commands.Command;
import service.IProductService;
import service.ProductService;

import java.util.Scanner;

public class DeleteProductCommand implements Command {

    @Override
    public void execute() {
        IProductService productService = ProductService.getInstance();
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the product code: ");
        String productCode = scanner.nextLine();
        productService.deleteProduct(productCode);
    }
}
