package Repository;
import Domain.Appointment;
import Domain.Patient;
import Utils.GenerateAppointments;
import Utils.GeneratePatients;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class AppointmentDbRepo extends DbRepo<Appointment> {
    PatientDbRepo patientDbRepo;

    public AppointmentDbRepo(PatientDbRepo patientDbRepo) throws RepoException, IOException {
        super();
        this.patientDbRepo = patientDbRepo;
        create_table_appointment();
        loadFromDb();
//        initData();
    }

    private void create_table_appointment() {
        try (final Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS appointment(" +
                    "ID int PRIMARY KEY, patientID int, dateApp date, timeApp time, purpose varchar(30)," +
                    "CONSTRAINT FK_appointment_patient FOREIGN KEY (patientID) references patient(ID)" +
                    "ON DELETE CASCADE);");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadFromDb() throws RepoException, IOException {
        try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM appointment;")) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                super.add(new Appointment(
                        rs.getInt(1), patientDbRepo.getById(rs.getInt(2)),
                        rs.getDate(3).toLocalDate(), rs.getTime(4).toLocalTime(),
                        rs.getString(5)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void add(Appointment app) throws RepoException, IOException {
        super.add(app);

        try (PreparedStatement stmt = connection.prepareStatement("INSERT INTO appointment VALUES (?,?,?,?,?);")) {
            setAppointment(stmt, app);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Appointment app) throws RepoException {
        super.update(app);

        try (PreparedStatement stmt = connection.prepareStatement(
                "UPDATE appointment SET patientID=?, dateApp=?, timeApp=?, purpose=? WHERE ID=?")) {
            stmt.setInt(2, app.getPacient().getID());
            stmt.setDate(3, Date.valueOf(app.getDate()));
            stmt.setTime(4, Time.valueOf(app.getTime()));
            stmt.setString(5, app.getPurpose());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteById(int ID) throws RepoException {
        super.deleteById(ID);

        try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM appointment WHERE ID=?")) {
            stmt.setInt(1, ID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void initData() {
        GeneratePatients patientGen = new GeneratePatients();
        ArrayList<Patient> randomPatients = patientGen.genPatients();
        GenerateAppointments appointmentsGen = new GenerateAppointments(randomPatients);
        ArrayList<Appointment> randomAppointments = appointmentsGen.genAppointments();

        try (PreparedStatement stmt = connection.prepareStatement("INSERT INTO appointment values(?,?,?,?,?);")) {
            for (Appointment app : randomAppointments) {
                setAppointment(stmt, app);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setAppointment(PreparedStatement stmt, Appointment app) throws SQLException {
        stmt.setInt(1, app.getID());
        stmt.setInt(2, app.getPacient().getID());
        stmt.setDate(3, Date.valueOf(app.getDate()));
        stmt.setTime(4, Time.valueOf(app.getTime()));
        stmt.setString(5, app.getPurpose());

        stmt.executeUpdate();
    }
}
