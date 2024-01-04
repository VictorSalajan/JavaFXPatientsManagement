package UI;

import Domain.Appointment;
import Domain.Patient;
import Repository.DuplicateIdException;
import Repository.IdNotFoundException;
import Repository.RepoException;
import Service.AppointmentService;
import Service.PatientService;
import Service.ValidationError;
import Utils.Utils;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Console {
    PatientService patientService;
    AppointmentService appointmentService;
    Scanner in = new Scanner(System.in);

    public Console(PatientService patientService, AppointmentService appointmentService) {
        this.patientService = patientService;
        this.appointmentService = appointmentService;
    }

    public Patient getPatientInput() {
        System.out.print("Enter patient ID: ");
        int ID = Integer.parseInt(in.nextLine());
        System.out.print(patientService.getById(ID) != null ? patientService.getById(ID) + "\n" : "");   // see crt details before updating/adding patients, if obj not null
        System.out.print("Enter patient last name: ");
        String lastName = in.nextLine();
        System.out.print("Enter patient first name: ");
        String firstName = in.nextLine();
        System.out.print("Enter patient age: ");
        int age = Integer.parseInt(in.nextLine());
        return new Patient(ID, lastName, firstName, age);
    }

    public void addPatient() throws RepoException {
        try  {
            Patient p = this.getPatientInput();
            patientService.add(p.getID(), p.getLastName(), p.getFirstName(), p.getAge());
        } catch (DuplicateIdException | NumberFormatException | IOException ex) {
            System.out.println(ex);
        }
    }

    public void displayAllPatients() {
        Iterator<Patient> iter = patientService.getIterator();
        while (iter.hasNext()) {
            System.out.println(iter.next());
        }
    }

    public void updatePatient() throws RepoException {
        try {
            Patient p = this.getPatientInput();
            patientService.update(p);
        } catch (IdNotFoundException | NumberFormatException ex) {
            System.out.println(ex);
        }
    }

    public void deletePatientById() throws RepoException {
        try {
            System.out.print("Enter patient ID: ");
            int ID = this.in.nextInt();
            patientService.deleteById(ID);
        } catch (IdNotFoundException | NumberFormatException ex) {
            System.out.println(ex);
        }
    }

    public Appointment getAppointmentInput() throws RepoException {
        System.out.print("Enter Appointment ID: ");
        int ID = Integer.parseInt(in.nextLine());
        System.out.print(appointmentService.getById(ID) != null ? appointmentService.getById(ID) + "\n" : "");   // see crt details before add/update on Appointment obj, if obj not null

        System.out.print("Enter Patient ID: ");
        int pacientID = Integer.parseInt(in.nextLine());
        if (patientService.getById(pacientID) == null)
            return null;

        System.out.print("Enter Appointment Date and Time: (dd/MM/yyyy HH:mm)");
        String dateTimeString = in.nextLine();
        LocalDate date = Utils.strToLocalDate(dateTimeString);
        LocalTime time = Utils.strToLocalTime(dateTimeString);

        System.out.print("Enter purpose of appointment: ");
        String purpose = in.nextLine();

        return new Appointment(ID, patientService.getById(pacientID), date, time, purpose);
    }

    public void addAppointment() throws RepoException {
        try {
            Appointment app = this.getAppointmentInput();
            appointmentService.add(app.getID(), app.getPacient().getID(), app.getDate(), app.getTime(), app.getPurpose());
        } catch (NullPointerException | DuplicateIdException | DateTimeParseException | NumberFormatException |
                 ValidationError | IOException ex) {
            System.out.println(ex);
        }
    }

    public void displayAllAppointments() {
        Iterator<Appointment> it = appointmentService.getIterator();
        while (it.hasNext())
            System.out.println(it.next());
    }

    public void updateAppointment() throws RepoException {
        try {
            Appointment app = this.getAppointmentInput();
            appointmentService.update(app);
        } catch (IdNotFoundException | NullPointerException | NumberFormatException | ValidationError | DateTimeParseException ex) {
            System.out.println(ex);
        }
    }

    public void deleteAppointmentById() throws RepoException {
        System.out.print("Enter Appointment ID to delete: ");
        try {
            int ID = Integer.parseInt(in.nextLine());
            appointmentService.deleteById(ID);
        } catch (IdNotFoundException | NumberFormatException ex) {
            System.out.println(ex);
        }
    }

    public void getAppointmentsByPatient() {
        List<Map.Entry<Patient, Integer>> sortedList = appointmentService.getAppointmentsByPatient();
        for (Map.Entry<Patient, Integer> entry : sortedList) {
            Patient patient = entry.getKey();
            int appointmentCount = entry.getValue();
            System.out.println(patient.toString() + ": " + appointmentCount + " appointment(s)");
        }
    }

    public void getAppointmentsByMonth() {
        List<Map.Entry<String, Integer>> sortedList = appointmentService.getAppointmentsByMonth();
        for (Map.Entry<String, Integer> entry : sortedList) {
            String month = entry.getKey();
            int appointmentCount = entry.getValue();
            System.out.println(month + ": " + appointmentCount + " appointment(s)");
        }
    }

    public void daysSinceLastAppointment() {
        List<Map.Entry<Patient, Integer>> sortedList = appointmentService.daysSinceLastAppointment();
        for (Map.Entry<Patient, Integer> entry : sortedList) {
            Patient p = entry.getKey();
            int nrDays = entry.getValue();
            System.out.println(p.toString() + ": " + nrDays + " passed since last appointment");
        }
    }

    public void printMenu() {
        System.out.println("1. Add patient");
        System.out.println("2. Display all patients");
        System.out.println("3. Update patient by ID");
        System.out.println("4. Delete patient by ID");
        System.out.println("5. Add appointment");
        System.out.println("6. Display all appointments");
        System.out.println("7. Update appointment by id");
        System.out.println("8. Delete appointment by ID");

        System.out.println("9. Get apppointments by patient");
        System.out.println("10. Get apppointments by month");
        System.out.println("11. Days passed since last appointment by patient");

        System.out.println("-1. Exit");

        System.out.print("Choose an option: ");
    }

    public void menu() throws RepoException {
        outer: while (true) {
            this.printMenu();
            int option = 0;
            try {
                option = Integer.parseInt(in.nextLine());
            } catch (NumberFormatException ex) {
                System.out.println(ex);
            }
            switch (option) {
                case 1:
                    this.addPatient(); break;
                case 2:
                    this.displayAllPatients(); break;
                case 3:
                    this.updatePatient(); break;
                case 4:
                    this.deletePatientById(); break;
                case 5:
                    this.addAppointment(); break;
                case 6:
                    this.displayAllAppointments(); break;
                case 7:
                    this.updateAppointment(); break;
                case 8:
                    this.deleteAppointmentById(); break;
                case 9:
                    this.getAppointmentsByPatient(); break;
                case 10:
                    this.getAppointmentsByMonth(); break;
                case 11:
                    this.daysSinceLastAppointment(); break;
                case -1:
                    break outer;
                    default:
                        System.out.println("Invalid option! Try again!");
        }
        }
    }
}
