module com.example.proburok {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.poi.ooxml;
    requires org.apache.poi.poi;

    requires java.desktop;
    requires org.apache.logging.log4j;
    requires java.sql;


    opens com.example.proburok to javafx.fxml;
    exports com.example.proburok;
}