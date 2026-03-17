# TestNG Selenium Project

A UI test automation framework built with **Selenium 4**, **TestNG 7**, and **Maven**, following the Page Object Model (POM) pattern.

---

## Table of Contents

1. [Project Structure](#project-structure)
2. [Architecture Overview](#architecture-overview)
3. [testng.xml — Detailed Breakdown](#testngxml--detailed-breakdown)
4. [Running Tests with Maven](#running-tests-with-maven)
5. [Screenshot on Failure](#screenshot-on-failure)
6. [Dependencies](#dependencies)

---

## Project Structure

```
TestNG_Selenium_Project/
├── pom.xml                          # Maven build descriptor
├── testng.xml                       # TestNG suite configuration
└── src/
    ├── main/java/org/dani/
    │   └── pages/
    │       ├── BasePage.java        # Shared page utilities (PageFactory, waits)
    │       ├── LoginPage.java       # Login page object
    │       └── HomePage.java       # Home page object
    ├── resources/
    │   └── screenshots/             # Auto-created; failure screenshots land here
    └── test/java/org/dani/
        ├── listeners/
        │   └── ScreenshotListener.java   # TestNG ITestListener — captures screenshots on failure
        └── tests/
            ├── BaseTest.java        # WebDriver lifecycle (@BeforeClass / @AfterClass)
            ├── LoginTests.java
            ├── PostTests.java
            └── RegistrationTests.java
```

**Key design decisions:**

| Package | Purpose |
|---|---|
| `org.dani.pages` | Page Object classes — no test logic, only UI interactions |
| `org.dani.tests` | Test classes and `BaseTest` — test logic only |
| `org.dani.listeners` | Cross-cutting infrastructure (listeners, reporters) — separated from tests |

---

## Architecture Overview

### Page Object Model (POM)

Each page of the app under test is represented by a dedicated class. Test classes call methods on page objects rather than interacting with Selenium directly. This keeps tests readable and centralises element locators.

```
Test class  →  Page Object  →  Selenium WebDriver  →  Browser
```

### Inheritance chain

```
BasePage          ← all page objects extend this
BaseTest          ← all test classes extend this
  └─ LoginTests
  └─ PostTests
  └─ RegistrationTests
```

`BasePage` initialises `PageFactory` with `AjaxElementLocatorFactory`, so every `@FindBy` field in a subclass automatically polls the DOM for up to 10 seconds before throwing `NoSuchElementException`.

`BaseTest` manages the `WebDriver` lifecycle: it creates a `ChromeDriver` before each test class and quits it after, even if tests fail (`alwaysRun = true`).

---

## testng.xml — Detailed Breakdown

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE suite SYSTEM "http://testng.org/testng-1.0.dtd">

<suite name="Regression Suite" verbose="2" preserve-order="true"
       parallel="none" thread-count="1">

    <listeners>
        <listener class-name="org.dani.listeners.ScreenshotListener"/>
    </listeners>

    <test name="Login Tests">
        <classes>
            <class name="org.dani.tests.LoginTests"/>
        </classes>
    </test>

    <test name="Post Tests">
        <classes>
            <class name="org.dani.tests.PostTests"/>
        </classes>
    </test>

    <test name="Registration Tests">
        <classes>
            <class name="org.dani.tests.RegistrationTests"/>
        </classes>
    </test>

</suite>
```

### `<suite>` attributes

| Attribute | Value | Meaning |
|---|---|---|
| `name` | `"Regression Suite"` | Label shown in TestNG reports. Should be descriptive, **not** a file path. |
| `verbose` | `2` | Console output level. `0` = silent, `2` = standard, `10` = maximum debug. |
| `preserve-order` | `true` | Tests run in the order they appear in this file (not sorted alphabetically). |
| `parallel` | `"none"` | All tests run sequentially in a single thread. Change to `"tests"` to run each `<test>` block in its own thread. |
| `thread-count` | `1` | Number of threads used when `parallel` is not `"none"`. Increase together with the `parallel` attribute. |

**Enabling parallel execution** (future reference):

```xml
<suite name="Regression Suite" parallel="tests" thread-count="3"></suite>
```

This would run `Login Tests`, `Post Tests`, and `Registration Tests` simultaneously, each in its own thread — useful for CI speed-up. Each test class must have its own `WebDriver` instance (already the case here via `BaseTest`).

### `<listeners>` block

```xml
<listeners>
    <listener class-name="org.dani.listeners.ScreenshotListener"/>
</listeners>
```

Registers a listener **at suite level** — it applies to every `<test>` block automatically. This is the correct place to register listeners rather than via `@Listeners` annotation on `BaseTest`, because:

- The annotation approach would register the listener once per test class, potentially firing it multiple times.
- The XML approach registers it exactly once for the entire suite.

### `<test>` blocks

Each `<test>` block groups related test classes under a meaningful name. The name appears in TestNG HTML reports, making it easy to identify which feature area failed.

```xml
<test name="Login Tests">
    <classes>
        <class name="org.dani.tests.LoginTests"/>
    </classes>
</test>
```

**Why not `<methods><include>`?**  
Explicitly listing methods inside `<methods>` is only necessary when you want to run a *subset* of a class's tests. Omitting the `<methods>` block means all `@Test` methods in the class are included automatically — less maintenance overhead as you add new tests.

---

## Running Tests with Maven

> **Note:** The `pom.xml` currently has no `maven-surefire-plugin` configuration, so the commands below for suite-level control require adding that plugin. The `-Dtest` flag works with any Maven project out of the box.

### Run the full suite

```bash
mvn test
```

Runs everything defined in `testng.xml`.

### Run a single test class

```bash
# Login tests only
mvn test -Dtest=LoginTests

# Post tests only
mvn test -Dtest=PostTests

# Registration tests only
mvn test -Dtest=RegistrationTests
```

### Run a single test method

```bash
mvn test -Dtest=LoginTests#testLogin
```

### Run multiple classes in one command

```bash
mvn test -Dtest=LoginTests,PostTests
```

### Skip tests (compile only)

```bash
mvn package -DskipTests
```

---

## Screenshot on Failure

`ScreenshotListener` implements TestNG's `ITestListener` interface and overrides `onTestFailure`. When any test fails:

1. The listener retrieves the `WebDriver` instance from `BaseTest`.
2. Captures a full-window screenshot using `TakesScreenshot`.
3. Saves the file to `src/resources/screenshots/` with the format:

```
<testMethodName>_<yyyy-MM-dd_HH-mm-ss>.png
```

Example: `testLogin_2026-03-17_14-30-05.png`

The directory is created automatically if it does not exist.

---

## Dependencies

| Library | Version | Scope |
|---|---|---|
| `org.testng:testng` | 7.12.0 | `test` |
| `org.seleniumhq.selenium:selenium-java` | 4.40.0 | `compile` |

Java 17+ is recommended. ChromeDriver is managed automatically by Selenium Manager (bundled in Selenium 4.6+) — no manual driver download required.
