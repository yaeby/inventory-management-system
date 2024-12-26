package commands.delivery;

import commands.Command;
import decorator.concrete.ExpressDeliveryDecorator;
import decorator.concrete.InsuranceDecorator;
import model.order.IOrder;
import model.order.Order;
import model.Product;
import repository.ProductRepository;
import service.DeliveryService;
import service.ProductService;

public class ProcessSpecialOrders implements Command {
    private final ProductService productService;
    private final DeliveryService deliveryService;

    public ProcessSpecialOrders() {
        productService = new ProductService(new ProductRepository());
        this.deliveryService = new DeliveryService();
    }

    @Override
    public void execute() {
        Product prod1 = productService.findById(1L);
        Product prod2 = productService.findById(2L);

        IOrder expressOrder = new ExpressDeliveryDecorator(new Order("ORD-003"));
        expressOrder.addProduct(prod1, 1);

        IOrder insuredOrder = new InsuranceDecorator(new Order("ORD-004"));
        insuredOrder.addProduct(prod1, 2);

        IOrder complexOrder = new InsuranceDecorator(
                new ExpressDeliveryDecorator(new Order("ORD-005"))
        );
        complexOrder.addProduct(prod1, 1);
        complexOrder.addProduct(prod2, 1);

        deliveryService.processOrder(expressOrder);
        deliveryService.processOrder(insuredOrder);
        deliveryService.processOrder(complexOrder);
    }
}
