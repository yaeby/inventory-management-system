package commands.delivery;

import commands.Command;
import model.CompositeOrder;
import model.Order;
import model.Product;
import service.DeliveryService;


public class ProcessOrders implements Command {
//    private final IProductService productService;
    private final DeliveryService deliveryService;

    public ProcessOrders() {
//        this.productService = ProductService.getInstance();
        this.deliveryService = new DeliveryService();
    }

    @Override
    public void execute() {
//        Product prod1 = productService.getProductByCode("PRD001");
        Product prod1 = new Product();
        prod1.setProductName("Laptop");
        prod1.setSellPrice(999.99);

//        Product prod2 = productService.getProductByCode("PRD002");
        Product prod2 = new Product();
        prod2.setProductName("Mouse");
        prod2.setSellPrice(29.99);

        Order order1 = new Order("ORD-001");
        order1.addProduct(prod1, 1);
        order1.addProduct(prod2, 2);

        Order order2 = new Order("ORD-002");
        order2.addProduct(prod2, 3);

        CompositeOrder cityOrders = new CompositeOrder("NYC-001", "CITY");
        cityOrders.addOrder(order1);
        cityOrders.addOrder(order2);

        deliveryService.processOrder(order1);
        deliveryService.processOrder(order2);
        deliveryService.processOrder(cityOrders);
    }
}
