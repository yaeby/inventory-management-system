package factory;

import commands.Command;
import commands.product.*;

public class ProductCommandFactory implements CommandFactory {

    @Override
    public Command createCommand(String commandType) {
        return switch (commandType) {
            case "1" -> new GetAllProductsCommand();
            case "2" -> new GetProductByCodeCommand();
            case "3" -> new AddProductCommand();
            case "4" -> new UpdateProductCommand();
            case "5" -> new DeleteProductCommand();
            default -> null;
        };
    }
}
