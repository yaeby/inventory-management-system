package commands.product;

import commands.Command;
import model.Product;
import service.ProductService;


public class DeleteProductCommand implements Command {

    private final ProductService productService;
    private final Product deletedProduct;

    public DeleteProductCommand(ProductService productService, Product product) {
        this.productService = productService;
        this.deletedProduct = product;
    }

    @Override
    public void execute() {
        productService.delete(deletedProduct.getId());
    }
}
