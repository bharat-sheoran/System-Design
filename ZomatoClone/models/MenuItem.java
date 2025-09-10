package models;

public class MenuItem {
    private String code;
    private String name;
    private int price;

    public MenuItem(String code, String name, int price){
        this.code = code;
        this.name = name;
        this.price = price;
    }

    public String getCode(){
        return this.code;
    }

    public void setCode(String c){
        this.code = c;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String n){
        this.name = n;
    }

    public int getPrice(){
        return this.price;
    }

    public void setPrice(int p){
        this.price = p;
    }
}
