# 📘 BookStore API Automation Project

This project automates testing of a FastAPI-based BookStore application using **Java**, **RestAssured**, **TestNG**, and **Allure Reports**. It includes full CRUD API testing, JWT authentication, and is integrated with **GitHub Actions** CI/CD.

---

## 🚀 Tech Stack

| Tool        | Purpose                          |
|-------------|----------------------------------|
| Java 17     | Programming Language             |
| Maven       | Build Tool                       |
| RestAssured | API Testing Framework            |
| TestNG      | Test Orchestration & Assertions  |
| Allure      | Test Reporting                   |
| GitHub Actions | CI/CD Workflow               |

---

## 🧪 Test Coverage

- ✅ Signup & Login
- ✅ JWT Token generation
- ✅ Create / Read / Update / Delete Book
- ✅ Schema validation
- ✅ Status code, headers & payload validations
- ✅ Positive & negative scenarios

---

## 📂 Project Structure

```
BookStoreAPIJuly2025/
├── src/
│   ├── main/              → Configuration files
│   └── test/              → Test classes
│       ├── apis/          → API endpoints & calls
│       ├── tests/         → TestNG test classes
│       └── utils/         → Helpers, token manager, etc.
├── testng-all-api.xml     → Master suite for API tests
├── pom.xml                → Maven project config
├── .github/workflows/     → CI/CD GitHub Actions config
└── README.md              → You’re here
```

---

## 🧾 How to Run

### 📌 Pre-requisites
- Java 17
- Maven 3.6+
- Allure CLI (optional for local reporting)

### 🔹 Run Tests Locally

```bash
mvn clean test
```

### 🔹 Generate Allure Report

```bash
mvn allure:report
allure open target/site/allure-maven-plugin
```

---

## ⚙️ GitHub Actions CI

Tests and Allure reports run automatically on every `push` to the `master` branch.

---

## ✍️ Author

**Senthil Kumar Balarajendiran**  
🔗 [LinkedIn](https://www.linkedin.com/in/senthilkumarbalarajendiran/)

---
[Still planning for furthor improvments on the reporting side.]
