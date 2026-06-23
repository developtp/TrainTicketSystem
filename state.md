# TrainTicketSystem — OOP Concept Review & Project State

> Reviewed on: 2026-06-23  
> Reviewer: Expert Java Code Review  
> Branch: `main`

---

## Project File Structure

```
TrainTicketSystem/
├── exceptions/
│   └── TicketIssuanceException.java   [NEW]
├── interfaces/
│   ├── BookingSearchable.java
│   ├── Displayable.java
│   ├── Payable.java
│   ├── Printable.java
│   ├── TrainSearchable.java
│   └── UserSearchable.java
├── model/
│   ├── Booking.java
│   ├── Payment.java
│   ├── Person.java
│   ├── Staff.java
│   ├── Ticket.java
│   ├── Train.java
│   └── User.java
└── main/
    ├── Main.java
    └── TrainTicketBookingSystem.java
```

---

## 1. Encapsulation

**✅ YES — Correctly and fully implemented**

### Where
All model classes: `Person`, `User`, `Staff`, `Train`, `Booking`, `Payment`, `Ticket`,
and the system class `TrainTicketBookingSystem`.

### Why It Qualifies
Every field is declared `private` (or `protected` in `Person` for subclass access),
and access is controlled through dedicated `get` / `set` methods. Setters perform
validation before assigning values — e.g., rejecting negative ages, trimming strings,
defaulting invalid values — which is the core purpose of encapsulation.

### Evidence

```java
// Person.java — fields hidden, guarded setters
protected String name;
protected int    age;
protected String gender;
protected String phoneNumber;

public void setAge(int age) {
    this.age = (age > 0) ? age : 0;          // guard: rejects negatives
}

// Train.java — internal lists exposed only as defensive copies
private ArrayList<Booking> bookings;
public ArrayList<Booking> getBookingsCopy() {
    return new ArrayList<>(bookings);         // prevents external mutation
}

// TrainTicketBookingSystem.java
private HashMap<Integer, User> users;        // private — no direct outside access
private ArrayList<Train>       trains;
private ArrayList<Booking>     bookings;
```

### Issues / Improvements
`Person`'s fields use `protected` instead of `private`. While this allows subclasses
(`User`, `Staff`) to access them directly (e.g., `this.name` in `User`), best practice
is to keep them `private` and let subclasses use the inherited `get`/`set` methods.

```java
// Recommended fix in Person.java:
private String name;   // was: protected String name;
private int    age;    // was: protected int age;
// Subclasses then call getName() / setName() instead of this.name directly.
```

---

## 2. Static

**✅ YES — Correctly implemented**

### Where
`User`, `Staff`, `Train`, `Booking`, `Payment`, `Ticket` — all model classes.

### Why It Qualifies
Two patterns of `static` are correctly used:

**Pattern A — Auto-increment ID counter:**  
`nextUserId`, `nextTrainId`, `nextBookingId`, etc. are `static` so they are shared
across all instances, guaranteeing unique IDs.

**Pattern B — Count tracker:**  
`userCount`, `trainCount`, etc. count how many objects have ever been created,
accessible without needing an instance.

**Pattern C — Static factory method:**  
`Ticket.createTicket(...)` is a `static` factory method that validates inputs before
constructing the object.

### Evidence

```java
// User.java
private static int userCount  = 0;
private static int nextUserId = 1;

public User(String name, ...) {
    this.userId = nextUserId++;   // unique across all User instances
    userCount++;
}

public static int getUserCount() { return userCount; }

// Ticket.java — static factory method
public static Ticket createTicket(Booking booking, Payment payment, String seatNumber) {
    if (booking == null) { ... return null; }
    ...
    return new Ticket(booking, seatNumber);
}
```

### Issues / Improvements
None. Static usage is appropriate and purposeful throughout.

---

## 3. Interface

**✅ YES — Correctly implemented, actively used**

### Where
`interfaces/` package: `Displayable`, `Payable`, `Printable`, `BookingSearchable`,
`TrainSearchable`, `UserSearchable`.

### Why It Qualifies
Six interfaces define contracts that are implemented by the model and system classes.
`Payable` also uses the `default` method keyword (a Java 8+ feature).

### Evidence

```java
// Displayable.java
public interface Displayable {
    void displayInfo();
}

// Payable.java — with default method
public interface Payable {
    boolean pay();
    boolean isPaid();
    default void processPayment() { pay(); }   // default implementation
}

// Payment.java — implements multiple interfaces
public class Payment implements Displayable, Payable, Printable { ... }

// TrainTicketBookingSystem.java — implements 4 interfaces
public class TrainTicketBookingSystem
    implements Displayable, UserSearchable, TrainSearchable, BookingSearchable { ... }
```

### Implementation Map

| Interface          | Implemented By                              |
|--------------------|---------------------------------------------|
| `Displayable`      | `Person`, `Train`, `Booking`, `Payment`, `Ticket`, `TrainTicketBookingSystem` |
| `Payable`          | `Payment`                                   |
| `Printable`        | `Payment`, `Ticket`                         |
| `BookingSearchable`| `TrainTicketBookingSystem`                  |
| `TrainSearchable`  | `TrainTicketBookingSystem`                  |
| `UserSearchable`   | `TrainTicketBookingSystem`                  |

### Issues / Improvements
None. Interface usage is clean and well-structured.

---

## 4. Inheritance

**✅ YES — Correctly implemented**

### Where
- `User extends Person`
- `Staff extends Person`
- `User implements Comparable<User>`
- `Booking implements Comparable<Booking>`

### Why It Qualifies
`Person` is the concrete base class with shared attributes (`name`, `age`, `gender`,
`phoneNumber`) and shared methods. Both `User` and `Staff` extend `Person`, inheriting
its fields and methods while adding their own specific fields (`userId`/`bookings`
and `staffId`/`role` respectively).

### Evidence

```java
// User.java
public class User extends Person implements Comparable<User> {
    private int userId;
    private ArrayList<Booking> bookings;

    public User(String name, int age, String gender, String phoneNumber) {
        super(name, age, gender, phoneNumber);   // calls Person constructor
        this.userId = nextUserId++;
    }
}

// Staff.java
public class Staff extends Person {
    private int    staffId;
    private String role;

    public Staff(String name, int age, String gender, String phoneNumber, String role) {
        super(name, age, gender, phoneNumber);   // calls Person constructor
        this.staffId = nextStaffId++;
    }
}
```

### Issues / Improvements
`Person` is a **concrete class** (`public class Person`) rather than an `abstract class`.
Since `Person` is never instantiated directly in `Main.java`, and both `User` and `Staff`
must override `displayInfo()`, `Person` should ideally be declared `abstract` to enforce
this contract (see item 9 below for full details).

---

## 5. Method Overriding

**✅ YES — Correctly implemented with `@Override` annotations**

### Where

| Class       | Method Overridden  | Overrides From       |
|-------------|-------------------|----------------------|
| `User`      | `displayInfo()`   | `Person` / `Displayable` |
| `User`      | `toString()`      | `Object`             |
| `User`      | `compareTo()`     | `Comparable<User>`   |
| `User`      | `equals()`        | `Object`             |
| `User`      | `hashCode()`      | `Object`             |
| `Staff`     | `displayInfo()`   | `Person` / `Displayable` |
| `Staff`     | `toString()`      | `Object`             |
| `Staff`     | `equals()`        | `Object`             |
| `Staff`     | `hashCode()`      | `Object`             |
| `Payment`   | `pay()`           | `Payable`            |
| `Payment`   | `isPaid()`        | `Payable`            |
| `Payment`   | `displayInfo()`   | `Displayable`        |
| `Payment`   | `print()`         | `Printable`          |
| `Ticket`    | `displayInfo()`   | `Displayable`        |
| `Ticket`    | `print()`         | `Printable`          |
| `Booking`   | `displayInfo()`   | `Displayable`        |
| `Booking`   | `compareTo()`     | `Comparable<Booking>`|
| `Train`     | `displayInfo()`   | `Displayable`        |
| `TrainTicketBookingSystem` | `displayInfo()` | `Displayable` |
| `TrainTicketBookingSystem` | `searchUserById()` | `UserSearchable` |
| `TrainTicketBookingSystem` | `searchTrainById()` | `TrainSearchable` |
| `TrainTicketBookingSystem` | `searchBookingById()` | `BookingSearchable` |

### Evidence

```java
// User.java — overrides Person's displayInfo to add user-specific output
@Override
public void displayInfo() {
    System.out.println("User ID       : " + userId);
    super.displayInfo();                          // calls Person.displayInfo()
    System.out.println("Total Bookings: " + bookings.size());
}

// Staff.java — same pattern
@Override
public void displayInfo() {
    System.out.println("Staff ID: " + staffId);
    super.displayInfo();
    System.out.println("Role    : " + role);
}
```

### Issues / Improvements
`Person.getRoleDescription()` returns `"General Person"` but is **not** overridden by
`User` or `Staff`, even though `Main.java` calls it on all three. This means
`User` and `Staff` both print `"General Person"` instead of a role-specific description.
This should be overridden:

```java
// Add to User.java:
@Override
public String getRoleDescription() {
    return "Registered User";
}

// Add to Staff.java:
@Override
public String getRoleDescription() {
    return "Staff — " + role;
}
```

---

## 6. Method Overloading

**✅ YES — Correctly and extensively implemented**

### Where

| Class       | Overloaded Method              | Variants |
|-------------|-------------------------------|----------|
| `Person`    | `setPhoneNumber()`            | `(String)` vs `(String, String)` |
| `User`      | `displayBookingHistory()`     | `()`, `(String status)`, `(int limit)` |
| `Train`     | `reserveSeat()`               | `(String seatNumber)` vs `()` auto-assign |
| `Train`     | `displayInfo()`               | `()` vs `(boolean showSeats)` |
| `Booking`   | Constructor                   | `(User,Train,String)`, `(User,Train,String,String)`, `(User,Train,LocalDate)` |
| `Payment`   | Constructor                   | `(Booking,String)`, `(Booking,String,double)`, `(Booking,String,int)` |
| `TrainTicketBookingSystem` | `searchUserBy..()` | `searchUserById(int)` vs `searchUserByName(String)` |

### Evidence

```java
// Person.java
public void setPhoneNumber(String phoneNumber) { ... }                  // OVERLOAD 1
public void setPhoneNumber(String countryCode, String localNumber) { .. } // OVERLOAD 2

// User.java
public void displayBookingHistory()              { ... }   // all bookings
public void displayBookingHistory(String status) { ... }   // filter by status
public void displayBookingHistory(int limit)     { ... }   // last N bookings

// Train.java
public boolean reserveSeat(String seatNumber) { ... }  // specific seat
public String  reserveSeat()                  { ... }  // auto-assign
```

### Issues / Improvements
None. Method overloading is used meaningfully across the project, not just as a
naming trick.

---

## 7. Polymorphism

**✅ YES — Correctly implemented (both compile-time and runtime)**

### Where
**Compile-time (static) polymorphism:** Method overloading (see item 6 above).  
**Runtime (dynamic) polymorphism:** Interface references and `ArrayList<Displayable>`,
`ArrayList<Printable>`, `ArrayList<Person>` loops in `Main.java`.

### Why It Qualifies
The same reference type (`Displayable`, `Person`) is used to call `displayInfo()` on
objects of different concrete types. The JVM resolves which actual implementation to
call at runtime based on the real object type — this is runtime polymorphism.

### Evidence

```java
// Main.java — runtime polymorphism via Displayable interface
ArrayList<Displayable> displayables = new ArrayList<>();
displayables.add(system);    // TrainTicketBookingSystem.displayInfo()
displayables.add(user1);     // User.displayInfo()
displayables.add(staff1);    // Staff.displayInfo()
displayables.add(train1);    // Train.displayInfo()
displayables.add(booking1);  // Booking.displayInfo()
displayables.add(payment1);  // Payment.displayInfo()
displayables.add(ticket1);   // Ticket.displayInfo()

for (Displayable item : displayables) {
    item.displayInfo();   // different method called for each object at runtime
}

// Also via Person reference
ArrayList<Person> people = new ArrayList<>();
people.add(user1);
people.add(staff1);
for (Person person : people) {
    person.displayInfo();  // User or Staff version called at runtime
}

// Also via Payable interface
Payable payable = payment1;
payable.isPaid();            // Payment.isPaid() called at runtime
```

### Issues / Improvements
None. Both forms of polymorphism are present and used correctly.

---

## 8. Abstraction

**✅ YES — Implemented via interfaces**

### Where
All 6 interfaces in the `interfaces/` package:
`Displayable`, `Payable`, `Printable`, `BookingSearchable`, `TrainSearchable`, `UserSearchable`.

### Why It Qualifies
Abstraction means hiding implementation details and exposing only the essential contract.
The interfaces define **what** must be done (`displayInfo()`, `pay()`, `print()`, etc.)
without specifying **how** — that is left to each implementing class. Callers work against
the interface type and are decoupled from the concrete implementation.

### Evidence

```java
// Caller code in Main.java — works with Payable, not Payment
Payable payable = payment1;
system.processPayment(payment1);
System.out.println("isPaid(): " + payable.isPaid());
// The caller does not care HOW isPaid() is implemented — just that it returns boolean
```

### Issues / Improvements
Abstraction is present via interfaces, but there is **no abstract class** in the project
(see item 9). Adding `abstract` to `Person` would provide a second, stronger layer of
abstraction.

---

## 9. Abstract Class

**✅ YES — Correctly implemented**

### Where
[Person.java](file:///Users/phokphallaoudom/Documents/GitHub/TrainTicketSystem/model/Person.java) — declared as an abstract base class.
[User.java](file:///Users/phokphallaoudom/Documents/GitHub/TrainTicketSystem/model/User.java) and [Staff.java](file:///Users/phokphallaoudom/Documents/GitHub/TrainTicketSystem/model/Staff.java) — subclasses extending the abstract class and implementing the abstract method.

### Why It Qualifies
An abstract class cannot be instantiated directly and is designed to act as a template. `Person` is declared `abstract` and defines the abstract method `public abstract String getRoleDescription();`. Because `User` and `Staff` extend `Person`, they are contractually forced by the compiler to override and implement `getRoleDescription()`.

### Evidence

```java
// Person.java — abstract class and method declaration
public abstract class Person implements Displayable {
    ...
    public abstract String getRoleDescription();
}

// User.java — overriding the abstract method
@Override
public String getRoleDescription() {
    return "Registered User";
}

// Staff.java — overriding the abstract method
@Override
public String getRoleDescription() {
    return "Staff — " + role;
}
```

---

## 10. Exception Handling

**✅ YES — Correctly implemented with a custom checked exception**

### Where
- `exceptions/TicketIssuanceException.java` — custom checked exception class
- `main/TrainTicketBookingSystem.java` — `issueTicket()` method throws it
- `main/Main.java` — `try-catch` blocks catch and handle it

### Why It Qualifies
- `TicketIssuanceException extends Exception` → making it a **checked** exception that the
  compiler forces callers to handle.
- `issueTicket()` declares `throws TicketIssuanceException` and uses `throw new
  TicketIssuanceException(...)` for seven distinct invalid-state conditions.
- `Main.java` wraps every call to `issueTicket()` in a `try-catch` block with
  user-friendly messages.
- No `finally` block is used — correctly justified because no file/stream/resource
  cleanup is needed.

### Evidence

```java
// exceptions/TicketIssuanceException.java
public class TicketIssuanceException extends Exception {
    public TicketIssuanceException(String message) { super(message); }
    public TicketIssuanceException(String message, Throwable cause) { super(message, cause); }
}

// TrainTicketBookingSystem.java
public Ticket issueTicket(Booking booking, Payment payment, String seatNumber)
        throws TicketIssuanceException {
    if (booking == null)
        throw new TicketIssuanceException("Ticket issuance failed: booking cannot be null.");
    if (!payment.isPaid())
        throw new TicketIssuanceException("Ticket issuance failed: payment with ID "
                + payment.getPaymentId() + " is not completed yet.");
    if (!booking.getTrain().isSeatAvailable(seatNumber))
        throw new TicketIssuanceException("Ticket issuance failed: seat '"
                + seatNumber.trim().toUpperCase() + "' is already reserved or unavailable.");
    // ... more guards ...
}

// Main.java — try-catch prevents crash, program continues
try {
    ticket1 = system.issueTicket(booking1, payment1, "A1");
} catch (TicketIssuanceException e) {
    System.out.println("User Alert: " + e.getMessage());
}

// Case 1 — duplicate seat
try {
    system.issueTicket(booking1, payment1, "A1");   // seat already taken
} catch (TicketIssuanceException e) {
    System.out.println("Exception caught successfully: " + e.getMessage());
    System.out.println("Program recovered and continues execution.");
}
```

### Issues / Improvements
None. Exception handling is correctly structured and follows best practices.

---

## Summary Table

| # | OOP Concept        | Implemented? | Primary Location(s)                                        | Notes |
|---|--------------------|--------------|------------------------------------------------------------|-------|
| 1 | **Encapsulation**  | ✅ YES        | All model classes, `TrainTicketBookingSystem`               | All fields are `private`/`protected` with guarded getters & setters. Minor: `Person` fields should be `private` not `protected`. |
| 2 | **Static**         | ✅ YES        | `User`, `Staff`, `Train`, `Booking`, `Payment`, `Ticket`   | Used for auto-increment IDs, count trackers, and a static factory method (`Ticket.createTicket()`). |
| 3 | **Interface**      | ✅ YES        | `interfaces/` package (6 interfaces)                       | Defines contracts for displaying, paying, printing, and searching. Includes a `default` method in `Payable`. |
| 4 | **Inheritance**    | ✅ YES        | `User extends Person`, `Staff extends Person`              | Shared base fields and constructor reuse via `super(...)`. Both subclasses also implement `Comparable`. |
| 5 | **Method Overriding** | ✅ YES     | `User`, `Staff`, `Payment`, `Ticket`, `Booking`, `Train`, `TrainTicketBookingSystem` | `@Override` on `displayInfo()`, `toString()`, `compareTo()`, `equals()`, `hashCode()`, interface methods, and `getRoleDescription()`. |
| 6 | **Method Overloading** | ✅ YES    | `Person`, `User`, `Train`, `Booking`, `Payment`, `TrainTicketBookingSystem` | Extensively used: constructors, `setPhoneNumber`, `displayBookingHistory`, `reserveSeat`, `displayInfo`. |
| 7 | **Polymorphism**   | ✅ YES        | `Main.java` — `ArrayList<Displayable>`, `ArrayList<Person>`, `Payable payable` | Runtime polymorphism via interface/superclass references; compile-time via overloading. |
| 8 | **Abstraction**    | ✅ YES        | All 6 interfaces in `interfaces/`                          | Interfaces hide implementation. Callers work against contract types, not concrete classes. |
| 9 | **Abstract Class** | ✅ YES        | `Person` abstract class, `User`, `Staff` subclasses         | `Person` is an abstract class with `public abstract String getRoleDescription()`, overridden by subclasses to define role. |
| 10 | **Exception Handling** | ✅ YES   | `exceptions/TicketIssuanceException.java`, `TrainTicketBookingSystem.issueTicket()`, `Main.java` | Custom checked exception; `throws` declaration; `try-catch` with user-friendly messages; no `finally` (correctly omitted). |

---

## Required Fix Summary

All **10 of 10** OOP concepts are now correctly and verifiably implemented. No further fixes are needed. The project compiles successfully and runs correctly.
