package Utils;

import Domain.Appointment;
import Domain.Entity;
import Domain.Patient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Utils<T extends Entity> {
    private static LocalDateTime parseDateTimeStr(String dateTimeString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return LocalDateTime.parse(dateTimeString, formatter);
    }

    public static LocalDate strToLocalDate(String dateTimeString) {
        LocalDateTime localDateTime = parseDateTimeStr(dateTimeString);
        return localDateTime.toLocalDate();
    }

    public static LocalTime strToLocalTime(String dateTimeString) {
        LocalDateTime localDateTime = parseDateTimeStr(dateTimeString);
        return localDateTime.toLocalTime();
    }

    public String getEntityFileReadyString(T entity) {
        if (entity.getClass() == Appointment.class) {
            return getAppointmentFileReadyString((Appointment) entity);
        }
        else
            return getPatientFileReadyString((Patient) entity);
    }

    public String getPatientFileReadyString(Patient entity) {
        String id = String.valueOf(entity.getID());
        String nume = entity.getLastName();
        String firstName = entity.getFirstName();
        String age = String.valueOf(entity.getAge());
        return String.join(",", id, nume, firstName, age);
    }

    public String getAppointmentFileReadyString(Appointment entity) {
        String id = String.valueOf(entity.getID());
        String idPatient = String.valueOf(entity.getPacient().getID());
        LocalDate date = entity.getDate();
        String dayWithZero = (date.getDayOfMonth() < 10) ? "0" + date.getDayOfMonth() : String.valueOf(date.getDayOfMonth());
        String monthWithZero = (date.getMonthValue() < 10 ? "0" + date.getMonthValue() : String.valueOf(date.getMonthValue()));
        String dateStr = dayWithZero + "/" + monthWithZero + "/" + String.valueOf(date.getYear());
        LocalTime time = entity.getTime();
        String timeStr = time.toString();
        String dateTimeStr = dateStr + " " + timeStr;
        return String.join(",", id, idPatient, dateTimeStr, entity.getPurpose());
    }
}
