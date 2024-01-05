package org.example.javafxa4;

import Repository.*;
import Service.AppointmentService;
import Service.PatientService;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

import static console.Main.initializeApp;


public class Application extends javafx.application.Application {
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

        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("hello-view.fxml"));
        Controller hc = new Controller(patientService, appointmentService);
        fxmlLoader.setController(hc);

        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        stage.setTitle("Patients and Appointments");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}