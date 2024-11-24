package commands.product;

import commands.Command;
import model.Product;
import service.ProductService;

public class AddProductCommand implements Command {

    private final ProductService productService;
    private final Product product;

    public AddProductCommand(ProductService productService, Product product) {
        this.productService = productService;
        this.product = product;
    }

    @Override
    public void execute() {
        productService.add(product);
    }
}
