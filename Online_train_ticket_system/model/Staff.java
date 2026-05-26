package model;

public class Staff extends Person {
    private int staffId;
    private String role;

    private static int staffCount = 0;
    private static int nextStaffId = 1;

    public Staff(String name, int age, String gender, String phoneNumber, String role) {
        super(name, age, gender, phoneNumber);
        this.staffId = nextStaffId;
        nextStaffId++;
        staffCount++;
        setRole(role);
    }

    public int getStaffId() {
        return staffId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = cleanText(role, "General Staff");
    }

    @Override
    public void displayInfo() {
        System.out.println("Staff ID: " + staffId);
        super.displayInfo();
        System.out.println("Role: " + role);
    }
    
    public static int getStaffCount() {
        return staffCount;
    }
}
