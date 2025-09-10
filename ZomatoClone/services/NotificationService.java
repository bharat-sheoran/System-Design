package services;

import java.util.ArrayList;

import models.MenuItem;
import models.Order;

public class NotificationService {
    public static void notify(Order order){
        System.out.println("\n Notification: New " + order.getType() + " order placed!");
        System.out.println("---------------------------------------------------------");
        System.out.println("Order ID: " + order.getOrderId());
        System.out.println("Custormer: " + order.getUser().getUserName());
        System.out.println("Restaurant: " + order.getRestaurant().getName());
        System.out.println("Items: ");

        ArrayList<MenuItem> items = order.getItems();
        for(MenuItem item : items){
            System.out.println(item.getName() + " - Rs " + item.getPrice());
        }

        System.out.println("Total: Rs " + order.getTotal());
        System.out.println("Scheduled for: " + order.getScheduled());
        System.out.println("Payment Done");
        System.out.println("---------------------------------------------------------\n");
    }
}
