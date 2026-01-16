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
    exports com.example.proburok.job;
    opens com.example.proburok.job to javafx.fxml;
    exports com.example.proburok.MQ;
    opens com.example.proburok.MQ to javafx.fxml;
    exports com.example.proburok.BD;
    opens com.example.proburok.BD to javafx.fxml;
    exports com.example.proburok.job_User;
    opens com.example.proburok.job_User to javafx.fxml;
    exports com.example.proburok.New_Class;
    opens com.example.proburok.New_Class to javafx.fxml;
}