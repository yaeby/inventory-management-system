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
        Product product = new Product();
        System.out.print("Enter the product code: ");
        product.setProductCode(scanner.nextLine());
        System.out.print("Enter the product name: ");
        product.setProductName(scanner.nextLine());
        System.out.print("Enter the quantity: ");
        product.setQuantity(Integer.parseInt(scanner.nextLine()));
        System.out.print("Enter the product brand: ");
        product.setBrand(scanner.nextLine());
        System.out.print("Enter the product price: ");
        product.setCostPrice(Double.parseDouble(scanner.nextLine()));
        System.out.print("Enter the sell price: ");
        product.setSellPrice(Double.parseDouble(scanner.nextLine()));
        product.setTotalCost(product.getCostPrice() * product.getQuantity());
        product.setTotalRevenue(product.getSellPrice() * product.getQuantity() - product.getTotalCost());
        productService.addProduct(product);
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