package commands.product;

import commands.Command;
import repository.ProductRepository;
import service.ProductService;

public class AddProductCommand implements Command {

    @Override
    public void execute() {
        ProductService productService = new ProductService(new ProductRepository());
        productService.add();
    }
}
