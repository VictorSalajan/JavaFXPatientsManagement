package Repository;

import Domain.*;

import java.io.IOException;

public class RepoAppointmentFactory {
    public static Repo<Appointment> createRepo(String repoType, String filePath, Repo<Patient> patientRepo) throws IOException, RepoException, ClassNotFoundException {
        if ("file".equals(repoType)) {
            IEntityFactory<Appointment> appointmentFactory = new AppointmentFactory(patientRepo);
            return new FileRepo<Appointment>(filePath, appointmentFactory);
        } else if ("memory".equals(repoType)) {
            return new Repo<Appointment>();
        } else if ("binary".equals(repoType)) {
            return new BinaryFileRepo<Appointment>(filePath);
        } else if ("sql".equals(repoType)) {
            return new AppointmentDbRepo((PatientDbRepo) patientRepo);
        } else {
            throw new IllegalArgumentException("Unknown repo: " + repoType);
        }
    }
}
