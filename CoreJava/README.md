## Getting Started

Welcome to the VS Code Java world. Here is a guideline to help you get started to write Java code in Visual Studio Code.

## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

> If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there.

## Dependency Management

The `JAVA PROJECTS` view allows you to manage your dependencies. More details can be found [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).

# Interview Questions till now:

## 1. Optional class in Java

> **“`Optional` is a container object introduced in Java 8 that may or may not contain a non-null value. It is mainly used to represent the absence of a value explicitly and reduce the chances of `NullPointerException`, especially when dealing with method return values.”**

For example:

```java
Optional<String> name = Optional.ofNullable(getName());

name.ifPresent(System.out::println);
```

Here, if `getName()` returns `null`, the `Optional` is empty instead of directly holding `null`.

### Important methods

```java
Optional.of(value)              // value must NOT be null
Optional.ofNullable(value)      // value can be null
Optional.empty()                // empty Optional

optional.isPresent()
optional.isEmpty()              // Java 11+

optional.get()                  // gets value, but risky if empty
optional.orElse("default")      // default value
optional.orElseGet(() -> "default")
optional.orElseThrow()
optional.map(...)
optional.filter(...)
```

### `orElse()` vs `orElseGet()` — common interview question

```java
optional.orElse(expensiveMethod());
```

`expensiveMethod()` is evaluated **even if the Optional already has a value**.

```java
optional.orElseGet(() -> expensiveMethod());
```

The method is evaluated **only when the Optional is empty**.

### Senior-level point

> **“I wouldn't use `Optional` everywhere. Its primary purpose is to communicate that a method may legitimately have no result. I generally use it as a return type rather than as a field, method parameter, or collection element.”**

For example:

```java
public Optional<User> findUserById(Long id) {
    return Optional.ofNullable(userRepository.findById(id));
}
```

This makes the API contract explicit: **the user may or may not exist.**

**One-line interview summary:**

> **“Optional is a Java 8 container used to explicitly model the presence or absence of a value, primarily for return types, helping us avoid implicit null handling and reduce NullPointerException risks.”**

## 2. Intermediate vs Terminal operations in Java 8 Streams

> **“Stream operations are divided into intermediate and terminal operations. Intermediate operations transform or filter a stream and return another Stream, while terminal operations consume the stream and produce a final result or side effect.”**

### 1. Intermediate operations

They:

* Return a **new Stream**
* Are **lazy** — they don't execute immediately
* Can be **chained**
* Usually transform/filter data

Common examples:

```java
filter()
map()
flatMap()
distinct()
sorted()
limit()
skip()
peek()
```

Example:

```java
List<String> result = names.stream()
        .filter(name -> name.startsWith("A"))
        .map(String::toUpperCase)
        .toList();
```

Here:

```text
names.stream()
      ↓
   filter()     → intermediate
      ↓
    map()       → intermediate
      ↓
   toList()     → terminal
```

The `filter()` and `map()` **don't actually run when they are declared**. They execute when the terminal operation `toList()` is encountered.

---

### 2. Terminal operations

They:

* **End/consume the stream**
* Trigger execution of the preceding intermediate operations
* Return a **non-Stream result** or perform an action
* A stream generally **cannot be reused** after a terminal operation

Common examples:

```java
forEach()
collect()
toList()
reduce()
count()
min()
max()
findFirst()
findAny()
anyMatch()
allMatch()
noneMatch()
```

Example:

```java
long count = names.stream()
        .filter(name -> name.length() > 5)
        .count();
```

`filter()` → intermediate
`count()` → terminal

---

### ⭐ Very important interview point: laziness

If there is **no terminal operation**, intermediate operations don't execute.

```java
names.stream()
     .filter(name -> {
         System.out.println(name);
         return name.startsWith("A");
     });
```

Nothing gets printed because there is no terminal operation.

Add:

```java
.toList();
```

Now the pipeline executes.

---

### How to say it in an interview

> **“Intermediate operations are lazy and return another Stream, allowing us to build a pipeline such as filter → map → sort. A terminal operation triggers the pipeline and produces the final result, such as a List, count, boolean, or reduced value. Once a stream has been consumed by a terminal operation, it cannot normally be reused.”**

**Easy memory trick:**

**Intermediate = build the pipeline**
**Terminal = execute the pipeline**

## 3. Remove duplicates from a list of city names

```java
List<String> cities = List.of(
    "Delhi", "Mumbai", "Delhi", "Pune", "Mumbai", "Bangalore"
);

List<String> uniqueCities = cities.stream()
        .distinct()
        .toList();

System.out.println(uniqueCities);
```

**Output:**

```text
[Delhi, Mumbai, Pune, Bangalore]
```

### Interview explanation

> **“I would use the `distinct()` intermediate operation because it removes duplicate elements from the stream. Then I use `toList()` as the terminal operation to collect the unique city names into a list.”**

Flow:

```text
List
 ↓
stream()
 ↓
distinct()    ← intermediate
 ↓
toList()      ← terminal
 ↓
Unique List
```

### One important point

`distinct()` uses the elements' **`equals()` and `hashCode()`** to determine duplicates.

For `String`, this means:

```java
"Delhi".equals("Delhi")  // true
```

So duplicate city names are removed.

**Time complexity:** approximately **O(n)**
**Space complexity:** **O(n)** for tracking distinct elements.


## 4. Dependency Injection in SpringBoot


> **“Dependency Injection, or DI, is a design pattern where an object receives the dependencies it needs from an external source instead of creating those dependencies itself. In Spring Boot, the Spring IoC container creates and manages these objects, called beans, and injects them where required.”**

### Without Dependency Injection

Suppose `OrderService` needs `PaymentService`:

```java
class OrderService {

    private PaymentService paymentService = new PaymentService();

}
```

Here, `OrderService` is **creating its own dependency**.

Problems:

* Tight coupling
* Harder to test
* Difficult to replace the implementation

---

### With Dependency Injection

```java
@Service
class PaymentService {
}
```

```java
@Service
class OrderService {

    private final PaymentService paymentService;

    @Autowired
    public OrderService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
}
```

Spring sees both classes as beans and effectively does:

```text
Spring IoC Container

PaymentService bean
       ↓
       ↓ inject
       ↓
OrderService bean
```

So `OrderService` doesn't do:

```java
new PaymentService()
```

**Spring creates and provides it.**

---

### Why constructor injection is preferred

In modern Spring Boot, you'd usually write:

```java
@Service
class OrderService {

    private final PaymentService paymentService;

    public OrderService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
}
```

`@Autowired` is optional when there is only **one constructor**.

Advantages:

1. Dependency is explicit.
2. `final` fields can be used.
3. Object cannot be created without its required dependency.
4. Easier unit testing.

For example:

```java
PaymentService paymentService = mock(PaymentService.class);

OrderService service = new OrderService(paymentService);
```

---

### Three common types of injection

| Type            | Example                          | Recommendation            |
| --------------- | -------------------------------- | ------------------------- |
| **Constructor** | `OrderService(PaymentService p)` | ✅ Preferred               |
| **Setter**      | `setPaymentService(...)`         | For optional dependencies |
| **Field**       | `@Autowired PaymentService p`    | Generally avoid           |

### The senior-level answer

> **“Dependency Injection is one of the mechanisms through which Spring implements Inversion of Control. Instead of application classes constructing their dependencies, the Spring IoC container manages the beans and supplies their dependencies. I generally prefer constructor injection because dependencies are explicit, immutable, and easier to test.”**

**Key distinction:**

```text
IoC = Who controls object creation/lifecycle?
       → Spring

DI  = How are dependencies supplied?
       → Spring injects them
```

## 5. Offset in Kafka

In **Kafka**, an **offset is the unique position/sequence number of a message within a partition**.

Think of it like a **line number in a file**.

```text
Kafka Topic: payments
Partition 0

Offset     Message
  0        payment-101
  1        payment-102
  2        payment-103
  3        payment-104
  4        payment-105
```

If your consumer has processed up to offset `2`, Kafka knows:

> "This consumer has consumed messages through position 2."

### Why is offset important?

Because Kafka consumers need to know **where to continue reading**.

Suppose your consumer processes:

```text
0 → 1 → 2
```

Then it crashes.

When it restarts, it can continue from:

```text
3 → 4 → 5 ...
```

instead of starting from the beginning.

---

### Offset belongs to a partition

This is VERY important.

Offsets are **not global across a topic**.

```text
Topic: payments

Partition 0:
Offset 0
Offset 1
Offset 2
Offset 3

Partition 1:
Offset 0
Offset 1
Offset 2
```

So:

```text
Partition 0, Offset 3
```

and

```text
Partition 1, Offset 3
```

are different records.

The actual identity of a Kafka record is effectively:

```text
(topic, partition, offset)
```

---

### Consumer group and offset

Now comes the important real-world part.

Suppose:

```text
Topic: payments

Partition 0
0 → 1 → 2 → 3 → 4
```

Consumer group:

```text
payment-service
```

Your consumer processes:

```text
0
1
2
```

and commits its progress.

Kafka stores the consumer group's position.

Conceptually:

```text
payment-service
       |
       ↓
payments - partition 0
       |
       ↓
committed offset
```

If the consumer crashes, another consumer in the **same consumer group** can continue from the committed position.

---

### `commit` is important

There are two common approaches:

### Auto commit

Kafka periodically commits the consumer's progress.

```properties
enable.auto.commit=true
```

### Manual commit

Your application explicitly commits after processing.

```java
consumer.commitSync();
```

This gives you more control.

For example:

```text
Read message
     ↓
Process payment
     ↓
Database update successful
     ↓
Commit offset
```

This is often important because you don't want:

```text
Read message
     ↓
Commit offset ❌
     ↓
Application crashes
     ↓
Payment was never processed
```

The message may not be processed again because Kafka thinks you've already consumed it.

---

### One subtle but VERY important point

Suppose the current committed offset is:

```text
5
```

People sometimes say:

> "The consumer has processed offset 5."

But Kafka's committed offset is commonly interpreted as the **next offset to consume**, so a committed offset of `5` means:

```text
0  1  2  3  4  | 5  6  7
processed       ↑
                next
```

So:

**Committed offset = position from which consumption resumes.**

That's an excellent detail to know for interviews.

---

### Interview answer

If they ask:

> **What is an offset in Kafka?**

You can say:

> "An offset is a sequential identifier assigned to each record within a Kafka partition. It represents the record's position in that partition. Consumers use offsets to track their progress, and consumer groups commit offsets so that after a restart or rebalance they can resume consumption from the appropriate position."

And the mental model I'd want you to remember is:

```text
TOPIC
  │
  ├── Partition 0
  │      0 → 1 → 2 → 3 → 4
  │                  ↑
  │              offset
  │
  └── Partition 1
         0 → 1 → 2 → 3

Consumer Group
       ↓
tracks/commits its position
       ↓
can resume after restart/rebalance
```

If you're preparing for **Spring Boot + Kafka real work**, the next concepts you should connect to offset are **consumer group → partition assignment → offset commit → acknowledgment → at-least-once delivery → duplicate processing**.


## 6. Calling other REST API services

Yes. In **Java Spring Boot**, when one REST API needs to call another REST API, you typically use an HTTP client.

There are **3 common approaches**:

1. `RestClient` — modern, synchronous, recommended for new Spring applications
2. `WebClient` — reactive/non-blocking
3. `RestTemplate` — older, still seen in existing projects

For real-world Spring Boot work, I’d learn **`RestClient` first**, then `WebClient`.

### Example: API A calls API B

Suppose:

```text
Order Service
     |
     | POST /payments
     ↓
Payment Service
```

Payment service exposes:

```http
POST http://localhost:8081/payments
```

with:

```json
{
  "orderId": 101,
  "amount": 500
}
```

### 1. Create DTO

```java
public record PaymentRequest(
        Long orderId,
        double amount
) {}
```

### 2. Create REST client

```java
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class PaymentClient {

    private final RestClient restClient;

    public PaymentClient(RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl("http://localhost:8081")
                .build();
    }

    public String makePayment(PaymentRequest request) {

        return restClient.post()
                .uri("/payments")
                .body(request)
                .retrieve()
                .body(String.class);
    }
}
```

### 3. Call it from your Order API

```java
@RestController
@RequestMapping("/orders")
public class OrderController {

    private final PaymentClient paymentClient;

    public OrderController(PaymentClient paymentClient) {
        this.paymentClient = paymentClient;
    }

    @PostMapping
    public String createOrder() {

        PaymentRequest request =
                new PaymentRequest(101L, 500);

        String paymentResponse =
                paymentClient.makePayment(request);

        return paymentResponse;
    }
}
```

Now when someone calls:

```http
POST /orders
```

your application internally does:

```text
Client
   |
   | POST /orders
   ↓
OrderController
   |
   ↓
PaymentClient
   |
   | POST /payments
   ↓
Payment Service
   |
   ↓
Payment Response
   |
   ↓
OrderController
   |
   ↓
Client
```

### The key distinction

This is something you should be very comfortable explaining in interviews:

**Incoming REST API call:**

```text
Client → Your Spring Boot Controller
```

handled by:

```java
@RestController
@PostMapping
```

**Outgoing REST API call:**

```text
Your Spring Boot Service → Another REST API
```

handled by:

```java
RestClient
```

So if an interviewer asks:

> **"How does your Spring Boot service communicate with another microservice?"**

A good answer is:

> "We use an HTTP client such as Spring's `RestClient` to invoke the downstream service's REST endpoint. We construct the request with the appropriate HTTP method, URI, headers and body, then handle the response and downstream errors. In production, we'd also configure timeouts, authentication, logging/tracing and appropriate retry behavior."

That's the **real-work version** rather than just knowing the syntax.

For a **Spring Boot interview**, I would show a small **Order Service → Payment Service** example. It demonstrates actual service-to-service REST communication without unnecessary code.

### 1. Payment Service — API being called

```java
@RestController
@RequestMapping("/payments")
public class PaymentController {

    @PostMapping
    public PaymentResponse makePayment(
            @RequestBody PaymentRequest request) {

        return new PaymentResponse(
                request.orderId(),
                "SUCCESS"
        );
    }
}
```

DTOs:

```java
public record PaymentRequest(
        Long orderId,
        double amount
) {}

public record PaymentResponse(
        Long orderId,
        String status
) {}
```

---

### 2. Order Service — calling Payment Service

For modern Spring Boot, use `RestClient`.

```java
@Service
public class PaymentClient {

    private final RestClient restClient;

    public PaymentClient(RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl("http://payment-service:8081")
                .build();
    }

    public PaymentResponse makePayment(PaymentRequest request) {

        return restClient.post()
                .uri("/payments")
                .body(request)
                .retrieve()
                .body(PaymentResponse.class);
    }
}
```

Then your Order Service calls it:

```java
@RestController
@RequestMapping("/orders")
public class OrderController {

    private final PaymentClient paymentClient;

    public OrderController(PaymentClient paymentClient) {
        this.paymentClient = paymentClient;
    }

    @PostMapping
    public PaymentResponse createOrder() {

        PaymentRequest request =
                new PaymentRequest(101L, 500.0);

        return paymentClient.makePayment(request);
    }
}
```

### 3. What happens

```text
Client
  │
  │ POST /orders
  ▼
Order Service
  │
  │ RestClient
  │ POST /payments
  ▼
Payment Service
  │
  │ PaymentResponse
  ▼
Order Service
  │
  ▼
Client
```

### 4. If interviewer asks "How do you handle errors?"

Don't stop at the happy path. Show:

```java
public PaymentResponse makePayment(PaymentRequest request) {

    return restClient.post()
            .uri("/payments")
            .body(request)
            .retrieve()
            .onStatus(
                status -> status.value() == 404,
                (req, res) -> {
                    throw new RuntimeException(
                        "Payment service not found"
                    );
                }
            )
            .body(PaymentResponse.class);
}
```

In production, you'd also configure **timeouts, authentication, retries where appropriate, logging, tracing, and circuit-breaker/fallback behavior**.

### Interview answer

If they ask **"How do two Spring Boot microservices communicate?"**, say:

> "For synchronous communication, one Spring Boot service can call another service's REST endpoint using an HTTP client such as `RestClient` or `WebClient`. We send the appropriate HTTP method, headers and request body, deserialize the response into a DTO, and handle downstream errors, timeouts and authentication. For asynchronous communication, we can use Kafka instead of a synchronous REST call."

**One distinction worth remembering:**

```text
REST → synchronous request/response
Kafka → asynchronous event/message communication
```

For your interviews, I would be comfortable writing the **`RestClient` example above from memory**.

##
