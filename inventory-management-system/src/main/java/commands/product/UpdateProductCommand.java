package commands.product;

import commands.Command;
import model.Product;
import service.ProductService;

public class UpdateProductCommand implements Command {

    private final ProductService productService;
    private final Product product;

    public UpdateProductCommand(ProductService productService, Product product) {
        this.productService = productService;
        this.product = product;
    }

    @Override
    public void execute() {
        productService.update(product);
    }
}
