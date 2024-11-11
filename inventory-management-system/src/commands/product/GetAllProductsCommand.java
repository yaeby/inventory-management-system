package commands.product;

import commands.Command;
import model.Product;
import repository.ProductRepository;
import service.IProductService;
import service.ProductService;

import java.sql.SQLException;
import java.util.List;

public class GetAllProductsCommand implements Command {

    @Override
    public void execute() {
        ProductService productService = new ProductService(new ProductRepository());
        List<Product> products = productService.findAll();
        for(Product product : products){
            System.out.println(product.toString());
        }
    }
}
