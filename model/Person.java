package model;

import interfaces.Displayable;
import java.util.Objects;

public class Person implements Displayable {

    protected String name;
    protected int age;
    protected String gender;
    protected String phoneNumber;

    // ─── Constructor ─────────────────────────────────────────────────────────────
    public Person(String name, int age, String gender, String phoneNumber) {
        setName(name);
        setAge(age);
        setGender(gender);
        setPhoneNumber(phoneNumber);
    }

    // ─── Helper ──────────────────────────────────────────────────────────────────
    protected String cleanText(String value, String defaultValue) {
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }
        return value.trim();
    }

    // ─── Getters ─────────────────────────────────────────────────────────────────
    public String getName()       { return name; }
    public int    getAge()        { return age; }
    public String getGender()     { return gender; }
    public String getPhoneNumber(){ return phoneNumber; }

    // ─── Setters ─────────────────────────────────────────────────────────────────
    public void setName(String name) {
        this.name = cleanText(name, "Unknown Name");
    }

    public void setAge(int age) {
        this.age = (age > 0) ? age : 0;
    }

    public void setGender(String gender) {
        this.gender = cleanText(gender, "Unknown Gender");
    }

    // EXISTING — single string e.g. "012345678"
    public void setPhoneNumber(String phoneNumber) {
        String cleaned = cleanText(phoneNumber, "No Phone");
        this.phoneNumber = (cleaned.length() >= 8) ? cleaned : "Invalid Phone";
    }

    // OVERLOAD (NEW) — accepts country code + local number separately
    // e.g. setPhoneNumber("+855", "12345678")  →  stores "+85512345678"
    public void setPhoneNumber(String countryCode, String localNumber) {
        String combined = cleanText(countryCode, "") + cleanText(localNumber, "");
        setPhoneNumber(combined); // delegates to the single-string version above
    }

    // ─── Displayable override ────────────────────────────────────────────────────
    @Override
    public void displayInfo() {
        System.out.println("Name  : " + name);
        System.out.println("Age   : " + age);
        System.out.println("Gender: " + gender);
        System.out.println("Phone : " + phoneNumber);
    }

    // ─── toString() override (NEW) ───────────────────────────────────────────────
    // Called automatically when you do: System.out.println(person)
    // or when concatenating: "User info: " + person
    // Without this, Java would print something ugly like: model.Person@3d4eac69
    @Override
    public String toString() {
        return String.format("Person{name='%s', age=%d, gender='%s', phone='%s'}",
                name, age, gender, phoneNumber);
    }

    // ─── equals() override (NEW) ─────────────────────────────────────────────────
    // By default Java compares object REFERENCES (memory address).
    // We override so two Person objects are "equal" if they have the same name + phone.
    // Subclasses (User, Staff) will override this further using their own IDs.
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;                    // same object in memory
        if (obj == null) return false;
        if (!(obj instanceof Person)) return false;      // must be a Person or subclass
        Person other = (Person) obj;
        return Objects.equals(this.name, other.name) &&
               Objects.equals(this.phoneNumber, other.phoneNumber);
    }

    // ─── hashCode() override (NEW) ───────────────────────────────────────────────
    // Java rule: if equals() says two objects are equal, hashCode() MUST return
    // the same value for both. Always override hashCode() when you override equals().
    // Used internally by HashMap, HashSet, etc.
    @Override
    public int hashCode() {
        return Objects.hash(name, phoneNumber);
    }
}