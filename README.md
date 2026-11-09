MediCare – Smart Hospital Management System

Overview

MediCare is a Java-based desktop application developed to simplify basic hospital management activities. It provides an easy-to-use graphical interface for managing patient records, doctor information, and appointments.

The application is developed using Java and JavaFX and uses local text files to store information.

Features

1. Patient Management

- Register new patients.
- View patient records.
- Delete patient records.

2. Doctor Management

- Register new doctors.
- View doctor details.
- Delete doctor records.

3. Appointment Management

- Book appointments with doctors.
- Cancel appointments.
- Generate unique appointment token IDs.
- Prevent duplicate doctor, date, and time bookings.
- Suggest alternative available slots when a selected slot is occupied.

4. Smart Appointment Scheduling

- Display available time slots based on doctor working hours.
- Prevent booking appointments on past dates.
- Refresh available slots after cancellation.

5. Dashboard and Analytics

- Total patients.
- Total doctors.
- Total appointments.
- Active appointments.
- Cancelled appointments.

6. Local Data Storage

- Stores patient, doctor, and appointment records in separate text files.
- Maintains records after closing and reopening the application.
- Works without an internet connection.

Technologies Used

- Programming Language: Java
- GUI Framework: JavaFX
- IDE: Eclipse
- Data Storage: Local text files
- JavaFX Components: TableView, Buttons, TextFields, DatePicker, Layouts and Alerts.

Project Structure

MediCareHospital/
│
├── src/
│   └── com/
│       └── medicare/
│           └── Main.java
│
├── data/
│   ├── patients.txt
│   ├── doctors.txt
│   └── appointments.txt
│
├── .project
├── .classpath
└── README.md

How to Run the Project

1. Install Java JDK.
2. Download and configure the JavaFX SDK.
3. Install Eclipse IDE.
4. Download or clone this repository.
5. Import the project into Eclipse using File → Import → Existing Projects into Workspace.
6. Configure the JavaFX library and VM arguments in the Run Configuration.
7. Open "src/com/medicare/Main.java".
8. Right-click and select Run As → Java Application.

Internet Requirement

MediCare is a locally running desktop application. An internet connection is not required for its core features after Java and JavaFX have been installed and configured.

Future Enhancements

- MySQL database integration.
- User login and authentication.
- Billing and payment management.
- Prescription management.
- Online appointment booking.
- Patient medical history management.

Project Purpose

The main objective of MediCare is to make basic hospital management easier, faster, and more organized through a simple desktop application.

Author

Developed as a hackathon project.

---

MediCare – Smart Hospital Management System