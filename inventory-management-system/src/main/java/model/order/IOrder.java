package model.order;

import model.Product;

public interface IOrder {
    double getTotalCost();
    void addProduct(Product product, int quantity);
    void display();
    String getOrderInfo();
}
