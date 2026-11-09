package com.medicare;



import javafx.application.Application;

import javafx.collections.FXCollections;

import javafx.collections.ObservableList;

import javafx.collections.transformation.FilteredList;

import javafx.geometry.Insets;

import javafx.geometry.Pos;

import javafx.scene.Scene;

import javafx.scene.control.*;

import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.layout.*;

import javafx.scene.paint.Color;

import javafx.scene.text.Font;

import javafx.stage.Stage;



import java.io.BufferedReader;

import java.io.BufferedWriter;

import java.io.IOException;

import java.nio.file.Files;

import java.nio.file.Path;

import java.nio.file.Paths;



public class Main extends Application {



       private BorderPane root;



       // Patient data and controls

       private final ObservableList<Patient> patients =

               FXCollections.observableArrayList();

       private final String PATIENT_FILE = "data/patients.txt";



       private TextField nameField, ageField, phoneField, searchField;

       private ComboBox<String> genderBox;

       private TextArea historyField;

       private TableView<Patient> patientTable;



       // Appointment data and controls
    private final ObservableList<Appointment> appointments = FXCollections.observableArrayList();
    private final String APPOINTMENT_FILE = "data/appointments.txt";
    private ComboBox<String> appointmentPatientBox, appointmentDoctorBox, appointmentTimeBox;
    private DatePicker appointmentDatePicker;
    private TableView<Appointment> appointmentTable;

    // Doctor data and controls

       private final ObservableList<Doctor> doctors =

               FXCollections.observableArrayList();

       private final String DOCTOR_FILE = "data/doctors.txt";



       private TextField doctorNameField, doctorPhoneField, doctorSearchField;

       private ComboBox<String> specializationBox, availabilityBox;

       private TableView<Doctor> doctorTable;



       @Override

       public void start(Stage stage) {

           loadPatients();

           loadDoctors();
        loadAppointments();



           root = new BorderPane();



           VBox sidebar = new VBox(20);

           sidebar.setPadding(new Insets(25));

           sidebar.setPrefWidth(220);

           sidebar.setStyle("-fx-background-color: #172554;");



           Label logo = new Label("🏥 MediCare");

           logo.setFont(Font.font("Arial", 24));

           logo.setTextFill(Color.WHITE);



           sidebar.getChildren().addAll(

                   logo,

                   new Separator(),

                   navButton("Dashboard"),

                   navButton("Patients"),

                   navButton("Doctors"),

                   navButton("Appointments"),

                   navButton("Search")

           );



           root.setLeft(sidebar);

           showDashboard();



           Scene scene = new Scene(root, 1150, 750);

           stage.setTitle("MediCare Hospital Management System");

           stage.setScene(scene);

           stage.show();

       }



       private Button navButton(String name) {

           Button btn = new Button(name);

           btn.setMaxWidth(Double.MAX_VALUE);

           btn.setAlignment(Pos.CENTER_LEFT);

           btn.setPadding(new Insets(12));

           btn.setStyle(

                   "-fx-background-color: transparent;" +

                   "-fx-text-fill: white;" +

                   "-fx-font-size: 15px;" +

                   "-fx-cursor: hand;"

           );



           btn.setOnAction(e -> {

               switch (name) {

                   case "Dashboard":

                       showDashboard();

                       break;

                   case "Patients":

                       showPatients();

                       break;

                   case "Doctors":

                       showDoctors();

                       break;

                   case "Appointments":

                       showAppointments();

                       break;

                   case "Search":

                       showSearch();

                       break;

                   default:

                       showPlaceholder(name);

               }

           });

           return btn;

       }



private void showDashboard() {

    VBox page = new VBox(25);
    page.setPadding(new Insets(30));
    page.setStyle("-fx-background-color: #f1f5f9;");

    Label heading = new Label("Hospital Dashboard");
    heading.setFont(Font.font("Arial", 28));

    Label welcome = new Label(
        "Welcome to MediCare Hospital Management System"
    );

    // Calculate appointment statistics
    long activeAppointments = appointments.stream()
        .filter(a -> a.getStatus().equals("Booked"))
        .count();

    long cancelledAppointments = appointments.stream()
        .filter(a -> a.getStatus().equals("Cancelled"))
        .count();

    // First row of dashboard cards
    HBox row1 = new HBox(20);

    row1.getChildren().addAll(
        createCard(
            "Total Patients",
            String.valueOf(patients.size()),
            "#2563eb"
        ),

        createCard(
            "Total Doctors",
            String.valueOf(doctors.size()),
            "#059669"
        ),

        createCard(
            "Total Appointments",
            String.valueOf(appointments.size()),
            "#d97706"
        )
    );

    // Second row of dashboard cards
    HBox row2 = new HBox(20);

    row2.getChildren().addAll(
        createCard(
            "Active Appointments",
            String.valueOf(activeAppointments),
            "#0891b2"
        ),

        createCard(
            "Cancelled Appointments",
            String.valueOf(cancelledAppointments),
            "#dc2626"
        )
    );

    // Quick action buttons
    Button patientBtn = new Button("+ Register Patient");
    Button doctorBtn = new Button("+ Add Doctor");
    Button appointmentBtn = new Button("+ Book Appointment");

    styleButton(patientBtn);
    styleButton(doctorBtn);
    styleButton(appointmentBtn);

    patientBtn.setOnAction(e -> showPatients());
    doctorBtn.setOnAction(e -> showDoctors());
    appointmentBtn.setOnAction(e -> showAppointments());

    HBox actions = new HBox(
        12, patientBtn, doctorBtn, appointmentBtn
    );

    Label recent = new Label("Hospital Overview");
    recent.setFont(Font.font("Arial", 20));

    Label activity = new Label(
        "Patients registered: " + patients.size()
        + "\nDoctors registered: " + doctors.size()
        + "\nActive appointments: " + activeAppointments
        + "\nCancelled appointments: " + cancelledAppointments
    );

    activity.setFont(Font.font("Arial", 15));

    VBox activityBox = new VBox(activity);
    activityBox.setPadding(new Insets(20));
    activityBox.setStyle(
        "-fx-background-color: white;" +
        "-fx-background-radius: 10;"
    );

    page.getChildren().addAll(
        heading,
        welcome,
        row1,
        row2,
        actions,
        recent,
        activityBox
    );

    root.setCenter(page);
}

   




       private VBox createCard(String title, String value, String color) {

           Label heading = new Label(title);

           heading.setTextFill(Color.WHITE);

           heading.setFont(Font.font("Arial", 16));



           Label count = new Label(value);

           count.setTextFill(Color.WHITE);

           count.setFont(Font.font("Arial", 30));



           VBox card = new VBox(15, heading, count);

           card.setPadding(new Insets(20));

           card.setPrefWidth(220);

           card.setStyle(

                   "-fx-background-color: " + color +

                   "; -fx-background-radius: 12;"

           );

           return card;

       }



       // ---------------- PATIENT MANAGEMENT ----------------



       private void showPatients() {

           VBox page = new VBox(15);

           page.setPadding(new Insets(25));

           page.setStyle("-fx-background-color: #f1f5f9;");



           Label heading = new Label("Patient Management");

           heading.setFont(Font.font("Arial", 27));



           GridPane form = new GridPane();

           form.setHgap(15);

           form.setVgap(12);

           form.setPadding(new Insets(20));

           form.setStyle(

                   "-fx-background-color: white;" +

                   "-fx-background-radius: 10;"

           );



           nameField = new TextField();

           nameField.setPromptText("Enter patient name");



           ageField = new TextField();

           ageField.setPromptText("Enter age");



           genderBox = new ComboBox<>();

           genderBox.getItems().addAll("Male", "Female", "Other");

           genderBox.setPromptText("Select gender");

           genderBox.setMaxWidth(Double.MAX_VALUE);



           phoneField = new TextField();

           phoneField.setPromptText("10-digit phone number");



           historyField = new TextArea();

           historyField.setPromptText("Enter medical history");

           historyField.setPrefRowCount(2);



           form.add(new Label("Patient Name:"), 0, 0);

           form.add(nameField, 1, 0);

           form.add(new Label("Age:"), 2, 0);

           form.add(ageField, 3, 0);



           form.add(new Label("Gender:"), 0, 1);

           form.add(genderBox, 1, 1);

           form.add(new Label("Phone:"), 2, 1);

           form.add(phoneField, 3, 1);



           form.add(new Label("Medical History:"), 0, 2);

           form.add(historyField, 1, 2, 3, 1);



           Button saveButton = new Button("Save Patient");

           Button clearButton = new Button("Clear");

           styleButton(saveButton);

           styleButton(clearButton);



           saveButton.setOnAction(e -> addPatient());

           clearButton.setOnAction(e -> clearPatientForm());



           form.add(new HBox(12, saveButton, clearButton), 1, 3);



           searchField = new TextField();

           searchField.setPromptText("Search by ID, name or phone...");

           searchField.setMaxWidth(400);



           patientTable = new TableView<>();

           TableColumn<Patient, String> idCol =

                   new TableColumn<>("Patient ID");

           idCol.setCellValueFactory(new PropertyValueFactory<>("id"));



           TableColumn<Patient, String> nameCol =

                   new TableColumn<>("Name");

           nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));



           TableColumn<Patient, Integer> ageCol =

                   new TableColumn<>("Age");

           ageCol.setCellValueFactory(new PropertyValueFactory<>("age"));



           TableColumn<Patient, String> genderCol =

                   new TableColumn<>("Gender");

           genderCol.setCellValueFactory(new PropertyValueFactory<>("gender"));



           TableColumn<Patient, String> phoneCol =

                   new TableColumn<>("Phone");

           phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));



           TableColumn<Patient, String> historyCol =

                   new TableColumn<>("Medical History");

           historyCol.setCellValueFactory(new PropertyValueFactory<>("history"));



           patientTable.getColumns().addAll(

                   idCol, nameCol, ageCol, genderCol, phoneCol, historyCol

           );

           patientTable.setColumnResizePolicy(

                   TableView.CONSTRAINED_RESIZE_POLICY

           );



           FilteredList<Patient> filteredPatients =

                   new FilteredList<>(patients, p -> true);



           searchField.textProperty().addListener((obs, oldValue, newValue) -> {

               String search = newValue.toLowerCase().trim();

               filteredPatients.setPredicate(p ->

                       p.getId().toLowerCase().contains(search)

                       || p.getName().toLowerCase().contains(search)

                       || p.getPhone().contains(search)

               );

           });

           patientTable.setItems(filteredPatients);



           Button deleteButton = new Button("Delete Selected Patient");

           styleButton(deleteButton);

           deleteButton.setOnAction(e -> deletePatient());



           Label listHeading = new Label("Registered Patients");

           listHeading.setFont(Font.font("Arial", 20));



           VBox.setVgrow(patientTable, Priority.ALWAYS);

           page.getChildren().addAll(

                   heading,

                   new Label("Register New Patient"),

                   form,

                   listHeading,

                   searchField,

                   patientTable,

                   deleteButton

           );



           root.setCenter(page);

       }



       private void addPatient() {

           try {

               String name = nameField.getText().trim();

               String ageText = ageField.getText().trim();

               String phone = phoneField.getText().trim();

               String gender = genderBox.getValue();

               String history = historyField.getText().trim();



               if (name.isEmpty() || ageText.isEmpty()

                       || phone.isEmpty() || gender == null) {

                   showAlert("Please fill all required patient fields.");

                   return;

               }



               if (!name.matches("[a-zA-Z .'-]+")) {

                   showAlert("Please enter a valid patient name.");

                   return;

               }



               int age = Integer.parseInt(ageText);

               if (age < 1 || age > 120) {

                   showAlert("Age must be between 1 and 120.");

                   return;

               }



               if (!phone.matches("[0-9]{10}")) {

                   showAlert("Enter a valid 10-digit phone number.");

                   return;

               }



               String id = generatePatientId();

               Patient patient = new Patient(

                       id, name, age, gender, phone, history

               );



               patients.add(patient);

               savePatients();

               showAlert("Patient registered successfully!\nPatient ID: " + id);

               clearPatientForm();



           } catch (NumberFormatException ex) {

               showAlert("Age must be a valid number.");

           }

       }



       private String generatePatientId() {

           int max = 0;

           for (Patient p : patients) {

               try {

                   int number = Integer.parseInt(p.getId().substring(1));

                   max = Math.max(max, number);

               } catch (NumberFormatException ignored) {

                   // Ignore malformed IDs in an existing data file.

               }

           }

           return String.format("P%03d", max + 1);

       }



       private void deletePatient() {

           Patient selected = patientTable.getSelectionModel().getSelectedItem();

           if (selected == null) {

               showAlert("Please select a patient to delete.");

               return;

           }



           Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);

           confirm.setTitle("Confirm deletion");

           confirm.setHeaderText(null);

           confirm.setContentText(

                   "Delete patient " + selected.getName() + "?"

           );



           if (confirm.showAndWait().orElse(ButtonType.CANCEL)

                   == ButtonType.OK) {

               patients.remove(selected);

               savePatients();

           }

       }



       private void clearPatientForm() {

           nameField.clear();

           ageField.clear();

           phoneField.clear();

           genderBox.setValue(null);

           historyField.clear();

       }



       private void savePatients() {

           try {

               Files.createDirectories(Paths.get("data"));

               try (BufferedWriter writer =

                            Files.newBufferedWriter(Paths.get(PATIENT_FILE))) {

                   for (Patient p : patients) {

                       writer.write(String.join("\t",

                               clean(p.getId()),

                               clean(p.getName()),

                               String.valueOf(p.getAge()),

                               clean(p.getGender()),

                               clean(p.getPhone()),

                               clean(p.getHistory())

                       ));

                       writer.newLine();

                   }

               }

           } catch (IOException ex) {

               showAlert("Error saving patient data: " + ex.getMessage());

           }

       }



       private void loadPatients() {

           Path path = Paths.get(PATIENT_FILE);

           if (!Files.exists(path)) {

               return;

           }



           try (BufferedReader reader = Files.newBufferedReader(path)) {

               String line;

               while ((line = reader.readLine()) != null) {

                   String[] data = line.split("\t", -1);

                   if (data.length == 6) {

                       try {

                           patients.add(new Patient(

                                   data[0], data[1], Integer.parseInt(data[2]),

                                   data[3], data[4], data[5]

                           ));

                       } catch (NumberFormatException ignored) {

                           // Skip an invalid patient record.

                       }

                   }

               }

           } catch (IOException ex) {

               System.err.println("Could not load patients: " + ex.getMessage());

           }

       }



       // ---------------- DOCTOR MANAGEMENT ----------------



       private void showDoctors() {

           VBox page = new VBox(15);

           page.setPadding(new Insets(25));

           page.setStyle("-fx-background-color: #f1f5f9;");



           Label heading = new Label("Doctor Management");

           heading.setFont(Font.font("Arial", 27));



           GridPane form = new GridPane();

           form.setHgap(15);

           form.setVgap(15);

           form.setPadding(new Insets(20));

           form.setStyle(

                   "-fx-background-color: white;" +

                   "-fx-background-radius: 10;"

           );



           doctorNameField = new TextField();

           doctorNameField.setPromptText("Enter doctor name");



           specializationBox = new ComboBox<>();

           specializationBox.getItems().addAll(

                   "Cardiologist", "Neurologist", "Dermatologist",

                   "Orthopedic", "Pediatrician", "General Physician",

                   "Dentist", "Gynecologist"

           );

           specializationBox.setPromptText("Select specialization");

           specializationBox.setMaxWidth(Double.MAX_VALUE);



           doctorPhoneField = new TextField();

           doctorPhoneField.setPromptText("10-digit phone number");



           availabilityBox = new ComboBox<>();

           availabilityBox.getItems().addAll(

                   "9 AM - 1 PM", "2 PM - 6 PM", "9 AM - 5 PM"

           );

           availabilityBox.setPromptText("Select working hours");

           availabilityBox.setMaxWidth(Double.MAX_VALUE);



           form.add(new Label("Doctor Name:"), 0, 0);

           form.add(doctorNameField, 1, 0);

           form.add(new Label("Specialization:"), 2, 0);

           form.add(specializationBox, 3, 0);



           form.add(new Label("Phone:"), 0, 1);

           form.add(doctorPhoneField, 1, 1);

           form.add(new Label("Availability:"), 2, 1);

           form.add(availabilityBox, 3, 1);



           Button saveButton = new Button("Add Doctor");

           Button clearButton = new Button("Clear");

           styleButton(saveButton);

           styleButton(clearButton);



           saveButton.setOnAction(e -> addDoctor());

           clearButton.setOnAction(e -> clearDoctorForm());

           form.add(new HBox(12, saveButton, clearButton), 1, 2);



           Label listHeading = new Label("Registered Doctors");

           listHeading.setFont(Font.font("Arial", 20));



           doctorSearchField = new TextField();

           doctorSearchField.setPromptText(

                   "Search by ID, name or specialization..."

           );

           doctorSearchField.setMaxWidth(400);



           doctorTable = new TableView<>();



           TableColumn<Doctor, String> idCol =

                   new TableColumn<>("Doctor ID");

           idCol.setCellValueFactory(new PropertyValueFactory<>("id"));



           TableColumn<Doctor, String> nameCol =

                   new TableColumn<>("Doctor Name");

           nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));



           TableColumn<Doctor, String> specCol =

                   new TableColumn<>("Specialization");

           specCol.setCellValueFactory(

                   new PropertyValueFactory<>("specialization")

           );



           TableColumn<Doctor, String> phoneCol =

                   new TableColumn<>("Phone");

           phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));



           TableColumn<Doctor, String> timeCol =

                   new TableColumn<>("Availability");

           timeCol.setCellValueFactory(

                   new PropertyValueFactory<>("availability")

           );



           doctorTable.getColumns().addAll(

                   idCol, nameCol, specCol, phoneCol, timeCol

           );

           doctorTable.setColumnResizePolicy(

                   TableView.CONSTRAINED_RESIZE_POLICY

           );



           FilteredList<Doctor> filteredDoctors =

                   new FilteredList<>(doctors, d -> true);



           doctorSearchField.textProperty().addListener(

                   (obs, oldValue, newValue) -> {

                       String search = newValue.toLowerCase().trim();

                       filteredDoctors.setPredicate(d ->

                               d.getId().toLowerCase().contains(search)

                               || d.getName().toLowerCase().contains(search)

                               || d.getSpecialization()

                                       .toLowerCase().contains(search)

                       );

                   }

           );

           doctorTable.setItems(filteredDoctors);



           Button deleteButton = new Button("Delete Selected Doctor");

           styleButton(deleteButton);

           deleteButton.setOnAction(e -> deleteDoctor());



           VBox.setVgrow(doctorTable, Priority.ALWAYS);

           page.getChildren().addAll(

                   heading,

                   new Label("Register New Doctor"),

                   form,

                   listHeading,

                   doctorSearchField,

                   doctorTable,

                   deleteButton

           );



           root.setCenter(page);

       }



       private void addDoctor() {

           String name = doctorNameField.getText().trim();

           String phone = doctorPhoneField.getText().trim();

           String specialization = specializationBox.getValue();

           String availability = availabilityBox.getValue();



           if (name.isEmpty() || phone.isEmpty()

                   || specialization == null || availability == null) {

               showAlert("Please fill all doctor details.");

               return;

           }



           if (!name.matches("[a-zA-Z .'-]+")) {

               showAlert("Enter a valid doctor name.");

               return;

           }



           if (!phone.matches("[0-9]{10}")) {

               showAlert("Enter a valid 10-digit phone number.");

               return;

           }



           String id = generateDoctorId();

           Doctor doctor = new Doctor(

                   id, name, specialization, phone, availability

           );



           doctors.add(doctor);

           saveDoctors();

           showAlert("Doctor added successfully!\nDoctor ID: " + id);

           clearDoctorForm();

       }



       private String generateDoctorId() {

           int max = 0;

           for (Doctor d : doctors) {

               try {

                   int number = Integer.parseInt(d.getId().substring(1));

                   max = Math.max(max, number);

               } catch (NumberFormatException ignored) {

                   // Ignore malformed IDs in an existing data file.

               }

           }

           return String.format("D%03d", max + 1);

       }



       private void deleteDoctor() {

           Doctor selected = doctorTable.getSelectionModel().getSelectedItem();

           if (selected == null) {

               showAlert("Please select a doctor to delete.");

               return;

           }



           Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);

           confirm.setTitle("Confirm deletion");

           confirm.setHeaderText(null);

           confirm.setContentText(

                   "Delete doctor " + selected.getName() + "?"

           );



           if (confirm.showAndWait().orElse(ButtonType.CANCEL)

                   == ButtonType.OK) {

               doctors.remove(selected);

               saveDoctors();

           }

       }



       private void clearDoctorForm() {

           doctorNameField.clear();

           doctorPhoneField.clear();

           specializationBox.setValue(null);

           availabilityBox.setValue(null);

       }



       private void saveDoctors() {

           try {

               Files.createDirectories(Paths.get("data"));

               try (BufferedWriter writer =

                            Files.newBufferedWriter(Paths.get(DOCTOR_FILE))) {

                   for (Doctor d : doctors) {

                       writer.write(String.join("\t",

                               clean(d.getId()),

                               clean(d.getName()),

                               clean(d.getSpecialization()),

                               clean(d.getPhone()),

                               clean(d.getAvailability())

                       ));

                       writer.newLine();

                   }

               }

           } catch (IOException ex) {

               showAlert("Error saving doctor data: " + ex.getMessage());

           }

       }



       private void loadDoctors() {

           Path path = Paths.get(DOCTOR_FILE);

           if (!Files.exists(path)) {

               return;

           }



           try (BufferedReader reader = Files.newBufferedReader(path)) {

               String line;

               while ((line = reader.readLine()) != null) {

                   String[] data = line.split("\t", -1);

                   if (data.length == 5) {

                       doctors.add(new Doctor(

                               data[0], data[1], data[2], data[3], data[4]

                       ));

                   }

               }

           } catch (IOException ex) {

               System.err.println("Could not load doctors: " + ex.getMessage());

           }

       }



       // ---------------- APPOINTMENT MANAGEMENT ----------------


private void showAppointments() {
    VBox page = new VBox(14);
    page.setPadding(new Insets(25));
    page.setStyle("-fx-background-color: #f1f5f9;");

    Label heading = new Label("Smart Appointment Management");
    heading.setFont(Font.font("Arial", 27));

    GridPane form = new GridPane();
    form.setHgap(15);
    form.setVgap(12);
    form.setPadding(new Insets(20));
    form.setStyle("-fx-background-color: white; -fx-background-radius: 10;");

    appointmentPatientBox = new ComboBox<>();
    for (Patient p : patients) {
        appointmentPatientBox.getItems().add(
            p.getId() + " - " + p.getName()
        );
    }
    appointmentPatientBox.setPromptText("Select patient");
    appointmentPatientBox.setMaxWidth(Double.MAX_VALUE);

    appointmentDoctorBox = new ComboBox<>();
    for (Doctor d : doctors) {
        appointmentDoctorBox.getItems().add(
            d.getId() + " - " + d.getName()
            + " (" + d.getSpecialization() + ")"
        );
    }
    appointmentDoctorBox.setPromptText("Select doctor");
    appointmentDoctorBox.setMaxWidth(Double.MAX_VALUE);

    appointmentDatePicker = new DatePicker();
    appointmentDatePicker.setValue(java.time.LocalDate.now());
    appointmentDatePicker.setDayCellFactory(picker -> new DateCell() {
        @Override
        public void updateItem(java.time.LocalDate date, boolean empty) {
            super.updateItem(date, empty);
            setDisable(empty || date.isBefore(java.time.LocalDate.now()));
        }
    });

    appointmentTimeBox = new ComboBox<>();
    appointmentTimeBox.setPromptText("Select available time");
    appointmentTimeBox.setMaxWidth(Double.MAX_VALUE);

    // Refresh available slots whenever doctor or date changes
    appointmentDoctorBox.valueProperty().addListener(
        (obs, oldValue, newValue) -> updateAvailableTimeSlots()
    );

    appointmentDatePicker.valueProperty().addListener(
        (obs, oldValue, newValue) -> updateAvailableTimeSlots()
    );

    form.add(new Label("Patient:"), 0, 0);
    form.add(appointmentPatientBox, 1, 0);
    form.add(new Label("Doctor:"), 2, 0);
    form.add(appointmentDoctorBox, 3, 0);

    form.add(new Label("Date:"), 0, 1);
    form.add(appointmentDatePicker, 1, 1);
    form.add(new Label("Available Time:"), 2, 1);
    form.add(appointmentTimeBox, 3, 1);

    Button bookButton = new Button("Book Appointment");
    Button clearButton = new Button("Clear");

    styleButton(bookButton);
    styleButton(clearButton);

    bookButton.setOnAction(e -> bookAppointment());
    clearButton.setOnAction(e -> clearAppointmentForm());

    form.add(new HBox(12, bookButton, clearButton), 1, 2);

    Label suggestion = new Label(
        "Smart feature: Available slots update automatically "
        + "based on doctor and date."
    );
    suggestion.setStyle("-fx-text-fill: #047857; -fx-font-size: 13px;");

    Label listHeading = new Label("Appointment Schedule");
    listHeading.setFont(Font.font("Arial", 20));

    TextField appointmentSearch = new TextField();
    appointmentSearch.setPromptText(
        "Search by appointment ID, patient, doctor or date..."
    );
    appointmentSearch.setMaxWidth(500);

    appointmentTable = new TableView<>();

    TableColumn<Appointment, String> idCol =
        new TableColumn<>("Token / Appointment ID");
    idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

    TableColumn<Appointment, String> patientCol =
        new TableColumn<>("Patient");
    patientCol.setCellValueFactory(
        new PropertyValueFactory<>("patientName")
    );

    TableColumn<Appointment, String> doctorCol =
        new TableColumn<>("Doctor");
    doctorCol.setCellValueFactory(
        new PropertyValueFactory<>("doctorName")
    );

    TableColumn<Appointment, String> dateCol =
        new TableColumn<>("Date");
    dateCol.setCellValueFactory(
        new PropertyValueFactory<>("date")
    );

    TableColumn<Appointment, String> timeCol =
        new TableColumn<>("Time");
    timeCol.setCellValueFactory(
        new PropertyValueFactory<>("time")
    );

    TableColumn<Appointment, String> statusCol =
        new TableColumn<>("Status");
    statusCol.setCellValueFactory(
        new PropertyValueFactory<>("status")
    );

    appointmentTable.getColumns().addAll(
        idCol, patientCol, doctorCol, dateCol, timeCol, statusCol
    );

    appointmentTable.setColumnResizePolicy(
        TableView.CONSTRAINED_RESIZE_POLICY
    );

    FilteredList<Appointment> filtered =
        new FilteredList<>(appointments, a -> true);

    appointmentSearch.textProperty().addListener(
        (obs, oldValue, newValue) -> {
            String q = newValue.toLowerCase().trim();

            filtered.setPredicate(a ->
                a.getId().toLowerCase().contains(q)
                || a.getPatientName().toLowerCase().contains(q)
                || a.getDoctorName().toLowerCase().contains(q)
                || a.getDate().contains(q)
            );
        }
    );

    appointmentTable.setItems(filtered);

    Button cancelButton =
        new Button("Cancel Selected Appointment");
    styleButton(cancelButton);
    cancelButton.setOnAction(e -> cancelAppointment());

    VBox.setVgrow(appointmentTable, Priority.ALWAYS);

    page.getChildren().addAll(
        heading,
        new Label("Book New Appointment"),
        form,
        suggestion,
        listHeading,
        appointmentSearch,
        appointmentTable,
        cancelButton
    );

    root.setCenter(page);

    updateAvailableTimeSlots();
}

private void updateAvailableTimeSlots() {

    if (appointmentDoctorBox == null
            || appointmentDatePicker == null
            || appointmentTimeBox == null) {
        return;
    }

    String doctorChoice = appointmentDoctorBox.getValue();
    java.time.LocalDate selectedDate =
        appointmentDatePicker.getValue();

    appointmentTimeBox.getItems().clear();
    appointmentTimeBox.setValue(null);

    if (doctorChoice == null || selectedDate == null) {
        return;
    }

    if (selectedDate.isBefore(java.time.LocalDate.now())) {
        return;
    }

    String doctorId = doctorChoice.split(" - ")[0];

    Doctor selectedDoctor = null;

    for (Doctor d : doctors) {
        if (d.getId().equals(doctorId)) {
            selectedDoctor = d;
            break;
        }
    }

    if (selectedDoctor == null) {
        return;
    }

    String availability = selectedDoctor.getAvailability();

    java.util.List<String> workingSlots =
        new java.util.ArrayList<>();

    if (availability.equals("9 AM - 1 PM")) {
        workingSlots.addAll(java.util.Arrays.asList(
            "09:00 AM", "10:00 AM", "11:00 AM", "12:00 PM"
        ));
    }
    else if (availability.equals("2 PM - 6 PM")) {
        workingSlots.addAll(java.util.Arrays.asList(
            "02:00 PM", "03:00 PM", "04:00 PM", "05:00 PM"
        ));
    }
    else if (availability.equals("9 AM - 5 PM")) {
        workingSlots.addAll(java.util.Arrays.asList(
            "09:00 AM", "10:00 AM", "11:00 AM",
            "12:00 PM", "02:00 PM", "03:00 PM", "04:00 PM"
        ));
    }

    String date = selectedDate.toString();

    for (String slot : workingSlots) {

        boolean alreadyBooked = false;

        for (Appointment a : appointments) {
            if (a.getDoctorId().equals(doctorId)
                    && a.getDate().equals(date)
                    && a.getTime().equals(slot)
                    && a.getStatus().equals("Booked")) {

                alreadyBooked = true;
                break;
            }
        }

        if (!alreadyBooked) {
            appointmentTimeBox.getItems().add(slot);
        }
    }

    if (appointmentTimeBox.getItems().isEmpty()) {
        showAlert(
            "No available slots for this doctor on this date. "
            + "Please select another date."
        );
    }
}



private void bookAppointment() {

    String patientChoice = appointmentPatientBox.getValue();
    String doctorChoice = appointmentDoctorBox.getValue();

    java.time.LocalDate selectedDate =
        appointmentDatePicker.getValue();

    String time = appointmentTimeBox.getValue();

    if (patientChoice == null || doctorChoice == null
            || selectedDate == null || time == null) {
        showAlert("Please select patient, doctor, date and time.");
        return;
    }

    if (selectedDate.isBefore(java.time.LocalDate.now())) {
        showAlert("Past dates cannot be selected.");
        return;
    }

    String patientId = patientChoice.split(" - ")[0];
    String doctorId = doctorChoice.split(" - ")[0];
    String date = selectedDate.toString();

    // Final duplicate-booking safety check
    for (Appointment a : appointments) {

        if (a.getDoctorId().equals(doctorId)
                && a.getDate().equals(date)
                && a.getTime().equals(time)
                && a.getStatus().equals("Booked")) {

            updateAvailableTimeSlots();

            if (!appointmentTimeBox.getItems().isEmpty()) {
                String suggested =
                    appointmentTimeBox.getItems().get(0);

                appointmentTimeBox.setValue(suggested);

                showAlert(
                    "This slot is already booked!\n"
                    + "Suggested available slot: " + suggested
                );
            } else {
                showAlert(
                    "This slot is already booked. "
                    + "Please select another date."
                );
            }

            return;
        }
    }

    String patientName =
        patientChoice.substring(patientChoice.indexOf(" - ") + 3);

    String doctorName =
        doctorChoice.substring(doctorChoice.indexOf(" - ") + 3);

    int max = 0;

    for (Appointment a : appointments) {
        try {
            max = Math.max(
                max,
                Integer.parseInt(a.getId().substring(1))
            );
        } catch (NumberFormatException ignored) {
        }
    }

    String id = String.format("A%03d", max + 1);

    Appointment appointment = new Appointment(
        id,
        patientId,
        patientName,
        doctorId,
        doctorName,
        date,
        time,
        "Booked"
    );

    appointments.add(appointment);
    saveAppointments();

    showAlert(
        "Appointment booked successfully!\n\n"
        + "Patient: " + patientName + "\n"
        + "Doctor: " + doctorName + "\n"
        + "Date: " + date + "\n"
        + "Time: " + time + "\n"
        + "Patient Token Number: " + id
    );

    clearAppointmentForm();
    showAppointments();
}


    private void cancelAppointment() {
        Appointment selected = appointmentTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Please select an appointment to cancel.");
            return;
        }
        if (!selected.getStatus().equals("Booked")) {
            showAlert("This appointment is already cancelled.");
            return;
        }
        selected.setStatus("Cancelled");
        saveAppointments();
        appointmentTable.refresh();
        updateAvailableTimeSlots();
        showAlert("Appointment " + selected.getId() + " cancelled.");
    }

    private void clearAppointmentForm() {
        appointmentPatientBox.setValue(null);
        appointmentDoctorBox.setValue(null);
        appointmentDatePicker.setValue(java.time.LocalDate.now());
        appointmentTimeBox.setValue(null);
    }

    private void saveAppointments() {
        try {
            Files.createDirectories(Paths.get("data"));
            try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(APPOINTMENT_FILE))) {
                for (Appointment a : appointments) {
                    writer.write(String.join("\t", clean(a.getId()), clean(a.getPatientId()),
                            clean(a.getPatientName()), clean(a.getDoctorId()), clean(a.getDoctorName()),
                            clean(a.getDate()), clean(a.getTime()), clean(a.getStatus())));
                    writer.newLine();
                }
            }
        } catch (IOException ex) {
            showAlert("Error saving appointments: " + ex.getMessage());
        }
    }

    private void loadAppointments() {
        Path path = Paths.get(APPOINTMENT_FILE);
        if (!Files.exists(path)) return;
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] d = line.split("\t", -1);
                if (d.length == 8) appointments.add(new Appointment(d[0], d[1], d[2], d[3], d[4], d[5], d[6], d[7]));
            }
        } catch (IOException ex) {
            System.err.println("Could not load appointments: " + ex.getMessage());
        }
    }

    // ---------------- SEARCH ----------------



       private void showSearch() {

           VBox page = new VBox(18);

           page.setPadding(new Insets(30));

           page.setStyle("-fx-background-color: #f1f5f9;");



           Label heading = new Label("Search Records");

           heading.setFont(Font.font("Arial", 27));



           TextField query = new TextField();

           query.setPromptText("Enter patient or doctor ID, name or phone...");

           query.setMaxWidth(500);



           TextArea results = new TextArea();

           results.setEditable(false);

           results.setPromptText("Search results will appear here.");



           Button searchButton = new Button("Search");

           styleButton(searchButton);



           searchButton.setOnAction(e -> {

               String q = query.getText().trim().toLowerCase();

               if (q.isEmpty()) {

                   showAlert("Enter something to search.");

                   return;

               }



               StringBuilder output = new StringBuilder();



               for (Patient p : patients) {

                   if (p.getId().toLowerCase().contains(q)

                           || p.getName().toLowerCase().contains(q)

                           || p.getPhone().contains(q)) {

                       output.append("PATIENT\n")

                               .append("ID: ").append(p.getId()).append("\n")

                               .append("Name: ").append(p.getName()).append("\n")

                               .append("Age: ").append(p.getAge()).append("\n")

                               .append("Phone: ").append(p.getPhone()).append("\n\n");

                   }

               }



               for (Doctor d : doctors) {

                   if (d.getId().toLowerCase().contains(q)

                           || d.getName().toLowerCase().contains(q)

                           || d.getPhone().contains(q)

                           || d.getSpecialization().toLowerCase().contains(q)) {

                       output.append("DOCTOR\n")

                               .append("ID: ").append(d.getId()).append("\n")

                               .append("Name: ").append(d.getName()).append("\n")

                               .append("Specialization: ")

                               .append(d.getSpecialization()).append("\n")

                               .append("Phone: ").append(d.getPhone()).append("\n\n");

                   }

               }



               results.setText(output.length() == 0

                       ? "No matching records found."

                       : output.toString());

           });



           page.getChildren().addAll(

                   heading, query, searchButton, new Label("Results:"), results

           );

           VBox.setVgrow(results, Priority.ALWAYS);

           root.setCenter(page);

       }



       private void showPlaceholder(String title) {

           VBox page = new VBox(20);

           page.setPadding(new Insets(35));

           page.setStyle("-fx-background-color: #f1f5f9;");



           Label heading = new Label(title);

           heading.setFont(Font.font("Arial", 28));



           page.getChildren().addAll(

                   heading,

                   new Label("This module will be implemented next.")

           );

           root.setCenter(page);

       }



       private void styleButton(Button button) {

           button.setPadding(new Insets(10, 18, 10, 18));

           button.setStyle(

                   "-fx-background-color: #2563eb;" +

                   "-fx-text-fill: white;" +

                   "-fx-font-size: 14px;" +

                   "-fx-background-radius: 7;" +

                   "-fx-cursor: hand;"

           );

       }



       private void showAlert(String message) {

           Alert alert = new Alert(Alert.AlertType.INFORMATION);

           alert.setTitle("MediCare");

           alert.setHeaderText(null);

           alert.setContentText(message);

           alert.showAndWait();

       }



       private String clean(String value) {

           if (value == null) {

               return "";

           }

           return value.replace("\t", " ")

                   .replace("\r", " ")

                   .replace("\n", " ");

       }



       // ---------------- MODEL CLASSES ----------------



       public static class Appointment {
        private final String id, patientId, patientName, doctorId, doctorName, date, time;
        private String status;
        public Appointment(String id, String patientId, String patientName, String doctorId,
                String doctorName, String date, String time, String status) {
            this.id = id; this.patientId = patientId; this.patientName = patientName;
            this.doctorId = doctorId; this.doctorName = doctorName; this.date = date;
            this.time = time; this.status = status;
        }
        public String getId() { return id; }
        public String getPatientId() { return patientId; }
        public String getPatientName() { return patientName; }
        public String getDoctorId() { return doctorId; }
        public String getDoctorName() { return doctorName; }
        public String getDate() { return date; }
        public String getTime() { return time; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }

    public static class Patient {

           private final String id;

           private final String name;

           private final int age;

           private final String gender;

           private final String phone;

           private final String history;



           public Patient(String id, String name, int age,

                          String gender, String phone, String history) {

               this.id = id;

               this.name = name;

               this.age = age;

               this.gender = gender;

               this.phone = phone;

               this.history = history;

           }



           public String getId() { return id; }

           public String getName() { return name; }

           public int getAge() { return age; }

           public String getGender() { return gender; }

           public String getPhone() { return phone; }

           public String getHistory() { return history; }

       }



       public static class Doctor {

           private final String id;

           private final String name;

           private final String specialization;

           private final String phone;

           private final String availability;



           public Doctor(String id, String name, String specialization,

                         String phone, String availability) {

               this.id = id;

               this.name = name;

               this.specialization = specialization;

               this.phone = phone;

               this.availability = availability;

           }



           public String getId() { return id; }

           public String getName() { return name; }

           public String getSpecialization() { return specialization; }

           public String getPhone() { return phone; }

           public String getAvailability() { return availability; }

       }



       public static void main(String[] args) {

           launch(args);

       }

}
