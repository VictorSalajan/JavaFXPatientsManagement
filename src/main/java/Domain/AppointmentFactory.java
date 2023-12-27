package Domain;

import Repository.Repo;
import Utils.Utils;

import java.time.LocalDate;
import java.time.LocalTime;

public class AppointmentFactory implements IEntityFactory<Appointment> {
    Repo<Patient> patientRepo;

    public AppointmentFactory(Repo<Patient> patientRepo) {
        this.patientRepo = patientRepo;
    }

    @Override
    public Appointment createEntity(String line) {
        int id = Integer.parseInt(line.split(",")[0]);
        int patientID = Integer.parseInt(line.split(",")[1]);
        Patient patient = this.patientRepo.getById(patientID);
        String dateTime = line.split(",")[2];
        LocalDate date = Utils.strToLocalDate(dateTime);
        LocalTime time = Utils.strToLocalTime(dateTime);
        String purpose = line.split(",")[3];

        return new Appointment(id, patient, date, time, purpose);
    }
}
