package models;

import java.util.ArrayList;

public class Cart {
    private Restaurant restaurant;
    private ArrayList<MenuItem> items;

    public Cart(){
        restaurant = null;
        items = new ArrayList<>();
    }

    public void addItem(MenuItem item){
        if(restaurant == null){
            System.out.println("Add Restaurant First");
        }
        items.add(item);
    }

    public double getTotalCost(){
        double sum = 0;
        for(MenuItem item: items){
            sum += item.getPrice();
        }
        return sum;
    }

    public boolean isEmpty(){
        if (items.size() == 0) {
            return true;
        }
        return false;
    }

    public void clear(){
        items.clear();
        restaurant = null;
    }

    public void setRestaurant(Restaurant r){
        restaurant = r;
    }

    public Restaurant getRestaurant(){
        return restaurant;
    }

    public ArrayList<MenuItem> getItems(){
        return items;
    }
}
