package console;

import Domain.*;
import Repository.*;
import Service.AppointmentService;
import Service.PatientService;
import UI.Console;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Main {

    public static void main(String[] args) {
        try {
            ArrayList<Object> services = initializeApp();
            PatientService patientService = (PatientService) services.get(0);
            AppointmentService appointmentService = (AppointmentService) services.get(1);

            Console console = new Console(patientService, appointmentService);
            console.menu();

        } catch (RepoException | IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }

//        repo.closeConnection();
    }

    public static ArrayList<Object> initializeApp() throws IOException, RepoException, ClassNotFoundException {
        Properties properties = loadConfig();

        Path basePath = Paths.get(System.getProperty("user.dir"), "src", "main", "resources");

        String repositoryType = properties.getProperty("repository.type");
        String patientRepoPath = basePath + properties.getProperty("patientsFilePath");
        String appointmentRepoPath = basePath + properties.getProperty("appointmentsFilePath");

        Repo<Patient> patientRepo = RepoPatientFactory.createRepo(repositoryType, patientRepoPath);
        Repo<Appointment> appointmentRepo = RepoAppointmentFactory.createRepo(repositoryType, appointmentRepoPath, patientRepo);

        PatientService patientService = new PatientService(patientRepo, appointmentRepo);
        AppointmentService appointmentService = new AppointmentService(appointmentRepo, patientRepo);

        return new ArrayList<>(List.of(patientService, appointmentService));
    }

    public static Properties loadConfig() {
        Properties properties = new Properties();

        try {
            InputStream input = Main.class.getResourceAsStream("/config.properties");
            properties.load(input);
        } catch (IOException | NullPointerException e) {
            System.out.println("Error loading config file. Using default: memory repo");
            properties.put("repository.type", "memory");

            return properties;
        }

        return properties;
    }
}
