# Train Ticket System — QA Analysis & UML Diagrams

As a Senior QA Analyst, Business Analyst, and System Reviewer, I have reviewed the proposed architecture and flows for the Train Ticket System. Before development or further iterations begin, it is critical to address several systemic gaps, ambiguities, and edge cases.

---

## 1. System Understanding

Based on the proposed design, the system functions as a console-based, point-to-point railway reservation manager.

**End-to-End Flow:**
1. A **User** registers by providing basic details (name, age, gender, phone) and receives a unique User ID.
2. The user browses pre-seeded **Trains** that operate on static **Routes**.
3. The user initiates a **Booking** for a specific Train and `TicketClass`. A seat is allocated, and the `PriceCalculator` determines the cost. The booking is placed in a `PENDING` state, locking the seat.
4. The user completes a **Payment** using an external-simulated method (CASH, ABA, WING, KHQR).
5. Upon successful payment, the Booking transitions to `CONFIRMED`, and a **Ticket** is automatically issued.
6. The user can cancel the booking at any time (`PENDING` or `CONFIRMED`), which releases the seat back to the train.

**System Boundaries:**
- The system lacks external integrations (no real payment gateway, no database).
- There is no authentication layer; the system relies on manual User ID entry.

---

## 2. Clarification Questions

### User Lifecycle
* **Authentication:** How does a user securely log back in? Currently, anyone knowing a `userId` can view or cancel bookings.
* **Updates:** Can a user update their phone number or name if they make a mistake?
* **Duplication:** What prevents a user from registering 10 times with the exact same name and phone number?
* **Deletion:** Is there a process for users to delete their accounts (GDPR/privacy compliance)? If so, what happens to their past financial transactions?

### Train & Route Lifecycle
* **Schedules:** Trains have a `Route` but no departure dates or times. Is a train considered a physical vehicle, or a scheduled trip? If a user books "Star Express", what day are they traveling?
* **Dynamic Changes:** Who manages trains? Can an administrator add or remove trains dynamically?
* **Intermediate Stops:** The `Route` assumes strictly point-to-point travel. What if the train stops at multiple stations? Can a user book a partial leg of the journey?

### Booking Lifecycle
* **Cart Abandonment:** A `PENDING` booking locks a seat. If a user closes the console before paying, does the seat remain locked forever?
* **Group Bookings:** Can a user book 3 seats at once, or do they have to go through the booking menu 3 separate times?
* **Modification:** Can a user change their seat or upgrade their class after a booking is created but before payment?

### Payment & Ticket Lifecycle
* **Refunds:** The system allows cancelling a `CONFIRMED` (paid) booking. How is the refund processed? Is it manual, or does the system need to track refund states?
* **Ticket Reissuance:** If a user loses their ticket ID, can they regenerate the ticket from their booking?

---

## 3. Missing Requirements & Business Rules

1. **Booking Timeout Rule:** There must be a TTL (Time-to-Live) for `PENDING` bookings (e.g., 15 minutes). If unpaid, the system must auto-cancel them and release the seats to prevent deadlocks.
2. **Travel Dates & Times:** A massive missing component. Bookings need a `travelDate` and `departureTime`. A single `Train` object currently acts as an infinite capacity vehicle without time constraints.
3. **Refund Policy:** A business rule must dictate what happens when a paid booking is cancelled. (e.g., "Full refund if >24 hours, 50% refund otherwise").
4. **Data Persistence:** The system resets entirely on exit. A requirement for database or file storage must be defined before production.
5. **Partial Payments / Overpayments:** Rules defining strict matching of payment amounts to booking prices are missing.

---

## 4. Potential Design Problems & Edge Cases

* **Concurrency Issues:** In a multi-user environment, two users might try to auto-reserve the last seat simultaneously. The seat allocation must be thread-safe.
* **Orphan Records:** If a Train is deleted by an admin, existing Bookings linked to that Train object will have dangling references or corrupt data.
* **State Transition Bypass:** Code must guarantee that a `CANCELLED` booking cannot be magically paid for or confirmed later.
* **Idempotency:** Paying for the same booking twice should be strictly blocked by the system.

---

# UML Deliverables

Below are the PlantUML diagrams defining the finalized understanding of the system. You can copy these blocks directly into a PlantUML viewer.

### 1. Use Case Diagram

```plantuml
@startuml
left to right direction
actor "Passenger (User)" as User
actor "System Administrator" as Admin

package "Train Ticket System" {
  usecase "Register User" as UC_Reg
  usecase "View Trains" as UC_View
  usecase "Search Trains By Route" as UC_Search
  usecase "Filter Trains" as UC_Filter
  usecase "Sort Trains" as UC_Sort
  usecase "Create Booking" as UC_Book
  usecase "Make Payment" as UC_Pay
  usecase "Cancel Booking" as UC_Cancel
  usecase "View My Bookings" as UC_History
  usecase "View Ticket" as UC_Ticket

  usecase "Calculate Ticket Price" as UC_CalcPrice
  usecase "Allocate Seat" as UC_AllocSeat
  usecase "Issue Ticket" as UC_Issue
  usecase "Release Seat" as UC_RelSeat

  UC_Book ..> UC_CalcPrice : <<include>>
  UC_Book ..> UC_AllocSeat : <<include>>
  UC_Pay ..> UC_Issue : <<include>>
  UC_Cancel ..> UC_RelSeat : <<include>>
}

User --> UC_Reg
User --> UC_View
User --> UC_Search
User --> UC_Filter
User --> UC_Sort
User --> UC_Book
User --> UC_Pay
User --> UC_Cancel
User --> UC_History
User --> UC_Ticket

Admin --> UC_View
@enduml
```

---

### 2. Class Diagram

```plantuml
@startuml
skinparam classAttributeIconSize 0

interface Displayable {
  + displayInfo(): void
}
interface Payable {
  + pay(): boolean
  + isPaid(): boolean
  + processPayment(): void
}
interface Printable {
  + print(): void
}
interface BookingSearchable {
  + searchBookingById(bookingId: int): Booking
}
interface TrainSearchable {
  + searchTrainById(trainId: int): Train
}
interface UserSearchable {
  + searchUserById(userId: int): User
  + searchUserByName(name: String): User
}

abstract class Person {
  - name: String
  - age: int
  - gender: String
  - phoneNumber: String
  + getRoleDescription(): String
}
Person ..|> Displayable

class User {
  - userId: int
  - bookings: ArrayList<Booking>
  + getRoleDescription(): String
}
User -up-|> Person

class Staff {
  - staffId: int
  - role: String
  + getRoleDescription(): String
}
Staff -up-|> Person

class Route {
  - departureStation: String
  - destinationStation: String
}
Route ..|> Displayable

class Train {
  - trainId: int
  - trainName: String
  - economyCapacity: int
  - businessCapacity: int
  - firstClassCapacity: int
  + reserveSeat(seatNumber: String, ticketClass: TicketClass): boolean
  + reserveSeat(ticketClass: TicketClass): String
  + releaseSeat(seatNumber: String): void
}
Train ..|> Displayable
Train *-- "1" Route
Train --> "1" TrainType

class Booking {
  - bookingId: int
  - seatNumber: String
  - price: double
  + confirmBooking(): void
  + cancelBooking(): void
}
Booking ..|> Displayable
Booking --> "1" User
Booking --> "1" Train
Booking --> "1" TicketClass
Booking --> "1" BookingStatus

class Payment {
  - paymentId: int
  - isPaid: boolean
}
Payment ..|> Displayable
Payment ..|> Payable
Payment ..|> Printable
Payment --> "1" Booking
Payment --> "1" PaymentMethod

class Ticket {
  - ticketId: int
  - seatNumber: String
  - issueDate: LocalDate
  + {static} createTicket(booking: Booking, payment: Payment, seatNumber: String): Ticket
}
Ticket ..|> Displayable
Ticket ..|> Printable
Ticket --> "1" Booking

class TrainTicketBookingSystem {
  - users: HashMap<Integer, User>
  - trains: ArrayList<Train>
  - bookings: ArrayList<Booking>
  + addUser(user: User): boolean
  + addTrain(train: Train): boolean
  + createBooking(booking: Booking): boolean
  + processPayment(payment: Payment): boolean
  + cancelBooking(bookingId: int): boolean
  + issueTicket(booking: Booking, payment: Payment, seatNumber: String): Ticket
}
TrainTicketBookingSystem ..|> Displayable
TrainTicketBookingSystem ..|> UserSearchable
TrainTicketBookingSystem ..|> TrainSearchable
TrainTicketBookingSystem ..|> BookingSearchable

class PriceCalculator {
  - {static} BASE_PRICE: double
  + {static} calculatePrice(trainType: TrainType, ticketClass: TicketClass): double
}

enum BookingStatus {
  PENDING
  CONFIRMED
  CANCELLED
}
enum TicketClass {
  ECONOMY
  BUSINESS
  FIRST_CLASS
}
enum TrainType {
  REGULAR
  EXPRESS
  LUXURY
}
enum PaymentMethod {
  CASH
  ABA
  WING
  KHQR
}
@enduml
```

---

### 3. Sequence Diagrams

#### A. Sequence Diagram — Register User

```plantuml
@startuml
actor User
participant "Main (Menu)" as Menu
participant "TrainTicketBookingSystem" as System
participant "User (Object)" as UserObj

User -> Menu: Select "Register User"
Menu -> Menu: Prompt for name, age, gender, phone
Menu -> UserObj **: new User(name, age, gender, phone)
Menu -> System: addUser(user)
activate System
System -> System: Store in users Map
System --> Menu: return success
deactivate System
Menu --> User: Display "User registered successfully"
@enduml
```

#### B. Sequence Diagram — Search Train By Route

```plantuml
@startuml
actor User
participant "Main (Menu)" as Menu
participant "TrainTicketBookingSystem" as System
participant "Train" as Train

User -> Menu: Select "Search Train By Route"
Menu -> Menu: Prompt for Departure & Destination
Menu -> System: searchTrainByRoute(dep, dest)
activate System
System -> System: Create empty List<Train>
loop for each Train in trains
    System -> Train: getRoute()
    Train --> System: Route
    alt Route matches dep & dest
        System -> System: add Train to List
    end
end
System --> Menu: return List<Train>
deactivate System
Menu --> User: Display matching trains
@enduml
```

#### C. Sequence Diagram — Create Booking

```plantuml
@startuml
actor User
participant "Main (Menu)" as Menu
participant "TrainTicketBookingSystem" as System
participant "Train" as Train
participant "PriceCalculator" as PriceCalc
participant "Booking (Object)" as BookingObj

User -> Menu: Select "Create Booking"
Menu -> System: searchUserById(userId)
System --> Menu: User object
Menu -> System: searchTrainById(trainId)
System --> Menu: Train object
Menu -> Menu: Prompt for TicketClass & Seat pref
Menu -> Train: reserveSeat(seat, ticketClass)
activate Train
Train -> Train: Check class capacity & prefix
Train --> Menu: return true (success)
deactivate Train
Menu -> BookingObj **: new Booking(user, train, ticketClass, seat)
activate BookingObj
BookingObj -> PriceCalc: calculatePrice(trainType, ticketClass)
activate PriceCalc
PriceCalc --> BookingObj: price
deactivate PriceCalc
BookingObj --> Menu: return Booking (PENDING)
deactivate BookingObj
Menu -> System: createBooking(booking)
System --> Menu: return success
Menu --> User: Display "Booking created (PENDING)"
@enduml
```

#### D. Sequence Diagram — Make Payment

```plantuml
@startuml
actor User
participant "Main (Menu)" as Menu
participant "TrainTicketBookingSystem" as System
participant "Payment (Object)" as PaymentObj
participant "Booking" as Booking
participant "Ticket (Object)" as TicketObj

User -> Menu: Select "Make Payment"
Menu -> System: searchBookingById(bookingId)
System --> Menu: Booking object
Menu -> Menu: Prompt for PaymentMethod
Menu -> PaymentObj **: new Payment(booking, method)
Menu -> System: processPayment(payment)
activate System
System -> PaymentObj: isPaid()
PaymentObj --> System: true
System -> Booking: confirmBooking()
activate Booking
Booking -> Booking: set status = CONFIRMED
Booking --> System: void
deactivate Booking
System -> TicketObj **: Ticket.createTicket(booking, payment, seat)
System -> System: Add Ticket to tracking list
System --> Menu: return true
deactivate System
Menu --> User: Display "Payment successful & Ticket issued"
@enduml
```

#### E. Sequence Diagram — Cancel Booking

```plantuml
@startuml
actor User
participant "Main (Menu)" as Menu
participant "TrainTicketBookingSystem" as System
participant "Booking" as Booking
participant "Train" as Train

User -> Menu: Select "Cancel Booking"
Menu -> System: searchBookingById(bookingId)
System --> Menu: Booking object
Menu -> System: cancelBooking(bookingId)
activate System
System -> Booking: cancelBooking()
activate Booking
Booking -> Booking: set status = CANCELLED
Booking -> Booking: getTrain()
Booking --> Train: releaseSeat(seatNumber)
activate Train
Train -> Train: Remove seat from reserved sets
Train --> Booking: void
deactivate Train
Booking --> System: void
deactivate Booking
System --> Menu: return true
deactivate System
Menu --> User: Display "Booking cancelled successfully"
@enduml
```
