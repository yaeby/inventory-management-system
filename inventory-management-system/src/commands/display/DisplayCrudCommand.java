package commands.display;

import commands.Command;
import menu.Printer;

public class DisplayCrudCommand implements Command {
    @Override
    public void execute() {
        Printer printer = new Printer();
        printer.printCrudCommands();
    }
}
