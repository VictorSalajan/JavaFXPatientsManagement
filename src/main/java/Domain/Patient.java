package Domain;

public class Patient extends Entity {
    private String lastName;
    private String firstName;
    private int age;

    public Patient(int ID, String lastName, String firstName, int age) {
        super(ID);
        this.lastName = lastName;
        this.firstName = firstName;
        this.age = age;
    }

    @Override
    public int getID() {
        return ID;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public int getAge() {
        return age;
    }

    @Override
    public String toString() {
        return String.format("Patient: %d, lastName: %s, firstName: %s, age: %d", ID, lastName, firstName, age);
    }
}