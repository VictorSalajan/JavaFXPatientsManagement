package Repository;

import Domain.Entity;
import org.sqlite.SQLiteDataSource;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;

public abstract class DbRepo<T extends Entity> extends Repo<T> {
    Path basePath = Paths.get(System.getProperty("user.dir"));
    private final String JDBC_URL = "jdbc:sqlite:" + basePath + "/patientsAndAppointments.sqlite";
    Connection connection;

    public DbRepo() {
        openConnection();
    }

    private void openConnection() {
        SQLiteDataSource ds = new SQLiteDataSource();
        ds.setUrl(JDBC_URL);

        try {
            if (connection == null || connection.isClosed())
                connection = ds.getConnection();
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
