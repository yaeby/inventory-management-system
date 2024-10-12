package menu;

public class Printer {

    public Printer(){
        System.out.println("\n\tWELCOME TO THE WAREHOUSE MANAGER");
    }

    public void printMenu(){
        System.out.println("""
               
                Select a command:
                1. get all products
                2. get product
                3. add product
                4. update product
                5. delete product
                0. exit
               """);
    }
}
