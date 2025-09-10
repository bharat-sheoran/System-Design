
// ---------------------------
// Data Structure for Payment Details
// ---------------------------
class PaymentRequest {
    String sender;
    String receiver;
    double amount;
    String currency;

    public PaymentRequest(String sender, String receiver, double amount, String currency){
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
        this.currency = currency;
    }
}

// --------------------------------------------------
// Banking System Interface and Implementations
// --------------------------------------------------
interface BankingSystem{
    public boolean processPayment(double amount);
}

class PaytmBankingSystem implements BankingSystem{
    public PaytmBankingSystem(){}

    @Override
    public boolean processPayment(double amount) {
        System.out.println("[Banking System Paytm] Processing Payment of " + amount);
        int r = (int)(Math.random() * 100);
        return r < 20;
    }
}

class RajorpayBankingSystem implements BankingSystem{
    public RajorpayBankingSystem(){}

    @Override
    public boolean processPayment(double amount) {
        System.out.println("[Banking System Razorpay] Processing Payment of " + amount);
        int r = (int)(Math.random() * 100);
        return r < 90;
    }
    
}


abstract class PaymentGateway {
    protected BankingSystem bankingSystem;

    public PaymentGateway(){
        bankingSystem = null;
    }

    // Template Method Defining the Standard payment flow
    public boolean processPayment(PaymentRequest request){
        if (!validatePayment(request)) {
            System.out.println("[Payment Gateway] Validation Failed for " + request.sender);
            return false;
        }
        if (!initiatePayment(request)) {
            System.out.println("[Payment Gateway] Initiation Failed for " + request.sender);
            return false;
        }
        if (!confirmPayment(request)) {
            System.out.println("[Payment Gateway] Confirmation Failed for " + request.sender);
            return false;
        }
        return true;
    }

    public abstract boolean validatePayment(PaymentRequest request);
    public abstract boolean initiatePayment(PaymentRequest request);
    public abstract boolean confirmPayment(PaymentRequest request);
}

// ---------------------------------------
// Concrete Payment Gateway for Paytm
// ---------------------------------------

class PaytmGateway extends PaymentGateway{
    public PaytmGateway(){
        bankingSystem = new PaytmBankingSystem();
    }

    @Override
    public boolean validatePayment(PaymentRequest request) {
        System.out.println("[Paytm] Validating Payment for " + request.sender);

        if (request.amount <= 0 || request.currency != "INR") {
            return false;
        }
        return true;
    }

    @Override
    public boolean initiatePayment(PaymentRequest request) {
        System.out.println("[Paytm] Initiating Payment of " + request.amount + " " + request.currency + " for " + request.sender);
        return bankingSystem.processPayment(request.amount);
    }

    @Override
    public boolean confirmPayment(PaymentRequest request) {
        System.out.println("[Paytm] Confirming Payment for " + request.sender);
        return true;
    }
}

class RazorpayGateway extends PaymentGateway{
    public RazorpayGateway(){
        bankingSystem = new RajorpayBankingSystem();
    }

    @Override
    public boolean validatePayment(PaymentRequest request) {
        System.out.println("[Razorpay] Validating Payment for " + request.sender);

        if (request.amount <= 0 || request.currency != "INR") {
            return false;
        }
        return true;
    }

    @Override
    public boolean initiatePayment(PaymentRequest request) {
        System.out.println("[Razorpay] Initiating Payment of " + request.amount + " " + request.currency + " for " + request.sender);
        return bankingSystem.processPayment(request.amount);
    }

    @Override
    public boolean confirmPayment(PaymentRequest request) {
        System.out.println("[Razorpay] Confirming Payment for " + request.sender);
        return true;
    }
}

class PaymentGatewayProxy extends PaymentGateway{
    private PaymentGateway realGateway;
    private int retries;

    public PaymentGatewayProxy(PaymentGateway realGateway, int retries){
        this.realGateway = realGateway;
        this.retries = retries;
    }

    @Override
    public boolean processPayment(PaymentRequest request){
        boolean result = false;
        for(int attempt = 0; attempt < retries; attempt++){
            if (attempt > 0) {
                System.out.println("[Proxy] Retrying Payment (attempt " + (attempt + 1) + ") for " +request.sender);
            }
            result = realGateway.processPayment(request);
            if(result) break;
        }
        if (!result) {
            System.out.println("[Proxy] Payment Failed after " + retries + " attempts for " + request.sender);
        }
        return result;
    }

    @Override
    public boolean validatePayment(PaymentRequest request) {
        return realGateway.validatePayment(request);
    }

    @Override
    public boolean initiatePayment(PaymentRequest request) {
        return realGateway.initiatePayment(request);
    }

    @Override
    public boolean confirmPayment(PaymentRequest request) {
        return realGateway.confirmPayment(request);
    }
}

// --------------------------------------
// Gateway Factory for creating Gateway
// --------------------------------------

enum GatewayType {
    PAYTM,
    RAZORPAY
}


class GatewayFactory {
    private static GatewayFactory instance;
    private GatewayFactory() {};

    public static GatewayFactory getInstance(){
        if (instance == null) {
            instance = new GatewayFactory();
        }
        return instance;
    }

    PaymentGateway getGateway(GatewayType gatewayType){
        if(gatewayType == GatewayType.PAYTM){
            PaymentGateway paymentGateway = new PaytmGateway();
            return new PaymentGatewayProxy(paymentGateway, 3);
        } else {
            PaymentGateway paymentGateway = new RazorpayGateway();
            return new PaymentGatewayProxy(paymentGateway, 1);
        }
    }
}


// ---------------------------
// Unified API Service
// ---------------------------

class PaymentService {
    private static PaymentService instance;
    private PaymentGateway gateway;
    private PaymentService(){
        gateway = null;
    }

    public static PaymentService getInstance(){
        if (instance == null) {
            instance = new PaymentService();
        }
        return instance;
    }

    public void setGateway(PaymentGateway g){
        this.gateway = g;
    }

    public boolean processPayment(PaymentRequest request){
        if (gateway == null) {
            System.out.println("[Payment Service] No Payment gateway selected");
            return false;
        }
        return gateway.processPayment(request);
    }
}

class PaymentController {
    private static PaymentController instance;
    private PaymentController(){}
    
    public static PaymentController getInstance(){
        if (instance == null) {
            instance = new PaymentController();
        }
        return instance;
    }

    public boolean handlePayment(GatewayType type, PaymentRequest req){
        PaymentGateway paymentGateway = GatewayFactory.getInstance().getGateway(type);
        PaymentService.getInstance().setGateway(paymentGateway);
        return PaymentService.getInstance().processPayment(req);
    }
}

class Main {
    public static void main(String[] args) {
        PaymentRequest req1 = new PaymentRequest("Aditya", "Shubham", 1000.0, "INR");
        System.out.println("Processing via Paytm");
        System.out.println("-----------------------------");
        boolean res1 = PaymentController.getInstance().handlePayment(GatewayType.PAYTM, req1);
        System.out.println("Payment Status: " + (res1?"SUCCESS":"FAIL"));
        System.out.println("-----------------------------");

        PaymentRequest req2 = new PaymentRequest("Shubham", "Aditya", 500.0, "INR");
        System.out.println("Processing via Razorpay");
        System.out.println("-----------------------------");
        boolean res2 = PaymentController.getInstance().handlePayment(GatewayType.RAZORPAY, req2);
        System.out.println("Payment Status: " + (res2?"SUCCESS":"FAIL"));
        System.out.println("-----------------------------");
    }
}