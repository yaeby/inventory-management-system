package decorator.concrete;

import decorator.BaseOrderDecorator;
import model.IOrder;
import model.Product;

public class InsuranceDecorator extends BaseOrderDecorator {
    private static final double INSURANCE_RATE = 0.05;
    private static final double MIN_INSURANCE_FEE = 10.0;

    public InsuranceDecorator(IOrder order) {
        super(order);
    }

    @Override
    public String getDescription() {
        return "Insurance Coverage";
    }

    @Override
    public double getAdditionalCost() {
        double insuranceFee = decoratedOrder.getTotalCost() * INSURANCE_RATE;
        return Math.max(Math.round(insuranceFee * 100.0)/100.0, MIN_INSURANCE_FEE);
    }

    @Override
    public void addProduct(Product product, int quantity) {
        decoratedOrder.addProduct(product, quantity);
    }
}
