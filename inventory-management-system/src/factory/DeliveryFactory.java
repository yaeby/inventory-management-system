package factory;

import commands.Command;
import commands.delivery.ProcessOrders;
import commands.delivery.ProcessSpecialOrders;

public class DeliveryFactory implements CommandFactory{
    @Override
    public Command createCommand(String commandType) {
        return switch (commandType){
            case "1" -> new ProcessOrders();
            case "2" -> new ProcessSpecialOrders();
            default -> null;
        };
    }
}
