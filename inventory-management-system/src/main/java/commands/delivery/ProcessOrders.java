package commands.delivery;

import commands.Command;
import model.order.CompositeOrder;
import model.order.Order;
import model.Product;
import repository.ProductRepository;
import service.DeliveryService;
import service.ProductService;


public class ProcessOrders implements Command {
    private final ProductService productService;
    private final DeliveryService deliveryService;

    public ProcessOrders() {
        productService = new ProductService(new ProductRepository());
        this.deliveryService = new DeliveryService();
    }

    @Override
    public void execute() {
        Product prod1 = productService.findById(1L);
        Product prod2 = productService.findById(2L);

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
