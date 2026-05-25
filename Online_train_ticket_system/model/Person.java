package model;

import interfaces.Displayable;

public class Person implements Displayable {
    protected String name;
    protected int age;
    protected String gender;
    protected String phoneNumber;

    public Person(String name, int age, String gender, String phoneNumber) {
        setName(name);
        setAge(age);
        setGender(gender);
        setPhoneNumber(phoneNumber);
    }

    protected String cleanText(String value, String defaultValue) {
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }
        return value.trim();
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

    public void setName(String name) {
        this.name = cleanText(name, "Unknown Name");
    }

    public void setAge(int age) {
        if (age > 0) {
            this.age = age;
        } else {
            this.age = 0;
        }
    }

    public void setGender(String gender) {
        this.gender = cleanText(gender, "Unknown Gender");
    }

    public void setPhoneNumber(String phoneNumber) {
        String cleanedPhone = cleanText(phoneNumber, "No Phone");

        if (cleanedPhone.length() >= 8) {
            this.phoneNumber = cleanedPhone;
        } else {
            this.phoneNumber = "Invalid Phone";
        }
    }

    @Override
    public void displayInfo() {
        System.out.println("Name: " + name);
        System.out.println("Age: " + age);
        System.out.println("Gender: " + gender);
        System.out.println("Phone: " + phoneNumber);
    }
}