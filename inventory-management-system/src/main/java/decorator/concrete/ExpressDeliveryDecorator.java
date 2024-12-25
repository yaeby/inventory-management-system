package decorator.concrete;

import decorator.BaseOrderDecorator;
import model.order.IOrder;
import model.Product;

public class ExpressDeliveryDecorator extends BaseOrderDecorator {
    private static final double EXPRESS_FEE = 25.0;

    public ExpressDeliveryDecorator(IOrder order) {
        super(order);
    }

    @Override
    public String getDescription() {
        return "Express Delivery";
    }

    @Override
    public double getAdditionalCost() {
        return EXPRESS_FEE;
    }

    @Override
    public void addProduct(Product product, int quantity) {
        decoratedOrder.addProduct(product, quantity);
    }
}