package Domain;

public class PatientFactory implements IEntityFactory<Patient> {
    @Override
    public Patient createEntity(String line) {
        int id = Integer.parseInt(line.split(",")[0]);
        String lastName = line.split(",")[1];
        String firstName = line.split(",")[2];
        int age = Integer.parseInt(line.split(",")[3]);

        return new Patient(id, lastName, firstName, age);
    }
}
