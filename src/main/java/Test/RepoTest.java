package Test;

import Domain.Appointment;
import Domain.Patient;
import Repository.Repo;
import Repository.RepoException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class RepoTest {
    @Test
    public void testRepo() throws RepoException, IOException {
        Repo<Patient> repoPatient = new Repo<>();
        Repo<Appointment> repoAppointment = new Repo<>();
        Patient p1 = new Patient(1,"pop","ana",20);
        Patient p2 = new Patient(2,"popescu","ana",20);
        repoPatient.add(p1);
        repoPatient.add(p2);
        assert repoPatient.getById(1) == p1;
        assert repoPatient.getById(2) == p2;
        assert Objects.equals(repoPatient.getAll(), new ArrayList<Patient>(List.of(p1, p2)));
        Appointment app1 = new Appointment(1, p1, LocalDate.now(), LocalTime.now(), "control");
        Appointment app2 = new Appointment(2, p2, LocalDate.now(), LocalTime.now(), "control");
        repoAppointment.add(app1);
        repoAppointment.add(app2);
        assert repoAppointment.getById(1) == app1;
        assert repoAppointment.getById(2) == app2;
        assert Objects.equals(repoAppointment.getAll(), new ArrayList<>(List.of(app1, app2)));

        Patient p3 = new Patient(2,"Ionescu","maria",20);
        repoPatient.update(p3);
        ArrayList<Patient> patientArrayList = repoPatient.getAll();
        assert patientArrayList.size() == 2;
        assert repoPatient.getById(2).getLastName() == "Ionescu";
        Iterator<Patient> iterP = repoPatient.iterator();
        Iterator<Appointment> iterA = repoAppointment.iterator();
        repoPatient.deleteById(1);
        repoAppointment.deleteById(1);
        assert patientArrayList.size() == 1;
        ArrayList<Appointment> appointmentArrayList = repoAppointment.getAll();
        assert appointmentArrayList.size() == 1;
    }
}
