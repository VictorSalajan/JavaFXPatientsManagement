package org.example.javafxa4;

import Domain.Appointment;
import Domain.Patient;
import Repository.*;
import Service.AppointmentService;
import Service.PatientService;
import Service.ValidationError;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static console.Main.initializeApp;


public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        ArrayList<Object> services = null;
        try {
            services = initializeApp();
        } catch (RepoException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        PatientService patientService = (PatientService) services.get(0);
        AppointmentService appointmentService = (AppointmentService) services.get(1);

        VBox mainVerticalBox = new VBox();
        mainVerticalBox.setPadding(new Insets(10));

        // create observable Patient array, set to list view, add to VB
        ListView<Patient> patientsListView = new ListView<>();
        ObservableList<Patient> patients = FXCollections.observableArrayList(patientService.getAll());
        patientsListView.setItems(patients);

        mainVerticalBox.getChildren().add(patientsListView);    // add patients list view to vertical box

        ListView<Appointment> appointmentListView = new ListView<>();
        ObservableList<Appointment> appointments = FXCollections.observableArrayList(appointmentService.getAll());
        appointmentListView.setItems(appointments);

        mainVerticalBox.getChildren().add(appointmentListView);

        // Patient form
        GridPane patientsGridPane = new GridPane();

        // form items
        Label idLabel = new Label("ID");
        TextField patientIdTextField = new TextField();

        Label lastNameLabel = new Label("lastName");
        TextField patientLastNameTextField = new TextField();

        Label firstNameLabel = new Label("firstName");
        TextField patientFirstNameTextField = new TextField();

        Label ageLabel = new Label("age");
        TextField patientAgeTextField = new TextField();

        ArrayList<Label> patientLabels = new ArrayList<>(List.of(idLabel, lastNameLabel, firstNameLabel, ageLabel));
        int row = 0;
        for (Label patientLabel : patientLabels) {
            patientsGridPane.add(patientLabel, 0, row++);                   // col 0, row i = 0..4
            patientLabel.setPadding(new Insets(0, 10, 0, 0));    // up, right, down, left (clockwise)
        }

        ArrayList<TextField> patientTextFields = new ArrayList<>(List.of(
           patientIdTextField, patientLastNameTextField, patientFirstNameTextField, patientAgeTextField
        ));
        row = 0;
        for (TextField patientTextField : patientTextFields) {
            patientsGridPane.add(patientTextField, 1, row++);
        }

        mainVerticalBox.getChildren().add(patientsGridPane);

        // Create Action Buttons
        Button addPatientButton = new Button("Add patient");
        Button updatePatientButton = new Button("Update patient");
        Button deletePatientButton = new Button("Delete patient");

        // Add buttons to HBox
        HBox patientsActionsHB = setEntityActionHBox(addPatientButton, updatePatientButton, deletePatientButton);

        // Add Horizontal Box to VB
        mainVerticalBox.getChildren().add(patientsActionsHB);

        // Add an action (EventHandler) to each button
        addPatientButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Patient p = getPatientFromTextFields(
                  patientIdTextField, patientLastNameTextField, patientFirstNameTextField, patientAgeTextField
                );
                try {
                    patientService.add(p.getID(), p.getLastName(), p.getFirstName(), p.getAge());
                    patients.setAll(patientService.getAll());                 // update modification for Patients List View
                } catch (RepoException | IOException e) {
                    showAlert(e.getMessage());
                }
            }
        });

        updatePatientButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    Patient p = getPatientFromTextFields(
                      patientIdTextField, patientLastNameTextField, patientFirstNameTextField, patientAgeTextField
                    );
                    patientService.update(p);
                    patients.setAll(patientService.getAll());
                } catch (NumberFormatException | RepoException e) {
                    showAlert(e.getMessage());
                }
            }
        });

        deletePatientButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    int ID = Integer.parseInt(patientIdTextField.getText());
                    patientService.deleteById(ID);
                    patients.setAll(patientService.getAll());
                    appointments.setAll(appointmentService.getAll());
                } catch (RepoException e) {
                    showAlert(e.getMessage());
                }
            }
        });

        // Get Patient details in Grid Pane when clicking on one in List View
        patientsListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Patient patient = patientsListView.getSelectionModel().getSelectedItem();
                if (patient != null) {
                    patientIdTextField.setText(Integer.toString(patient.getID()));
                    patientLastNameTextField.setText(patient.getLastName());
                    patientFirstNameTextField.setText(patient.getFirstName());
                    patientAgeTextField.setText(Integer.toString(patient.getAge()));
                }
            }
        });

        // Appointment form
        GridPane appointmentsGridPane = new GridPane();

        // form items (patient attribute labels and text fields)
        Label idLabelApp = new Label("ID");
        TextField appointmentIdTextField = new TextField();

        Label patientIdLabel = new Label("PacientID");
        TextField appointmentPatientIdTextField = new TextField();

        Label dateLabel = new Label("date");
        TextField appointmentDateTextField = new TextField();

        Label timeLabel = new Label("time");
        TextField appointmentTimeTextField = new TextField();

        Label purposeLabel = new Label("purpose");
        TextField appointmentPurposeTextField = new TextField();

        ArrayList<Label> appointmentLabels = new ArrayList<>(List.of(
                idLabelApp, patientIdLabel, dateLabel, timeLabel, purposeLabel));
        row = 0;
        for (Label label : appointmentLabels) {
            appointmentsGridPane.add(label, 0, row++);
            label.setPadding(new Insets(0, 10, 0, 0));
        }

        ArrayList<TextField> appointmentTextFields = new ArrayList<>(List.of(
                appointmentIdTextField, appointmentPatientIdTextField, appointmentDateTextField, appointmentTimeTextField,
                appointmentPurposeTextField
        ));
        row = 0;
        for (TextField appointmentTextField : appointmentTextFields) {
            appointmentsGridPane.add(appointmentTextField, 1, row++);
        }

        mainVerticalBox.getChildren().add(appointmentsGridPane);

        // Create Action Buttons
        Button addAppointmentButton = new Button("Add Appointment");
        Button updateAppointmentButton = new Button("Update Appointment");
        Button deleteAppointmentButton = new Button("Delete Appointment");

        // Add buttons to HBox
        HBox appointmentsActionHB = setEntityActionHBox(addAppointmentButton, updateAppointmentButton, deleteAppointmentButton);

        // Add Horizontal Box to VB
        mainVerticalBox.getChildren().add(appointmentsActionHB);

        // Add an action (EventHandler) to each button
        addAppointmentButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Appointment app = getAppointmentFromTextFields(
                        patientService, appointmentIdTextField, appointmentPatientIdTextField, appointmentDateTextField,
                        appointmentTimeTextField, appointmentPurposeTextField
                );

                try {
                    appointmentService.add(
                            app.getID(), app.getPacient().getID(), app.getDate(), app.getTime(), app.getPurpose());
                    appointments.setAll(appointmentService.getAll());
                } catch (RepoException | IOException | ValidationError e) {
                    showAlert(e.getMessage());
                }
            }
        });

        updateAppointmentButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    Appointment app = getAppointmentFromTextFields(
                      patientService, appointmentIdTextField, appointmentPatientIdTextField, appointmentDateTextField,
                      appointmentTimeTextField, appointmentPurposeTextField
                    );

                    appointmentService.update(app);
                    appointments.setAll(appointmentService.getAll());
                } catch (NumberFormatException | RepoException | ValidationError e) {
                    showAlert(e.getMessage());
                }
            }
        });

        deleteAppointmentButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    int ID = Integer.parseInt(appointmentIdTextField.getText());
                    appointmentService.deleteById(ID);
                    appointments.setAll(appointmentService.getAll());
                } catch (RepoException e) {
                    showAlert(e.getMessage());
                }
            }
        });

        // Get Patient details in Grid Pane when clicking on one in List View
        appointmentListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Appointment appointment = appointmentListView.getSelectionModel().getSelectedItem();
                appointmentIdTextField.setText(Integer.toString(appointment.getID()));
                appointmentPatientIdTextField.setText(Integer.toString(appointment.getPacient().getID()));
                appointmentDateTextField.setText(String.valueOf(appointment.getDate()));
                appointmentTimeTextField.setText(String.valueOf(appointment.getTime()));
                appointmentPurposeTextField.setText(appointment.getPurpose());
            }
        });

        Scene scene = new Scene(mainVerticalBox, 1000, 800);
        stage.setTitle("Patients and Appointments");
        stage.setScene(scene);
        stage.show();

//        if (!stage.isShowing()) {
//            try {
//                patientRepo.closeConnection();
//                appointmentRepo.closeConnection();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
    }

    /**
     * Create Alert tree to handle user input exceptions
     * @param e - exception String
     */
    private static void showAlert(String e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(e);
        alert.show();
    }

    private static HBox setEntityActionHBox(Button add, Button update, Button delete) {
        // Create HBox for Action Buttons
        HBox entityActionsHB = new HBox();
        entityActionsHB.setPadding(new Insets(10));
        entityActionsHB.setAlignment(Pos.BASELINE_CENTER);

        // Add buttons to Horizontal Box
        entityActionsHB.getChildren().add(add);
        entityActionsHB.getChildren().add(update);
        entityActionsHB.getChildren().add(delete);

        return entityActionsHB;
    }

    private static Patient getPatientFromTextFields(
            TextField idField, TextField lastNameField, TextField firstNameField, TextField ageField) {
        int ID = Integer.parseInt(idField.getText());
        String lastName = lastNameField.getText();
        String firstName = firstNameField.getText();
        int age = Integer.parseInt(ageField.getText());

        return new Patient(ID, lastName, firstName, age);
    }

    private static Appointment getAppointmentFromTextFields(
            PatientService patientService,
            TextField idField, TextField patientIdField, TextField dateField, TextField timeField, TextField purposeField
    ) {
        int ID = Integer.parseInt(idField.getText());
        int patientID = Integer.parseInt(patientIdField.getText());
        LocalDate date = LocalDate.parse(dateField.getText());
        LocalTime time = LocalTime.parse((timeField.getText()));
        String purpose = purposeField.getText();

        return new Appointment(ID, patientService.getById(patientID), date, time, purpose);
    }

    public static void main(String[] args) {
        launch();
    }
}