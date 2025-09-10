package models;

public class PickupOrder extends Order {
    private String restaurantAddress;


    public PickupOrder() {
        this.restaurantAddress = "Restaurant Address";
    }

    @Override
    public String getType() {
        return "Pickup";
    }

    public String getRestaurantAddress(){
        return this.restaurantAddress;
    }

    public void setRestaurantAddress(String address){
        this.restaurantAddress = address;
    }
}
