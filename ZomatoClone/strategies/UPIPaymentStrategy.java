package strategies;

public class UPIPaymentStrategy implements PaymentStrategies{
    private String mobile;

    public UPIPaymentStrategy(String mobile){
        this.mobile = mobile;
    }

    @Override
    public void pay(double amount) {
        System.out.println("Paid Rs " + amount + " using UPI (" + mobile + ")");
    }
}
