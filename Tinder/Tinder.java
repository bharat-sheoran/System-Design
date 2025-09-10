
// --------------------------- Observer Pattern ----------------------- //

// Observer Pattern: Interface for notification observers

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

interface NotificationObserver {
    public void update(String message);
}

class UserNotificationObserver implements NotificationObserver {
    private String userId;

    public UserNotificationObserver(String userId) {
        this.userId = userId;
    }

    @Override
    public void update(String message) {
        System.out.println("Notification for user " + userId + ": " + message);
    }
}

// Observable for Observer Pattern
class NotificationService {
    private HashMap<String, NotificationObserver> observers;
    private static NotificationService instance;
    private NotificationService(){
        observers = new HashMap<>();
    }

    public static NotificationService getInstance(){
        if(instance == null){
            instance = new NotificationService();
        }
        return instance;
    }

    public void registerObserver(String userId, NotificationObserver observer) {
        observers.put(userId, observer);
    }

    public void removeObserver(String userId){
        observers.remove(userId);
    }

    public void notifyUser(String userId, String message) {
        NotificationObserver observer = observers.get(userId);
        if (observer != null) {
            observer.update(message);
        }
    }

    public void notifyAll(String message){
        for(NotificationObserver observer : observers.values()){
            observer.update(message);
        }
    }
}

// -------------------------- Basic Models ---------------------------- //

enum Gender {
    MALE,
    FEMALE,
    NON_BINARY,
    OTHER
}

class Location {
    private double latitude;
    private double longitude;

    public Location(){
        this.latitude = 0.0;
        this.longitude = 0.0;
    }

    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    // Calculate distance in Kilometers between two locations using haversine formula
    public double distanceInKm(Location other){
        double earthRadius = 6371.0;
        double dLat = (other.latitude - latitude) * Math.PI / 180.0;
        double dLon = (other.longitude - longitude) * Math.PI / 180.0;

        double a = Math.sin(dLat/2) * Math.sin(dLat/2) + 
            Math.cos(latitude * Math.PI) * Math.cos(other.latitude * Math.PI / 180) * 
            Math.sin(dLon / 2) * Math.sin(dLon / 2);
        
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return earthRadius * c;
    }
}

// Interest Class
class Interest {
    private String name;
    private String category;

    public Interest(){
        this.name = "";
        this.category = "";
    }

    public Interest(String name, String category) {
        this.name = name;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }
}

// Preference Class
class Preference {
    private ArrayList<Gender> interestedIn;
    private int minAge;
    private int maxAge;
    private double maxDistance;
    private ArrayList<String> interests;

    public Preference(){
        this.interestedIn = new ArrayList<>();
        this.minAge = 18;
        this.maxAge = 100;
        this.maxDistance = 100.0;
        this.interests = new ArrayList<>();
    }

    public void addGenderPreference(Gender gender){
        interestedIn.add(gender);
    }

    public void removeGenderPreference(Gender gender){
        interestedIn.remove(gender);
    }

    public void setAgeRange(int minAge, int maxAge){
        this.minAge = minAge;
        this.maxAge = maxAge;
    }

    public void setMaxDistance(double maxDistance){
        this.maxDistance = maxDistance;
    }

    public void addInterest(String interest){
        interests.add(interest);
    }

    public void removeInterest(String interest){
        interests.remove(interest);
    }

    public boolean isInterestedIn(Gender gender){
        return interestedIn.contains(gender);
    }

    public boolean isAgeInRange(int age){
        return age >= minAge && age <= maxAge;
    }

    public boolean isDistanceAcceptable(double distance){
        return distance <= maxDistance;
    }

    public ArrayList<String> getInterests(){
        return interests;
    }

    public int getMinAge(){
        return minAge;
    }

    public int getMaxAge(){
        return maxAge;
    }

    public double getMaxDistance(){
        return maxDistance;
    }
}

// ------------------------- Message System ---------------------------
class Message {
    private String senderId;
    private String content;
    private LocalDateTime time;

    public Message(String senderId, String content) {
        this.senderId = senderId;
        this.content = content;
        this.time = LocalDateTime.now();
    }

    public String getSenderId() {
        return senderId;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getFormattedTime(){
        return this.time;
    }
}

class ChatRoom {
    private String id;
    private ArrayList<String> participantsId;
    private ArrayList<Message> messages;

    public ChatRoom(String id, ArrayList<String> participantsId) {
        this.id = id;
        this.participantsId = participantsId;
        this.messages = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void addMessage(String senderId, String content){
        Message msg = new Message(senderId, content);
        messages.add(msg);
    }

    public boolean hasParticipant(String userId){
        return participantsId.contains(userId);
    }

    public ArrayList<Message> getMessages(){
        return messages;
    }

    public ArrayList<String> getParticipantsId(){
        return participantsId;
    }

    public void displayChat(){
        System.out.println("======= Chat Room: " + id + " =======");
        for(Message msg : messages){
            System.out.println("[" + msg.getFormattedTime() + "] " + msg.getSenderId() + ": " + msg.getContent());
        }
        System.out.println("===================================");
    }
}

// --------------------- Profile System -------------------------

class UserProfile {
    private String name;
    private int age;
    private Gender gender;
    private String bio;
    private ArrayList<String> photos;
    private ArrayList<Interest> interests;
    private Location location;

    public UserProfile(){
        this.name = "";
        this.age = 0;
        this.gender = Gender.OTHER;
        this.bio = "";
        this.photos = new ArrayList<>();
        this.interests = new ArrayList<>();
        this.location = new Location();
    }

    public String getName(){
        return this.name;
    }

    public int getAge(){
        return this.age;
    }

    public Gender getGender(){
        return this.gender;
    }

    public String getBio(){
        return this.bio;
    }

    public ArrayList<String> getPhotos(){
        return this.photos;
    }

    public ArrayList<Interest> getInterests(){
        return this.interests;
    }

    public Location getLocation(){
        return this.location;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setAge(int age){
        this.age = age;
    }

    public void setGender(Gender gender){
        this.gender = gender;
    }

    public void setBio(String bio){
        this.bio = bio;
    }

    public void addPhoto(String photo){
        this.photos.add(photo);
    }

    public void removePhoto(String photo){
        this.photos.remove(photo);
    }

    public void addInterest(Interest interest){
        this.interests.add(interest);
    }

    public void removeInterest(Interest interest){
        this.interests.remove(interest);
    }

    public void setLocation(Location location){
        this.location = location;
    }

    public void display() {
        System.out.println("=========== Profile =============");
        System.out.println("Name: " + name);
        System.out.println("Age: " + age);
        System.out.print("Gender: ");
        switch (gender) {
            case Gender.MALE:
                System.out.println("Male");
                break;
            case Gender.FEMALE:
                System.out.println("Female");
                break;
            case Gender.OTHER:
                System.out.println("Other");
                break;
            case Gender.NON_BINARY:
                System.out.println("Non Binary");
                break;
            default:
                break;
        }

        System.out.println("Bio: " + bio);
        System.out.print("Photos: ");
        for (String photo : photos) {
            System.out.print(photo + " ");
        }
        System.out.println();

        System.out.println("Interests: ");
        for (Interest interest : interests) {
            System.out.print(interest.getName() + interest.getCategory() + "). ");
        }
        System.out.println();

        System.out.println("Location: " + location.getLatitude() + ", " + location.getLongitude());
        System.out.println("=================================");
    }
}

// ------------------------------- User System ----------------------------------

enum SwipeAction {
    LEFT,
    RIGHT
}

class User {
    private String id;
    private UserProfile userProfile;
    private Preference preference;
    private HashMap<String, SwipeAction> swipeHistory;
    private NotificationObserver notificationObserver;

    public User(String userId){
        id = userId;
        userProfile = new UserProfile();
        preference = new Preference();
        notificationObserver = new UserNotificationObserver(userId);
        swipeHistory = new HashMap<>();
        NotificationService.getInstance().registerObserver(userId, notificationObserver);
    }

    public String getId(){
        return id;
    }

    public UserProfile getUserProfile(){
        return userProfile;
    }

    public Preference getPreference(){
        return preference;
    }

    public void swipe(String otherUserId, SwipeAction action){
        swipeHistory.put(otherUserId, action);
    }

    public boolean hasLiked(String otherUserId){
        if (swipeHistory.containsKey(otherUserId)) {
            return swipeHistory.get(otherUserId) == SwipeAction.RIGHT;
        }
        return false;
    }

    public boolean hasDisliked(String otherUserId){
        if (swipeHistory.containsKey(otherUserId)) {
            return swipeHistory.get(otherUserId) == SwipeAction.LEFT;
        }
        return false;
    }

    public boolean hasInteractedWith(String otherUserId){
        return swipeHistory.containsKey(otherUserId);
    }

    public void display(){
        userProfile.display();
    }
}

// --------------------------- Location Service -------------------------

interface LocationStrategy {
    ArrayList<User> findNearbyUsers(Location location, double maxDistance, ArrayList<User> allUsers);
}

class BasicLocationStrategy implements LocationStrategy{
    @Override
    public ArrayList<User> findNearbyUsers(Location location, double maxDistance, ArrayList<User> allUsers) {
        ArrayList<User> nearyByUser = new ArrayList<>();
        for (User user : allUsers) {
            if (location.distanceInKm(user.getUserProfile().getLocation()) <= maxDistance) {
                nearyByUser.add(user);
            }
        }
        return nearyByUser;
    }
}

class LocationService {
    private LocationStrategy strategy;
    private static LocationService instance;

    private LocationService(){
        strategy = new BasicLocationStrategy();
    }

    public static LocationService getInstance(){
        if (instance == null) {
            instance = new LocationService();
        }
        return instance;
    }

    public void setStrategy(LocationStrategy strategy){
        this.strategy = strategy;
    }

    public ArrayList<User> findNearbyUsers(Location location, double maxDistance, ArrayList<User> allUsers){
        return strategy.findNearbyUsers(location, maxDistance, allUsers);
    }
}

enum MatcherType{
    BASIC,
    INTERESTS_BASED,
    LOCATION_BASED
}

interface Matcher{
    public double CalculateMatchScore(User user1, User user2);
}

class BasicMatcher implements Matcher {

    @Override
    public double CalculateMatchScore(User user1, User user2) {
        boolean user1LikesUser2Gender = user1.getPreference().isInterestedIn(user2.getUserProfile().getGender());
        boolean user2LikesUser1Gender = user2.getPreference().isInterestedIn(user1.getUserProfile().getGender());

        if (!user1LikesUser2Gender || !user2LikesUser1Gender) {
            return 0.0;
        }

        boolean user1AgeInRange = user1.getPreference().isAgeInRange(user2.getUserProfile().getAge());
        boolean user2AgeInRange = user2.getPreference().isAgeInRange(user1.getUserProfile().getAge());

        if (!user1AgeInRange || !user2AgeInRange) {
            return 0.0;
        }

        double distance = user1.getUserProfile().getLocation().distanceInKm(user2.getUserProfile().getLocation());
        boolean user1LikesUser2Distance = user1.getPreference().isDistanceAcceptable(distance);
        boolean user2LikesUser1Distance = user2.getPreference().isDistanceAcceptable(distance);

        if (!user1LikesUser2Distance || !user2LikesUser1Distance) {
            return 0.0;
        }

        return 0.5;
    }
}

class InterestsBasedMatcher implements Matcher {

    @Override
    public double CalculateMatchScore(User user1, User user2) {
        BasicMatcher basicMatcher = new BasicMatcher();
        double baseScore = basicMatcher.CalculateMatchScore(user1, user2);

        if (baseScore == 0.0) {
            return 0.0;
        }

        ArrayList<String> user1InterestNames = new ArrayList<>();
        for(Interest interest: user1.getUserProfile().getInterests()){
            user1InterestNames.add(interest.getName());
        }

        int sharedInterests = 0;
        for(Interest interest: user2.getUserProfile().getInterests()){
            if (user1InterestNames.contains(interest.getName())) {
                sharedInterests++;
            }
        }

        double maxInterests = Math.max(user1.getUserProfile().getInterests().size(), user2.getUserProfile().getInterests().size());
        double interestScore = maxInterests > 0 ? 0.5 * (sharedInterests / maxInterests): 0.0;

        return baseScore + interestScore;
    }
}

class LocationBasedMatcher implements Matcher {

    @Override
    public double CalculateMatchScore(User user1, User user2) {
        InterestsBasedMatcher interestsMatcher = new InterestsBasedMatcher();
        double baseScore = interestsMatcher.CalculateMatchScore(user1, user2);

        if (baseScore == 0.0) {
            return 0.0;
        }

        double distance = user1.getUserProfile().getLocation().distanceInKm(user2.getUserProfile().getLocation());
        double maxDistance = Math.min(user1.getPreference().getMaxDistance(), user2.getPreference().getMaxDistance());

        double proximityScore = maxDistance > 0 ? 0.2 * (1.0 - (distance / maxDistance)): 0.0;

        return baseScore + proximityScore;
    }
}

class MatcherFactory {
    public static Matcher createMatcher(MatcherType type){
        switch (type) {
            case BASIC:
                return new BasicMatcher();
            case INTERESTS_BASED:
                return new InterestsBasedMatcher();
            case LOCATION_BASED:
                return new LocationBasedMatcher();
            default:
                return null;
        }
    }
}

// ---------------- Dating App ------------------------

class Tinder {
    private ArrayList<User> users;
    private ArrayList<ChatRoom> chatRooms;
    private Matcher matcher;
    private static Tinder instance;

    private Tinder(){
        matcher = MatcherFactory.createMatcher(MatcherType.LOCATION_BASED);
        users = new ArrayList<>();
        chatRooms = new ArrayList<>();
    }

    public static Tinder getInstance(){
        if (instance == null) {
            instance = new Tinder();
        }
        return instance;
    }

    public void setMatcher(MatcherType type){
        matcher = MatcherFactory.createMatcher(type);
    }

    public User createUser(String userId){
        User user = new User(userId);
        users.add(user);
        return user;
    }

    public User getUserById(String userId){
        for(User user: users){
            if (user.getId().equals(userId)) {
                return user;
            }
        }
        return null;
    }

    public ArrayList<User> findNearbyUsers(String userId, double maxDistance){
        User user = getUserById(userId);
        if (user == null) {
            return new ArrayList<>();
        }

        ArrayList<User> nearbyUsers = LocationService.getInstance().findNearbyUsers(user.getUserProfile().getLocation(), maxDistance, users);

        nearbyUsers.remove(user);

        ArrayList<User> filteredUsers = new ArrayList<>();
        for (User otherUser : nearbyUsers) {
            if (user.hasInteractedWith(otherUser.getId())) {
                continue;
            }

            double score = matcher.CalculateMatchScore(otherUser, user);

            if (score > 0) {
                filteredUsers.add(otherUser);
            }
        }
        return filteredUsers;
    }

    public boolean swipe(String userId, String targetUserId, SwipeAction action){
        User user = getUserById(userId);
        User targetUser = getUserById(targetUserId);

        if (user == null || targetUser == null) {
            System.out.println("User not found");
        }

        user.swipe(targetUserId, action);

        if (action == SwipeAction.RIGHT && targetUser.hasLiked(userId)) {
            String chatRoomId = userId + "_" + targetUserId;
            ArrayList<String> participants = new ArrayList<>();
            participants.add(userId);
            participants.add(targetUserId);
            ChatRoom chatRoom = new ChatRoom(chatRoomId, participants);
            chatRooms.add(chatRoom);
            NotificationService.getInstance().notifyUser(userId, "You have a new match with " + targetUser.getUserProfile());
            NotificationService.getInstance().notifyUser(targetUserId, "You have a new match with " + user.getUserProfile());
            return true;
        }
        return false;
    }

    public ChatRoom getChatRoom(String userId1, String userId2){
        for(ChatRoom chatRoom: chatRooms){
            if (chatRoom.hasParticipant(userId1) && chatRoom.hasParticipant(userId2)) {
                return chatRoom;
            }
        }
        return null;
    }

    public void sendMessage(String senderId, String receiverId, String message){
        ChatRoom chatRoom = getChatRoom(senderId, receiverId);
        if (chatRoom == null) {
            System.out.println("Chat room not found");
            return;
        }
        chatRoom.addMessage(senderId, message);

        NotificationService.getInstance().notifyUser(receiverId, "You have a new message from " + getUserById(receiverId));
    }

    public void displayUser(String userId){
        User user = getUserById(userId);
        if (user == null) {
            System.out.println("User not found");
            return;
        }
        user.display();
    }

    public void displayChatRoom(String userId1, String userId2){
        ChatRoom chatRoom = getChatRoom(userId1, userId2);
        if (chatRoom == null) {
            System.out.println("Chat room not found");
            return;
        }
        chatRoom.displayChat();
    }
}

// ----------------------- Main ----------------------------

class Main {
    public static void main(String[] args) {
        Tinder tinder = Tinder.getInstance();
        User user1 = tinder.createUser("user1");
        User user2 = tinder.createUser("user2");
        
        user1.getUserProfile().setName("Rohan");
        user1.getUserProfile().setAge(28);
        user1.getUserProfile().setGender(Gender.MALE);
        user1.getUserProfile().setBio("I am a Software Developer");
        user1.getUserProfile().addPhoto("rohan.jpg");
        user1.getUserProfile().addInterest(new Interest("Coding", "Programming"));
        user1.getUserProfile().addInterest(new Interest("Travel", "Lifestyle"));
        user1.getUserProfile().addInterest(new Interest("Music", "Entertainment"));

        user1.getPreference().addGenderPreference(Gender.FEMALE);
        user1.getPreference().setAgeRange(25, 30);
        user1.getPreference().setMaxDistance(10);
        user1.getPreference().addInterest("Coding");
        user1.getPreference().addInterest("Travel");

        user2.getUserProfile().setName("Neha");
        user2.getUserProfile().setAge(27);
        user2.getUserProfile().setGender(Gender.FEMALE);
        user2.getUserProfile().setBio("Art Teacher");
        user2.getUserProfile().addPhoto("neha.jpg");
        user2.getUserProfile().addInterest(new Interest("Painting", "Art"));
        user2.getUserProfile().addInterest(new Interest("Travel", "Lifestyle"));
        user2.getUserProfile().addInterest(new Interest("Music", "Entertainment"));

        user2.getPreference().addGenderPreference(Gender.MALE);
        user2.getPreference().setAgeRange(27, 30);
        user2.getPreference().setMaxDistance(15.0);
        user2.getPreference().addInterest("Coding");
        user2.getPreference().addInterest("Movies");

        Location location1 = new Location();
        location1.setLatitude(1.01);
        location1.setLongitude(1.02);
        user1.getUserProfile().setLocation(location1);

        Location location2 = new Location();
        location2.setLatitude(1.03);
        location2.setLongitude(1.04);
        user2.getUserProfile().setLocation(location2);

        System.out.println("--------- User Profiles ------------");
        tinder.displayUser("user1");
        tinder.displayUser("user2");

        System.out.println("--------- Nearby Users ------------");
        ArrayList<User> nearbyUsers = tinder.findNearbyUsers("user1", 5.0);
        System.out.println("Found " + nearbyUsers.size() + " nearby users");
        for (User user : nearbyUsers) {
            System.out.println("- " + user.getUserProfile().getName() + " (" + user.getId() + ")");
        }

        System.out.println("---------------- Swipe Actions ----------------");
        System.out.println("User1 Swipes right on User 2");
        tinder.swipe("user2", "user1", SwipeAction.RIGHT);

        System.out.println("--------------- Chat Rooms ---------------------");
        tinder.sendMessage("user1", "user2", "Hi Neha, Kaise ho?");

        tinder.sendMessage("user2", "user1", "Hi Rohan, Ma bdiya tum btao");

        tinder.displayChatRoom("user1", "user2");
    }
}


