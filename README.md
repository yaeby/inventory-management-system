<div align="center">

# Behavioral Design Patterns
</div>

## Author: Copta Adrian | FAF-223

----

## Objectives:

* Study and understand the Behavioral Design Patterns.
* As a continuation of the previous laboratory work, think about what communication between software entities might be involved in your system.
* Implement some additional functionalities using behavioral design patterns.

## Used Design Patterns:

* Chain of Responsibility 
* Command
* Template

----

## Implementation
Continuing working on our inventory manager, we migrated from a console app to a
javafx gui, which gave us the opportunity tu visualize better the inventory of
our warehouse and also to implement new features and try out new design patterns.

1. ### Chain of Responsibility
First thing that you have to do when using this app is to log in, and to validate
the user we implemented the chain of responsibility dp, which handles every user's
input.
  
First we created our base handler, which is `LoginHandler`:
```java
public abstract class LoginHandler {
    private LoginHandler next;
    private static User user;

    public void setNext(LoginHandler next) {
        this.next = next;
    }

    public abstract LoginResult handle(String username, String password);

    protected LoginResult handleNext(String username, String password) {
        if(next == null) {
            return new LoginResult(true, null);
        }
        return next.handle(username, password);
    }

    public User getUser() {
        return user;
    }

    protected void setUser(User user) {
        LoginHandler.user = user;
    }
}
```
Using this class we will be able to set different levels of validations for the user.
And for the login task we will set just to validate the Username and Password.  
A handler to check if the Username is registered:
```java
public class UserExistsHandler extends LoginHandler{
    private final UserService userService;

    public UserExistsHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public LoginResult handle(String username, String password) {
        User user = userService.findByName(username);
        if(user == null) {
            return new LoginResult(false, "User does not exist");
        }
        setUser(user);
        return handleNext(username, password);
    }
}

```
And one to check if the Password matched the Password written by the user:
```java
public class ValidPasswordHandler extends LoginHandler {

    @Override
    public LoginResult handle(String username, String password) {
        User user = getUser();
        if (user != null) {
            if (!password.equals(user.getPassword())) {
                return new LoginResult(false, "Password is incorrect");
            }
            return handleNext(username, password);
        }
        return new LoginResult(false, "Unexpected error: User not found");
    }
}
```
Now, we set in our `LoginController`, which is the controller for the `login.fxml`,
the levels of verification:
```java
@FXML
private void handleLogin() {
    String username = usernameField.getText();
    String password = passwordField.getText();

    LoginHandler handler = new UserExistsHandler(userService);
    handler.setNext(new ValidPasswordHandler());

    try {
        LoginResult result = handler.handle(username, password);
        if (result.success()) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/base.fxml"));
            Parent root = loader.load();

            BaseController baseController = loader.getController();
            baseController.setUser(handler.getUser());

            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
        } else {
            errorLabel.setText(result.message());
        }
    } catch (IOException e) {
        errorLabel.setText("Error loading dashboard");
        e.printStackTrace();
    }
}
```
Thus, in the future we will be able to set different levels of verification for different tasks,
for example adding a step to handle the rol of the user of something else.

2. ### Command
Previously, we implemented an abstract factory dp which was creating different `CommandFactoryes`
which on their move were creating `Command`s on a specific request of the user.
But now we use a javafx gui, and we will just modify our implementation to fit with the javafx buttons.
We have our `Command` interface:
```java
public interface Command {
    void execute();
}
```
Now, we modify our specific commands:
```java
public class AddProductCommand implements Command {

    private final ProductService productService;
    private final Product product;

    public AddProductCommand(ProductService productService, Product product) {
        this.productService = productService;
        this.product = product;
    }

    @Override
    public void execute() {
        productService.add(product);
    }
}
```
```java
public class DeleteProductCommand implements Command {

    private final ProductService productService;
    private final Product deletedProduct;

    public DeleteProductCommand(ProductService productService, Product product) {
        this.productService = productService;
        this.deletedProduct = product;
    }

    @Override
    public void execute() {
        productService.delete(deletedProduct.getId());
    }
}
```
```java
public class UpdateProductCommand implements Command {

    private final ProductService productService;
    private final Product product;

    public UpdateProductCommand(ProductService productService, Product product) {
        this.productService = productService;
        this.product = product;
    }

    @Override
    public void execute() {
        productService.update(product);
    }
}
```
Now when a user wants to (for example) add/edit/delete a specific products, a specific command will be executed.
For exampe `DeleProduct` Command:
```java
private void handleDeleteProduct(Product product) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Product");
        alert.setHeaderText("Delete Product");
        alert.setContentText("Are you sure you want to delete " + product.getProductName() + "?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                Command command = new DeleteProductCommand(productService, product);
                command.execute();
                loadProducts();
            } catch (Exception e) {
                showError("Error deleting product", e.getMessage());
            }
        }
    }
```

3. ### Template
Making changes in our project, we changed the child classes of the `Service` class
to be more dry, and we were able to do it by following the Template Design Pattern:
This is our `Service` which is part of the Bridge Design Pattern:
```java
public abstract class Service<T, ID> {
    protected IRepository<T, ID> repository;

    public Service(IRepository<T, ID> repository) {
        this.repository = repository;
    }

    public List<T> findAll() {
        return repository.findAll();
    }

    public T findById(ID id) {
        return repository.findById(id);
    }

    public void add(T entity) {
        repository.add(entity);
    }

    public void update(T entity) {
        repository.update(entity);
    }

    public void delete(ID id) {
        repository.delete(id);
    }
}
```
This class not only serves as a Bridge, but also as a skeleton for our concrete 
services: `ProductService` and `UserService`
```java
public class ProductService extends Service<Product, Long>{

    public ProductService(IRepository<Product, Long> repository) {
        super(repository);
    }

    @Override
    public List<Product> findAll(){
        return repository.findAll();
    }

    @Override
    public Product findById(Long id){
        return repository.findById(id);
    }

    @Override
    public void add(Product product) {
        product.setTotalCost(product.getQuantity() * product.getSellPrice());
        product.setTotalRevenue((product.getQuantity() * product.getSellPrice()) - product.getTotalCost());
        repository.add(product);
    }

    @Override
    public void update(Product entity) {
        super.update(entity);
    }

    @Override
    public void delete(Long id) {
        super.delete(id);
    }
}
```
In the future we can create Services for Orders or other business logic 
and use `Service` skeleton as the template for our new classes.

-----

## Conclusions / Screenshots / Results
In conclusion, the task of this laboratory work was successfully covered, 
implementing new features and new design patterns.
From the beginning till now the project had survived a lot of modifications and new features   
becoming more and more robust, dry and flexible.

<figure>
  <img src="../assets/img_3.png" alt="Result Login">
  <figcaption><small>Login</small></figcaption>
</figure>

<figure>
  <img src="../assets/img_4.png" alt="Result Products">
  <figcaption><small>Products</small></figcaption>
</figure>

----