
// ___________________________
// Notification & Decorators
// ___________________________

import java.util.ArrayList;

interface INotification {
    public String getContent();
}

class SimpleNotification implements INotification {
    private String text;

    SimpleNotification(String text) {
        this.text = text;
    }

    @Override
    public String getContent() {
        return this.text;
    }
}

abstract class NotificationDecorator implements INotification {
    protected INotification notification;

    public NotificationDecorator(INotification n){
        this.notification = n;
    }
}

class TimeStampDecorator extends NotificationDecorator {
    
    public TimeStampDecorator(INotification n) {
        super(n);
    }

    @Override
    public String getContent() {
        return "[2025-04-13 14:22:00]" + notification.getContent();
    }
    
}

class SignatureDecorator extends NotificationDecorator {
    private String signature;

    public SignatureDecorator(INotification n, String signature) {
        super(n);
        this.signature = signature;
    }

    @Override
    public String getContent() {
        return notification.getContent() + "\n-- " + signature + "\n\n";
    }
}

/*====================================
 * Observer Pattern Components
 =====================================*/

interface IObserver {
    public void update();
}

interface IObservable {
    public void addObserver(IObserver o);
    public void removeObserver(IObserver o);
    public void notifyObservers();
}

class NotificationObservable implements IObservable {
    private ArrayList<IObserver> observers;
    private INotification currentNotification;

    public NotificationObservable(){
        currentNotification = null;
        observers = new ArrayList<>();
    }

    @Override
    public void addObserver(IObserver o) {
        this.observers.add(o);
    }

    @Override
    public void removeObserver(IObserver o) {
        this.observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        for(int i = 0; i < observers.size(); i++){
            observers.get(i).update();
        }
    }

    public void setNotification(INotification n){
        this.currentNotification = n;
        notifyObservers();
    }

    public INotification getNotification(){
        return this.currentNotification;
    }

    public String getNotificationContent(){
        return this.currentNotification.getContent();
    }
}

// Observer 1
class Logger implements IObserver {
    private NotificationObservable observable;

    public Logger(){
        this.observable = NotificationService.getInstance().getObservable();
        observable.addObserver(this);
    }

    public Logger(NotificationObservable observable){
        this.observable = observable;
    }

    @Override
    public void update() {
        System.out.println("Logging New Notification: \n" + observable.getNotificationContent());
    }
    
}


/*=========================================
 * Strategy Pattern Components (Observer 2)
 ==========================================*/

interface INotificationStrategy{
    public void sendNotification(INotification n);
}

class EmailNotificationStrategy implements INotificationStrategy {
    private String emailId;

    public EmailNotificationStrategy(String email){
        this.emailId = email;
    }

    @Override
    public void sendNotification(INotification n) {
        System.out.println("Sending Email Notification to: " + emailId + "\n" + n.getContent());
    }
    
}

class SMSNotificationStrategy implements INotificationStrategy{
    private String mobile;

    public SMSNotificationStrategy(String mobile){
        this.mobile = mobile;
    }

    @Override
    public void sendNotification(INotification n) {
        System.out.println("Sending SNS Notification to: " + mobile + "\n" + n.getContent());
    }
}

class PopUpStrategy implements INotificationStrategy{

    @Override
    public void sendNotification(INotification n) {
        System.out.println("PopUp Notification to: " + "\n" + n.getContent());
    }
    
}

class NotificationEngine implements IObserver {
    private NotificationObservable notificationObservable;
    private ArrayList<INotificationStrategy> notificationStrategies;

    public NotificationEngine(){
        this.notificationObservable = NotificationService.getInstance().getObservable();
        notificationObservable.addObserver(this);
        this.notificationStrategies = new ArrayList<>();
    }

    public NotificationEngine(NotificationObservable observable){
        this.notificationObservable = observable;
        this.notificationStrategies = new ArrayList<>();
    }

    public void addNotificationStrategy(INotificationStrategy ns){
        this.notificationStrategies.add(ns);
    }

    @Override
    public void update() {
        String notificationContent = notificationObservable.getNotificationContent();
        for(INotificationStrategy notificationStrategy: notificationStrategies){
            notificationStrategy.sendNotification(new SimpleNotification(notificationContent));
        }
    }
    
}

/*==================================
 * Notification Service
 *==================================*/

class NotificationService {
    private NotificationObservable observable;
    private static NotificationService instance;
    private ArrayList<INotification> notifications;

    private NotificationService(){
        this.observable = new NotificationObservable();
        this.notifications = new ArrayList<>();
    }

    public static NotificationService getInstance(){
        if (instance == null) {
            instance = new NotificationService();
        }
        return instance;
    }

    public NotificationObservable getObservable(){
        return observable;
    }

    void sendNotification(INotification n){
        notifications.add(n);
        observable.setNotification(n);
    }
}

public class Notification {
    public static void main(String args[]){
        NotificationService notificationService = NotificationService.getInstance();
        NotificationEngine notificationEngine = new NotificationEngine();

        notificationEngine.addNotificationStrategy(new EmailNotificationStrategy("random@gmail.com"));
        notificationEngine.addNotificationStrategy(new SMSNotificationStrategy("+91 1234567890"));
        notificationEngine.addNotificationStrategy(new PopUpStrategy());

        INotification n = new SimpleNotification("Your order has been shipped");
        n = new TimeStampDecorator(n);
        n = new SignatureDecorator(n, "Customer Care");
        notificationService.sendNotification(n);
    }
}
