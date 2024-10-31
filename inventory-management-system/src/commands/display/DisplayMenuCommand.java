package commands.display;

import commands.Command;
import menu.Printer;

public class DisplayMenuCommand implements Command {
    @Override
    public void execute() {
        Printer printer = new Printer();
        printer.printMenu();
    }
}
