package Test;

import Domain.Appointment;
import Domain.Patient;
import Repository.Repo;
import Repository.RepoException;
import Service.AppointmentService;
import Service.PatientService;
import Service.ValidationError;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Iterator;

public class ServiceTest {
    @Test
    public void testPatientService() throws RepoException, IOException {
        Repo<Patient> repoPatient = new Repo<>();
        Repo<Appointment> repoAppointment = new Repo<>();
        PatientService ps = new PatientService(repoPatient, repoAppointment);
        ps.add(1,"pop","ana",20);
        ps.add(2,"popescu","ana",20);
        assert ps.getAll().size() == 2;
        assert ps.getById(1) == ps.getAll().get(0);
        Iterator<Patient> iter = ps.getIterator();
        Patient p1 = new Patient(1,"Cozma","George",20);
        ps.update(p1);
        assert ps.getById(1).getLastName() == "Cozma";
        ps.deleteById(2);
        assert ps.getAll().size() == 1;
        assert ps.getAll().get(0).getID() == 1;
    }

    @Test
    public void testAppointmentService() throws RepoException, ValidationError, IOException {
        Repo<Patient> repoPatient = new Repo<>();
        Patient p1 = new Patient(1,"pop","ana",20);
        Patient p2 = new Patient(2,"popescu","ana",20);
        repoPatient.add(p1);
        repoPatient.add(p2);
        Repo<Appointment> repoAppointment = new Repo<>();
        AppointmentService as = new AppointmentService(repoAppointment, repoPatient);
        as.add(1, p1.getID(), LocalDate.now(), LocalTime.now(), "control");
        as.add(2, p2.getID(), LocalDate.now(), LocalTime.now().plusMinutes(60), "control");
        assert as.getAll().size() == 2;
        assert as.getAll().get(0).getID() == 1;
        assert as.getAll().get(1).getID() == 2;
        assert as.getById(1) == as.getAll().get(0);
        Iterator<Appointment> iter = as.getIterator();
        Appointment app = new Appointment(2, p2, LocalDate.now(), LocalTime.now().plusMinutes(200), "altceva");
        as.update(app);
        assert as.getAll().get(1).getPurpose() == "altceva";
    }
}
