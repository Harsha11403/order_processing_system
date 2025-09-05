# Order Processing System

This project is a simple event-driven backend that simulates how an e-commerce platform handles orders.  
It processes incoming events such as order creation, payments, shipping, and cancellations, updates the order state, and notifies observers (logs and alerts).

---

## Requirements

- **Java 17** or newer  
- **Maven 3.x**  
- A terminal or IDE (Eclipse, IntelliJ, or Spring Tool Suite) to run the application  

---

## How It Works

1. Events are stored in a `events.ndjson` file (`src/main/resources/events.ndjson`). Each line is one JSON object.  
2. The application reads the file, deserializes events into Java classes, and sends them to the processor.  
3. The `EventProcessor` updates order states (`PENDING → PARTIALLY_PAID → PAID → SHIPPED → CANCELLED`) according to the rules.  
4. Observers are notified when an event changes the order’s state. Currently, there are two:
   - `LoggerObserver` → prints logs to the console.
   - `AlertObserver` → raises alerts for critical changes (like shipping or cancellation).  
5. At the end, the system prints the final state of all orders.

---

## Installation & Setup

1. Clone or unzip the project into your workspace.  
2. Navigate to the project directory.  
3. Build the project:

   ```bash
   mvn clean install
4. Run the processor:

   ```bash
   mvn exec:java -Dexec.mainClass="com.precize.project.order_processing_system.OrderProcessor"

By default it loads events from:

src/main/resources/events.ndjson

## Supported Events

1. **OrderCreated** – Creates a new order with items.  
2. **PaymentReceived** – Records a payment for an order.  
3. **ShippingScheduled** – Marks an order as scheduled for shipping.  
4. **OrderCancelled** – Cancels an order (unless already shipped).  

---

## Requirements

1. Java 11+  
2. Maven 3.6+  
3. NDJSON input file (`src/main/resources/events.ndjson`)  

## Project Structure

  ```bash
  src/main/java/com/precize/project/order_processing_system
  │
  ├── domain       # Core domain classes (Order, OrderStatus)
  ├── events       # Event definitions (abstract Event + subclasses)
  ├── ingest       # Reads and parses NDJSON input
  ├── observe      # Observer implementations (Logger, Alert)
  ├── process      # EventProcessor with business logic
  ├── repo         # In-memory OrderRepository
  └── OrderProcessor.java   # Main entry point

---

