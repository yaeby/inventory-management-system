package commands.product;

import commands.Command;
import service.IProductService;
import service.ProductService;

public class AddProductCommand implements Command {

    @Override
    public void execute() {
        IProductService productService = ProductService.getInstance();
        productService.addProduct();
    }
}
