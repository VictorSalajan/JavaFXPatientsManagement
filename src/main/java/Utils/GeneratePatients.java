package Utils;

import Domain.Patient;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GeneratePatients {
    Random generator = new Random();
    ArrayList<String> firstNames = new ArrayList<>(List.of(
            "Alexander", "Brooke", "Charlotte", "David", "Emily", "Finn", "Grace",
            "Henry", "Isabella", "Jack", "Katherine", "Liam", "Mia", "Noah", "Olivia"
    ));
    ArrayList<String> lastNames = new ArrayList<>(List.of(
            "Anderson", "Baker", "Carter", "Davis", "Evans", "Foster", "Green", "Hughes",
            "Ingram", "Johnson", "Knight", "Lawson", "Miller", "Nelson", "Owens"
    ));
    ArrayList<Patient> randomPatients = new ArrayList<>();

    public ArrayList<Patient> genPatients() {
        for (int id = 1; id <= 100; ) {
            int age = generator.nextInt(51) + 20;       // 20 <= age <= 70
            int firstNameIndex = generator.nextInt(firstNames.size());
            int lastNameIndex = generator.nextInt(lastNames.size());
            String firstName = firstNames.get(firstNameIndex);
            String lastName = lastNames.get(lastNameIndex);
            Patient p = new Patient(id, lastName, firstName, age);
            if (isNameUnique(lastName, firstName)) {
                randomPatients.add(p);
                id++;
            }
        }
        return randomPatients;
    }

    private boolean isNameUnique(String lastName, String firstName) {
        for (Patient randomPatient : randomPatients) {
            if (randomPatient.getLastName() == lastName && randomPatient.getFirstName() == firstName)
                return false;
        }
        return true;
    }
}
