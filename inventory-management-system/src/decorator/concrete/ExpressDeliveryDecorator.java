package decorator.concrete;

import decorator.BaseOrderDecorator;
import model.IOrder;
import model.Product;

public class ExpressDeliveryDecorator extends BaseOrderDecorator {
    private static final double EXPRESS_FEE = 25.0;
    private String deliveryTime;

    public ExpressDeliveryDecorator(IOrder order, String deliveryTime) {
        super(order);
        this.deliveryTime = deliveryTime;
    }

    @Override
    public String getDescription() {
        return "Express Delivery (" + deliveryTime + ")";
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