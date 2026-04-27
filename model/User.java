package model;

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
        setName(name);
        setAge(age);
        setGender(gender);
        setPhoneNumber(phoneNumber);
    }

    // Getter
    public int getUserID() {
        return userID;
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

    // Setter
    public void setUserID(int userID) {
        if (userID > 0) {
            this.userID = userID;
        } else {
            throw new IllegalArgumentException("User ID must be a positive .");
        }
    }
 
    public void setName(String name) {
        if (name != null && !name.trim().isEmpty()) {
            this.name = name;
        } else {
            throw new IllegalArgumentException("Name cannot be null or empty.");
        }
    }
 
    public void setAge(int age) {
        if (age > 0 && age < 100) {
            this.age = age;
        } else {
            throw new IllegalArgumentException("Age must be between 1 and 99.");
        }
    }
 
    public void setGender(String gender) {
        if (gender.equalsIgnoreCase("male") || gender.equalsIgnoreCase("female")) {
            this.gender = gender;
        } else {
            throw new IllegalArgumentException("Gender must be Male or Female.");
        }
    }
 
    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber.matches("\\d{10}")) {
            this.phoneNumber = phoneNumber;
        } else {
            throw new IllegalArgumentException("Phone number must be exactly 10 digits.");
        }
    }
}