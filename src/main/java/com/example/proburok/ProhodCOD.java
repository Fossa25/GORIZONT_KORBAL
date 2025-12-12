package com.example.proburok;

import java.sql.Date;
import java.time.DateTimeException;
import java.time.LocalDate;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;


public class ProhodCOD extends Configs{
    @FXML private DatePicker data;
    @FXML private ComboBox<String> sehenbox;
    @FXML private TextField gorizont;
    @FXML private TextField sehen;
    @FXML private TextField nameProhod;

    @FXML private TextField nomer;


    @FXML private TextField idi;
    @FXML private Button singUpButtun;
    private String NOMER;
    private Date DATA;
    private String SEHEN;
    private String GORIZONT;
    private String NAME;

    private String HIFR;
    @FXML private TableView<Probnik> Tablshen;

    @FXML private TableView<Probnik1> Tablshen1;

    @FXML private TableColumn<Probnik, String> stolb1;
    @FXML private TableColumn<Probnik, Double> stolb2;
    @FXML private TableColumn<Probnik, Double> stolb3;

    @FXML private TableColumn<Probnik1, String> stolb11;
    @FXML private TableColumn<Probnik1, String> stolb21;
    @FXML private TableColumn<Probnik, Double> stolb31;

    @FXML private ImageView instr;
    @FXML private ImageView dokumGeolog;
    @FXML private ImageView dokumGeolog11;
    @FXML private ImageView pethat;
    @FXML private ImageView tabl;
    @FXML private ImageView othet;
    @FXML private ComboBox<String> ushatok;

    @FXML
    void initialize() {

        idi.setVisible(false);
        tabltrabl();
        data.setValue(LocalDate.now());
        pethat.setOnMouseClicked(mouseEvent -> openNewScene("/com/example/proburok/Pehat.fxml"));
        openImagedok(tabl,instr,dokumGeolog,dokumGeolog11,"yes");
        othet.setOnMouseClicked(mouseEvent -> {OpenDok(Put_othet,"Отчет_");});


        ushatok.getItems().addAll("ПГУ 1", "ПГУ 2", "ПУ ГКР №3", "ПУ ГКР №9");

        ushatok.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && data.getValue() != null) {
                String year = String.valueOf(data.getValue().getYear());
                String prefix = "";
                if (newValue.equals("ПГУ 1")) prefix = "1";
                else if (newValue.equals("ПГУ 2")) prefix = "2";
                else if (newValue.equals("ПУ ГКР №3")) prefix = "3";
                else if (newValue.equals("ПУ ГКР №9")) prefix = "9";

                // Получаем следующий порядковый номер из БД
                DatabaseHandler dbHandler = new DatabaseHandler();
                int nextNumber = dbHandler.getNextSequenceNumber(prefix, year);

                // Устанавливаем номер в формате "1-5-2025"
                nomer.setText(prefix + "-" + nextNumber + "-" + year);

            }
        });
        singUpButtun.setOnMouseClicked(mouseEvent -> {
            NOMER = nomer.getText() != null ? nomer.getText().trim() : "";
            SEHEN = sehen.getText() != null ? sehen.getText().trim() : "";
            GORIZONT = gorizont.getText() != null ? gorizont.getText().trim() : "";

            NAME = nameProhod.getText() != null ? nameProhod.getText().trim() : "";
            HIFR = idi.getText() != null ? idi.getText().trim() : "";
            LocalDate selectedDate = data.getValue();
            try {
                StringBuilder errors = new StringBuilder();
                if (NOMER.isEmpty()) errors.append("- Не заполнен номер\n");
                if (selectedDate == null) {
                    errors.append("- Не выбрана дата\n");
                } else {
                    DATA = Date.valueOf(selectedDate); // Преобразуем только если дата выбрана
                }
                if (SEHEN.isEmpty()) errors.append("- Не выбрано сечение\n");
                if (GORIZONT.isEmpty()) errors.append("- Не заполнен горизонт\n");

                if (NAME.isEmpty()) errors.append("- Не заполнено название выработки\n");
                if (errors.length() > 0) {
                    showAlert("Заполните обязательные поля:\n" + errors);
                    return;
                }
                String nameBD = NAME + " № " + NOMER;
                String prim =  "Необходима привязка выработки" ;

                // Все данные валидны - сохраняем
                DatabaseHandler Tabl = new DatabaseHandler();
                Tabl.Dobavlenie(NOMER, DATA, SEHEN, GORIZONT,  nameBD,NAME,HIFR,ushatok.getValue(),prim);
                ohistka();

            } catch (DateTimeException e) {
                showAlert("Ошибка в формате даты!");
            } catch (Exception e) {
                showAlert("Произошла ошибка: " + e.getMessage());
            }
        });
    }
    private void ohistka() {
        ushatok.setValue("");
        nomer.setText("");
        sehen.setText("");
        gorizont.setText("");

        nameProhod.setText("");
        idi.setText("");

    }
    private void tabltrabl(){
        stolb1.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getVirobotka()));
        stolb2.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPlohad()).asObject());
        stolb3.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getId()).asObject());
        // Для второй таблицы
        stolb11.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getVirobotka1()));
        stolb21.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNety()));
        stolb31.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getId()).asObject());

        ObservableList<Probnik> tabl = FXCollections.observableArrayList(
                new Probnik("Вентиляционный, транспортный наклонный съезд", 32.0,1),
                new Probnik("Транспортный штрек", 19.3,2),
                new Probnik("Слоевой заезд, ниша перегрузки", 16.2,3),
                new Probnik("Вентиляционный квершлаг", 23.2,4),
                new Probnik("Породный штрек", 17.3,5),
                new Probnik("Ниша ПТП", 33.3,6),
                new Probnik("Камера ПТП", 18.2,7),
                new Probnik("Ниша ВМП, ниша складирования" , 24.5,8),
                new Probnik("Камера ВШД", 30.6,9),
                new Probnik("Ниша материалов", 42.1,10)

        );
        ObservableList<Probnik1> tabl1 = FXCollections.observableArrayList(
                new Probnik1 ("Породный (транспортный) штрек / слоевой заезд (Т-образное)","19,3/19,3",11),
                new Probnik1 ("Породный (транспортный) штрек / слоевой заезд (Т-образное)","16,2/16,2",12),
                new Probnik1 ("Откаточные выработки / откаточные выработки (X-образное)","16,2/16,2",13),
                new Probnik1 ("Вентиляционный (транспортный) съезд / заезд на штрек (X-образное)","32,0/16,2",14),
                new Probnik1 ("Породный (транспортный) штрек / наши ШАС (X-образное)","19,3/16,0",15)
        );

        stolb1.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item);
                setStyle("-fx-font-family: 'Verdana'; -fx-font-size: 14px;");
            }
        });
        stolb2.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : String.valueOf(item));
                setStyle("-fx-font-family: 'Verdana'; -fx-font-size: 14px;");
            }
        });
        stolb11.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item);
                setStyle("-fx-font-family: 'Verdana'; -fx-font-size: 14px;");
            }
        });
        stolb21.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item);
                setStyle("-fx-font-family: 'Verdana'; -fx-font-size: 14px;");
            }
        });
        Tablshen.setItems(tabl);
        Tablshen1.setItems(tabl1);

        Tablshen.setOnMouseClicked(mouseEvent -> {
            Probnik selectedPerson = Tablshen.getSelectionModel().getSelectedItem();
            if (selectedPerson != null) {
                // Отображаем значение из первого столбца ("Имя") в TextField
                sehen.setText(String.valueOf(selectedPerson.getPlohad()));
                idi.setText(String.valueOf(selectedPerson.getId()));
            }
        });
        Tablshen1.setOnMouseClicked(mouseEvent -> {
            Probnik1 selectedPerson = Tablshen1.getSelectionModel().getSelectedItem();
            if (selectedPerson != null) {
                // Отображаем значение из первого столбца ("Имя") в TextField
                sehen.setText(String.valueOf(selectedPerson.getNety()));
                idi.setText(String.valueOf(selectedPerson.getId()));
            }
        });
    }



}






