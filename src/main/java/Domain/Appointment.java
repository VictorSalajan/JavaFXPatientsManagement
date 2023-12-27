package Domain;

import java.time.LocalDate;
import java.time.LocalTime;

public class Appointment extends Entity {

    private Patient patient;
    private LocalDate date;
    private LocalTime time;
    private String purpose;

    public Appointment(int ID, Patient patient, LocalDate date, LocalTime time, String purpose) {
        super(ID);
        this.patient = patient;
        this.date = date;
        this.time = time;
        this.purpose = purpose;
    }

    @Override
    public int getID() {
        return this.ID;
    }

    public Patient getPacient() {
        return patient;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public String getPurpose() {
        return purpose;
    }

    @Override
    public String toString() {
        String patientInfo = (this.patient != null) ? this.patient.toString() : "No patient information";
        return String.format("Appointment: %d, %s, date: %s/%s/%s, time: %s, purpose: %s",
                ID, patientInfo, String.valueOf(date.getDayOfMonth()), String.valueOf(date.getMonth()),
                String.valueOf(date.getYear()), time.toString(), purpose);
    }
}
