package Service;

import Domain.Appointment;
import Repository.Repo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Iterator;

public class AppointmentValidator {
    public static void validateAppointment(Appointment app, Repo<Appointment> appRepo) throws ValidationError {
        ArrayList<String> errors = new ArrayList<>();
        Iterator<Appointment> appIter = appRepo.iterator();
        LocalDate appToAddDate = app.getDate();
        LocalTime appToAddtime = app.getTime();
        LocalDateTime appToAddDateTime = LocalDateTime.of(appToAddDate, appToAddtime);
        if (appToAddDateTime.getYear() > 2024)
            errors.add("Appointment cannot be further in the future than 2024!");
        if (app.getPurpose() == "")
            errors.add("Appointment purpose cannot be null!");
        while (appIter.hasNext()) {
            Appointment nextApp = appIter.next();
            if (app.equals(nextApp)) {
                continue;               // when updating: do not evaluate overlap with itself
            }
            LocalDate date = nextApp.getDate();
            LocalTime time = nextApp.getTime();
            LocalDateTime crtDateTime = LocalDateTime.of(date, time);
            long minDiff = ChronoUnit.MINUTES.between(crtDateTime, appToAddDateTime);
            if (appToAddDateTime.toLocalDate().isEqual(crtDateTime.toLocalDate())) {
                if (minDiff < 60)
                    errors.add("Appointments overlapping! One appointment has 60 minutes!");
            }
        }
        if (!errors.isEmpty())
            throw new ValidationError(errors.toString());
    }
}
