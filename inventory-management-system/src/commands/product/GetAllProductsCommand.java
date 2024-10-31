package commands.product;

import commands.Command;
import model.Product;
import service.IProductService;
import service.ProductService;

import java.sql.SQLException;
import java.util.List;

public class GetAllProductsCommand implements Command {

    @Override
    public void execute() {
        IProductService productService = ProductService.getInstance();
        try {
            List<Product> products = productService.getAllProducts();
            for(Product product : products){
                System.out.println(product.toString());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
