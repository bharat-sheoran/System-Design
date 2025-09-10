import java.util.ArrayList;

import models.Order;
import models.Restaurant;
import models.User;
import strategies.UPIPaymentStrategy;

public class Zomato {
    public static void main(String args[]){
        ZomatoApp zomato = new ZomatoApp();
    
        User user = new User(101, "Bharat", "Haryana");
        System.out.println("User: " + user.getUserName());
        
        ArrayList<Restaurant> restaurants = zomato.searchRestaurant("Delhi");

        if (restaurants.isEmpty()) {
            System.out.println("No Restaurants found");
            return;
        }
        System.out.println("Found Restaurants: ");
        for(Restaurant restaurant: restaurants){
            System.out.println(" - " + restaurant.getName());
        }

        zomato.selectRestaurant(user, restaurants.get(0));

        System.out.println("Selected Restaurants: " + restaurants.get(0).getName());

        zomato.addToCart(user, "P1");
        zomato.addToCart(user, "P2");

        Order order = zomato.checkOutNow(user, "Delivery", new UPIPaymentStrategy("9034781966"));
        zomato.payForOrder(user, order);
    }
}
