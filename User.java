public class User {

    private int userID;
    private String name;
    private int age;
    private String gender;
    private String phoneNumber;

    // Constructor
    public User(int userID, String name, int age,
                String gender, String phoneNumber) {

        this.userID = userID;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
    }

    // Getter
    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    // Setter
    public void setAge(int age) {
        if (age > 0) {
            this.age = age;
        }
    }
}