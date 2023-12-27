package Repository;
import Domain.Patient;
import Utils.GeneratePatients;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class PatientDbRepo extends DbRepo<Patient> {
    public PatientDbRepo() throws RepoException, IOException {
        super();
        create_table_patient();
        loadFromDb();
//        initData();
    }

    private void create_table_patient() {
        try (final Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS patient(" +
                    "ID int primary key, lastName varchar(50), firstName varchar(50), age int);");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadFromDb() throws RepoException, IOException {
        try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM patient;")) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                super.add(new Patient(
                        rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void add(Patient p) throws RepoException, IOException {
        super.add(p);

        try (PreparedStatement stmt = connection.prepareStatement("INSERT INTO patient VALUES(?,?,?,?);")) {
            setPatient(stmt, p);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Patient p) throws RepoException {
        super.update(p);

        try (PreparedStatement stmt = connection.prepareStatement(
                "UPDATE patient SET lastName=?, firstName=?, age=? WHERE ID=?")) {
            stmt.setString(1, p.getLastName());
            stmt.setString(2, p.getFirstName());
            stmt.setInt(3, p.getAge());
            stmt.setInt(4, p.getID());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteById(int ID) throws RepoException {
        super.deleteById(ID);

        try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM patient WHERE ID=?")) {
            stmt.setInt(1, ID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (PreparedStatement stmt2 = connection.prepareStatement("DELETE FROM appointment WHERE patientID=?")) {
            stmt2.setInt(1, ID);
            stmt2.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void initData() {
        GeneratePatients patientGen = new GeneratePatients();
        ArrayList<Patient> randomPatients = patientGen.genPatients();

        try (PreparedStatement stmt = connection.prepareStatement("INSERT INTO patient values (?,?,?,?);")) {
            for (Patient p : randomPatients) {
                setPatient(stmt, p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setPatient(PreparedStatement stmt, Patient p) throws SQLException {
        stmt.setInt(1, p.getID());
        stmt.setString(2, p.getLastName());
        stmt.setString(3, p.getFirstName());
        stmt.setInt(4, p.getAge());

        stmt.executeUpdate();
    }
}
