package decorator;

import model.IOrder;

public interface OrderDecorator extends IOrder {
    String getDescription();
    double getAdditionalCost();
}
