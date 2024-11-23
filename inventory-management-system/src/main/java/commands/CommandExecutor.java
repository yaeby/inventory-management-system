package commands;

import factory.CommandFactory;
import factory.DeliveryFactory;
import factory.ProductCommandFactory;
import factory.UserCommandFactory;
import menu.Printer;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CommandExecutor {

    private final Map<String, CommandFactory> factories = new HashMap<>();
    private final Printer printer = new Printer();

    public CommandExecutor() {
        factories.put("product", new ProductCommandFactory());
        factories.put("user", new UserCommandFactory());
        factories.put("delivery", new DeliveryFactory());
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            printer.printMenu();
            System.out.print("> ");
            String category = scanner.nextLine().toLowerCase();

            if (category.equals("0") || category.equals("exit")) {
                printer.printExitMessage();
                break;
            }

            CommandFactory factory = factories.get(category);
            if (factory == null) {
                printer.printInvalidCategory();
                continue;
            }

            printer.printCommand(category);

            System.out.print("> ");
            String commandType = scanner.nextLine();

            if (commandType.equalsIgnoreCase("b")) {
                continue;
            }

            Command command = factory.createCommand(commandType);
            if (command != null) {
                command.execute();
            } else {
                printer.printInvalidCommand();
            }
        }
    }
}
