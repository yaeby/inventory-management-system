import commands.*;
import repository.IProductRepository;
import repository.ProductRepository;
import service.IProductService;
import service.ProductService;

public class Main {

    public static void main(String[] args) {
        IProductRepository productRepository = new ProductRepository();
        IProductService productService = new ProductService(productRepository);
        CommandExecutor executor = new CommandExecutor();

        executor.register("1", new GetAllProductsCommand(productService));
        executor.register("2", new GetProductByCodeCommand(productService));
        executor.register("3", new AddProductCommand(productService));
        executor.register("4", new UpdateProductCommand(productService));
        executor.register("5", new DeleteProductCommand(productService));

        executor.run();
    }
}
