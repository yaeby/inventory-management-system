package factory;

import commands.Command;
import commands.delivery.Delivery;

public class DeliveryFactory implements CommandFactory{
    @Override
    public Command createCommand(String commandType) {
        return switch (commandType){
            case "1" -> new Delivery();
            default -> null;
        };
    }
}
