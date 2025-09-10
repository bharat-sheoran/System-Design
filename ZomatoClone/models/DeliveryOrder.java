package models;

public class DeliveryOrder extends Order {
    private String userAddress;

    public DeliveryOrder(String userAddress){
        this.userAddress = userAddress;
    }

    @Override
    public String getType() {
        return "Delivery";
    }

    public String getUserAddress(){
        return this.userAddress;
    }

    public void setUserAddress(String address){
        this.userAddress = address;
    }
}
