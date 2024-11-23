package menu;

public class Printer {

    public Printer(){
        System.out.println("\n\tWELCOME TO THE WAREHOUSE MANAGER");
    }

    public void printCommand(String category){
        switch (category){
            case "product", "user":
                printCrudCommands();
                break;
            case "delivery":
                printDeliveryCommands();
                break;
        }
    }

    public void printDeliveryCommands(){
        System.out.println("""
               
                Select a command:
                1. process orders
                2. process special orders
               """);
    }

    public void printCrudCommands(){
        System.out.println("""
               
                Select a command:
                1. get all
                2. get specific
                3. add
                4. update
                5. delete
                b. back
               """);
    }

    public void printMenu(){
        System.out.println("""
                
                Select a command:
                product - product commands
                user - user commands
                delivery - delivery commands
                0 - exit
                """);
    }

    public void printExitMessage() {
        System.out.println("Exiting... Thank you for using the Warehouse Manager!");
    }

    public void printInvalidCategory() {
        System.out.println("Invalid command category. Please select a valid option.");
    }

    public void printInvalidCommand() {
        System.out.println("Invalid command type. Please select a valid command.");
    }
}
