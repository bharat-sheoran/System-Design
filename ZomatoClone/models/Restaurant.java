package models;

import java.util.ArrayList;

public class Restaurant {
    private static int nextRestaurantId;
    private int restaurantId = 0;
    private String name;
    private String location;
    private ArrayList<MenuItem> menu;

    public Restaurant(String name, String location) {
        this.name = name;
        this.location = location;
        this.restaurantId = ++nextRestaurantId;
        this.menu = new ArrayList<>();
    }

    public String getName(){
        return this.name;
    }

    public void setName(String n){
        this.name = n;
    }

    public String getLocation(){
        return this.location;
    }

    public void setLocation(String l){
        this.location = l;
    }
    
    public void addMenuItem(MenuItem item){
        menu.add(item);
    }

    public ArrayList<MenuItem> getMenu(){
        return menu;
    }
}
