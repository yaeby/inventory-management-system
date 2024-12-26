package decorator;

import model.order.IOrder;

public interface OrderDecorator extends IOrder {
    String getDescription();
    double getAdditionalCost();
}
