package commands;

import model.Product;
import service.IProductService;

public class AddProductCommand implements Command {
    private final IProductService productService;

    public AddProductCommand(IProductService productService) {
        this.productService = productService;
    }
    @Override
    public void execute() {
        Product product = productService.addProduct();
        if(product != null) {
            System.out.println("Product added successfully");
        } else {
            System.out.println("Product not added");
        }
    }
}
