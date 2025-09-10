package managers;

import java.util.ArrayList;

import models.Order;

public class OrderManager {
    private ArrayList<Order> orders;
    private static OrderManager instance;

    private OrderManager(){
        this.orders = new ArrayList<>();
    }

    public static OrderManager getInstance(){
        if(instance == null){
            instance = new OrderManager();
        }
        return instance;
    }

    public void addOrder(Order o){
        this.orders.add(o);
    }

    public void listOrders(){
        System.out.println("\n------ All Orders --------");
        for(Order order: orders){
            System.out.println(order.getType() + "order for" + order.getUser().getUserName() + " | Total: Rs " + order.getTotal() + " | At: " + order.getScheduled());
        }
    }
}
