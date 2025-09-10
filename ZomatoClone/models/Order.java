package models;

import java.util.ArrayList;

import strategies.PaymentStrategies;

public abstract class Order {
    protected static int nextOrderId;
    protected int orderId;
    protected User user;
    protected Restaurant restaurant;
    protected ArrayList<MenuItem> items;
    protected PaymentStrategies paymentStrategy;
    protected double total;
    protected String scheduled;

    public Order(){
        user = null;
        restaurant = null;
        // Payment strategy null
        total = 0.0;
        scheduled = "";
        orderId = ++nextOrderId;
    }

    // Create processPayment here

    public abstract String getType();

    public int getOrderId(){
        return orderId;
    }

    public void setUser(User u){
        user = u;
    }

    public User getUser(){
        return user;
    }

    public void setRestaurant(Restaurant r){
        restaurant = r;
    }

    public Restaurant getRestaurant(){
        return this.restaurant;
    }

    public void setItems(ArrayList<MenuItem> items){
        this.items = items;
        total = 0;
        for(MenuItem i: items){
            total += i.getPrice();
        }
    }

    public double getTotal(){
        return total;
    }

    public String getScheduled(){
        return scheduled;
    }

    public ArrayList<MenuItem> getItems(){
        return items;
    }

    public void setPaymentStrategy(PaymentStrategies py){
        this.paymentStrategy = py;
    }

    public PaymentStrategies getPaymentStrategy(){
        return paymentStrategy;
    }


    public void setScheduled(String time){
        this.scheduled = time;
    }

    public void setTotal(double total){
        this.total = total;
    }
}
