package Utils;

import Domain.Appointment;
import Domain.Patient;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GenerateAppointments {
    Random generator = new Random();
    ArrayList<String> fields = new ArrayList<>(List.of(
            "cardiologie", "endocrinologie", "ginecologie", "neurologie", "medicina de familie"));
    ArrayList<Appointment> randomAppointments = new ArrayList<>();
    ArrayList<Patient> randomPatients;

    public GenerateAppointments(ArrayList<Patient> randomPatients) {
        this.randomPatients = randomPatients;
    }

    public ArrayList<Appointment> genAppointments() {
        for (int id = 1; id <= 100; ) {
            int patientID = generator.nextInt(randomPatients.size());
            Patient p = randomPatients.get(patientID);
            int day = generator.nextInt(28) + 1;
            int month = generator.nextInt(12) + 1;
            int year = 2023;
            LocalDate randomDate = LocalDate.of(year, month, day);
            int hour = generator.nextInt(15) + 6;               // 06:00 -> 20:00
            int minutes = generator.nextInt(60);
            LocalTime randomTime = LocalTime.of(hour, minutes);
            int purposeIndex = generator.nextInt(fields.size());
            String purpose = fields.get(purposeIndex);
            Appointment app = new Appointment(id, p, randomDate, randomTime, purpose);

            if (!isOverlapping(randomDate, randomTime)) {
                randomAppointments.add(app);
                id++;
            }
        }
        return randomAppointments;
    }

    private boolean isOverlapping(LocalDate date, LocalTime time) {
        for (Appointment randomAppointment : randomAppointments) {
            if (randomAppointment.getDate().equals(date))
                if (ChronoUnit.MINUTES.between(time, randomAppointment.getTime()) < 60)
                    return true;
        }
        return false;
    }
}
