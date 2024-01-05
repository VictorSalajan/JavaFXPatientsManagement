package org.example.javafxa4;

import Domain.Appointment;
import Domain.Patient;
import Repository.RepoException;
import Service.AppointmentService;
import Service.PatientService;
import Service.ValidationError;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public class Controller {
    PatientService patientService;
    AppointmentService appointmentService;
    ObservableList<Patient> patients;
    ObservableList<Appointment> appointments;
    @FXML
    private ListView<Patient> patientsListView;
    @FXML
    private ListView<Appointment> appointmentsListView;
    @FXML
    private Button addPatientButton;
    @FXML
    private Button updatePatientButton;
    @FXML
    private Button deletePatientButton;
    @FXML
    private Button addAppointmentButton;
    @FXML
    private Button updateAppointmentButton;
    @FXML
    private Button deleteAppointmentButton;
    @FXML
    private TextField patientIdTextField;
    @FXML
    private TextField patientLastNameTextField;
    @FXML
    private TextField patientFirstNameTextField;
    @FXML
    private TextField patientAgeTextField;
    @FXML
    private TextField appointmentIdTextField;
    @FXML
    private TextField appointmentPatientIdTextField;
    @FXML
    private TextField appointmentDateTextField;
    @FXML
    private TextField appointmentTimeTextField;
    @FXML
    private TextField appointmentPurposeTextField;

    public Controller(PatientService patientService, AppointmentService appointmentService) {
        this.patientService = patientService;
        this.appointmentService = appointmentService;
    }

    public void initialize() {
        patients = FXCollections.observableArrayList(patientService.getAll());
        appointments = FXCollections.observableArrayList(appointmentService.getAll());
        patientsListView.setItems(patients);
        appointmentsListView.setItems(appointments);
    }

    @FXML
    void onPatientsListViewClicked(MouseEvent event) {
        Patient p = patientsListView.getSelectionModel().getSelectedItem();
        if (p != null) {
            patientIdTextField.setText(String.valueOf(p.getID()));
            patientLastNameTextField.setText(p.getLastName());
            patientFirstNameTextField.setText(p.getFirstName());
            patientAgeTextField.setText(String.valueOf(p.getAge()));
        }
    }

    @FXML
    void onAppointmentsListViewClicked(MouseEvent event) {
        Appointment app = appointmentsListView.getSelectionModel().getSelectedItem();
        if (app != null) {
            appointmentIdTextField.setText(String.valueOf(app.getID()));
            appointmentPatientIdTextField.setText(String.valueOf(app.getPacient().getID()));
            appointmentDateTextField.setText(String.valueOf(app.getDate()));
            appointmentTimeTextField.setText(String.valueOf(app.getTime()));
            appointmentPurposeTextField.setText(app.getPurpose());
        }
    }

    @FXML
    void onAddPatientClicked(MouseEvent event) {
        try {
            Patient p = getPatientFromTextFields(
                    patientIdTextField, patientLastNameTextField, patientFirstNameTextField, patientAgeTextField
            );
            patientService.add(p.getID(), p.getLastName(), p.getFirstName(), p.getAge());
            patients.setAll(patientService.getAll());
        } catch (Exception e) {
            showAlert(e.getMessage());
        }
    }

    @FXML
    void onUpdatePatientClicked(MouseEvent event) {
        try {
            Patient newPatient = getPatientFromTextFields(
                    patientIdTextField, patientLastNameTextField, patientFirstNameTextField, patientAgeTextField
            );
            patientService.update(newPatient);
            patients.setAll(patientService.getAll());
            appointments.setAll(appointmentService.getAll());
        } catch (Exception e) {
            showAlert(e.getMessage());
        }
    }

    @FXML
    void onDeletePatientClicked(MouseEvent event) {
        try {
            int id = Integer.parseInt(patientIdTextField.getText());
            patientService.deleteById(id);
            patients.setAll(patientService.getAll());
            appointments.setAll(appointmentService.getAll());
        } catch (Exception e) {
            showAlert(e.getMessage());
        }
    }

    @FXML
    void onAddAppointmentClicked(MouseEvent event) {
        try {
            Appointment app = getAppointmentFromTextFields(
                    appointmentIdTextField, appointmentPatientIdTextField, appointmentDateTextField, appointmentTimeTextField,
                    appointmentPurposeTextField
            );
            appointmentService.add(app.getID(), app.getPacient().getID(), app.getDate(), app.getTime(), app.getPurpose());
            appointments.setAll(appointmentService.getAll());
        } catch (RepoException | ValidationError | IOException e) {
            showAlert(e.getMessage());
        }
    }

    @FXML
    void onUpdateAppointmentClicked(MouseEvent event) {
        try {
            Appointment app = getAppointmentFromTextFields(
                    appointmentIdTextField, appointmentPatientIdTextField, appointmentDateTextField, appointmentTimeTextField,
                    appointmentPurposeTextField
            );
            appointmentService.update(app);
            appointments.setAll(appointmentService.getAll());
        } catch (RepoException | ValidationError e) {
            showAlert(e.getMessage());
        }
    }

    @FXML
    void onDeleteAppointmentClicked(MouseEvent event) {
        try {
            int id = Integer.parseInt(appointmentIdTextField.getText());
            appointmentService.deleteById(id);
            appointments.setAll(appointmentService.getAll());
        } catch (RepoException e) {
            showAlert(e.getMessage());
        }
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

    private Patient getPatientFromTextFields(
            TextField idField, TextField lastNameField, TextField firstNameField, TextField ageField) {
        int id = Integer.parseInt(idField.getText());
        String lastName = lastNameField.getText();
        String firstName = firstNameField.getText();
        int age = Integer.parseInt(ageField.getText());
        return new Patient(id, lastName, firstName, age);
    }

    private Appointment getAppointmentFromTextFields(
            TextField idField, TextField patientIdField, TextField dateField, TextField timeField, TextField purposeField
    ) {
        int id = Integer.parseInt(idField.getText());
        int patientId = Integer.parseInt(patientIdField.getText());
        Patient patient = patientService.getById(patientId);
        LocalDate date = LocalDate.parse(dateField.getText());
        LocalTime time = LocalTime.parse(timeField.getText());
        String purpose = purposeField.getText();
        return new Appointment(id, patient, date, time, purpose);
    }
}