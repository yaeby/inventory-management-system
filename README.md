<div align="center">

# Creational Design Patterns
</div>

## Author: Copta Adrian | FAF-223

----

## Objectives:

* Get familiar with the Creational DPs;
* Choose a specific domain;
* Implement at least 3 CDPs for the specific domain;

## Used Design Patterns:

* Singleton
* Generic Builder
* Abstract Factory

----

## Implementation
To cover the objectives of this laboratory work and understand what are creational design patterns and how they work, I continued working on the inventory-management-system from the previous laboratory work and developed this project using creational design patterns.

1. ### Singleton

I modified the way the project stores the data, now we use mySql, where we created a database for our project. So, once we have a database, we have to connect to it, and to have only one existing connection we use the Singleton Design Pattern.
```java
public class ConnectionFactory {
    private static final String driver = "com.mysql.cj.jdbc.Driver";
    private static final String url = "jdbc:mysql://localhost:3306/inventory_db";
    private static volatile ConnectionFactory instance;
    private final Connection connection;

    private ConnectionFactory(){
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, "your root", "your password");
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static ConnectionFactory getInstance(){
        ConnectionFactory result = instance;
        if(result == null){
            synchronized (ConnectionFactory.class){
                result = instance;
                if(result == null){
                    instance = result = new ConnectionFactory();
                }
            }
        }
        return result;
    }

    public Connection getConnection(){
        return connection;
    }
}
```
I used lazy initialization and double-checked locking system in case we will have multi-threads in future

2. ### Builder 
In my project we have some entities, and to create this objects step by step and at the same time generic ones, I implemented a generic design pattern.
```java
public class GenericBuilder<T> {
    private final Supplier<T> supplier;

    private GenericBuilder(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    public static <T> GenericBuilder<T> of(Supplier<T> supplier) {
        return new GenericBuilder<>(supplier);
    }

    public <P> GenericBuilder<T> with(BiConsumer<T, P> consumer, P value) {
        return new GenericBuilder<>(() -> {
            T object = supplier.get();
            consumer.accept(object, value);
            return object;
        });
    }

    public T build() {
        return supplier.get();
    }
}
```
And now, for example in our repository layer, when we read data from the database and want to create a specific product, we may use the builder, for example we have 2 methods where we have to create a product:
```java
@Override
public List<Product> findAll() {
    String query = "SELECT * FROM product";
    List<Product> products = new ArrayList<>();
    try (Statement statement = connection.createStatement();
         ResultSet resultSet = statement.executeQuery(query)) {
        while (resultSet.next()) {
            products.add(createProduct(resultSet));
        }
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
    return products;
}

@Override
public Product findByProductCode(String productCode) {
    String query = "SELECT * FROM product WHERE product_code = ?";
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
        preparedStatement.setString(1, productCode);
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                return createProduct(resultSet);
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
}
```
Here you can see that we extract products from database, and we use a method which serves as our director for creating a product:
```java
private Product createProduct(ResultSet resultSet) throws SQLException {
        return GenericBuilder.of(Product::new)
                .with(Product::setId, resultSet.getLong("id"))
                .with(Product::setProductCode, resultSet.getString("product_code"))
                .with(Product::setProductName, resultSet.getString("product_name"))
                .with(Product::setBrand, resultSet.getString("brand"))
                .with(Product::setQuantity, resultSet.getInt("quantity"))
                .with(Product::setCostPrice, resultSet.getDouble("cost_price"))
                .with(Product::setSellPrice, resultSet.getDouble("sell_price"))
                .with(Product::setTotalCost, resultSet.getDouble("total_cost"))
                .with(Product::setTotalRevenue, resultSet.getDouble("total_revenue"))
                .build();
    }
```
Thus, the Open/Close principle is maintained, and if we will want to have a new product, we will be able to create it also using this generic builder.

3. ### Abstract Factory

To run the operations of our project, we have specific command classes for each command, and all these classes implement the command interface, for example:
```java
public class DeleteProductCommand implements Command {

    @Override
    public void execute() {
        IProductService productService = ProductService.getInstance();
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the product code: ");
        String productCode = scanner.nextLine();
        productService.deleteProduct(productCode);
    }
}
```

```java
public class GetUserByIdCommand implements Command {

    @Override
    public void execute() {
        IUserService userService = UserService.getInstance();
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the user ID: ");
        Long userId = scanner.nextLong();
        try {
            User user = userService.getUserById(userId);
            System.out.println(user);
        } catch (ResourceNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }
}
```

So, each entity in our database has some crud operations, and to maintain the Open/Close Principle, we implement the abstract factory, because we do not know what command we will need. First we implement the abstract factory interface:
```java
public interface CommandFactory {
    Command createCommand(String commandType);
}
```
And now the concrete factories, for example product factory:
```java
public class ProductCommandFactory implements CommandFactory {

    @Override
    public Command createCommand(String commandType) {
        return switch (commandType) {
            case "1" -> new GetAllProductsCommand();
            case "2" -> new GetProductByCodeCommand();
            case "3" -> new AddProductCommand();
            case "4" -> new UpdateProductCommand();
            case "5" -> new DeleteProductCommand();
            default -> null;
        };
    }
}
```
And user factory:
```java
public class UserCommandFactory implements CommandFactory {

    @Override
    public Command createCommand(String commandType) {
        return switch (commandType) {
            case "1" -> new GetAllUsersCommand();
            case "2" -> new GetUserByIdCommand();
            case "3" -> new AddUserCommand();
            case "4" -> new UpdateUserCommand();
            case "5" -> new DeleteUserCommand();
            default -> null;
        };
    }
}
```
Now, we modified our command executor, to run using these factories. The command executor will have a hash map where we identify for what input what factory we use:
```java
public class CommandExecutor {

    private final Map<String, CommandFactory> factories = new HashMap<>();
    private final Printer printer = new Printer();

    public CommandExecutor() {
        factories.put("product", new ProductCommandFactory());
        factories.put("user", new UserCommandFactory());
    }
    //...
}
```
Thus, we will be able in the future to create as many commands as we want and we will just add their factories in the hashmap. Now, the run method will be short and abstract, obeying the OCP:
```java
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

            printer.printCrudCommands();
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
```
To run our warehouse manager we just call the run method of this class, and it will handle every input:
```java
public class Main {

    public static void main(String[] args) {
        CommandExecutor executor = new CommandExecutor();
        executor.run();
    }
}
```

-----

## Conclusions / Screenshots / Results
In conclusion, the task of this laboratory work was done successfully, implementing these creational design patterns, making our project more simple, abstract and robust. The creational design pattern gave the opportunity to develop vastly this project, maintaining the solid principle and its clarity.   

![img.png](inventory-management-system/assets/img.png)

----




