package model;

import java.util.Objects;

public class Staff extends Person {

    private int    staffId;
    private String role;

    private static int staffCount  = 0;
    private static int nextStaffId = 1;

    // ─── Constructor ─────────────────────────────────────────────────────────────
    public Staff(String name, int age, String gender, String phoneNumber, String role) {
        super(name, age, gender, phoneNumber);
        this.staffId = nextStaffId++;
        staffCount++;
        setRole(role);
    }

    // ─── Getters / Setters ───────────────────────────────────────────────────────
    public int    getStaffId() { return staffId; }
    public String getRole()    { return role; }

    public void setRole(String role) {
        this.role = cleanText(role, "General Staff");
    }

    // ─── displayInfo() override (EXISTING) ───────────────────────────────────────
    // Overrides Person.displayInfo() — adds staffId and role around the shared info
    @Override
    public void displayInfo() {
        System.out.println("Staff ID: " + staffId);
        super.displayInfo();   // calls Person.displayInfo() — name, age, gender, phone
        System.out.println("Role    : " + role);
    }

    // ─── toString() override (NEW) ───────────────────────────────────────────────
    // Chains super.toString() from Person so staff inherits the base description
    @Override
    public String toString() {
        return String.format("Staff{id=%d, name='%s', age=%d, role='%s'}",
                staffId, name, age, role);
    }

    // ─── equals() override (NEW) ─────────────────────────────────────────────────
    // Two Staff objects are equal only if they share the same staffId.
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Staff)) return false;
        Staff other = (Staff) obj;
        return this.staffId == other.staffId;
    }

    // ─── hashCode() override (NEW) ───────────────────────────────────────────────
    @Override
    public int hashCode() {
        return Objects.hash(staffId);
    }

    public static int getStaffCount() { return staffCount; }
}