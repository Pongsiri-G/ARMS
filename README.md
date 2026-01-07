
# Academic Request Management System (ARMS)

A desktop-based academic request management system developed as part of the course
**01418211: Software Construction** at Kasetsart University.

This project provides a centralized system for managing academic-related requests between students, advisors, and faculty officers, covering the full request lifecycle from submission to approval.

---

## 🏗 System Overview

ARMS is designed as a **monolithic desktop application** built with Java and JavaFX, focusing on clear separation of concerns using an MVC-inspired architecture.
The system supports multiple user roles and enforces role-based access to academic request workflows.

---

## Core Application (Java + JavaFX)

A desktop application that serves all system roles through a unified interface.

### Supported Roles

* Student
* Advisor
* Faculty Officer
* Administrator

### Key Features

* Automated PDF generation using iText PDF for Thai-language documents with custom font embedding.
* In-app pdf rendering and preview using Apache PDFBox
* Submit academic requests (e.g. registration issues, academic petitions)
* View request history and current request status
* Advisor review and approval workflow
* Officer verification and final approval
* Role-based access control (RBAC)
* Request status tracking (Pending / Approved / Rejected)
* File-based data persistence
* User authentication and authorization
* JavaFX-based UI with CSS styling



---

## 📡 Technologies

### Core

* Java
* JavaFX
* FXML

### UI

* JavaFX CSS

### Data

* File-based persistence (CSV / text files)

### Utils
* Itex PDF
* Apache PDFBox

### Design Concepts

* MVC-style separation
* Role-Based Access Control (RBAC)
* Layered architecture
* Object-Oriented Design principles

---

## 📂 Project Structure

```
ARMS/
├── src/
│   └── main/
│       ├── java/
│       │   └── ku/
│       │       └── cs/
│       │           ├─ cs21167project  
│       │           │  └── MainApplication  # Main Application
│       │           ├── controllers/   # JavaFX controllers
│       │           ├── models/        # Domain models
│       │           └── services/      # Business logic
│       │           
│       │              
│       └── resources/
│           ├── images/     # System Images
│           ├── font/       # System fonts
│           ├── ku/cs/view/ # UI layouts
│           └── style/      # UI styles
├── data/                   # Runtime data storage
├── pom.xml                 # Maven configuration
└── README.md
```

---

## 🛠️ Installation & Execution Guide

### Option 1: Run from Pre-built Application (Recommended)

1. Download the application from the GitHub repository
   Navigate to **Tags** and select **ConfirmFinal**

2. Download and extract the `.zip` file
   After extraction, a folder named **copy-and-pasta** will be created

3. Launch the application
   Double-click the executable file inside the folder to start the system

> **Note:** This application supports **Windows operating system only**

---

### Option 2: Run from Source Code (Development)

1. Clone the repository

```bash
git clone https://github.com/Pongsiri-G/ARMS.git
cd ARMS
```

2. Open the project

   * Recommended IDE: **IntelliJ IDEA**
   * Import as a **Maven project**

3. Run the application

   * Run the main JavaFX application class
   * Ensure Java version is compatible with JavaFX (**Java 11+ recommended**)

---

## 🔐 Sample System Accounts (Username / Password)

The following sample accounts are provided for testing different system roles:

* **Administrator**

  * Username: `Admin`
  * Password: `1111`

* **Advisor**

  * Username: `Jak`
  * Password: `jak`

* **Faculty Officer**

  * Username: `Farm`
  * Password: `farm`

* **Department Officer**

  * Username: `Rain`
  * Password: `rain`

* **Student**

  * Username: `Nicha`
  * Password: `nicha`

---

## 👨‍🎓 Authors

* Pongsiri-G
* TawanPolsan-2005
* FirstGameGG
* B184B
---
