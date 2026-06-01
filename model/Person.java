package model;

import interfaces.Displayable;
import java.util.Objects;

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

    public String getName()        { return name; }
    public int    getAge()         { return age; }
    public String getGender()      { return gender; }
    public String getPhoneNumber() { return phoneNumber; }

    public void setName(String name) {
        this.name = cleanText(name, "Unknown Name");
    }

    public void setAge(int age) {
        this.age = (age > 0) ? age : 0;
    }

    public void setGender(String gender) {
        this.gender = cleanText(gender, "Unknown Gender");
    }

    // OVERLOAD 1 — single full number string
    public void setPhoneNumber(String phoneNumber) {
        String cleaned = cleanText(phoneNumber, "No Phone");
        this.phoneNumber = (cleaned.length() >= 8) ? cleaned : "Invalid Phone";
    }

    // OVERLOAD 2 — country code + local number separately
    public void setPhoneNumber(String countryCode, String localNumber) {
        String combined = cleanText(countryCode, "") + cleanText(localNumber, "");
        setPhoneNumber(combined);
    }

    @Override
    public void displayInfo() {
        System.out.println("Name  : " + name);
        System.out.println("Age   : " + age);
        System.out.println("Gender: " + gender);
        System.out.println("Phone : " + phoneNumber);
    }

    @Override
    public String toString() {
        return String.format("Person{name='%s', age=%d, gender='%s', phone='%s'}",
                name, age, gender, phoneNumber);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof Person)) return false;
        Person other = (Person) obj;
        return Objects.equals(this.name, other.name) &&
               Objects.equals(this.phoneNumber, other.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, phoneNumber);
    }
    public String getRoleDescription() {
        return "General Person";
    }
}