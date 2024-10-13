package commands;

import model.Product;
import service.IProductService;

import java.util.List;

public class GetAllProductsCommand implements Command {
    private final IProductService productService;

    public GetAllProductsCommand(IProductService productService) {
        this.productService = productService;
    }

    @Override
    public void execute() {
        List<Product> products = productService.getAllProducts();
        for(Product product : products){
            System.out.println(product.toString());
        }
    }
}
