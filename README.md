# TrainTicketSystem

[![Java Version](https://img.shields.io/badge/Java-8%2B-orange.svg)](https://www.oracle.com/java/)
[![Version Control](https://img.shields.io/badge/Version%20Control-Git-blue.svg)](https://git-scm.com/)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](https://opensource.org/licenses/MIT)

A clean, professional, console-based railway reservation system written in Java. This project showcases advanced Object-Oriented Programming (OOP) design patterns, encapsulation, and interface-driven architectures across 11 key OOP missions.

---

## 📖 Overview

The **Train Ticket System** is an interactive, console-driven application that simulates a point-to-point train reservation system. It allows users to register, search for trains by route, filter and sort available trains, book seats in specific classes (Economy, Business, and First Class), process simulated payments, and issue tickets or cancel bookings.

---

## ✨ Features

- **Interactive Command-Line Interface**: A robust 11-option CLI menu that handles invalid inputs and gracefully guides user transactions.
- **Seat Allocation & Prefix Validation**: Validates seat requests against capacity and automatically checks prefix letters (`E` for Economy, `B` for Business, `F` for First Class).
- **Dynamic Pricing Engine**: Calculates final ticket prices on-the-fly using train and class multipliers.
- **Polymorphic Search, Sort & Filter**: Filters trains by type or availability and sorts by base price, available seats, or train speed/type.
- **Booking & Ticket Lifecycle Management**: Transition bookings from `PENDING` to `CONFIRMED` upon payment, automatically releasing seats back to capacity on cancellation.
- **Robust Exception Handling**: Employs custom checked exceptions to prevent invalid transactions (e.g., booking a filled seat or double payment).

---

## 🛠️ Technologies Used

- **Programming Language**: Java (Java 8+ supported, leveraging Default Methods and the `java.time` API)
- **Frameworks**: None (Pure Core Java SE)
- **Libraries**: Java Standard Library (`java.util`, `java.time`, `java.io`)
- **Build System**: Standard javac compiler (class files compiled to `bin/` directory)
- **IDE Compatibility**: VS Code, Eclipse, IntelliJ IDEA
- **Version Control**: Git

---

## 📂 Project Structure

```
TrainTicketSystem/
├── bin/                       # Compiled class files
├── enums/                     # Type-safe enumerations
│   ├── BookingStatus.java     # PENDING, CONFIRMED, CANCELLED
│   ├── PaymentMethod.java     # CASH, ABA, WING, KHQR
│   ├── TicketClass.java       # ECONOMY, BUSINESS, FIRST_CLASS
│   └── TrainType.java         # REGULAR, EXPRESS, LUXURY
├── exceptions/                # Custom checked exceptions
│   └── TicketIssuanceException.java
├── interfaces/                # Behavioral contracts
│   ├── BookingSearchable.java
│   ├── Displayable.java
│   ├── Payable.java
│   ├── Printable.java
│   ├── TrainSearchable.java
│   └── UserSearchable.java
├── main/                      # Entry point and system coordinator
│   ├── Main.java              # Interactive console UI
│   └── TrainTicketBookingSystem.java
├── model/                     # Core domain objects
│   ├── Booking.java           # Central transactional coordinator
│   ├── Payment.java           # Encapsulates financial state
│   ├── Person.java            # Abstract base class
│   ├── Route.java             # Value object for travel stations
│   ├── Staff.java             # Employee representation
│   ├── Ticket.java            # Confirmed booking document
│   ├── Train.java             # Station capacity & seat tracker
│   └── User.java              # Customer representation
└── service/                   # Domain logic helpers
    └── PriceCalculator.java   # Dynamic pricing multiplier engine
```

---

## 🎯 Mission Progress

| Mission | Topic | Status | What is Implemented |
| :--- | :--- | :--- | :--- |
| **OOP Mission 1** | Encapsulation | **Completed** | Strict use of private properties, getters/setters guard validation, defensive array list copying, and capacity/pricing controls. |
| **OOP Mission 2** | Static | **Completed** | Shared auto-increment ID counters (`nextUserId`, etc.), static pricing calculator utility, and static factory constructor (`Ticket.createTicket()`). |
| **OOP Mission 3** | Interface | **Completed** | Declarative system contracts (`Displayable`, `Payable`, `Printable`, search interfaces) and use of Java 8 default methods. |
| **OOP Mission 4** | Inheritance | **Completed** | Class hierarchy establishing abstract `Person` as base, and concrete classes `User` and `Staff` inheriting common state and using `super(...)` constructors. |
| **OOP Mission 5** | Overriding | **Completed** | Polymorphic method customization with `@Override` annotation for abstract methods (`getRoleDescription`), string formatting, and equals/hashCode comparisons. |
| **OOP Mission 6** | Overloading | **Completed** | Signature overloading for class constructors (e.g. `Booking` dates format) and seat reservation methods (e.g., custom seat label vs auto-allocation). |
| **OOP Mission 7** | Polymorphism | **Completed** | Storing and executing logic across lists of generic base references (`Person` subclasses, interfaces list) resolved dynamically at runtime. |
| **OOP Mission 8** | Abstraction | **Completed** | Hiding complex system data management by exposing functionality strictly through high-level coordinate search and display interfaces. |
| **OOP Mission 9** | Abstract Class | **Completed** | Abstracting common properties into the `Person` abstract class, which forces sub-entities to implement template methods like `getRoleDescription()`. |
| **OOP Mission 10** | Exception Handling | **Completed** | Defining custom checked `TicketIssuanceException` thrown in booking managers, propagated, and gracefully handled in the UI loop via `try-catch`. |
| **OOP Mission 11** | Other Inheritance | **Completed** | Interface inheritance implementing Java's built-in `Comparable<User>` and `Comparable<Booking>` for sorting operations, along with multiple interface inheritance. |

---

## 🚀 How to Run

Follow these instructions to compile and run the project locally via terminal:

### 1. Compile the Project
From the repository root directory, run:
```bash
javac -d bin -sourcepath . main/Main.java
```
This compiles all dependent Java classes and places the `.class` bytecodes inside the `bin/` directory.

### 2. Execute the Application
Once compiled successfully, run the console menu:
```bash
java -cp bin main.Main
```
