public class Main {
    public static void main(String[] args) {

        User user1 = new User();
        user1.passengerID = 1;
        user1.name = "Alice";   
        user1.age = 30;
        user1.gender = "Female";
        user1.phoneNumber = "1234567890";

        System.out.println("Passenger Name: " + user1.name);
        System.out.println("Passenger Age: " + user1.age);
        System.out.println("Passenger Gender: " + user1.gender);
        System.out.println("Passenger Phone Number: " + user1.phoneNumber);
    }

}

