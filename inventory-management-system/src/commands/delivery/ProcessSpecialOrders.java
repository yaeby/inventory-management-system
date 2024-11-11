package commands.delivery;

import commands.Command;
import decorator.concrete.ExpressDeliveryDecorator;
import decorator.concrete.InsuranceDecorator;
import model.IOrder;
import model.Order;
import model.Product;
import service.DeliveryService;
import service.IProductService;
import service.ProductService;

public class ProcessSpecialOrders implements Command {

    private final IProductService productService;
    private final DeliveryService deliveryService;

    public ProcessSpecialOrders() {
        this.productService = ProductService.getInstance();
        this.deliveryService = new DeliveryService();
    }

    @Override
    public void execute() {
        Product prod1 = productService.getProductByCode("PRD001");
        Product prod2 = productService.getProductByCode("PRD002");
//        Product prod2 = new Product();
//        prod2.setProductName("Phone case");
//        prod2.setSellPrice(15.99);

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
