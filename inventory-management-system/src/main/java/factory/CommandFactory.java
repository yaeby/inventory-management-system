package factory;

import commands.Command;

public interface CommandFactory {
    Command createCommand(String commandType);
}
