package com.example.roprpizza;

public class OrderHistoryModal {

    String orderName, orderPrice, orderStatus, orderDate, orderAllergies, orderID;

    public OrderHistoryModal(String orderName, String orderPrice, String orderStatus, String orderDate,String orderAllergies, String orderID) {
        this.orderName = orderName;
        this.orderPrice = orderPrice;
        this.orderStatus = orderStatus;
        this.orderDate = orderDate;
        this.orderAllergies = orderAllergies;
        this.orderID = orderID;
    }

    public String getOrderAllergies() {
        return orderAllergies;
    }

    public String getOrderID() {
        return orderID;
    }

    public String getOrderName() {
        return orderName;
    }

    public String getOrderPrice() {
        return orderPrice;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public String getOrderDate() {
        return orderDate;
    }
}
