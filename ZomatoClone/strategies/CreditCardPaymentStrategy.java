package strategies;

public class CreditCardPaymentStrategy implements PaymentStrategies {
    private String cardNumber;

    public CreditCardPaymentStrategy(String cardNumber){
        this.cardNumber = cardNumber;
    }

    @Override
    public void pay(double amount) {
        System.out.println("Paid Rs " + amount + " using CreditCard (" + cardNumber + ")");
    }
    
}
