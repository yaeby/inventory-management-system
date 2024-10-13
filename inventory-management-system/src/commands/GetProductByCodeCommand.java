package commands;

import model.Product;
import service.IProductService;

import java.util.Scanner;

public class GetProductByCodeCommand implements Command {
    private final IProductService productService;
    private final Scanner scanner = new Scanner(System.in);

    public GetProductByCodeCommand(IProductService productService) {
        this.productService = productService;
    }
    @Override
    public void execute() {
        System.out.print("Enter the product code: ");
        String productCode = scanner.nextLine();
        Product product = productService.getProductByCode(productCode);
        if(product != null) {
            System.out.println(product);
        } else {
            System.out.println("Product not found.");
        }
    }
}
