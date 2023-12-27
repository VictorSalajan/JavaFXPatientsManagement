package Test;

import Domain.Appointment;
import Domain.Patient;
import Utils.Utils;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class AppointmentTest {

    @Test
    public void testAppointment() {
        Patient p1 = new Patient(1,"ana","pop",20);
        LocalDate date = Utils.strToLocalDate("10/10/2020 10:00");
        LocalTime time = Utils.strToLocalTime("10/10/2020 10:00");
        Appointment app = new Appointment(1, p1, date, time, "control");
        assert app.getID() == 1;
        assert app.getPacient().equals(p1);
        assert app.getDate().getMonthValue() == 10;
        assert app.getDate().getYear() == 2020;
        assert app.getDate().getDayOfMonth() == 10;
        assert app.getTime().getHour() == 10;
        assert app.getPurpose() == "control";
        assert Objects.equals(app.toString(), String.format("Appointment: %d, %s, data: %s/%s/%s, time: %s, purpose: %s",
                app.getID(), app.getPacient().toString(), String.valueOf(app.getDate().getDayOfMonth()),
                String.valueOf(app.getDate().getMonth()), String.valueOf(app.getDate().getYear()),
                app.getTime().toString(), app.getPurpose()));
    }
}
