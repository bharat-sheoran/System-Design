package models;

public class User {
    private int userId;
    private String name;
    private String address;
    private Cart cart;

    public User(int userId, String name, String address) {
        this.userId = userId;
        this.name = name;
        this.address = address;
        cart = new Cart();
    }

    public String getUserName() {
        return name;
    }

    public void setUserName(String name) {
        this.name = name;
    }

    public String getAddress(){
        return this.address;
    }

    public void setAddress(String address){
        this.address = address;
    }

    public Cart getCart(){
        return cart;
    }

    public String getUserAddress(){
        return address;
    }
}
