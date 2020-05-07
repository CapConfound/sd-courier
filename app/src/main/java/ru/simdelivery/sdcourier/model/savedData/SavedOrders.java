package ru.simdelivery.sdcourier.model.savedData;

import java.util.List;

import ru.simdelivery.sdcourier.model.Order;

public class SavedOrders {

    public static List<Order> MyOrder;
    public static List<Order> FreeOrder;
    public static List<Order> OrderHistory;

    public static List<Order> getMyOrder() {
        return MyOrder;
    }

    public static void setMyOrder(List<Order> myOrder) {
        MyOrder = myOrder;
    }

    public static List<Order> getFreeOrder() {
        return FreeOrder;
    }

    public static void setFreeOrder(List<Order> freeOrder) {
        FreeOrder = freeOrder;
    }
}
