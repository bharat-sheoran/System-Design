package factories;

import java.util.ArrayList;

import models.Cart;
import models.DeliveryOrder;
import models.MenuItem;
import models.Order;
import models.PickupOrder;
import models.Restaurant;
import models.User;
import strategies.PaymentStrategies;

public class ScheduledOrderFactory extends OrderFactory {

    private String scheduledTime;

    public ScheduledOrderFactory(String scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    @Override
    public Order createOrder(User user, Cart cart, Restaurant restaurant, PaymentStrategies paymentStrategy, ArrayList<MenuItem> menuItems,
            String orderType) {
        Order order = null;

        if (orderType == "Delivery") {
            DeliveryOrder deliveryOrder = new DeliveryOrder(user.getUserAddress());
            order = deliveryOrder;
        } else {
            PickupOrder pickupOrder = new PickupOrder();
            pickupOrder.setRestaurantAddress(restaurant.getLocation());
            order = pickupOrder;
        }
        order.setUser(user);
        order.setRestaurant(restaurant);
        order.setItems(menuItems);
        order.setPaymentStrategy(paymentStrategy);
        order.setScheduled(scheduledTime);
        order.setTotal(cart.getTotalCost());
        return order;
    }

}
