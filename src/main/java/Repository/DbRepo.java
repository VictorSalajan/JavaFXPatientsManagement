package Repository;
import Domain.Entity;
import org.sqlite.SQLiteDataSource;

import java.sql.*;

public abstract class DbRepo<T extends Entity> extends Repo<T> {
    private String JDBC_URL = "jdbc:sqlite:/home/victor/Info/An2/MAP/JavaFXPatientsManagement/patientsAndAppointments.sqlite";
    Connection connection;

    public DbRepo() {
        openConnection();
    }

    private void openConnection() {
        SQLiteDataSource ds = new SQLiteDataSource();
        ds.setUrl(JDBC_URL);

        try {
            if (connection == null || connection.isClosed()) {
                connection = ds.getConnection();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
