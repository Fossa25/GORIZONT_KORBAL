package com.example.proburok.job_User;

import com.example.proburok.MQ.DatabaseHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class SingUpController {

    @FXML private TextField login_fild;

    @FXML private PasswordField password_fild;

    @FXML private Button singUpButtun;

    @FXML private ComboBox<String> singUpContri;

    @FXML private TextField singUpLastname;

    @FXML private TextField singUpName;

    @FXML
    void initialize() {
        singUpContri.getItems().addAll("Администратор", "Проходчик", "Геолог","Геомеханик","Маркшейдер");

        singUpButtun.setOnAction(actionEvent -> {

            singUpNewUser();
            singUpName.clear();
            singUpLastname.clear();
                    password_fild.clear();
            login_fild.clear();

        });

    }

    private void singUpNewUser() {
            DatabaseHandler dbHandler = new DatabaseHandler();
        String firstname=singUpName.getText();
        String lastname=singUpLastname.getText();
        String password=password_fild.getText();
        String username=login_fild.getText();
        String location=singUpContri.getValue();
        User user = new User(firstname,lastname,password,username,location);


        dbHandler.singUpUser(user);



}

}
