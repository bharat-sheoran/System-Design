package factories;

import java.util.ArrayList;

import models.Cart;
import models.MenuItem;
import models.Order;
import models.Restaurant;
import models.User;
import strategies.PaymentStrategies;

public abstract class OrderFactory {
    public abstract Order createOrder(User user, Cart cart, Restaurant restaurant, PaymentStrategies paymentStrategy, ArrayList<MenuItem> menuItems, String orderType);
}
