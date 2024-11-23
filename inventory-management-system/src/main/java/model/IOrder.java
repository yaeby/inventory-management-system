package model;

public interface IOrder {
    double getTotalCost();
    void addProduct(Product product, int quantity);
    void display();
    String getOrderInfo();
}
