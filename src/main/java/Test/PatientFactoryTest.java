package Test;

import Domain.Patient;
import Domain.PatientFactory;
import org.junit.jupiter.api.Test;

import java.util.Objects;

public class PatientFactoryTest {
    @Test
    public void testPatientFactory() {
        Patient p = new Patient(1,"pop", "ana", 20);
        PatientFactory factory = new PatientFactory();
        Patient p2 = factory.createEntity("1,pop,ana,20");
        assert p.equals(p2);
        assert p.getID() == p2.getID();
        assert Objects.equals(p.getLastName(), p2.getLastName());
        assert Objects.equals(p.getFirstName(), p2.getFirstName());
        assert p.getAge() == p2.getAge();
    }
}
