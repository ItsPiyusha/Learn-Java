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

# Basic Definitions and Concepts
* API -> Application Programmer Interface
* Package -> collection of functionally similar classes
* java.lang package by default available always inherently
* import -> to avail
* syntax of import outside of class always
* Java follows single root inherentance hierarchy-> all classes implicitly extend **Object class**
* System-> static class having fields in, out
* out has type PrintStream(from java.io package) which is another class shows -> HAS-A relationship
* src -> java files
* bin -> .class files
* to generate .class files in bin folder use command -> javac -d ..\bin File.java
* ```cd ..\bin``` (go to bin folder to run the class file) -> ```java File``` (case - sensitive file name)

* Java is both compiled and interpreted language
* it is platform independent because JVM is platform specific - .class files will be platform independent -> JVM creates platform specific bytecodes
* JDK = Java dev tools(javac, java, javap, jar,jar signer, appletviewer) + JRE( Java API libs ) + JVM (containing 1. class loader, 2. Interpreter, 3. JIT compiler, 4. HotSpot profiler)
* Java src code -->(Java compiler) --> ByteCode -->(JIT compiler) --> Native code 
* class files -> class loader -><- Runtime data areas(Method area + Heap + Java stacks + PC registers + Native method stacks) -><- Execution Engine -><- Native method interface -> native method library
* Java Hot Spot -> adaptive learning like AI
* main function expects String, we'll parse string to num to calculate the sum and avoid concatenation
CoreJava/src/java01/Sum.java
```
package java01;

public class Sum {
    public static void main(String[] ss){
        int num1 = Integer.parseInt(ss[0]);
        int num2 = Integer.parseInt(ss[1]);
        System.out.println("Sum="+(num1 + num2));
    }
}
```
```
piyushalomte@Piyushas-MacBook-Pro ~ % cd Documents
piyushalomte@Piyushas-MacBook-Pro Documents % cd Learn-Java
piyushalomte@Piyushas-MacBook-Pro Learn-Java % code . 
piyushalomte@Piyushas-MacBook-Pro Learn-Java % mkdir -p CoreJava/bin/java01
piyushalomte@Piyushas-MacBook-Pro Learn-Java % javac -d CoreJava/bin CoreJava/src/java01/Sum.java
piyushalomte@Piyushas-MacBook-Pro Learn-Java % java -cp CoreJava/bin java01.Sum 10 20
Sum=30
```
the flow is 
```text
Sum.java
   │
   │ javac
   ▼
Sum.class
   │
   │ java + arguments
   ▼
output: 30
```


# Java Data Types

## Definition

A data type defines what kind of value a variable can store and what operations can be performed on that value.

Java is **statically typed**, so a variable's type is known at compile time.

```text
Java Data Types
│
├── Primitive
│   ├── byte
│   ├── short
│   ├── int
│   ├── long
│   ├── float
│   ├── double
│   ├── char
│   └── boolean
│
└── Reference
    ├── Class/Object
    ├── String
    ├── Array
    ├── Interface
    ├── Enum
    └── Record
```

## 1. Primitive Types

| Type      |                         Size | Typical use                         |
| --------- | ---------------------------: | ----------------------------------- |
| `byte`    |                        8-bit | Small integers                      |
| `short`   |                       16-bit | Rare in application code            |
| `int`     |                       32-bit | Default integer choice              |
| `long`    |                       64-bit | Large integers / IDs                |
| `float`   |                       32-bit | Lower-precision decimal             |
| `double`  |                       64-bit | General floating-point calculations |
| `char`    |                       16-bit | Single UTF-16 code unit             |
| `boolean` | JVM-dependent representation | `true` / `false`                    |

### Practical choices

```text
Normal integer       → int
Large integer / ID   → long
Floating point       → double
Money                → BigDecimal
Boolean              → boolean
Text                 → String
Single character     → char
```

**Do not use `double` for financial amounts. Use `BigDecimal`.**

```java
BigDecimal amount = new BigDecimal("100.50");
```

Avoid:

```java
new BigDecimal(100.50); // may capture binary floating-point approximation
```

## 2. Reference Types

Reference variables refer to objects.

```java
BankAccount account = new BankAccount();
String name = "Piyusha";
int[] amounts = {100, 200, 500};
```

Common reference types:

* Classes
* Objects
* Arrays
* Interfaces
* Strings
* Enums
* Records

Example:

```java
List<String> names = new ArrayList<>();
```

`List` is the reference type/interface and `ArrayList` is the implementation.

## 3. Primitive vs Reference

| Primitive        | Reference                                |
| ---------------- | ---------------------------------------- |
| 8 built-in types | Classes, arrays, interfaces, enums, etc. |
| Stores a value   | Holds a reference to an object           |
| Cannot be `null` | Can be `null`                            |
| `int`            | `Integer`                                |
| `double`         | `Double`                                 |
| `boolean`        | `Boolean`                                |

## 4. Wrapper Classes

```text
byte      → Byte
short     → Short
int       → Integer
long      → Long
float     → Float
double    → Double
char      → Character
boolean   → Boolean
```

Collections use wrapper types because generics work with reference types:

```java
List<Integer> numbers = new ArrayList<>();
```

Not:

```java
List<int> numbers; // invalid
```

### Autoboxing / Unboxing

```java
Integer x = 100; // autoboxing
int y = x;       // unboxing
```

Autoboxing and generics were introduced in **Java 5**.

## 5. `null`

Primitives cannot be `null`:

```java
int age = null;       // invalid
```

Reference variables can:

```java
Integer age = null;   // valid
String name = null;   // valid
```

`null` means a reference currently refers to no object.

## 6. Default Values

Instance/static fields receive default values:

```text
byte/short/int   → 0
long             → 0L
float            → 0.0f
double           → 0.0d
char             → '\u0000'
boolean          → false
reference        → null
```

Local variables do **not** receive automatic default values.

```java
void test() {
    int count;
    System.out.println(count); // compile-time error
}
```

## 7. Type Conversion

### Widening — automatic

```java
int x = 100;
long y = x;
```

### Narrowing — explicit cast

```java
long x = 100;
int y = (int) x;
```

Narrowing can lose information:

```java
double amount = 100.99;
int value = (int) amount;

// value = 100
```
popular interview point -> long -> float is considered automatic type of conversion(widening) 
Rule -> src and dest must be compatible, dest should store larger magnitude values than src data type.
**FLOAT > LONG**
* Any arithmetic operation results in bigger data type
## 8. Overflow

```java
int x = 2_147_483_647;
x++;
```

The value wraps around because the maximum `int` value was exceeded.

Use `long` when the domain requires a larger integer range.

## 9. Banking Project Examples

```java
class Customer {
    long customerId;
    String name;
    String email;
    boolean active;
}
```

```java
class BankAccount {
    long accountId;
    String accountNumber;
    BigDecimal balance;
    boolean active;
}
```

```java
class Transaction {
    long transactionId;
    long accountId;
    BigDecimal amount;
    TransactionStatus status;
}
```

```java
enum TransactionStatus {
    PENDING,
    SUCCESS,
    FAILED
}
```

### Senior-level rule

Choose a type based on:

1. Meaning of the value
2. Required range
3. Required precision
4. Database type
5. API contract
6. Whether absence (`null`) is meaningful

## Interview Answer

> "Java data types define what kind of value a variable can store. Java is statically typed, so types are checked at compile time. Data types are broadly divided into primitive and reference types. Java has eight primitive types: byte, short, int, long, float, double, char, and boolean. Reference types include classes, arrays, interfaces, enums, records, and other objects. Primitive variables represent values directly, while reference variables refer to objects. Java also provides wrapper classes such as Integer and Long when an object representation is required."

## Quick Interview Questions

**Q: How many primitive types does Java have?**

8.

**Q: Is String a primitive?**

No. `String` is a class/reference type.

**Q: Can int store null?**

No.

**Q: Can Integer store null?**

Yes.

**Q: Why use BigDecimal for money?**

Because binary floating-point types such as `double` are not suitable for exact decimal financial arithmetic.

**Q: Why use Integer instead of int?**

When an object/reference representation is required, such as with generics/collections, or when `null` needs to represent absence.

**Q: What is the difference between widening and narrowing?**

Widening converts to a type with a larger compatible range and is generally implicit; narrowing converts to a smaller type and requires an explicit cast because information may be lost.

**Java version note:** The eight primitive types are part of Java's original language fundamentals (Java 1.0). Wrapper classes, autoboxing/unboxing, and generics were introduced in **Java 5**.

Range check -> Byte.MIN_VALUE or MAX_VALUE, Integer,Long,Float

# Basic Rules


* **Uninitialized Variables (Rules 1 & 6):** The Java compiler (`javac`) prevents accessing unitialized data members/variables before assigning them a value.
* *Example shown:* Declaring `int n;` or `Emp e;` and trying to print them via `sop(n);` or `sop(e);` without initialization causes a compilation error.
* **Non-Public Classes (Rule 2):** Files with default-scoped (non-public) classes do not require the file name to match any of the class names defined inside it.
* **Multiple Non-Public Classes (Rule 3):** A single source code file can contain more than one non-public class.
* **Single Public Class Limit (Rule 4):** A Java source code file can contain at most **one** `public` class.
* **File Naming for Public Classes (Rule 5):** If a file contains a `public` class, the file name **must** exactly match the name of that public class with a `.java` extension (e.g., `public class Example` must be stored in `Example.java`).
* **No Pointer Arithmetic (Code Example):** Java does not support pointer arithmetic. Attempting operations like incrementing a String (`s++;`) results in a compilation error.

* write the program st 58:00 in core java day 1 2
* Here if - else,for, do, while, continue, break, >>,>>>,Shift operators covered
# Assignment 1
1. Accept i/ps from user, till user enters"quit" refer 1:33:00 + 2:28:00 in core java day1 2

# Scanner class
* java.util package, .* -> only load the required classes
* A Scanner breaks its input into tokens using a delimiter, space by default
* Throws inputMismatchException
* Steps
1. import java.util.Scanner;
2. create instance
3. check the data type hasNextInt/Byte/Long()
4. read and parse data nextInt/Double/Line/Boolean() or just next()
5. before terminating app, close the scanner

* Assignment at 1:54:00 day1 2

# Revision Questions:
* Why Java? platform independence(WORA)
* how platform independent -> JVM is machine specific
* Only runtime needs a main method, so you can definitely compile java class without main.
* Can you write a java class without main and compile it? YES
* Can you write a java class without main and run it? NO
* What are legal access specifiers for members(data members & methods)? private, default, protected, public
* which are applicable to classes? -> default, public
* can a java src file contain multiple default classes? : YES
* can a java src file contain multiple public classes? : NO
* Any rules regarding default class name & src file name? : NO
* Any rules regarding public class name & src file name? : YES
* Pointer arithmetic -> means operator attaching to reference type -> sc++; or sc += 10; ->not allowed in java

# JVM Architecture or Java memory areas
Absolutely. For interviews, don't just memorize **"heap, stack, method area"**. You should be able to explain **what gets stored where, who manages it, and what happens when a method executes**.

## JVM Memory Model — Interview Explanation

### Start with this interview answer

> **"The JVM memory is divided into several runtime data areas. The major areas are Heap, JVM Stack, Method Area, PC Register, and Native Method Stack.**
>
> **The Heap is shared across threads and stores objects and arrays. It is managed by the Garbage Collector. Each thread has its own JVM Stack, which contains stack frames for method calls. A frame contains local variables, the operand stack, and information needed for the method execution.**
>
> **The Method Area is shared and contains class-level information such as class metadata, runtime constant pools, and method information. The PC register is private to each thread and keeps track of the current instruction being executed. The Native Method Stack supports execution of native methods."**

That's already a solid **45–60 second answer**.

---

## 1. First understand the big picture

When you run:

```bash
java MyProgram
```

the JVM creates runtime memory areas.

Conceptually:

```text
                     JVM PROCESS
                         │
          ┌──────────────┴──────────────┐
          │                             │
      SHARED                        PER THREAD
          │                             │
     ┌────┴────┐              ┌─────────┼─────────┐
     │         │              │         │         │
   HEAP   METHOD AREA       Stack      PC      Native
                          Thread 1   Register    Stack
                          Thread 2
                          Thread 3
```

The most important distinction:

> **Heap and Method Area are shared between threads. Stack and PC Register are thread-specific.**

---

## 2. Heap — most important

The **Heap** is where objects and arrays are allocated.

Example:

```java
BankAccount account = new BankAccount();
```

Conceptually:

```text
Stack                         Heap

account ──────────────────→ BankAccount object
                              balance = 5000
                              id = 101
```

The variable `account` is a reference.

The actual `BankAccount` object is allocated on the heap.

### Another example

```java
int[] amounts = new int[3];
```

The array object is on the heap.

```text
Stack                         Heap

amounts ─────────────────→  int[3]
                             [100, 200, 300]
```

### Garbage Collection

When an object is no longer reachable:

```java
BankAccount account = new BankAccount();

account = null;
```

the object may eventually become eligible for garbage collection.

Important interview wording:

> **"Eligible for GC" does not mean "immediately deleted."**

The Garbage Collector determines when/how memory is reclaimed.

---

## 3. Stack — per thread

Every thread gets its own JVM Stack.

Suppose:

```java
public static void main(String[] args) {
    int amount = 100;
    calculate(amount);
}

static void calculate(int amount) {
    int tax = 10;
    System.out.println(amount + tax);
}
```

When `main()` starts:

```text
Thread 1 Stack

┌──────────────────────┐
│ main() frame         │
│ amount = 100         │
│ args → ...           │
└──────────────────────┘
```

When `calculate()` is called:

```text
┌──────────────────────┐
│ calculate() frame    │
│ amount = 100         │
│ tax = 10              │
├──────────────────────┤
│ main() frame         │
│ amount = 100         │
│ args → ...            │
└──────────────────────┘
```

When `calculate()` finishes, its frame is removed:

```text
┌──────────────────────┐
│ main() frame         │
│ amount = 100         │
│ args → ...            │
└──────────────────────┘
```

This is why we say:

> **Method calls create stack frames.**

---

## 4. What is inside a Stack Frame?

This is a **very good interview follow-up**.

Each method invocation gets a frame.

Conceptually, a frame contains:

```text
Stack Frame
│
├── Local Variables
├── Operand Stack
└── Reference to runtime constant pool /
    method execution information
```

### Local variables

Example:

```java
int amount = 100;
```

The local variable is stored in the method's frame.

### Operand stack

The JVM uses an operand stack while executing bytecode.

For example:

```java
int result = a + b;
```

The JVM loads operands onto the operand stack, performs the operation, and places the result back.

You don't normally manipulate this stack directly in Java code.

---

## 5. Method Area

The **Method Area** is shared among threads.

It stores class-level/runtime information such as:

* Class metadata
* Method information
* Field information
* Runtime constant pool
* Static-related class information

Example:

```java
class BankAccount {

    static String bankName = "ABC Bank";

    int balance;

    void deposit(int amount) {
        balance += amount;
    }
}
```

When `BankAccount` is loaded, the JVM needs information about:

```text
BankAccount
 ├── class metadata
 ├── method information
 ├── field information
 └── runtime constant pool
```

### Important Java 8+ interview nuance

You may hear:

> "Method Area = PermGen"

That's outdated.

In **HotSpot**:

```text
Java 7 and earlier → PermGen
Java 8+            → Metaspace
```

Java 8 removed PermGen and introduced **Metaspace** for class metadata.

So in an interview, say:

> **"Method Area is a JVM specification concept. In HotSpot, class metadata is implemented using Metaspace since Java 8."**

That's a much stronger answer.

---

## 6. PC Register

PC = **Program Counter**.

Every thread has its own PC register.

Its job is essentially to keep track of **which JVM instruction that thread is currently executing / will execute next**, subject to JVM specification details.

Conceptually:

```text
Thread 1
PC → instruction 25

Thread 2
PC → instruction 108
```

Why is it per-thread?

Because different threads execute different instructions at the same time.

---

## 7. Native Method Stack

Java can call native code, commonly through mechanisms such as JNI.

For example, some Java APIs ultimately interact with operating-system/native implementations.

The JVM provides a **Native Method Stack** for native method execution.

For most backend interviews, one sentence is enough:

> **"The native method stack supports execution of native methods outside the JVM's Java bytecode execution model."**

Don't spend five minutes on it unless the interviewer asks.

---

## 8. The question interviewers LOVE

### "Where is this stored?"

Consider:

```java
class Customer {
    static String bank = "ABC";

    int age;

    void show() {
        int x = 10;
        Customer c = new Customer();
    }
}
```

You should reason like this:

```text
Customer c = new Customer();

        Stack                         Heap
┌─────────────────┐            ┌──────────────────┐
│ c ──────────────┼──────────→ │ Customer object  │
│                 │            │ age = 0          │
└─────────────────┘            └──────────────────┘
```

`c` → local reference in the method's frame.

`new Customer()` → object allocated in the heap.

`x` → local variable in the current stack frame.

`bank` → class/static state associated with the class; don't oversimplify it as "stored in Method Area." The JVM specification doesn't mandate one exact physical location for static fields.

That's an important interview correction.

---

## 9. Heap vs Stack

| Heap                                    | Stack                              |
| --------------------------------------- | ---------------------------------- |
| Shared among threads                    | Each thread has its own            |
| Objects/arrays                          | Stack frames                       |
| Managed by GC                           | Frames removed as methods return   |
| Generally larger                        | Generally smaller                  |
| Lifetime depends on object reachability | Lifetime tied to method invocation |
| `new BankAccount()` object              | `account` local reference          |

### Don't say:

> "All variables are stored in stack."

That's an oversimplification and can get you into trouble.

A better statement is:

> **"Local variables are represented in a method's stack frame, while objects and arrays are allocated in the heap. JVM implementations may optimize or eliminate allocations, so this is a conceptual model rather than a guarantee of physical memory placement."**

That's senior-level wording.

---

## 10. StackOverflowError vs OutOfMemoryError

Another very common interview question.

### StackOverflowError

Usually caused by excessive/deep method calls, often recursion.

```java
static void test() {
    test();
}
```

Eventually:

```text
StackOverflowError
```

because the thread's stack cannot accommodate more frames.

### OutOfMemoryError

Can occur when the JVM cannot satisfy memory allocation.

For example, conceptually:

```java
List<byte[]> list = new ArrayList<>();

while (true) {
    list.add(new byte[1024 * 1024]);
}
```

Eventually the heap may be exhausted:

```text
OutOfMemoryError: Java heap space
```

So:

```text
Stack too deep → StackOverflowError

Heap allocation cannot be satisfied → OutOfMemoryError
```

---

## 11. One complete example

Suppose:

```java
public class Bank {

    static String name = "ABC Bank";

    public static void main(String[] args) {

        int amount = 1000;

        Account account = new Account();

        account.deposit(amount);
    }
}
```

Conceptually:

```text
                    JVM
                     │
       ┌─────────────┴─────────────┐
       │                           │
     HEAP                      THREAD 1
       │                           │
       │                        STACK
       │                           │
       │                    ┌───────────────┐
       │                    │ main() frame  │
       │                    │ amount = 1000 │
       │                    │ account ──────┼─────┐
       │                    └───────────────┘     │
       │                                          │
       ▼                                          ▼
┌──────────────────┐                    ┌────────────────┐
│ Account object   │                    │ Account data   │
│ balance = ...    │                    │                │
└──────────────────┘                    └────────────────┘
```

Meanwhile, class-related information for `Bank`/`Account` is maintained in the JVM's class metadata areas.

---

## 12. One subtle point: JVM memory ≠ Java memory only

When people say:

> "JVM memory"

they sometimes mean the JVM's **runtime data areas**, but a real JVM process also uses **native/off-heap memory**.

For example:

```text
JVM Process
│
├── Heap
├── Thread Stacks
├── Metaspace
├── Code Cache
├── Native memory
└── other JVM/internal structures
```

You don't need to dump all of this in your first interview answer.

Give the core model first, then expand if asked.

---

## 13. The interview flow I want you to memorize

If interviewer says:

### "Explain JVM memory."

Say:

> **"JVM runtime memory is divided into several runtime data areas. The main ones are Heap, JVM Stack, Method Area, PC Register, and Native Method Stack.**
>
> **The Heap is shared across threads and stores objects and arrays, and it is managed by the Garbage Collector. Each thread has its own JVM Stack, and every method invocation creates a stack frame containing local variables and an operand stack.**
>
> **The Method Area is shared and contains class-level runtime information such as class metadata and the runtime constant pool. In HotSpot, class metadata is stored in Metaspace since Java 8.**
>
> **Each thread also has its own PC register to track the current bytecode execution position, while the Native Method Stack supports native method execution."**

Then **stop**.

Let the interviewer ask follow-ups.

---

## 14. Follow-up questions you should be ready for

You should be able to answer these next:

1. **What exactly is stored in the heap?**
2. **What exactly is stored in a stack frame?**
3. **Heap vs stack?**
4. **Why is stack thread-specific?**
5. **Why is heap shared?**
6. **What is garbage collection?**
7. **When does an object become eligible for GC?**
8. **What is StackOverflowError?**
9. **What is OutOfMemoryError?**
10. **What is Metaspace?**
11. **What happened to PermGen?**
12. **Where are static variables stored?**
13. **Where are local variables stored?**
14. **Where are String objects stored?**
15. **What happens in memory when `new` is used?**
16. **What happens when one method calls another?**

For your **Core Java → Banking System** learning path, I'd learn these as one connected topic rather than memorizing isolated definitions. The next natural topic is **"What exactly happens in JVM memory when `BankAccount account = new BankAccount()` executes?"** — that will make heap, stack, references, objects, class loading, and GC click together.

# Java Class Declaration

## Definition

A **class** is a blueprint for creating objects. A class declaration defines the class name, its access/modifier rules, inheritance, implemented interfaces, and its members.

### General Syntax

```java
[access modifier] [other modifiers] class ClassName
        [extends ParentClass]
        [implements Interface1, Interface2] {

    // fields
    // constructors
    // methods
}
```

Example:

```java
public class BankAccount
        extends Account
        implements Transferable, Auditable {

    private double balance;

    public void deposit(double amount) {
        balance += amount;
    }
}
```

---

## 1. Class Name Must Be a Valid Identifier

```java
class BankAccount { }      // ✅
class Bank123 { }          // ✅
class 123Bank { }          // ❌
class Bank-Account { }     // ❌
```

Rules for identifiers:

* Cannot start with a digit
* Cannot contain spaces
* Cannot use most special characters
* Cannot be a Java keyword
* Case-sensitive
* `_` and `$` are technically allowed

### Naming Convention

Use **PascalCase**:

```java
class BankAccount { }
class PaymentProcessor { }
class CustomerService { }
```

---

## 2. Top-Level Class Access Modifiers

A top-level class can be:

```java
public class BankAccount {
}
```

or package-private:

```java
class BankAccount {
}
```

A top-level class cannot be:

```java
private class BankAccount { }    // ❌
protected class BankAccount { }  // ❌
```

`private` and `protected` are allowed for nested classes.

---

## 3. Public Class and File Name

If a top-level class is `public`, the filename must match the class name.

```java
// BankAccount.java

public class BankAccount {
}
```

✅

But:

```java
// BankAccount.java

public class Customer {
}
```

❌

The file must be:

```text
Customer.java
```

---

## 4. Multiple Classes in One File

A `.java` file can contain multiple top-level classes:

```java
public class BankAccount {
}

class Customer {
}

class Transaction {
}
```

But there can be **at most one public top-level class** in the file.

The public class determines the filename.

---

## 5. `extends` — Class Inheritance

A class can extend **only one class**.

```java
class SavingsAccount extends BankAccount {
}
```

Valid:

```java
class SavingsAccount extends BankAccount {
}
```

Invalid:

```java
class SavingsAccount
        extends BankAccount, Account {
}
```

Java does not support multiple class inheritance.

### Rule

```text
extends → maximum ONE class
```

---

## 6. `implements` — Multiple Interfaces

A class can implement multiple interfaces.

```java
class BankAccount
        implements Transferable, Auditable {
}
```

### Rule

```text
implements → MULTIPLE interfaces allowed
```

A class can therefore have:

```java
class SavingsAccount
        extends BankAccount
        implements Transferable, Auditable {
}
```

The order is:

```text
extends
   ↓
implements
```

Not:

```java
implements Transferable
extends BankAccount  // ❌
```

---

## 7. `abstract` Class

An abstract class cannot be directly instantiated.

```java
abstract class Account {

    abstract void calculateInterest();
}
```

This is invalid:

```java
Account account = new Account(); // ❌
```

A concrete subclass can extend it:

```java
class SavingsAccount extends Account {

    @Override
    void calculateInterest() {
        // implementation
    }
}
```

Use an abstract class when you want to provide a common base and potentially require subclasses to implement certain behavior.

---

## 8. `final` Class

A `final` class cannot be extended.

```java
final class Transaction {
}
```

This is invalid:

```java
class OnlineTransaction extends Transaction {
} // ❌
```

Example from Java:

```java
public final class String {
}
```

`String` cannot be subclassed.

---

## 9. `sealed` Class

**Sealed classes became a permanent Java feature in Java 17.**

A sealed class restricts which classes can extend it.

```java
public sealed class Payment
        permits UpiPayment, CardPayment {
}
```

Permitted subclasses must declare themselves as one of:

```text
final
sealed
non-sealed
```

Example:

```java
public sealed class Payment
        permits UpiPayment, CardPayment {
}

final class UpiPayment extends Payment {
}

non-sealed class CardPayment extends Payment {
}
```

This is useful when the domain has a controlled set of subtypes.

Example:

```text
Payment
├── UpiPayment
└── CardPayment
```

---

## 10. Common Class Modifiers

| Modifier     | Meaning                                            |
| ------------ | -------------------------------------------------- |
| `public`     | Accessible wherever the class itself is accessible |
| `abstract`   | Cannot be instantiated directly                    |
| `final`      | Cannot be extended                                 |
| `sealed`     | Restricts permitted subclasses                     |
| `non-sealed` | Opens inheritance under a sealed hierarchy         |

---

## 11. Class Declaration Examples

### Simple class

```java
class Customer {
}
```

### Public class

```java
public class Customer {
}
```

### Abstract class

```java
abstract class Account {
}
```

### Final class

```java
final class Transaction {
}
```

### Inheritance

```java
class SavingsAccount extends Account {
}
```

### Multiple interfaces

```java
class SavingsAccount
        implements Transferable, Auditable {
}
```

### Complete declaration

```java
public final class BankAccount
        extends Account
        implements Transferable, Auditable {
}
```

---

## 12. Banking Project Example

```java
public class BankAccount
        extends Account
        implements Transferable {

    private BigDecimal balance;

    public void deposit(BigDecimal amount) {
        balance = balance.add(amount);
    }
}
```

Here:

```text
public
  → accessible from other packages

class
  → declares a class

BankAccount
  → class name

extends Account
  → inherits from Account

implements Transferable
  → provides Transferable behavior

private BigDecimal balance
  → encapsulated account state
```

---

## 13. Important Rules — Quick Revision

```text
1. Class name must be a valid identifier.

2. Top-level class can be public or package-private.

3. Top-level class cannot be private/protected.

4. A public top-level class must match the filename.

5. A source file can have multiple top-level classes,
   but at most one can be public.

6. A class can extend only ONE class.

7. A class can implement MULTIPLE interfaces.

8. `extends` comes before `implements`.

9. Abstract classes cannot be instantiated directly.

10. Final classes cannot be extended.

11. Sealed classes restrict permitted subclasses.

12. Sealed classes became final in Java 17.
```

---

## Interview Answer

> **"A Java class declaration consists of optional modifiers, the `class` keyword, a valid class name, and optionally an `extends` clause and an `implements` clause. A class can extend only one class but can implement multiple interfaces. A top-level public class must have the same name as its source file, and a source file can contain at most one public top-level class. A class can also be abstract, final, or sealed depending on the inheritance and instantiation requirements."**

---

## Quick Interview Questions

### Q1. Can a class extend multiple classes?

No.

```java
class C extends A, B { } // ❌
```

### Q2. Can a class implement multiple interfaces?

Yes.

```java
class C implements A, B { } // ✅
```

### Q3. Can a top-level class be private?

No.

### Q4. Can a `.java` file contain multiple classes?

Yes, but at most one top-level class can be `public`.

### Q5. What determines the filename?

The public top-level class.

```java
public class BankAccount { }
```

→ `BankAccount.java`

### Q6. Can an abstract class be instantiated?

No.

### Q7. Can a final class be inherited?

No.

### Q8. What is a sealed class?

A class that explicitly restricts which classes can extend it.

### Q9. When did sealed classes become a permanent Java feature?

**Java 17.**

### Q10. `extends` vs `implements`?

```text
extends     → inherit from a class
implements  → implement one or more interfaces
```

---

## One-line memory trick

```text
Class Declaration
      ↓
Modifier → class → Name → extends ONE → implements MANY
```

This is the format I'd continue using for your Java revision notes: **short theory + rules + code + project application + interview answer + rapid-fire questions**.

## Nested Class Rules

A **nested class** is a class declared inside another class or interface.

### Types of Nested Classes

```text
Nested Class
├── Static nested class
└── Inner class
    ├── Member inner class
    ├── Local inner class
    └── Anonymous inner class
```

### 1. Static Nested Class

A static nested class belongs to the outer class, not to an instance of the outer class.

```java
class Bank {

    static class Account {
        void display() {
            System.out.println("Account");
        }
    }
}
```

Usage:

```java
Bank.Account account = new Bank.Account();
account.display();
```

Rules:

* Declared using `static`.
* Does not require an outer-class object.
* Can directly access only `static` members of the outer class.
* Can access private members of the outer class.
* Can have static and instance members of its own.

---

### 2. Member Inner Class

A non-static class declared directly inside another class.

```java
class Bank {

    private String name = "ABC Bank";

    class Account {
        void display() {
            System.out.println(name);
        }
    }
}
```

Usage:

```java
Bank bank = new Bank();
Bank.Account account = bank.new Account();
account.display();
```

Rules:

* Must be associated with an instance of the outer class.
* Can directly access both instance and static members of the outer class.
* Can have access modifiers such as `private`, `protected`, `public`, or package-private.
* Can access private members of the outer class.

---

### 3. Local Class

A class declared inside a method, constructor, or block.

```java
class Bank {

    void process() {

        class Transaction {
            void execute() {
                System.out.println("Processing transaction");
            }
        }

        Transaction transaction = new Transaction();
        transaction.execute();
    }
}
```

Rules:

* Scope is limited to the block where it is declared.
* Cannot use most access modifiers such as `public`, `protected`, or `private`.
* Can access effectively final or final local variables from the enclosing method.
* Useful when a helper class is needed only inside one method.

---

### 4. Anonymous Class

A class without a name, created and instantiated at the same time.

```java
Runnable task = new Runnable() {
    @Override
    public void run() {
        System.out.println("Transaction processing");
    }
};
```

Rules:

* Has no explicit class name.
* Created using `new`.
* Usually used for one-time implementations.
* Can extend one class or implement one interface.
* Cannot explicitly declare constructors.
* Since Java 8, lambdas are often preferred when the target is a functional interface.

---

## Interface Declaration Rules

An **interface** defines a contract that implementing classes agree to follow.

General syntax:

```java
[access modifier] [modifier] interface InterfaceName
        [extends Interface1, Interface2] {

    // constants
    // abstract methods
    // default methods
    // static methods
    // private methods
}
```

Example:

```java
public interface Transferable {

    void transfer();

    default void validate() {
        System.out.println("Validating transfer");
    }
}
```

### Interface Rules

1. Interface name must be a valid identifier.

2. Naming convention is **PascalCase**.

```java
interface Transferable {}
interface PaymentProcessor {}
```

3. A top-level interface can be:

   * `public`
   * package-private

4. A top-level interface cannot be:

   * `private`
   * `protected`

   However, a **nested interface** can use `private`, `protected`, or `public`.

5. A public top-level interface must have the same filename as the interface.

```java
public interface Transferable {}
```

File:

```text
Transferable.java
```

6. Interface fields are implicitly:

```java
public static final
```

So:

```java
interface BankConfig {
    int MAX_TRANSACTION = 100000;
}
```

is equivalent to:

```java
interface BankConfig {
    public static final int MAX_TRANSACTION = 100000;
}
```

7. Interface fields must be initialized.

```java
interface Config {
    int LIMIT = 1000;  // valid
}
```

8. Interface methods are implicitly `public` and `abstract` **unless they are `default`, `static`, or `private` methods**.

```java
interface Transferable {
    void transfer();
}
```

is equivalent to:

```java
interface Transferable {
    public abstract void transfer();
}
```

9. A class uses `implements` to implement an interface.

```java
class BankAccount implements Transferable {
    
    @Override
    public void transfer() {
        System.out.println("Transfer completed");
    }
}
```

10. A class can implement multiple interfaces.

```java
class BankAccount
        implements Transferable, Auditable, Serializable {
}
```

11. An interface can extend multiple interfaces.

```java
interface Transferable {}
interface Auditable {}

interface SecureTransfer
        extends Transferable, Auditable {
}
```

This is one major difference from class inheritance:

```text
Class → extends → ONE class
Class → implements → MANY interfaces

Interface → extends → MANY interfaces
```

12. An interface cannot extend a class.

```java
interface Payment extends Account { } // ❌
```

13. An interface cannot be instantiated directly.

```java
Transferable t = new Transferable(); // ❌
```

14. An interface reference can point to an implementing object.

```java
Transferable t = new BankAccount(); // ✅
```

This demonstrates **polymorphism**.

15. Interface methods can have implementations.

### Default method — Java 8

```java
interface Transferable {

    default void validate() {
        System.out.println("Validating");
    }
}
```

16. Static methods in interfaces were introduced in **Java 8**.

```java
interface PaymentUtil {

    static void log() {
        System.out.println("Payment logged");
    }
}
```

Called using the interface:

```java
PaymentUtil.log();
```

17. Private interface methods were introduced in **Java 9**.

```java
interface PaymentUtil {

    private void audit() {
        System.out.println("Audit");
    }

    default void process() {
        audit();
    }
}
```

18. An interface can be nested inside another class or interface.

```java
class Bank {

    interface PaymentRule {
        boolean isValid();
    }
}
```

Usage:

```java
class Payment implements Bank.PaymentRule {

    @Override
    public boolean isValid() {
        return true;
    }
}
```

A nested interface declared inside another interface is implicitly `public static`.

```java
interface Bank {

    interface PaymentRule {
        boolean isValid();
    }
}
```

Conceptually:

```java
public static interface PaymentRule
```

19. Since Java 8, interfaces can contain:

* abstract methods
* default methods
* static methods

Since Java 9:

* private methods

20. Since Java 17, interfaces can also participate in sealed hierarchies.

```java
public sealed interface Payment
        permits UpiPayment, CardPayment {
}
```

Implementations must follow the sealed hierarchy rules.

---

## Nested Class vs Interface — Quick Comparison

| Feature                      | Nested Class                               | Nested Interface                      |
| ---------------------------- | ------------------------------------------ | ------------------------------------- |
| Declared inside another type | Yes                                        | Yes                                   |
| Can be instantiated directly | Depends on type                            | No                                    |
| Can have fields              | Yes                                        | Yes, implicitly constants             |
| Can have constructors        | Yes, except anonymous classes              | No                                    |
| Can have instance state      | Yes                                        | No                                    |
| Can have methods             | Yes                                        | Yes                                   |
| Can be `private`             | Yes                                        | Yes, when nested                      |
| Can be `static`              | Static nested class                        | Nested interface is implicitly static |
| Main purpose                 | Encapsulate closely related implementation | Define a related contract             |

---

## Banking Project Example

A nested class can model an implementation detail that should not be exposed outside the parent class.

```java
public class BankAccount {

    private final String accountNumber;

    public BankAccount(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    private static class AccountValidator {

        static boolean isValid(String accountNumber) {
            return accountNumber != null
                    && !accountNumber.isBlank();
        }
    }
}
```

A nested interface can define a contract closely related to the enclosing class:

```java
public class PaymentService {

    public interface PaymentProcessor {
        void process();
    }
}
```

Implementation:

```java
class UpiProcessor
        implements PaymentService.PaymentProcessor {

    @Override
    public void process() {
        System.out.println("Processing UPI payment");
    }
}
```

---

## Interview Answer

> “Java supports nested types, including static nested classes, inner classes, local classes, anonymous classes, and nested interfaces. A static nested class does not require an outer-class instance, while a non-static inner class is associated with an outer-class object. Interfaces define contracts and can be implemented by multiple classes. A class can extend only one class but can implement multiple interfaces, while an interface can extend multiple interfaces. Interface fields are implicitly public, static, and final, and interface methods can be abstract, default, static, or private depending on the Java version.”

### Quick Interview Questions

**Q: Can a class be declared inside another class?**
Yes. It is called a nested class.

**Q: What is the difference between static nested class and inner class?**
A static nested class does not require an outer-class instance; an inner class does.

**Q: Can an inner class access private members of the outer class?**
Yes.

**Q: Can a class extend multiple classes?**
No.

**Q: Can a class implement multiple interfaces?**
Yes.

**Q: Can an interface extend multiple interfaces?**
Yes.

**Q: Can an interface extend a class?**
No.

**Q: Can an interface be instantiated?**
No.

**Q: What are interface variables by default?**
`public static final`.

**Q: What are normal interface methods by default?**
`public abstract`, unless they are `default`, `static`, or `private`.

**Q: When were default and static interface methods introduced?**
Java 8.

**Q: When were private interface methods introduced?**
Java 9.

**Q: Can a nested interface be private?**
Yes, when declared inside a class or another suitable enclosing type.

### Memory Trick

```text
Nested Classes
→ Static nested → no outer object
→ Inner         → needs outer object
→ Local         → inside method/block
→ Anonymous     → no class name

Classes
→ extends ONE class
→ implements MANY interfaces

Interfaces
→ extends MANY interfaces
```

### Version Note

```text
Nested classes/interfaces → core Java feature (Java 1.1-era nested types)

Default interface methods → Java 8
Static interface methods  → Java 8
Private interface methods → Java 9
Sealed interfaces         → Java 17
```
# Object-Related Concepts

## 1. Object

An **object** is an instance of a class. It has **state (fields)** and **behavior (methods)**.

```java
BankAccount account = new BankAccount();
```

```text
class  → blueprint
object → actual instance
```

---

## 2. Constructor

A **constructor** initializes an object when `new` is used.

```java
class BankAccount {

    BankAccount() {
        System.out.println("Account created");
    }
}
```

Rules:

* Same name as the class.
* No return type, not even `void`.
* Called automatically when an object is created.
* Can be overloaded.
* Not inherited.
* If no constructor is written, Java provides a default no-arg constructor **only if the class has no constructor**.

```java
BankAccount account = new BankAccount();
```

---

## 3. `this` Keyword

`this` refers to the **current object**.

### Common uses

**1. Resolve field/parameter name conflict**

```java
class Account {

    private String number;

    Account(String number) {
        this.number = number;
    }
}
```

**2. Call another constructor**

```java
Account() {
    this("UNKNOWN");
}
```

**3. Pass current object**

```java
process(this);
```

**4. Return current object**

```java
return this;
```

Memory trick:

```text
this = current object
```

---

## 4. `new` Keyword

`new` creates an object and invokes its constructor.

```java
BankAccount account = new BankAccount();
```

Conceptually:

```text
new
 ↓
create object
 ↓
constructor runs
 ↓
reference returned
```

---

## 5. Reference Variable

A reference variable stores a **reference to an object**, not the object itself.

```java
BankAccount account = new BankAccount();
```

```text
account ───────→ BankAccount object
```

Multiple references can point to the same object:

```java
BankAccount a = new BankAccount();
BankAccount b = a;
```

```text
a ──┐
    ├──→ same object
b ──┘
```

---

## 6. `null`

`null` means a reference points to **no object**.

```java
BankAccount account = null;
```

Calling a method through it causes:

```java
account.deposit(); // NullPointerException
```

---

## 7. Constructor vs Method

| Constructor                   | Method                                          |
| ----------------------------- | ----------------------------------------------- |
| Initializes object            | Performs behavior                               |
| Same name as class            | Any valid name                                  |
| No return type                | Has return type or `void`                       |
| Called during object creation | Called explicitly                               |
| Not inherited                 | Can be inherited/overridden depending on method |

---

## Interview Answer

> **“An object is an instance of a class. A constructor initializes an object when it is created using `new`. The `this` keyword refers to the current object and is commonly used to distinguish instance fields from parameters or to invoke another constructor. A reference variable holds a reference to an object, while `null` represents the absence of an object reference.”**

### One-line Memory

```text
Class → blueprint
Object → instance
new → creates object
Constructor → initializes object
this → current object
Reference → points to object
null → points to no object
```

## Object Lifecycle — Compact Flow

```text
Class loaded
    ↓
new BankAccount(...)
    ↓
Memory allocated for object
    ↓
Instance fields get default values
    ↓
Constructor executes
    ↓
Object becomes usable
    ↓
Reference(s) point to object
    ↓
Object is used
    ↓
References are removed / object becomes unreachable
    ↓
Eligible for Garbage Collection
    ↓
GC reclaims memory
```

### Example

```java
BankAccount account = new BankAccount("123");
```

```text
new
 ↓
allocate object
 ↓
default field values
 ↓
constructor runs
 ↓
account ──→ object
 ↓
object used
 ↓
account = null
 ↓
object may become unreachable
 ↓
eligible for GC
```

### Key Point

**Garbage Collection is automatic, but becoming eligible for GC does not mean the object is immediately destroyed.**

# Assignment 2 ->
Create a java application for the following
1.1 Create a class to represent 3D Box. Provide tight encapsulation Data members(instance variables = state) -width, height, depth : double --- private
1.2 Add a constructor - to init  state of the box
1.3 Add a business logic in instance methods
1. To return Box details in string form (dims of Box)
method declaration -- access specifier, ret type, name, args method def -- body 
2. return computed volume of the box
1.4 Create a TestBox class, which allows user to supply 3 dims as user inputs via scanner. create Box object and display volume.

# Memory Diagrams
* Stack Heap Method area, class pointer
* java.lang.NullPointerException
* unreachable object marked for GC

# Garbage Collection
* JVM creates 2 threads 
* main thread(executes main sequentially) - foreground thread
* G.C. -- deamon thread -- background thread - JVM activates it periodically(only if required) -- GC releases the memory occupied by un-referenced objects allocated on the heap(the obj whose no. of ref = 0)
* How to request for GC? -> API of System class -> ```public static void gc()```
* Object class API -> ```protected void finalize() throws Throwable``` -> Automatically called by the garbage collector on an object before garbage collection of the object takes place.
* Releasing of non-java resources. (closeing of DB connection, closing file handles, closing socket connection) is NOT done automatically by GC
* Triggers for marking the object for GC :
1. Nullifying all valid refs.
2. re-assigning the reference to another object
3. Object created within a method & its ref NOT returned to the caller
4. Island of isolation

* Method area will get empty when JVM terminates, class unloading happens, GC doesn't clear method area

## Garbage Collection (GC)

**Garbage Collection** is the JVM's automatic process of reclaiming memory occupied by objects that are **no longer reachable**.

### Key Points

* **Automatic** — JVM manages it; developers don't explicitly free objects.
* Works mainly on the **Heap**.
* An object becomes **GC-eligible when it is unreachable**.
* GC does **not** mean immediate destruction.
* `System.gc()` is only a **request/hint**, not a guarantee.
* GC primarily reclaims **unreachable objects**, not simply objects whose variables are set to `null`.
* Multiple references can keep an object reachable.
* Java can have different **Garbage Collectors** optimized for different goals.
* GC involves a trade-off between **throughput, latency, and memory usage**.
* **Stop-the-world (STW)** pauses can occur during GC, though modern collectors minimize pause time.
* Objects generally become harder/less expensive to collect as they survive longer; generational collectors exploit this.
* `OutOfMemoryError` can occur when the JVM cannot satisfy memory allocation, even though GC runs.
* **Memory leak in Java** can still happen when unwanted objects remain reachable, e.g. through static collections, caches, listeners, or `ThreadLocal`s.
* `finalize()` is **deprecated for removal** and should not be used for resource cleanup.
* Use **try-with-resources** for resources such as files, sockets, and DB connections; GC is not a replacement for explicit resource management.

### Reachability

```text
Reference exists
      ↓
Object reachable
      ↓
Not GC-eligible

Reference removed
      ↓
No path from GC roots
      ↓
Object unreachable
      ↓
GC-eligible
```

### GC Roots

Objects can remain reachable through GC roots such as:

```text
Active thread references
Static references
Local variables / stack references
JNI references
```

### Example

```java
BankAccount a = new BankAccount();
BankAccount b = a;

a = null;       // object still reachable through b
b = null;       // object may now be GC-eligible
```

### Interview Answer

> **“Garbage Collection is the JVM's automatic memory-management mechanism that identifies objects that are no longer reachable and reclaims their heap memory. An object becoming eligible for GC does not mean it is immediately collected. Modern JVMs use different collectors and may perform concurrent and stop-the-world phases to balance throughput, latency, and memory usage.”**

### One-Line Memory

```text
Unreachable object → GC-eligible → GC may reclaim heap memory
```

### Most Important Interview Questions

**Q: Where does GC mainly work?**
Heap.

**Q: When is an object eligible for GC?**
When it is no longer reachable from GC roots.

**Q: Does `System.gc()` force GC?**
No. It only requests/suggests GC.

**Q: Can Java have memory leaks?**
Yes. Objects can remain reachable even when they are no longer logically needed.

**Q: Does GC clean stack memory?**
No. Stack frames are managed as method calls return; GC primarily manages heap objects.

**Q: Is GC immediate after `obj = null`?**
No. The object only becomes potentially GC-eligible if no other reachable reference exists.

**Q: Is `finalize()` recommended?**
No. It is deprecated for removal.

**Q: What does `OutOfMemoryError` mean?**
The JVM could not satisfy a memory allocation request; it does not necessarily mean GC never ran.

# Daemon Thread

A **daemon thread** is a background thread that provides services to other threads and **does not keep the JVM alive**.

### Key Points

* Runs in the **background**.
* JVM does **not wait** for daemon threads to finish.
* JVM exits when **all non-daemon threads finish**.
* A daemon thread may be terminated when JVM exits.
* Must call `setDaemon(true)` **before** `start()`.
* By default, a newly created thread inherits the daemon status of its parent thread.
* Common use: background/support tasks.
* **GC is not simply “a daemon thread”** — Garbage Collection is a JVM subsystem and collector implementation may use multiple internal threads.

### Example

```java
Thread t = new Thread(() -> {
    while (true) {
        System.out.println("Background work");
    }
});

t.setDaemon(true);
t.start();
```

If the `main` thread finishes and no other non-daemon threads remain:

```text
main thread → finished
      ↓
No non-daemon threads
      ↓
JVM exits
      ↓
Daemon thread stops
```

### Daemon vs Non-Daemon

| Non-Daemon                          | Daemon                           |
| ----------------------------------- | -------------------------------- |
| Keeps JVM alive                     | Does not keep JVM alive          |
| JVM waits for it                    | JVM doesn't wait                 |
| Used for important application work | Used for background/support work |
| Default for `main` thread           | Must be explicitly/inherited     |

### Important Rule

```java
t.setDaemon(true);
t.start();          // ✅
```

```java
t.start();
t.setDaemon(true);  // ❌ IllegalThreadStateException
```

### Interview Answer

> **“A daemon thread is a background thread that does not prevent the JVM from shutting down. Once all non-daemon threads finish, the JVM can exit without waiting for daemon threads. Its daemon status must be set before the thread is started.”**

### Memory Trick

```text
Non-daemon → JVM waits
Daemon     → JVM doesn't wait
```


* **GC is not itself a daemon thread.** GC is a **JVM subsystem/process** for automatic memory management.
* JVM garbage collectors use **internal JVM threads** to perform GC work.
* Some of those internal GC threads may have daemon-like behavior, but you should **not define GC as “a daemon thread.”**
* The important relationship is:

```text
Daemon thread
→ JVM does not wait for it

GC
→ JVM's automatic heap-memory management mechanism
```

### Interview-safe answer

> **“Garbage Collection is a JVM memory-management mechanism, not a daemon thread. The JVM uses internal GC threads to perform collection, but GC itself should not be described as a daemon thread.”**

**Memory trick:**
`GC ≠ daemon thread`
`GC → uses JVM-internal threads`

# Assignment 2 continued:
1. Create Cubes(in Box class itself) overload constructor to create a cube
2. Add an instance method to Box class to test equality of 2 boxes : based upon box dims.
* instance method here because static method cannot access instance variables
3. Add a method to Box class to return a new Box with modified offset dims & test it with the tester.
# Constructor Chaining, Constructor overloading
DRY principle -> Do not repeat yourself
```
class Box{
    private double width, depth,height;
    Box(double w, double d, double height){
        width = w;
        depth = d;
        this.height = height;
    }
    Box(double side) // -> for a cube, constructor overloading
    {
        this(side,side,side); // constructor chaining
    }
}
```
* Heart of Java -> toString, equals, hashcode, compare, compareTo. 
* ```boolean isEqual(Box anotherBox)// primitive and ref types of variables are passed by value(copy) 

# Packages