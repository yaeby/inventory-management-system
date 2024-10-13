package commands;

import menu.Printer;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CommandExecutor {
    private final Map<String, Command> commandMap;
    private final Scanner scanner;
    private final Printer printer;

    public CommandExecutor() {
        commandMap = new HashMap<>();
        scanner = new Scanner(System.in);
        printer = new Printer();
    }

    public void register(String commandKey, Command command) {
        commandMap.put(commandKey, command);
    }

    public void run() {
        boolean quit = false;
        while (!quit) {
            printer.printMenu();
            System.out.print("> ");
            String input = scanner.nextLine();
            if (input.equals("0") || input.equalsIgnoreCase("exit")) {
                quit = true;
                scanner.close();
            } else {
                Command command = commandMap.get(input);
                if (command != null) {
                    command.execute();
                } else {
                    System.out.println("Invalid input: " + input);
                }
            }
        }
    }
}
