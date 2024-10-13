package commands;

import service.IProductService;

import java.util.Scanner;

public class DeleteProductCommand implements Command {
    private final IProductService productService;
    Scanner scanner = new Scanner(System.in);

    public DeleteProductCommand(IProductService productService) {
        this.productService = productService;
    }
    @Override
    public void execute() {
        System.out.print("Enter the product code: ");
        String productCode = scanner.nextLine();
        productService.deleteProduct(productCode);
    }
}
