package managers;

import java.util.ArrayList;

import models.Restaurant;

public class RestaurantManager {
    private ArrayList<Restaurant> restaurants;
    private static RestaurantManager instance;

    private RestaurantManager(){
        this.restaurants = new ArrayList<>();
    }

    public static RestaurantManager getInstance(){
        if(instance == null){
            instance = new RestaurantManager();
        }
        return instance;
    }

    public void addRestuarant(Restaurant r){
        this.restaurants.add(r);
    }

    public ArrayList<Restaurant> searchByLocation(String loc){
        ArrayList<Restaurant> result = new ArrayList<>();
        loc = loc.toLowerCase();

        for(Restaurant r: restaurants){
            String rl = r.getLocation();
            rl = rl.toLowerCase();
            if(rl.contains(loc)){
                result.add(r);
            }
        }
        return result;
    }
}
