# Why Java?

### 1. Platform Independent — WORA

```java
javac Hello.java   // .java → bytecode (.class)
java Hello         // JVM executes bytecode
```

**Why?** Same bytecode can run on any OS with a compatible JVM.

---

### 2. Simple & Robust

```java
int age = 30;              // simple syntax
String name = "Piyusha";

try {
    // code
} catch (Exception e) {    // robust error handling
}
```
Simple -> Java doesn't support complex features in cpp like multiple inheritance, operator overloading
Robust -> doesn't allow pointer arithmetic
---

### 3. Secure

When the JVM loads the class, it performs verification as part of class loading automatically.

```text
BankAccount.java
       │
       │ javac
       ▼
BankAccount.class
       │
       │ JVM loads
       ▼
Bytecode verification
       │
       ▼
Execution
```

The verifier checks that the bytecode satisfies JVM constraints.

---

### 4. Object-Oriented
Though java is not 100% Object oriented
```java
class Account {
    private double balance;       // Encapsulation

    void deposit(double amount) { // Abstraction
        balance += amount;
    }
}

class SavingsAccount extends Account { } // Inheritance
```

```java
Account account = new SavingsAccount();   // Polymorphism
```

**OOP → Encapsulation + Inheritance + Polymorphism + Abstraction**

---

### 5. Automatic Memory Management

```java
Account account = new Account();

account = null;    // object becomes eligible for GC
```

**JVM → Garbage Collector → automatically manages unused objects**
Unlike CPP where we need to call destructor everytime to free the memory
---

### 6. Multithreaded

```java
Thread thread = new Thread(() -> {
    System.out.println("Running in another thread");
});

thread.start();
```

Java provides built-in support for concurrent execution.

---

### 7. Functional Programming Support
After Java 8:
Java supports functional-style programming using **lambda expressions, functional interfaces, method references, and Streams**.

```java
List<Integer> amounts = List.of(1000, 2000, 5000);

amounts.stream()
       .filter(amount -> amount > 1500)
       .forEach(System.out::println);
```

Here:

```text
amount -> amount > 1500   → Lambda expression
filter()                  → Functional operation
stream()                  → Stream API
System.out::println       → Method reference
```

It allows Java to express **what should be done** more concisely, especially when processing collections.

### 8. Rich I/O & Networking

**I/O:**

```java
Files.readString(Path.of("data.txt"));
```

**Networking:**

```java
Socket socket = new Socket("example.com", 80);
```

Java provides APIs for files, streams, TCP/IP, UDP/IP, URLs, etc.


# Java Security features

You should know it at **two levels**:

### Level 1 — Core Java interview: STOP HERE

This is enough to answer:

> **“What security features does Java provide?”**

Know these well:

```text
Java Security
│
├── Strong type checking
├── Encapsulation + access modifiers
├── No explicit pointer arithmetic
├── Automatic memory management
├── Bytecode verification
├── Class Loader
└── Security APIs
    ├── Cryptography
    ├── Certificates
    └── TLS/SSL
```

And be able to explain:

```java
private double balance;
```

→ prevents arbitrary direct access.

```java
javac Bank.java
```

→ produces bytecode.

```java
java Bank
```

→ JVM loads/verifies/executes that bytecode.

And know the distinction:

> **Authentication** → Who are you?
> **Authorization** → What are you allowed to do?

**STOP.** Don't spend days implementing cryptographic algorithms yourself.

---

### Level 2 — Backend/Senior interview: learn these separately

This is where I **would create a separate section**, but not necessarily a whole folder yet:

```text
Security & Cryptography
├── Hashing
├── Encryption vs hashing
├── Symmetric encryption
├── Asymmetric encryption
├── Digital signatures
├── Public/private keys
├── Certificates
├── TLS/HTTPS
├── KeyStore
├── Authentication
└── Authorization
```

Why?

Because at your target level, you may be asked things like:

> “How does HTTPS work?”

> “What's the difference between hashing and encryption?”

> “How does a server verify a client's identity?”

> “What is a digital signature?”

> “Where would you store private keys?”

Those are **backend security concepts**, not merely “Java syntax.”

---


# Java Security — Each Point with Code

## 1. Strong Type Checking

Java checks that you use the **correct data type**.

```java
int age = 30;

age = "Piyusha";   // ❌ Compile-time error
```

You cannot assign a `String` to an `int`.

Another example:

```java
String name = "Piyusha";

name = 100;        // ❌ Compile-time error
```

### Why is this related to security?

Strong typing prevents many invalid operations from reaching runtime.

```java
int balance = 5000;

// balance + "hello" as an int operation is not allowed
```

The compiler catches many mistakes **before the program runs**.

**Interview line:**

> Java's strong type system prevents invalid type operations and catches many programming errors at compile time, improving reliability and memory safety.

---

## 2. Encapsulation + Access Modifiers

Java allows you to **restrict direct access to data**.

```java
class BankAccount {

    private double balance;

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
        }
    }

    public double getBalance() {
        return balance;
    }
}
```

Now:

```java
BankAccount account = new BankAccount();

account.deposit(5000);

System.out.println(account.getBalance());
```

But this is not allowed:

```java
account.balance = -100000;  // ❌ private
```

The caller must go through:

```java
account.deposit(5000);
```

instead of directly modifying the internal state.

### Access modifiers

```text
private    → only inside the class
default    → same package
protected  → package + subclasses
public     → accessible from anywhere
```

### Why security?

You control **who can access or modify internal state**.

**Interview line:**

> Encapsulation protects an object's internal state by restricting direct access and exposing controlled operations through methods.

---

## 3. No Explicit Pointer Arithmetic

Languages like C allow you to manipulate memory addresses directly.

Java does **not** expose raw memory pointers.

You write:

```java
int[] numbers = {10, 20, 30};

System.out.println(numbers[0]);
```

You cannot do something like:

```text
address = address + 4
```

or directly manipulate arbitrary memory locations.

Java references are different from C/C++ pointers:

```java
BankAccount account = new BankAccount();
```

`account` is a **reference** to an object.

You cannot do:

```java
account++;       // ❌
account + 4;     // ❌
```

### Why security?

Without direct pointer arithmetic, normal Java code cannot arbitrarily access or overwrite memory.

This contributes to Java's **memory safety**.

**Interview line:**

> Java does not expose raw pointers or pointer arithmetic, which prevents application code from directly manipulating arbitrary memory addresses.

---

## 4. Automatic Memory Management

Java automatically manages objects using the **Garbage Collector (GC)**.

```java
BankAccount account = new BankAccount();

account = null;
```

Once an object is no longer reachable:

```text
Stack
  │
  │ account
  ▼
Heap
┌─────────────────┐
│ BankAccount     │
│ balance = 5000  │
└─────────────────┘
```

After:

```java
account = null;
```

there is no reference from your application to that object.

```text
Stack
  │
  │ account → null

Heap
┌─────────────────┐
│ BankAccount     │  ← unreachable
└─────────────────┘
```

The GC can eventually reclaim that memory.

### Why security?

It reduces certain classes of memory-management bugs common in manually managed languages, such as:

* use-after-free
* double-free
* dangling pointers

Java doesn't eliminate every memory-related security problem, but automatic memory management provides an important layer of memory safety.

**Interview line:**

> Java's garbage collection automatically manages object memory, reducing risks associated with manual memory management.

---

## 5. Bytecode Verification

This is an important JVM-level security concept.

Suppose you write:

```java
class BankAccount {

    private double balance;

    public void deposit(double amount) {
        balance += amount;
    }
}
```

You compile it:

```bash
javac BankAccount.java
```

You get:

```text
BankAccount.class
```

That `.class` file contains **bytecode**, not native machine code.

When the JVM loads the class, it performs verification as part of class loading.

```text
BankAccount.java
       │
       │ javac
       ▼
BankAccount.class
       │
       │ JVM loads
       ▼
Bytecode verification
       │
       ▼
Execution
```

The verifier checks that the bytecode satisfies JVM constraints.

For example, it helps ensure that bytecode doesn't perform invalid operations such as using values with incompatible types or violating JVM structural rules.

### Why security?

Because the JVM doesn't blindly execute arbitrary `.class` files.

**Interview line:**

> Bytecode verification checks loaded bytecode against JVM safety constraints before execution, helping prevent malformed or invalid bytecode from being executed.

---

## 6. Class Loader

The JVM doesn't simply take every `.class` file and execute it.

The **Class Loader** loads classes into the JVM.

For example:

```java
BankAccount account = new BankAccount();
```

The JVM needs to load:

```text
BankAccount.class
```

The class-loading process is roughly:

```text
BankAccount.class
       │
       ▼
Class Loader
       │
       ▼
JVM
       │
       ▼
Verification
       │
       ▼
Execution
```

Java has different class loaders, such as:

```text
Bootstrap Class Loader
        ↓
Platform Class Loader
        ↓
Application Class Loader
```

For example:

```java
String name = "Piyusha";
```

`String` comes from the Java platform libraries.

Your own:

```java
BankAccount
```

is typically loaded by the application class loader.

### Why security?

Class loading provides **separation and controlled loading of classes**.

The JVM's class-loading architecture also works with the runtime's type-safety and access-control mechanisms.

**Interview line:**

> The Class Loader is responsible for loading classes into the JVM and participates in maintaining class and namespace boundaries.

---

## 7. Security APIs

This is the biggest category.

Java provides APIs for things such as:

```text
Cryptography
Certificates
TLS/SSL
Keys
Digital signatures
Secure communication
```

Let's see each.

---

## 7.1 Cryptography — Hashing

For example, SHA-256:

```java
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class HashExample {

    public static void main(String[] args) throws Exception {

        String password = "hello123";

        MessageDigest md = MessageDigest.getInstance("SHA-256");

        byte[] hash =
                md.digest(password.getBytes(StandardCharsets.UTF_8));

        System.out.println(java.util.HexFormat.of().formatHex(hash));
    }
}
```

Conceptually:

```text
"hello123"
     │
     ▼
 SHA-256
     │
     ▼
fixed-length hash
```

Important:

**Hashing ≠ encryption.**

Hashing is generally one-way.

---

## 7.2 Encryption

Java also provides `Cipher`.

For example:

```java
import javax.crypto.Cipher;
```

You can use:

```java
Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
```

Conceptually:

```text
Plaintext
   │
   │ encryption + key
   ▼
Ciphertext
```

And later:

```text
Ciphertext
   │
   │ decryption + key
   ▼
Plaintext
```

You normally should use established cryptographic APIs correctly rather than implementing encryption algorithms yourself.

---

## 7.3 Digital Signatures

Java provides:

```java
Signature signature =
        Signature.getInstance("SHA256withRSA");
```

Conceptually:

```text
Private Key
     +
Message
     ↓
Digital Signature
```

Someone else can use the corresponding public key to verify it.

Used for things like:

* verifying software/data authenticity
* certificates
* signed messages

---

## 7.4 Certificates / KeyStore

Java provides `KeyStore`:

```java
KeyStore keyStore = KeyStore.getInstance("PKCS12");
```

A keystore can contain keys and certificates.

Conceptually:

```text
KeyStore
│
├── Private Key
├── Certificate
└── Trusted Certificates
```

This becomes important when working with HTTPS/TLS.

---

## 7.5 TLS / SSL

Java provides APIs for secure network communication.

For example:

```java
import javax.net.ssl.SSLContext;

SSLContext context =
        SSLContext.getInstance("TLS");
```

The idea is:

```text
Without TLS:

Client ────────────────> Server
       readable data


With TLS:

Client ════════════════> Server
          encrypted
          communication
```

HTTPS is essentially HTTP running over TLS.

### Why?

TLS provides things such as:

* encryption
* server authentication
* integrity protection

**Interview line:**

> Java provides TLS APIs through packages such as `javax.net.ssl` for establishing secure network communication.

---

### Put Everything Together

This is the mental model I want you to remember:

```text
                    JAVA SECURITY
                         │
        ┌────────────────┼────────────────┐
        │                │                │
   LANGUAGE LEVEL     JVM LEVEL       SECURITY APIs
        │                │                │
        ▼                ▼                ▼
 Strong typing      Class Loader      Cryptography
 Encapsulation      Bytecode         Certificates
 No pointers        verification     KeyStore
 Memory safety                       Digital signatures
                                      TLS/SSL
```

And the **interview answer** can be:

> **"Java provides security at multiple levels. At the language level, strong type checking, encapsulation, access control, and the absence of explicit pointer arithmetic improve memory and type safety. At the JVM level, class loading and bytecode verification help ensure that loaded classes follow JVM safety constraints. Java also provides security APIs for cryptography, digital signatures, certificates, and TLS-based secure communication."**

# Java Naming Conventions

* **Class/Interface:** `PascalCase` 
* **Method/Variable:** `camelCase` 
* **Constant:** `UPPER_SNAKE_CASE` 
* **Package:** `lowercase` 
* **Enum:** `PascalCase` (constants `UPPER_SNAKE_CASE`)

### Java Naming Conventions

* **PascalCase** → Each word starts with a capital letter: `BankAccount`
* **camelCase** → First word lowercase, subsequent words capitalized: `accountBalance`
* **UPPER_SNAKE_CASE** → Words uppercase and separated by `_`: `MAX_AMOUNT`
* **lowercase** → All letters lowercase, typically used for packages: `com.bank.account`
* **Enum constants** → Usually `UPPER_SNAKE_CASE`: `PENDING_APPROVAL`

# Java Environment
Before going deeper into Core Java, you should understand **JDK, JRE, JVM, `PATH`, `CLASSPATH`, source path, classpath, and how compilation/execution actually happen**.

Since you're using **Java 25**, I'll explain it using that environment.

## 1. First: the Java environment

Think of your setup as:

```text
Your Java Code
     │
     ▼
   JDK
     │
     ├── javac  → compiles .java → .class
     │
     ├── java   → starts JVM
     │
     └── JVM    → executes bytecode
```

You need the **JDK** to develop Java applications.

---

## 2. JDK

**JDK = Java Development Kit**

It contains the tools needed to **develop, compile, debug and run** Java applications.

Important commands:

```bash
java --version
javac --version
```

Example:

```text
java 25.0.4
javac 25.0.4
```

For your current setup, you're using **JDK 25.0.4**.

---

## 3. JVM

**JVM = Java Virtual Machine**

The JVM actually executes Java bytecode.

```text
Hello.java
    │
    │ javac
    ▼
Hello.class
    │
    │ java
    ▼
   JVM
    │
    ▼
Program runs
```

You don't directly compile Java source code into machine code yourself.

```bash
javac Hello.java
```

produces:

```text
Hello.class
```

Then:

```bash
java Hello
```

starts the JVM and executes the bytecode.

---

## 4. JRE

**JRE = Java Runtime Environment**

Historically:

```text
JDK
 └── JRE
      └── JVM
```

The JRE concept represented the runtime pieces needed to execute Java applications.

### Important for modern Java

Don't treat **JRE as a separately installed product** in modern Java.

Since Java 9's modularization, Oracle/OpenJDK distributions generally don't ship a separate traditional JRE installation.

For your learning:

> **JDK = what you install for Java development. JVM = what executes your bytecode.**

That's the useful modern mental model.

---

## 5. `PATH`

This is an **operating-system environment variable**.

It tells your shell where to look for executable commands.

For example, when you type:

```bash
java --version
```

your shell searches directories listed in `PATH`.

You can see it on macOS:

```bash
echo $PATH
```

If Java's `bin` directory is correctly configured, this works:

```bash
java --version
javac --version
```

Conceptually:

```text
PATH
 │
 ├── /some/directory/bin
 ├── /another/directory/bin
 └── Java/bin
                  │
                  ├── java
                  └── javac
```

### Simple definition

> **PATH tells the OS where to find executable programs.**

---

## 6. `CLASSPATH`

This is where things become confusing.

**Classpath tells Java where to look for compiled classes and libraries.**

Suppose you have:

```text
project/
└── src/
    └── Hello.java
```

Compile it:

```bash
javac -d bin src/Hello.java
```

Now:

```text
project/
├── src/
│   └── Hello.java
└── bin/
    └── Hello.class
```

If you run:

```bash
java Hello
```

Java needs to know:

> "Where is `Hello.class`?"

You can tell it:

```bash
java -cp bin Hello
```

Here:

```text
-cp bin
```

means:

> Use `bin` as the classpath.

---

## 7. `-cp` vs `CLASSPATH`

You can specify the classpath directly:

```bash
java -cp bin Hello
```

or using the environment variable:

```bash
export CLASSPATH=bin
java Hello
```

But in modern projects, **explicit `-cp` or build tools are generally preferable to relying on a global `CLASSPATH` environment variable.**

For your learning project, you'll often see:

```bash
javac -d bin src/Hello.java
java -cp bin Hello
```

---

## 8. What exactly is inside the Classpath?

It can contain:

### Directories

```bash
java -cp bin Hello
```

### JAR files

```bash
java -cp "bin:lib/mysql.jar" Hello
```

On macOS/Linux, classpath entries are separated by:

```text
:
```

On Windows:

```text
;
```

So:

```text
macOS/Linux:
bin:lib/mysql.jar

Windows:
bin;lib\mysql.jar
```

---

## 9. Source Path vs Classpath

This distinction is **very important**.

### Source path

Where Java source files are located:

```text
src/
```

Example:

```text
src/java17/sealedclasses/Payment.java
```

### Classpath

Where compiled classes and libraries are found:

```text
bin/
lib/*.jar
```

Think:

```text
              PROJECT
                 │
       ┌─────────┴─────────┐
       │                   │
    SOURCE              RUNTIME
       │                   │
      src/            bin/ + libraries
       │                   │
    .java                .class
```

---

## 10. Your CoreJava project

Your structure is roughly:

```text
CoreJava/
├── src/
│   └── java17/
│       └── sealedclasses/
│           ├── Payment.java
│           ├── BankTransfer.java
│           ├── CardPayment.java
│           └── UpiPayment.java
│
├── bin/
│
└── lib/
```

So:

```text
src/  → source files
bin/  → compiled .class files
lib/  → external JAR libraries
```

That's a perfectly reasonable learning setup.

---

## 11. VS Code Java configuration

Your VS Code configuration can tell the Java extension things such as:

```json
{
    "java.project.sourcePaths": ["src"],
    "java.project.outputPath": "bin",
    "java.project.referencedLibraries": [
        "lib/**/*.jar"
    ]
}
```

Meaning:

```text
sourcePaths
     ↓
src/

outputPath
     ↓
bin/

referencedLibraries
     ↓
lib/*.jar
```

So VS Code can understand your project structure.

---

## 12. The complete flow

This is the diagram I want you to remember:

```text
                 JAVA DEVELOPMENT ENVIRONMENT

                         JDK
                          │
             ┌────────────┼────────────┐
             │            │            │
           javac         java        tools
             │            │
             ▼            ▼
         .java          JVM
             │            │
             │         loads classes
             │            │
             ▼            ▼
          .class  ────→ execution
```

And the environment variables:

```text
PATH
 │
 └── Where can the OS find java/javac?

CLASSPATH
 │
 └── Where can Java find classes/libraries?

Source Path
 │
 └── Where are .java source files?

Output Path
 │
 └── Where should compiled .class files go?
```

---

## 13. What you actually need to know for interviews

You **do not need to memorize environment configuration commands**.

Be able to explain:

> **JDK** provides Java development tools such as `javac`.
> **JVM** executes Java bytecode.
> **PATH** tells the operating system where to find commands such as `java` and `javac`.
> **Classpath** tells Java where to find compiled classes and external libraries.
> **Source path** contains `.java` files, while the output directory contains compiled `.class` files.

That's the Core Java level.

### One thing I would add to your notes

```text
JDK → Development
JVM → Execution
PATH → Finds executables
Classpath → Finds classes/libraries
Source path → Finds source code
Output path → Stores compiled classes
```

Once you understand this, **JDK/JRE/JVM + compilation + classpath** becomes much less confusing, and you'll have the foundation needed before moving into packages, imports, access modifiers, and Java modules.

# Rules on identifiers

### Identifier in Java

An **identifier** is the **name given by the programmer to identify a program element**.

Examples:

```java
class BankAccount {          // BankAccount → identifier
    private double balance;  // balance → identifier

    void deposit(double amount) {  // deposit, amount → identifiers
        balance += amount;
    }
}
```

### What can be an identifier?

```text
Class        → BankAccount
Variable     → balance
Method       → deposit
Object       → account
Parameter    → amount
Package      → banking
```

### Rules

```java
int accountBalance;     // ✅
int _balance;           // ✅
int $amount;            // ✅
int account2;           // ✅

int 2account;           // ❌ cannot start with digit
int account-balance;    // ❌ '-' is not allowed
int class;              // ❌ keyword
```

**Key rule:** An identifier can contain **letters, digits, `_`, and `$`**, but **cannot start with a digit**, cannot contain spaces, and cannot be a Java keyword.

**One-line revision:**

> **Identifier = a programmer-defined name used to identify classes, methods, variables, packages, parameters, etc.**

### Rules for Java Identifiers

1. **Can contain letters, digits, `_`, and `$`**

   ```java
   accountBalance
   account2
   _balance
   $amount
   ```

2. **Cannot start with a digit**

   ```java
   2account   // ❌
   account2   // ✅
   ```

3. **Cannot contain spaces**

   ```java
   account balance   // ❌
   accountBalance    // ✅
   ```

4. **Cannot use special characters** like `-`, `@`, `#`, `%`

   ```java
   account-balance   // ❌
   account_balance   // ✅
   ```

5. **Cannot be a Java keyword**

   ```java
   int class;     // ❌
   int public;    // ❌
   int account;   // ✅
   ```

6. **Java identifiers are case-sensitive**

   ```java
   int balance;
   int Balance;   // Different identifier
   ```

7. **Can use Unicode characters**, because Java identifiers support Unicode.

   ```java
   int café = 10;   // technically valid
   ```

   However, standard Java naming conventions should be preferred for readability.

### One-line revision

> **Identifier = letters/digits/`_`/`$`, cannot start with a digit, contain spaces or most special characters, use keywords, and is case-sensitive.**

# Access Specifiers/Modifier in Java

In Java, **access specifiers (access modifiers)** control **where a class, method, variable, or constructor can be accessed from**.

Java has **4 access levels**:

```text
             Same Class   Same Package   Subclass   Other Package
public           ✅            ✅            ✅            ✅
protected        ✅            ✅            ✅            ⚠️
default          ✅            ✅            ❌            ❌
private          ✅            ❌            ❌            ❌
```

> `default` is not a keyword here. It means **no access modifier is written**.

---

## 1. `private` — Same class only

```java
class Account {

    private double balance;

    private void calculateInterest() {
        // ...
    }
}
```

Accessible only inside `Account`.

```java
Account account = new Account();

account.balance;              // ❌
account.calculateInterest();  // ❌
```

Typical use: **hide internal implementation/details**.

---

## 2. `default` — Same package

If you don't write any modifier:

```java
class Account {

    double balance;

    void deposit() {
        // ...
    }
}
```

These are accessible from classes in the **same package**:

```java
package banking;

class BankService {

    void update() {
        Account account = new Account();

        account.balance;  // ✅
        account.deposit(); // ✅
    }
}
```

But from another package:

```java
package customer;

class CustomerService {

    void update() {
        Account account = new Account();

        account.balance;  // ❌
    }
}
```

---

## 3. `protected` — Same package + subclasses

```java
class Account {

    protected double balance;
}
```

### Same package

```java
class BankService {

    void update(Account account) {
        account.balance;  // ✅
    }
}
```

### Different package + subclass

```java
class SavingsAccount extends Account {

    void showBalance() {
        System.out.println(balance); // ✅
    }
}
```

### Different package + non-subclass

```java
class CustomerService {

    void update(Account account) {
        account.balance;  // ❌
    }
}
```

### Important `protected` nuance

Across packages, `protected` access is available through **inheritance**, not simply because you have an object of the superclass.

This is a common interview trap.

---

## 4. `public` — Everywhere

```java
public class Account {

    public double balance;

    public void deposit() {
        // ...
    }
}
```

Accessible from anywhere, provided the class itself is accessible.

```java
Account account = new Account();

account.balance;   // ✅
account.deposit(); // ✅
```

---

### The easiest way to remember

Think of access as **increasing visibility**:

```text
private
   ↓
default
   ↓
protected
   ↓
public
```

Or:

```text
private    → My room
default    → My building
protected  → Family + building
public     → Anyone
```

---

## Interview-ready answer

> **Java has four access levels: private, default, protected, and public. Private members are accessible only within the same class. Default members are accessible within the same package. Protected members are accessible within the same package and through inheritance in other packages. Public members are accessible from anywhere.**

### One important distinction

**Access modifier vs access specifier:** In Java interviews, people often use these terms interchangeably. Technically, Java documentation commonly refers to them as **access control/access modifiers**.

And remember: **`private`, `protected`, and `public` are keywords; “default access” means you write no modifier.**
