public class Main {

    public static void main(String[] args) {

        User user1 = new User(
                1,
                "Alice",
                30,
                "Female",
                "1234567890"
        );

        System.out.println("Passenger Name: " + user1.getName());
        System.out.println("Passenger Age: " + user1.getAge());
        System.out.println("Passenger Gender: " + user1.getGender());
        System.out.println("Passenger Phone Number: " + user1.getPhoneNumber());
    }

}