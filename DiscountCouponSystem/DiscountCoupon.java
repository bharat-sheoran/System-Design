
// -------------------------------------
// Discount Strategy (Strategy Pattern)
// -------------------------------------

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

interface DiscountStrategy {
    public double calculate(double baseAmount);
}

class FlatDiscountStrategy implements DiscountStrategy {
    private double amount;

    public FlatDiscountStrategy(double amount){
        this.amount = amount;
    }

    @Override
    public double calculate(double baseAmount) {
        return Math.min(amount, baseAmount);
    }
}

class PercentageDiscountStrategy implements DiscountStrategy {
    private double percentage;

    public PercentageDiscountStrategy(double percentage){
        this.percentage = percentage;
    }

    @Override
    public double calculate(double baseAmount) {
        return baseAmount * percentage / 100;
    }
}

class PercentageWithCapStrategy implements DiscountStrategy {
    private double percent;
    private double cap;

    public PercentageWithCapStrategy(double percent, double cap){
        this.percent = percent;
        this.cap = cap;
    }

    @Override
    public double calculate(double baseAmount) {
        double discount = baseAmount * percent / 100;
        if (discount > cap) {
            return cap;
        }
        return discount;
    }
}

enum StrategyType {
    FLAT,
    PERCENT,
    PERCENT_WITH_CAP
}

// -----------------------------------
// DiscountStrategyManager (Singleton)
// -----------------------------------

class DiscountStrategyManager {
    private static DiscountStrategyManager instance;
    private DiscountStrategyManager() {}

    public static DiscountStrategyManager getInstance() {
        if (instance == null) {
            instance = new DiscountStrategyManager();
        }
        return instance;
    }

    public DiscountStrategy getStrategy(StrategyType type, double amount) {
        switch (type) {
            case FLAT:
                return new FlatDiscountStrategy(amount);
            case PERCENT:
                return new PercentageDiscountStrategy(amount);
            default:
                return null;
        }
    }

    public DiscountStrategy getStrategy(StrategyType type, double amount, double cap) {
        switch (type) {
            case PERCENT_WITH_CAP:
                return new PercentageWithCapStrategy(amount, cap);
            default:
                return null;
        }
    }
}

class Product {
    private String name;
    private String category;
    private double price;

    public Product(String name, String category, double price) {
        this.name = name;
        this.category = category;
        this.price = price;
    }

    public String getName() {
        return this.name;
    }

    public String getCategory(){
        return this.category;
    }

    public double getPrice(){
        return this.price;
    }
}

class CartItem {
    private Product product;
    private int quantity;

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return this.product;
    }

    public double itemTotal() {
        return this.product.getPrice() * quantity;
    }
}

class Cart{
    private ArrayList<CartItem> items;
    private double originalTotal;
    private double currentTotal;
    private boolean loyaltyMember;
    String paymentBank;

    public Cart(){
        this.originalTotal = 0.0;
        this.currentTotal = 0.0;
        this.loyaltyMember = false;
        this.paymentBank = "";
        this.items = new ArrayList<>();
    }

    public void addProduct(Product prod){
        CartItem item = new CartItem(prod, 1);
        items.add(item);
        originalTotal += item.itemTotal();
        currentTotal += item.itemTotal();
    }

    public void addProduct(Product prod, int qty){
        CartItem item = new CartItem(prod, qty);
        items.add(item);
        originalTotal += item.itemTotal();
        currentTotal += item.itemTotal();
    }

    public double getOriginalTotal(){
        return originalTotal;
    }

    public double getCurrentTotal(){
        return currentTotal;
    }

    public void applyDiscount(double d){
        currentTotal -= d;
        if (currentTotal < 0) {
            currentTotal = 0;
        }
    }

    public void setLoyaltyMember(boolean member){
        loyaltyMember = member;
    }

    public boolean isLoyaltyMember(){
        return loyaltyMember;
    }

    public void setPaymentBank(String bank){
        paymentBank = bank;
    }

    public String getPaymentBank(){
        return paymentBank;
    }

    public ArrayList<CartItem> getItems(){
        return items;
    }
}

// --------------------------------------------
// Coupon base class (Chain of Responsibility)
// --------------------------------------------

abstract class Coupon {
    private Coupon next;

    public Coupon(){
        next = null;
    }

    public void setNext(Coupon next){
        this.next = next;
    }

    public Coupon getNext(){
        return next;
    }

    public void applyDiscount(Cart cart){
        if (isApplicable(cart)) {
            double discount = getDiscount(cart);
            cart.applyDiscount(discount);
            System.out.println(name() + " applied: " + discount);
            if (!isCombinable(cart)) {
                return;
            }
        }
        if (next != null) {
            next.applyDiscount(cart);
        }
    }

    public abstract boolean isApplicable(Cart cart);
    public abstract double getDiscount(Cart cart);
    public boolean isCombinable(Cart cart){
        return true;
    }
    public abstract String name();
}

// ----------------------------
// Concrete Coupons
// ----------------------------

class SeasonalOffer extends Coupon {
    private double percent;
    private String category;
    private DiscountStrategy strat;

    public SeasonalOffer(double percent, String category){
        this.percent = percent;
        this.category = category;
        strat = DiscountStrategyManager.getInstance().getStrategy(StrategyType.PERCENT, percent);
    }

    @Override
    public boolean isApplicable(Cart cart) {
        for(CartItem item: cart.getItems()){
            if (item.getProduct().getCategory().equals(category)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public double getDiscount(Cart cart) {
        double subTotal = 0.0;
        for(CartItem item: cart.getItems()){
            if (item.getProduct().getCategory().equals(category)) {
                subTotal += item.itemTotal();
            }
        }
        return strat.calculate(subTotal);
    }

    @Override
    public String name() {
        return "Seasonal Offer" + Integer.toString((int)percent) + "%off" + category;
    }
}

class LoyaltyDiscount extends Coupon {
    private double percent;
    private DiscountStrategy strat;

    public LoyaltyDiscount(double percent){
        this.percent = percent;
        strat = DiscountStrategyManager.getInstance().getStrategy(StrategyType.PERCENT, percent);
    }

    @Override
    public boolean isApplicable(Cart cart) {
        return cart.isLoyaltyMember();
    }

    @Override
    public double getDiscount(Cart cart) {
        return strat.calculate(cart.getCurrentTotal());
    }

    @Override
    public String name() {
        return "Loyalty Member Discount" + Integer.toString((int)percent) + "%off";
    }
}

class BulkPurchaseDiscount extends Coupon {
    private double threshold;
    private double flatOff;
    private DiscountStrategy strat;

    public BulkPurchaseDiscount(double thr, double off){
        this.threshold = thr;
        this.flatOff = off;
        this.strat = DiscountStrategyManager.getInstance().getStrategy(StrategyType.FLAT, flatOff);
    }

    @Override
    public boolean isApplicable(Cart cart) {
        return cart.getOriginalTotal() >= threshold;
    }

    @Override
    public double getDiscount(Cart cart) {
        return strat.calculate(cart.getCurrentTotal());
    }

    @Override
    public String name() {
        return "Bulk Purchase Rs " + (Integer.toString((int)flatOff)) + " off over " + (Integer.toString((int)threshold));
    }
}

class BankingCoupon extends Coupon {
    private String bank;
    private double minSpend;
    private double percent;
    private double offCap;
    private DiscountStrategy strat;

    public BankingCoupon(String bank, double ms, double percent, double offCap){
        this.bank = bank;
        this.minSpend = ms;
        this.percent = percent;
        this.offCap = percent;
        this.strat = DiscountStrategyManager.getInstance().getStrategy(StrategyType.PERCENT_WITH_CAP, percent, offCap);
    }

    @Override
    public boolean isApplicable(Cart cart) {
        return cart.getPaymentBank().equals(bank) && cart.getOriginalTotal() >= minSpend;
    }

    @Override
    public double getDiscount(Cart cart) {
        return strat.calculate(cart.getCurrentTotal());
    }

    @Override
    public String name() {
        return bank + " Bank Rs " + Integer.toString((int) percent) + " off upto " + Integer.toString((int) offCap);
    }
}

// -----------------------------
// Coupon Manager (Singleton)
// -----------------------------

class CouponManager {
    private static CouponManager instance;
    private Coupon head;
    private Lock lock;
    private CouponManager (){
        head = null;
        lock = new ReentrantLock();
    }

    public static CouponManager getInstance(){
        if (instance == null) {
            instance = new CouponManager();
        }
        return instance;
    }

    public void registerCoupon(Coupon coupon){
        lock.lock();
        if (head == null) {
            head = coupon;
        }else{
            Coupon curr = head;
            while (curr.getNext() != null) {
                curr = curr.getNext();
            }
            curr.setNext(coupon);
        }
    }

    public ArrayList<String> getApplicable(Cart cart){
        lock.lock();
        ArrayList<String> res = new ArrayList<>();
        Coupon curr = head;

        while (curr != null) {
            if (curr.isApplicable(cart)) {
                res.add(curr.name());
            }
            curr = curr.getNext();
        }
        return res;
    }

    public double applyAll(Cart cart){
        lock.lock();
        if (head != null) {
            head.applyDiscount(cart);
        }
        return cart.getCurrentTotal();
    }
}

class DiscountCoupon {
    public static void main(String[] args) {
        CouponManager mgr = CouponManager.getInstance();
        mgr.registerCoupon(new SeasonalOffer(10, "Clothing"));
        mgr.registerCoupon(new LoyaltyDiscount(5));
        mgr.registerCoupon(new BulkPurchaseDiscount(1000, 100));
        mgr.registerCoupon(new BankingCoupon("ABC", 2000, 15, 500));

        Product p1 = new Product("Winter Jacket", "Clothing", 1000);
        Product p2 = new Product("SmartPhone", "Electronics", 20000);
        Product p3 = new Product("Jeans", "Clothing", 1000);
        Product p4 = new Product("Headphones", "Electronics", 2000);

        Cart cart = new Cart();
        cart.addProduct(p1, 1);
        cart.addProduct(p2, 1);
        cart.addProduct(p3, 2);
        cart.addProduct(p4, 1);
        cart.setLoyaltyMember(true);
        cart.setPaymentBank("ABC");

        System.out.println("Original Cart Total: " + cart.getOriginalTotal() + " Rs");

        ArrayList<String> applicable = mgr.getApplicable(cart);
        System.out.println("Applicable Coupons: ");
        for(String name: applicable){
            System.out.println(" - " + name);
        }

        double finalTotal = mgr.applyAll(cart);
        System.out.println("Final Cart Total after discounts: " + finalTotal + " Rs");
    }
}
