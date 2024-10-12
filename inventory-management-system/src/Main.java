import commands.Command;
import commands.ICommand;
import repository.IProductRepository;
import repository.ProductRepository;
import service.IProductService;
import service.ProductService;

public class Main {

    public static void main(String[] args) {
        IProductRepository productRepository = new ProductRepository();
        IProductService productService = new ProductService(productRepository);
        ICommand command = new Command(productService);

        command.run();
    }
}
