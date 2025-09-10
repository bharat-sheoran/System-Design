import java.util.ArrayList;
import java.util.HashMap;

// //////////////////////////////////////////
// Product and Factory
// /////////////////////////////////////////

class Product {
    private int sku;
    private String name;
    private double price;

    public Product(int sku, String name, double price){
        this.name = name;
        this.sku = sku;
        this.price = price;
    }

    public String getName(){
        return this.name;
    }

    public int getsku(){
        return this.sku;
    }

    public double getPrice(){
        return this.price;
    }
}

class ProductFactory{

    public static Product createProduct(int sku){
        String name;
        double price;


        if (sku == 101) {
            name = "Apple";
            price = 20;
        }else if(sku == 102){
            name = "Banana";
            price = 10;
        }else if (sku == 103){
            name = "Chocolate";
            price = 50;
        }else if(sku == 201){
            name = "T-Shirt";
            price = 500;
        }else if (sku == 202){
            name = "Jeans";
            price = 1000;
        }else{
            name = "Item" + Integer.toString(sku);
            price = 100;
        }

        return new Product(sku, name, price);
    }
}

// //////////////////////////////////////////////////
// Inventory Store (Abstract) & DBInventoryStore
// /////////////////////////////////////////////////

interface InventoryStore {
    public void addProduct(Product prod, int qty);
    public void removeProduct(int sku, int qty);
    public int checkStock(int sku);
    public ArrayList<Product> listAvailaibleProducts();
}

class DBInventoryStore implements InventoryStore {
    private HashMap<Integer, Integer> stock;
    private HashMap<Integer, Product> products;

    public DBInventoryStore(){
        stock = new HashMap<>();
        products = new HashMap<>();
    }


    @Override
    public void addProduct(Product prod, int qty) {
        int sku = prod.getsku();
        if (!products.containsKey(sku)) {
            products.put(sku, prod);
        }
        stock.put(sku, qty);
    }

    @Override
    public void removeProduct(int sku, int qty) {
        if (!stock.containsKey(sku)) {
            return;
        }
        int currentQuantity = stock.get(sku);
        int remainingQuantity = currentQuantity - qty;
        if (remainingQuantity > 0) {
            stock.put(sku, remainingQuantity);
        }else {
            stock.remove(sku);
        }
    }

    @Override
    public int checkStock(int sku) {
        if (!stock.containsKey(sku)) {
            return 0;
        }

        return stock.get(sku);
    }

    @Override
    public ArrayList<Product> listAvailaibleProducts() {
        ArrayList<Product> availaible = new ArrayList<>();
        for(int sku : stock.keySet()){
            int qty = stock.get(sku);
            if (qty > 0 && products.containsKey(sku)) {
                availaible.add(products.get(sku));
            }
        }
        return availaible;
    }
}

// /////////////////////////////////////////////
// Inventory Manager
// ////////////////////////////////////////////

class InventoryManager {
    private InventoryStore store;

    public InventoryManager(InventoryStore store){
        this.store = store;
    }

    public void addStock(int sku, int qty){
        Product prod = ProductFactory.createProduct(sku);
        store.addProduct(prod, qty);
        System.out.println("[Inventory Manager] Added SKU" + sku + " Qty " + qty);
    }

    public void removeStock(int sku, int qty){
        store.removeProduct(sku, qty);
    }

    public int checkStock(int sku){
        return store.checkStock(sku);
    }

    public ArrayList<Product> getAvailaibleProducts(){
        return store.listAvailaibleProducts();
    }
}

// /////////////////////////////////////////////
// Replenish Strategy
// ////////////////////////////////////////////

interface ReplenishStrategy {
    public void replenish(InventoryManager manager, HashMap<Integer, Integer> itemsToReplenish);
}

class ThresholdReplenishStrategy implements ReplenishStrategy{
    private int threshold;

    public ThresholdReplenishStrategy(int threshold){
        this.threshold = threshold;
    }

    @Override
    public void replenish(InventoryManager manager, HashMap<Integer, Integer> itemsToReplenish) {
        System.out.println("[ThresholdReplenish] Checking Threshold ...");
        for(int sku : itemsToReplenish.keySet()){
            int qty = itemsToReplenish.get(sku);
            int currentQty = manager.checkStock(sku);
            if (currentQty < threshold) {
                manager.addStock(sku, qty);
                System.out.println("  -> SKU " + sku + " was " + currentQty + ", replenished by " + qty);
            }
        }
    }
}

class WeeklyReplenishStrategy implements ReplenishStrategy {

    @Override
    public void replenish(InventoryManager manager, HashMap<Integer, Integer> itemsToReplenish) {
        System.out.println("[WeeklyReplenish] Weekly replenishment triggered for inventory");
    }
}

class DarkStore {
    private String name;
    private double x, y;
    InventoryManager inventoryManager;
    ReplenishStrategy replenishStrategy;

    public DarkStore(String n, double x_coord, double y_coord){
        this.name = n;
        this.x = x_coord;
        this.y = y_coord;

        inventoryManager = new InventoryManager(new DBInventoryStore());
    }

    public double distanceTo(double ux, double uy){
        return Math.sqrt((x - ux)*(x - ux) + (y - uy)*(y - uy));
    }

    public void runReplenishment(HashMap<Integer, Integer> itemsToReplenish){
        if (replenishStrategy != null) {
            replenishStrategy.replenish(inventoryManager, itemsToReplenish);
        }
    }

    public ArrayList<Product> getAllProducts(){
        return inventoryManager.getAvailaibleProducts();
    }

    public int checkStock(int sku){
        return inventoryManager.checkStock(sku);
    }

    public void removeStock(int sku, int qty){
        inventoryManager.removeStock(sku, qty);
    }

    public void addStock(int sku, int qty){
        Product prod = ProductFactory.createProduct(sku);
        inventoryManager.addStock(sku, qty);
    }

    public void setReplenishStrategy(ReplenishStrategy strategy){
        this.replenishStrategy = strategy;
    }

    public String getName(){
        return this.name;
    }

    public double getXCoordinate(){
        return this.x;
    }

    public double getYCoordinate(){
        return this.y;
    }

    public InventoryManager getInventoryManager(){
        return this.inventoryManager;
    }
}

// ////////////////////////////////////////////
// DarkStoreManager (singleton)
// ///////////////////////////////////////////

class Pair<U, V> {
    public final U first;
    public final V second;

    public Pair(U first, V second) {
        this.first = first;
        this.second = second;
    }
}

class DarkStoreManager {
    private static DarkStoreManager instance;
    private ArrayList<DarkStore> darkStores;

    private DarkStoreManager(){
        darkStores = new ArrayList<>();
    }

    public static DarkStoreManager getInstance(){
        if (instance == null) {
            instance = new DarkStoreManager();
        }
        return instance;
    }

    public void registerDarkStore(DarkStore store){
        darkStores.add(store);
    }

    public ArrayList<DarkStore> getNearestDarkStore(double ux, double uy, double maxDistance){
        ArrayList<Pair<Double, DarkStore>> distlist = new ArrayList<>();
        for(DarkStore store : darkStores){
            double distance = store.distanceTo(ux, uy);
            if (distance <= maxDistance) {
                distlist.add(new Pair<>(distance, store));
            }
        }
        
        distlist.sort((a, b) -> Double.compare(a.first, b.first));
        ArrayList<DarkStore> result = new ArrayList<>();
        for (Pair<Double, DarkStore> pair : distlist) {
            result.add(pair.second);
        }
        return result;
    }
}

class User {
    private String name;
    private double ux, uy;
    private Cart cart;

    public User(String name, double ux, double uy){
        this.name = name;
        this.ux = ux;
        this.uy = uy;
        cart = new Cart();
    }

    public String getName(){
        return this.name;
    }

    public Cart getCart(){
        return this.cart;
    }

    public double[] getLocation(){
        return new double[] { this.ux, this.uy };
    }
}

class DeliveryAgent {
    private String name;

    public DeliveryAgent(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }
}

class Cart {
    private ArrayList<Pair<Product, Integer>> items;
    
    public Cart(){
        items = new ArrayList<>();
    }

    public void addToCart(int sku, int quantity){
        Product p = ProductFactory.createProduct(sku);
        items.add(new Pair<Product, Integer>(p, quantity));
        System.out.println("[Cart] Added SKU " + sku + " (" + p + p.getName() + " ) x" + quantity);
    }

    public double getTotal(){
        double sum = 0.0;
        for(Pair<Product, Integer> item : items){
            sum += item.first.getPrice() * item.second;
        }
        return sum;
    }

    public ArrayList<Pair<Product, Integer>> getItems(){
        return this.items;
    }
}

// //////////////////////////////////////////////////
// Order & Order Management
// //////////////////////////////////////////////////

class Order {
    private static int nextId;
    private int orderId;
    private User user;
    private ArrayList<Pair<Product, Integer>> items;
    private ArrayList<DeliveryAgent> partners;
    private double totalAmount;

    public Order(User user){
        this.orderId = nextId++;
        this.user = user;
        this.totalAmount = 0.0;
        items = new ArrayList<>();
        partners = new ArrayList<>();
    }

    public static int getNextId() {
        return nextId;
    }

    public static void setNextId(int nextId) {
        Order.nextId = nextId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ArrayList<Pair<Product, Integer>> getItems() {
        return items;
    }

    public void addToItems(Pair<Product, Integer> item) {
        this.items.add(item);
    }

    public ArrayList<DeliveryAgent> getPartners() {
        return partners;
    }

    public void addPartner(DeliveryAgent partner) {
        this.partners.add(partner);
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
}


class OrderManager {
    private static OrderManager instance;
    private ArrayList<Order> orders;

    private OrderManager(){
        orders = new ArrayList<>();
    }

    public static OrderManager getInstance(){
        if (instance == null) {
            instance = new OrderManager();
        }
        return instance;
    }

    public void placeOrder(User user, Cart cart){
        System.out.println("[Order Manager] Placing Order for: " + user.getName());

        ArrayList<Pair<Product, Integer>> requestedItems = cart.getItems();

        double maxDistance = 5.0;
        ArrayList<DarkStore> nearbyDarkStores = DarkStoreManager.getInstance().getNearestDarkStore(user.getLocation()[0], user.getLocation()[1], maxDistance);

        if (nearbyDarkStores.isEmpty()) {
            System.out.println("No Dark Store within 5KM. Cannot fullfill order");
            return;
        }

        DarkStore firstStore = nearbyDarkStores.getFirst();
        boolean allInFirst = true;
        for (Pair<Product, Integer> item : requestedItems) {
            int sku = item.first.getsku();
            int qty = item.second;
            if (firstStore.checkStock(sku) < qty) {
                allInFirst = false;
                break;
            }
        }


        Order order = new Order(user);

        if (allInFirst) {
            System.out.println("All items at: " + firstStore.getName());

            for(Pair<Product, Integer> item : requestedItems){
                int sku = item.first.getsku();
                int qty = item.second;
                firstStore.removeStock(sku, qty);
                order.addToItems(new Pair<Product, Integer>(item.first, qty));
            }
        }else{
            System.out.println("Splitting Orders across stores...");
            HashMap<Integer, Integer> allItems = new HashMap<>(); // SKU -> qty

            for(Pair<Product, Integer> item: requestedItems){
                allItems.put(item.first.getsku(), item.second);
            }

            int partnerId = 1;
            for(DarkStore store: nearbyDarkStores){
                if (allItems.isEmpty()) break;

                System.out.println("    Checking: " + store.getName());
                boolean assigned = false;
                ArrayList<Integer> toErase = new ArrayList<>();

                for(Integer item: allItems.keySet()){
                    int availaibleQty = store.checkStock(item);
                    if (availaibleQty <= 0) continue;

                    int takenQty = Math.min(availaibleQty, allItems.get(item));
                    store.removeStock(item, takenQty);
                    System.out.println("    " + store.getName() + " Supplies SKU " + item + " x" + takenQty);

                    order.addToItems(new Pair<Product, Integer>(ProductFactory.createProduct(item), takenQty));

                    if (allItems.get(item) >  takenQty) {
                        allItems.put(item, allItems.get(item) - takenQty);
                    }else{
                        toErase.add(item);
                    }
                    assigned = true;
                }

                for(int sku: toErase) allItems.remove(sku);

                if (assigned) {
                    String pname = "Partner " + Integer.toString(partnerId++);
                    order.addPartner(new DeliveryAgent(pname));
                    System.out.println("    Assigned" + pname + " for " + store.getName());
                }
            }

            if (!allItems.isEmpty()) {
                System.out.println("    Could not fulfill");
                for(Integer sku: allItems.keySet()){
                    System.out.println("    SKU" + sku + " x" + allItems.get(sku));
                }
            }

            double sum = 0;
            for(Pair<Product, Integer> item: order.getItems()){
                sum += item.first.getPrice();
            }
            order.setTotalAmount(sum);
        }

        // Printing Order summary

        System.out.println("[Order Manager] Order #" + order.getOrderId() + " Summary:");
        System.out.print("    User: " + user.getName() + "\n  Items:\n");
        for(Pair<Product, Integer> item: order.getItems()){
            System.out.println("    SKU" + item.first.getsku() + " (" + item.first.getName() + ") x" + item.second + " @ Rs" + item.first.getPrice());
        }
        System.out.println("  Total: Rs" + order.getTotalAmount() + " Partners:");
        for(DeliveryAgent dp: order.getPartners()){
            System.out.println("    " + dp.getName());
        }

        orders.add(order);
    }
}

class Zepto {
    
    public static void showAllItems(User user){
        System.out.println("[Zepto] All Availaible products withing 5KM for " + user.getName() + ":");

        DarkStoreManager dsManager = DarkStoreManager.getInstance();
        ArrayList<DarkStore> nearbyDarkStores = dsManager.getNearestDarkStore(user.getLocation()[0], user.getLocation()[1], 5.0);

        HashMap<Integer, Double> skuToPrice = new HashMap<>();
        HashMap<Integer, String> skuToName = new HashMap<>();

        for(DarkStore store: nearbyDarkStores){
            ArrayList<Product> products = store.getAllProducts();

            for(Product product: products){
                int sku = product.getsku();

                if (!skuToPrice.containsKey(sku)) {
                    skuToPrice.put(sku, product.getPrice());
                    skuToName.put(sku, product.getName());
                }
            }
        }

        for(Integer entry: skuToPrice.keySet()){
            System.out.println("    SKU" + entry + " (" + skuToName.get(entry) + ") @ Rs" + skuToPrice.get(entry));
        }
    }

    public static void initialize(){
        DarkStoreManager dsManager = DarkStoreManager.getInstance();

        DarkStore darkStoreA = new DarkStore("DarkStoreA", 0.0, 0.0);
        darkStoreA.setReplenishStrategy(new ThresholdReplenishStrategy(3));
        System.out.println("Adding Stocks in DarkStore A");
        darkStoreA.addStock(101, 5);
        darkStoreA.addStock(102, 2);

        DarkStore darkStoreB = new DarkStore("DarkStoreB", 4.0, 1.0);
        darkStoreB.setReplenishStrategy(new ThresholdReplenishStrategy(3));

        System.out.println("Adding stocks in DarkStoreB...");
        darkStoreB.addStock(101, 3);
        darkStoreB.addStock(103, 10);

        DarkStore darkStoreC = new DarkStore("DarkStoreC", 2.0, 3.0);
        darkStoreC.setReplenishStrategy(new ThresholdReplenishStrategy(3));

        System.out.println("Adding stocks in DarkStoreB...");
        darkStoreB.addStock(102, 5);
        darkStoreB.addStock(201, 7);

        dsManager.registerDarkStore(darkStoreA);
        dsManager.registerDarkStore(darkStoreB);
        dsManager.registerDarkStore(darkStoreC);
    }
}

class Main {
    public static void main(String[] args) {
        Zepto.initialize();

        User user = new User("Bharat", 1.0, 1.0);
        System.out.println("User with Name " + user.getName() + " comes on platform");

        Zepto.showAllItems(user);

        System.out.println("Adding Items to cart");
        user.getCart().addToCart(101, 4);
        user.getCart().addToCart(102, 3);
        user.getCart().addToCart(103, 2);

        OrderManager.getInstance().placeOrder(user, user.getCart());
    }
}
