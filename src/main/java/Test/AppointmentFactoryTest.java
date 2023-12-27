package Test;

import Domain.Appointment;
import Domain.AppointmentFactory;
import Domain.Patient;
import Repository.Repo;
import Repository.RepoException;
import Utils.Utils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class AppointmentFactoryTest {
    @Test
    public void testAppointmentFactory() throws RepoException, IOException {
        Patient p = new Patient(1,"pop", "ana", 20);
        Repo<Patient> patientRepo = new Repo<>();
        patientRepo.add(p);
        LocalDate date = Utils.strToLocalDate("10/10/2020 10:00");
        LocalTime time = Utils.strToLocalTime("10/10/2020 10:00");
        Appointment app = new Appointment(1, p, date, time, "ceva");
        AppointmentFactory factory = new AppointmentFactory(patientRepo);
        Appointment app2 = factory.createEntity("1,1,10/10/2020 10:00,ceva");
        assert app.equals(app2);
        assert app.getID() == app2.getID();
        assert Objects.equals(app.getPurpose(), app2.getPurpose());
    }
}
