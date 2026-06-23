package model;

import java.util.Objects;

public class Staff extends Person {

    private int    staffId;
    private String role;

    private static int staffCount  = 0;
    private static int nextStaffId = 1;

    public Staff(String name, int age, String gender, String phoneNumber, String role) {
        super(name, age, gender, phoneNumber);
        this.staffId = nextStaffId++;
        staffCount++;
        setRole(role);
    }

    public int    getStaffId() { return staffId; }
    public String getRole()    { return role; }

    public void setRole(String role) {
        this.role = cleanText(role, "General Staff");
    }

    // OVERRIDE — adds staffId and role around shared Person info
    @Override
    public void displayInfo() {
        System.out.println("Staff ID: " + staffId);
        super.displayInfo();
        System.out.println("Role    : " + role);
    }

    // OVERRIDE — chains super.toString() so Person controls its own fields
    @Override
    public String toString() {
        return String.format("Staff{id=%d, role='%s', base=[%s]}",
                staffId, role, super.toString());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Staff)) return false;
        Staff other = (Staff) obj;
        return this.staffId == other.staffId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(staffId);
    }

    @Override
    public String getRoleDescription() {
        return "Staff — " + role;
    }

    public static int getStaffCount() { return staffCount; }
}