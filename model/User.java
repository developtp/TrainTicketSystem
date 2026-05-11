package model;

public class User implements Displayable {

    private static int userCounter = 1;

    private int userId;
    private String name;
    private int age;
    private String gender;
    private String phoneNumber;

    public User(String name, int age, String gender, String phoneNumber) {
        this.userId = userCounter++;
        setName(name);
        setAge(age);
        setGender(gender);
        setPhoneNumber(phoneNumber);
    }

    public int getUserId() {
        return userId;
    }

    public static int getUserCount() {
        return userCounter - 1;
    }

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

    // No setUserId() because userId should not be changed

    public void setName(String name) {
        if (name != null && !name.trim().isEmpty()) {
            this.name = name;
        } else {
            System.out.println("Invalid name.");
        }
    }

    public void setAge(int age) {
        if (age > 0 && age < 100) {
            this.age = age;
        } else {
            System.out.println("Invalid age.");
        }
    }

    public void setGender(String gender) {
        if (gender != null &&
           (gender.equalsIgnoreCase("Male") || gender.equalsIgnoreCase("Female"))) {
            this.gender = gender;
        } else {
            System.out.println("Invalid gender.");
        }
    }

    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber != null && phoneNumber.matches("\\d{10}")) {
            this.phoneNumber = phoneNumber;
        } else {
            System.out.println("Invalid phone number. Must be 10 digits.");
        }
    }

    @Override
    public void displayInfo() {
        System.out.println("User ID : " + userId);
        System.out.println("Name    : " + name);
        System.out.println("Age     : " + age);
        System.out.println("Gender  : " + gender);
        System.out.println("Phone   : " + phoneNumber);
    }
}