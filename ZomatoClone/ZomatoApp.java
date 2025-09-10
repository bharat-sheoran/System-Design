import java.util.ArrayList;

import factories.NowOrderFactory;
import factories.OrderFactory;
import factories.ScheduledOrderFactory;
import managers.OrderManager;
import managers.RestaurantManager;
import models.Cart;
import models.MenuItem;
import models.Order;
import models.Restaurant;
import models.User;
import services.NotificationService;
import strategies.PaymentStrategies;

public class ZomatoApp {
    public ZomatoApp(){
        initializeRestaurants();
    }

    void initializeRestaurants(){
        Restaurant restaurant1 = new Restaurant("Bikaner", "Delhi");
        restaurant1.addMenuItem(new MenuItem("P1", "Chole Bhature", 120));
        restaurant1.addMenuItem(new MenuItem("P2", "Samosa", 15));

        Restaurant restaurant2 = new Restaurant("Haldiram", "Kolkata");
        restaurant2.addMenuItem(new MenuItem("P1", "Raj Kachori", 80));
        restaurant2.addMenuItem(new MenuItem("P2", "Pav Bhaji", 100));
        restaurant2.addMenuItem(new MenuItem("P3", "Dhokla", 50));

        Restaurant restaurant3 = new Restaurant("Sarvana Bhawan", "Chennai");
        restaurant3.addMenuItem(new MenuItem("P1", "Masala Dosa", 90));
        restaurant3.addMenuItem(new MenuItem("P2", "Idli Vada", 60));
        restaurant3.addMenuItem(new MenuItem("P3", "Filter Coffee", 30));

        RestaurantManager restaurantManager = RestaurantManager.getInstance();
        restaurantManager.addRestuarant(restaurant1);
        restaurantManager.addRestuarant(restaurant2);
        restaurantManager.addRestuarant(restaurant3);
    }

    ArrayList<Restaurant> searchRestaurant(String location){
        return RestaurantManager.getInstance().searchByLocation(location);
    }

    void selectRestaurant(User user, Restaurant restaurant){
        Cart cart = user.getCart();
        cart.setRestaurant(restaurant);
    }

    void addToCart(User user, String itemCode){
        Restaurant restaurant = user.getCart().getRestaurant();
        if (restaurant == null) {
            System.out.println("Please select Restaurant first");
            return;
        }

        for(MenuItem item: restaurant.getMenu()){
            if (item.getCode() == itemCode) {
                user.getCart().addItem(item);
                break;
            }
        }
    }

    Order checkOutNow(User user, String orderType, PaymentStrategies paymentStrategy){
        return checkout(user, orderType, paymentStrategy, new NowOrderFactory());
    }

    Order checkoutScheduled(User user, String orderType, PaymentStrategies paymentStrategy, String ScheduledTime){
        return checkout(user, orderType, paymentStrategy, new ScheduledOrderFactory(ScheduledTime));
    }

    Order checkout(User user, String orderType, PaymentStrategies paymentStrategy, OrderFactory orderFactory){
        if (user.getCart().isEmpty()) return null;

        Cart cart = user.getCart();
        Restaurant restaurant = cart.getRestaurant();
        ArrayList<MenuItem> menuItems = cart.getItems();
        double totalCost = cart.getTotalCost();

        Order order = orderFactory.createOrder(user, cart, restaurant, paymentStrategy, menuItems, orderType);
        OrderManager.getInstance().addOrder(order);
        return order;
    }

    void payForOrder(User user, Order order){
        NotificationService notification = new NotificationService();
        notification.notify(order);
        user.getCart().clear();
    }
}
