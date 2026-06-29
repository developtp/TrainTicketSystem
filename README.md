# TrainTicketSystem

![Java](https://img.shields.io/badge/Language-Java-orange.svg)
![Paradigm](https://img.shields.io/badge/Paradigm-OOP-blue.svg)
![Status](https://img.shields.io/badge/Status-Completed-success.svg)

An online train ticket booking application written in Java. This project serves as a comprehensive practice portfolio demonstrating **10 foundational Object-Oriented Programming (OOP)** concepts, structured architecture, custom exception handling, and robust validation mechanisms.

---

## Overview

The **TrainTicketSystem** models real-world train ticket transactions:
* **Users** search for **Trains**, create **Bookings**, and settle payments.
* **Payments** are processed through different channels (e.g., ABA, Wing) and checked for success.
* **Tickets** are issued with specific seat assignments once payments are completed successfully.
* The system is coordinated by the [TrainTicketBookingSystem](file:///l:/CamTech/OOCY2GIT/TrainTicketSystem/main/TrainTicketBookingSystem.java) and verified by a test harness in [Main.java](file:///l:/CamTech/OOCY2GIT/TrainTicketSystem/main/Main.java).

The core learning objective is to show clean, idiomatic Java designs that move beyond basic syntax to implement a decoupled, resilient, and exception-safe architecture.

---

## Features

* **Complete Transactional Flow**: Seamlessly coordinates booking creation, payment tracking, and ticketing.
* **Strict State Guards**: Prevents ticket issuance for unpaid bookings or double-booking of seats.
* **Double-Layered Searching**: Provides both numeric ID and name-based (case-insensitive) search lookups.
* **Automated Seat Management**: Supports specific seat selections or auto-assignment of the next available seat.
* **Polymorphic Printing**: Formats distinct printable details for receipt invoices and boarding passes via shared interfaces.
* **Robust Error Handling**: Incorporates custom checked exceptions to recover cleanly from failures without application crashes.

---

## Technologies Used

* **Programming Language**: Java (JDK 8 or higher)
* **Standard Library**: Java Collections Framework (`ArrayList`, `HashMap`, `HashSet`), Java Date-Time API (`LocalDate`)
* **Build System**: Native compilation (compiled directly via `javac`)
* **IDE Configuration**: Visual Studio Code (compatible with standard Java IDEs like Eclipse or IntelliJ IDEA)
* **Version Control**: Git

---

## Project Structure

The project code is organized into modular packages to isolate concerns:

```text
TrainTicketSystem/
├── exceptions/
│   └── TicketIssuanceException.java   # Checked exceptions for ticket allocation failures
├── interfaces/
│   ├── BookingSearchable.java         # Contracts for locating bookings
│   ├── Displayable.java               # Contract for standard console outputs
│   ├── Payable.java                   # Contracts and default methods for payments
│   ├── Printable.java                 # Contracts for printing tickets/receipts
│   ├── TrainSearchable.java           # Contracts for locating trains
│   └── UserSearchable.java            # Contracts for locating users
├── main/
│   ├── Main.java                      # Program entry point and automated test suite
│   └── TrainTicketBookingSystem.java  # System coordinator / controller
├── model/
│   ├── Booking.java                   # Links a User, Train, and date details
│   ├── Payment.java                   # Handles transaction states
│   ├── Person.java                    # Abstract base template for users/staff
│   ├── Staff.java                     # System staff role definitions
│   ├── Ticket.java                    # Issued seat tickets
│   ├── Train.java                     # Represents trains, routes, and seats
│   └── User.java                      # Customer model with booking histories
└── state.md                           # OOP Concept checklist and progress state
```

---

## Mission Progress

Below is the status of the 10 learning milestones designed to review OOP concepts:

| Mission | Topic | Status | What is Implemented |
| ------- | ----- | ------ | ------------------- |
| **Mission 1** | **Encapsulation** | **Completed** | `private` and `protected` fields in models (like [Person.java](file:///l:/CamTech/OOCY2GIT/TrainTicketSystem/model/Person.java)) with data validation in setters (e.g. non-negative age guards). |
| **Mission 2** | **Static Members** | **Completed** | Shared auto-increment ID counters (`nextUserId`, `nextTrainId`, etc.) and a static factory constructor method `Ticket.createTicket()`. |
| **Mission 3** | **Interfaces** | **Completed** | 6 interface contracts defining actions (`Displayable`, `Printable`, `Payable` with default methods, and Searchables). |
| **Mission 4** | **Inheritance** | **Completed** | Class hierarchy where [User.java](file:///l:/CamTech/OOCY2GIT/TrainTicketSystem/model/User.java) and [Staff.java](file:///l:/CamTech/OOCY2GIT/TrainTicketSystem/model/Staff.java) extend the abstract base [Person.java](file:///l:/CamTech/OOCY2GIT/TrainTicketSystem/model/Person.java). |
| **Mission 5** | **Method Overriding** | **Completed** | Redefining behavior: overriding `displayInfo()`, `toString()`, `equals()`, `hashCode()`, `compareTo()`, and `getRoleDescription()`. |
| **Mission 6** | **Method Overloading** | **Completed** | Multiple variants for constructors and methods (e.g. `setPhoneNumber`, `displayBookingHistory`, `reserveSeat`, and `searchUserByName`). |
| **Mission 7** | **Polymorphism** | **Completed** | Dynamic runtime dispatch using collections of interfaces (`ArrayList<Displayable>`, `ArrayList<Person>`) and compile-time overloading. |
| **Mission 8** | **Abstraction** | **Completed** | Callers decouple dependencies by programing against interface contracts rather than concrete implementations. |
| **Mission 9** | **Abstract Class** | **Completed** | [Person.java](file:///l:/CamTech/OOCY2GIT/TrainTicketSystem/model/Person.java) declared `abstract` with abstract method `getRoleDescription()`, forcing subclasses to implement it. |
| **Mission 10** | **Exception Handling** | **Completed** | Checked exceptions ([TicketIssuanceException.java](file:///l:/CamTech/OOCY2GIT/TrainTicketSystem/exceptions/TicketIssuanceException.java)) thrown for business violations and caught in `try-catch` blocks. |

---

## Skills Demonstrated

* **Robust Class Modeling**: Translating domain operations into cohesive classes with proper relationships (association, composition, inheritance).
* **Defensive Programming**: Copying collections returned to caller contexts (`getBookingsCopy()`) and validating string contents (trimming and null default guards).
* **Interface-Driven Design**: Using decoupling mechanisms to ensure systems can process different types of displayable or printable objects cleanly.
* **Custom Checked Exceptions**: Defining clear failure boundaries in business actions to prevent runtime crashes and allow programmatic recovery.
* **Standard Object Contracts**: Implementing standard Java methods (`toString`, `equals`, `hashCode`, `Comparable`) for correct data structures integration (e.g. `HashSet`, `HashMap`).

---

## Current Progress

* **Status**: 100% Completed
* All **10 OOP core concepts** are fully implemented, checked, and integrated.
* The test execution suite in [Main.java](file:///l:/CamTech/OOCY2GIT/TrainTicketSystem/main/Main.java) confirms that the application behaves correctly under standard usage patterns and successfully catches and recovers from exception cases.

---

## How to Run

Follow these instructions to compile and execute the project locally.

### 1. Compile the Source Code
Compile all Java files into a dedicated binary destination directory (`bin`):
```bash
# From the project root directory:
javac -d bin exceptions/TicketIssuanceException.java interfaces/*.java model/*.java main/*.java
```

### 2. Execute the Test Harness
Run the compiled `Main` class to run all validation and feature tests:
```bash
java -cp bin main.Main
```

---

## Repository Purpose

This project acts as a practical demonstration of OOP software development principles in Java. It illustrates the evolutionary progress of a simple application to one featuring loose coupling, type hierarchies, collection lookups, and solid runtime error resilience.

---

## Authors

Developed and maintained by:
* **Thou Panha**
* **Phok Phalla Oudom**
* **Menghong Soeung**
