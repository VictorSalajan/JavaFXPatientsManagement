package Service;

import Domain.Appointment;
import Domain.Patient;
import Repository.DuplicateIdException;
import Repository.IdNotFoundException;
import Repository.Repo;
import Repository.RepoException;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Stream;


public class AppointmentService {
    public Repo<Appointment> appointmentRepo;
    public Repo<Patient> patientRepo;

    /**
     * Constructs a new AppointmentService object
     * @param appointmentRepo: Repo<Appointment>
     * @param patientRepo: Repo<Patient>
     */
    public AppointmentService(Repo<Appointment> appointmentRepo, Repo<Patient> patientRepo) {
        this.appointmentRepo = appointmentRepo;
        this.patientRepo = patientRepo;
    }

    /**
     * Add an Appointment
     * @param ID: int
     * @param patientID: int
     * @param data: Date
     * @param time: Time
     * @param purpose: String
     * @throws DuplicateIdException : RepoException
     */
    public void add(int ID, int patientID, LocalDate data, LocalTime time, String purpose) throws RepoException, ValidationError, IOException {
        if (patientRepo.getAll().isEmpty())
            throw new NullPointerException("To add an appointment, patient repo cannot be null!");
        Patient patient = patientRepo.getById(patientID);

        Appointment app = new Appointment(ID, patient, data, time, purpose);

        AppointmentValidator.validateAppointment(app, appointmentRepo);
        appointmentRepo.add(app);
    }

    /**
     * Returns an Appointment by ID
     * @param ID: int
     * @return Appointment
     */
    public Appointment getById(int ID)  throws RepoException{
        return appointmentRepo.getById(ID);
    }

    /**
     * Returns a list of all Appointments
     * @return entities: ArrayList<T>
     */
    public ArrayList<Appointment> getAll() {
        return appointmentRepo.getAll();
    }

    public Iterator<Appointment> getIterator() {
        return appointmentRepo.iterator();
    }

    /**
     * Update an Appointment by ID
     * @param newApp: Appointment
     * @throws IdNotFoundException
     */
    public void update(Appointment newApp) throws RepoException, ValidationError {
        if (newApp.getPacient() == null) {
            throw new IdNotFoundException("Could not update appointment with a non-existing patient!");
        }
        AppointmentValidator.validateAppointment(newApp, appointmentRepo);
        appointmentRepo.update(newApp);
    }

    /**
     * Delete an Appointment by ID
     * @param ID: int
     * @throws IdNotFoundException: RepoException
     */
    public void deleteById(int ID) throws RepoException {
        appointmentRepo.deleteById(ID);
    }

    public void getAppointmentsByPatient() {
        ArrayList<Appointment> appList = getAll();
        Map<Patient, Integer> patientsMap = new HashMap<>();
        for (Appointment appointment : appList) {
            Patient patient = appointment.getPacient();
            patientsMap.put(patient, patientsMap.getOrDefault(patient, 0) + 1);
        }

        List<Map.Entry<Patient, Integer>> sortedList = new ArrayList<>(patientsMap.entrySet());
        sortedList.sort(Map.Entry.<Patient, Integer>comparingByValue().reversed());
        for (Map.Entry<Patient, Integer> entry : sortedList) {
            Patient patient = entry.getKey();
            int appointmentCount = entry.getValue();
            System.out.println(patient.toString() + ": " + appointmentCount + " appointment(s)");
        }
    }

    public void getAppointmentsByMonth() {
        ArrayList<Appointment> appList = getAll();
        Map<String, Integer> appsByMonthMap = new HashMap<>();
        for (Appointment appointment : appList) {
            String month = String.valueOf(appointment.getDate().getMonth());
            appsByMonthMap.put(month, appsByMonthMap.getOrDefault(month, 0) + 1);
        }

        List<Map.Entry<String, Integer>> sortedList = new ArrayList<>(appsByMonthMap.entrySet());
        sortedList.sort(Map.Entry.<String, Integer>comparingByValue().reversed());
        for (Map.Entry<String, Integer> entry : sortedList) {
            String month = entry.getKey();
            int appointmentCount = entry.getValue();
            System.out.println(month + ": " + appointmentCount + " appointment(s)");
        }
    }

    public void daysSinceLastAppointment() {
        ArrayList<Appointment> appList = getAll();
        Map<Patient, Integer> daysPassedByPatient = new HashMap<>();
        for (Appointment appointment : appList) {
            Patient patient = appointment.getPacient();
            Integer crtLatestDay = daysPassedByPatient.getOrDefault(patient, 0);
            Integer newDay = appointment.getDate().getDayOfYear();
            if (newDay > crtLatestDay)
                daysPassedByPatient.put(patient, newDay);
        }

        List<Map.Entry<Patient, Integer>> sortedList = new ArrayList<>(daysPassedByPatient.entrySet());
        sortedList.sort(Map.Entry.<Patient, Integer>comparingByValue().reversed());
        for (Map.Entry<Patient, Integer> entry : sortedList) {
            Patient p = entry.getKey();
            int nrDays = entry.getValue();
            System.out.println(p.toString() + ": " + nrDays + " passed since last appointment");
        }
    }
}
