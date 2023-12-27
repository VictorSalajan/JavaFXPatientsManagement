module org.example.javafxa4 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.testng;
    requires org.junit.jupiter.api;
    requires org.xerial.sqlitejdbc;


    opens org.example.javafxa4 to javafx.fxml;
    exports org.example.javafxa4;
}