package Test;

import Domain.Patient;
import org.junit.jupiter.api.Test;

import java.util.Objects;


public class PatientTest {
    @Test
    public void testPatient() {
        Patient p1 = new Patient(1,"pop","ana",20);
        assert p1.getID() == 1;
        assert Objects.equals(p1.getLastName(), "pop");
        assert Objects.equals(p1.getFirstName(), "ana");
        assert p1.getAge() == 20;
        assert Objects.equals(p1.toString(), String.format("Patient: %d, lastName: %s, firstName: %s, age: %d",
                p1.getID(), p1.getLastName(), p1.getFirstName(), p1.getAge()));

    }

}
