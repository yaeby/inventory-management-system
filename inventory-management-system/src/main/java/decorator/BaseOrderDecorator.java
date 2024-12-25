package decorator;

import model.order.IOrder;

public abstract class BaseOrderDecorator implements OrderDecorator {
    protected IOrder decoratedOrder;

    public BaseOrderDecorator(IOrder order) {
        this.decoratedOrder = order;
    }

    @Override
    public double getTotalCost() {
        return decoratedOrder.getTotalCost() + getAdditionalCost();
    }

    @Override
    public void display() {
        decoratedOrder.display();
        System.out.println("Additional Service: " + getDescription() +
                " (+" + getAdditionalCost() + ")");
    }

    @Override
    public String getOrderInfo() {
        return decoratedOrder.getOrderInfo() + " + " + getDescription();
    }
}
