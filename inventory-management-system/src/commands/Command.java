package commands;

import menu.Printer;
import model.Product;
import service.IProductService;

import java.util.List;
import java.util.Scanner;

public class Command implements ICommand {
    private final IProductService productService;
    private final Scanner scanner;

    public Command(IProductService productService) {
        this.productService = productService;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void run(){
        boolean quit = true;
        String input;
        Printer printer = new Printer();

        while(quit){
            printer.printMenu();
            System.out.print("> ");
            input = scanner.nextLine();
            switch (input){
                case "1":
                    command1();
                    break;
                case "2":
                    command2();
                    break;
                case "3":
                    command3();
                    break;
                case "4":
                    command4();
                    break;
                case "5":
                    command5();
                    break;
                case "0", "exit":
                    scanner.close();
                    quit = false;
                    break;
                default:
                    System.out.println("Invalid input: " + input);
            }
        }
    }

    private void command1(){
        List<Product> products = productService.getAllProducts();

        for(Product product : products){
            System.out.println(product.toString());
        }
    }

    private void command2(){
        System.out.print("Enter the product code: ");
        String productCode = scanner.nextLine();
        Product product = productService.getProductByCode(productCode);
        if(product != null) {
            System.out.println(product);
        }
    }

    private void command3(){
        Product product = productService.addProduct();
        if(product != null) {
            System.out.println("Product added successfully");
        } else {
            System.out.println("Product not added");
        }
    }

    private void command4(){
        System.out.print("Enter the product code: ");
        String productCode = scanner.nextLine();
        productService.updateProduct(productCode);
    }

    private void command5(){
        System.out.print("Enter the product code: ");
        String productCode = scanner.nextLine();
        productService.deleteProduct(productCode);
    }
}
