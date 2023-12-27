package Test;

import Domain.Entity;
import Domain.Patient;
import org.testng.annotations.Test;

public class EntityTest {
    @Test
    public void testEntity() {
        Entity entity1 = new Patient(1, "pop", "ana", 20);
        Entity entity2 = new Patient(1, "pop", "ana", 20);
        Entity entity3 = new Patient(2, "pop", "ana", 20);
        assert entity1.getID() == 1;
        assert entity1.equals(entity2); // Ids equal
        assert !entity1.equals(entity3); // Ids different
    }
}
