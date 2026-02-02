package com.example.proburok.job;

import com.example.proburok.New_Class.Baza;
import com.example.proburok.MQ.DatabaseHandler;
import com.example.proburok.New_Class.Baza_Geolg;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.io.*;
import java.util.List;
import java.util.*;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import javafx.stage.Popup;

public class GeomexCOD extends Configs {

    private static final Logger log = LogManager.getLogger(GeomexCOD.class);

    @FXML private ImageView instr;
    @FXML private ImageView dokumGeolog;
    @FXML private ImageView dokumGeolog11;
    @FXML private ImageView tabl;
    @FXML private TextField cehen;
    @FXML private ComboBox<String> gorbox;
    @FXML private TextField katigoria;
    @FXML private ComboBox<String> namebox;
    @FXML private TextField nomer;
    @FXML private TextField nomer1;
    @FXML private TextArea opisanie;
    @FXML private Button singUpButtun;
    @FXML private TextField privazka;
    @FXML private TextField idi;
    @FXML private TextField dlina;
    private final DatabaseHandler dbHandler = new DatabaseHandler();
    @FXML private TextArea primhanie;
    List<String> soprigenii = Arrays.asList("11","12","13","14","15");
    @FXML private ImageView PlanVKL;
    @FXML private ImageView PlanVKLNe;
    @FXML private ImageView PoperVKL;
    @FXML private ImageView PoperVKLNe;
    @FXML private ImageView ProdolVKL;
    @FXML private ImageView ProdolVKLNe;
    @FXML private ImageView TP;
    @FXML private Button butnA;
    @FXML private Button butnB;
    @FXML private Button butnN;
    @FXML private TextArea opisanieA;
    @FXML private TextArea opisanieB;
    @FXML private TextArea opisanieN;
    @FXML private TextField texfak;
    public String textopisaniA = "Проходка в разуплотненном горном массиве после его подработки. (Укажите конкретную причину).";
    public String textopisaniB = "Разуплотнение горного массива при сейсмическом воздействии от взрывных работ.";
    public String seri= "-fx-background-radius: 50px; -fx-background-color: #5e5d5d;";
    public String zoloto = "-fx-background-color: aa9455";

    @FXML private Button singUpButton1;
    @FXML private AnchorPane GM;
    private Popup popup;
    public String tfopis;
    @FXML private ComboBox<String> interval;
    @FXML
    void initialize() {

        UnaryOperator<TextFormatter.Change> filter = change -> {
            if (change.getText().contains(";")) {
                return null; // отклоняем изменение
            }
            return change;
        };
        opisanieA.setTextFormatter(new TextFormatter<>(filter));
        opisanieB.setTextFormatter(new TextFormatter<>(filter));
        butnA.setOnMouseClicked(mouseEvent -> {
            String stari =texfak.getText() ;
            if(stari == null) stari = "";
            if (butnA.getStyle().equals(seri)){
                butnA.setStyle(zoloto);
                opisanieA.setText(textopisaniA);
                opisanieA.setEditable(true);
                texfak.setText("А"+stari);

            } else if (butnA.getStyle().equals(zoloto)) {
                butnA.setStyle(seri);
                String nowi = stari.replace("А","");
                texfak.setText(nowi);
                opisanieA.setText(null);
                opisanieA.setEditable(false);
            }
        });
        butnB.setOnMouseClicked(mouseEvent -> {
            String stari =texfak.getText() ;
            if(stari == null) stari = "";
            if (butnB.getStyle().equals(seri)){
                butnB.setStyle(zoloto);
                opisanieB.setText(textopisaniB);
                opisanieB.setEditable(true);
                switch (stari) {
                    case "" -> texfak.setText("Б");
                    case "А" -> texfak.setText(stari + "Б");
                }
            } else if (butnB.getStyle().equals(zoloto)) {
                butnB.setStyle(seri);
                String nowi = stari.replace("Б","");
                texfak.setText(nowi);
                opisanieB.setText(null);
                opisanieB.setEditable(false);
            }
        });
        butnN.setOnMouseClicked(mouseEvent -> {

            if (butnN.getStyle().equals(seri)){
                butnN.setStyle(zoloto);
                butnA.setDisable(true);butnB.setDisable(true);
                butnA.setStyle(seri);butnB.setStyle(seri);

                texfak.setText("-");
                opisanieA.setText(null);opisanieA.setEditable(false);
                opisanieB.setText(null);opisanieB.setEditable(false);

                opisanieN.setText("технологические факторы не будут оказывать влияние.");

            } else if (butnN.getStyle().equals(zoloto)) {
                butnN.setStyle(seri);
                butnA.setDisable(false);butnB.setDisable(false);
                texfak.setText(null);
                opisanieN.setText(null);
            }
        });

        popup = new Popup();
        popup.getContent().add(GM);
        // Обработчик для кнопки
        singUpButton1.setOnAction(e -> {
            if (!popup.isShowing()) {
                // Получаем экранные координаты кнопки
                Point2D point = singUpButton1.localToScreen(0, 0);

                // Показываем popup рядом с кнопкой
                popup.show(
                        singUpButton1.getScene().getWindow(),
                        point.getX()-518, // справа от кнопки
                        point.getY() -170
                );
            } else {
                popup.hide();
            }
        });
        openImagedok(tabl,instr,dokumGeolog,dokumGeolog11,"yes");

        TP.setOnMouseClicked(e -> {
            if (nomer1.getText().isEmpty())  return;
             int fot = Integer.parseInt(nomer1.getText()) - 1;
             System.out.println(fot);
            if (soprigenii.contains(idi.getText())) {
                openPas(Put +"/"+"soprigenii"+"/",fot);
            }
            else { openPas(Put +"/"+"obvid"+"/",fot);}
        });
        idi.setVisible(false);
        primhanie.setVisible(false);
        setupComboBoxes();
        setupButtonAction();
        setupCursor_2(PlanVKL,PlanVKLNe);
        setupCursor_2(PoperVKL,PoperVKLNe);
        setupCursor_2(ProdolVKL,ProdolVKLNe);
        setupImageHandlers();
        singUpButtun.setVisible(false);
        instr.setOnMouseClicked(mouseEvent -> {OpenDok(Put_instr, "Инструкция_");});
    }
    private void setupComboBoxes() {
        ObservableList<String> horizons = FXCollections.observableArrayList(
                dbHandler.getAllBaza().stream()
                        .map(Baza::getGORIZONT)
                        .distinct()
                        .collect(Collectors.toList())
        );
        gorbox.setItems(horizons);

        gorbox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !newVal.isEmpty()) {
                updateNamesComboBox(newVal);
            }
        });
        namebox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null  && !newVal.isEmpty() && gorbox.getValue() != null) {
                populateFields(gorbox.getValue(), newVal);
            }
        });
        interval.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null  && !newVal.isEmpty() && gorbox.getValue() != null) {
                populateGEO(nomer.getText(), newVal);
            }
        });


        addTextChangeListener(nomer);
       addTextChangeListener(cehen);
        addTextChangeListener(privazka);
        addTextChangeListener(dlina);
    }

    private void addTextChangeListener(TextField field) {
        field.textProperty().addListener((obs, oldVal, newVal) -> {
            checkFieldsAndUpdateButton();
        });
    }
    private void checkFieldsAndUpdateButton() {
        boolean allValid = validateRequiredFields() &&
                gorbox.getValue() != null &&
                namebox.getValue() != null;
        singUpButtun.setDisable(!allValid);
    }
    private void updateNamesComboBox(String horizon) {

        ObservableList<String> names = FXCollections.observableArrayList(
                dbHandler.poiskName(horizon).stream()
                        .map(Baza::getNAME)
                        .distinct()
                        .collect(Collectors.toList()));
        clearFields();
        namebox.setItems(names);
    }

    private void updateInterBox(String pas) {

        ObservableList<String> names = FXCollections.observableArrayList(
                dbHandler.getAllRows(pas).stream()
                        .map(Baza_Geolg::getcolumnOTDO)
                        .distinct()
                        .collect(Collectors.toList()));
        clearGEO();
        interval.setItems(names);

    }


    private void updateUI(Baza data) {
        Platform.runLater(() -> {
            try {
                nomer.setText(data.getNOMER());
                cehen.setText(data.getSEHEN());
                privazka.setText(data.getPRIVIZKA());
                dlina.setText(data.getDLINA());
                primhanie.setText(data.getPRIM());
                idi.setText(data.getIDI());
                setupImageHandlers();
                // Обновляем изображения
                proverkaImageGeolg(Put + "/" + nomer.getText() + "/" + "План", PlanVKL, PlanVKLNe);
                proverkaImageGeolg(Put + "/" + nomer.getText() + "/" + "Поперечный", PoperVKL, PoperVKLNe);
                proverkaImageGeolg(Put + "/" + nomer.getText() + "/" + "Продольный", ProdolVKL, ProdolVKLNe);

                // Проверяем заполненность полей
                boolean allValid = validateRequiredFields();
                singUpButtun.setVisible(allValid);
                singUpButtun.setDisable(!allValid);

                if (!allValid && primhanie.getText() != null && !primhanie.getText().isEmpty()) {
                    showAlert(primhanie.getText());
                }

            } catch (Exception e) {
                log.error("Error updating UI", e);
                showAlert("Ошибка при обновлении данных: " + e.getMessage());
            }
        });
        updateInterBox(data.getNOMER());
    }
    private void updateGEO(Baza_Geolg data) {
        Platform.runLater(() -> {
            try {
                katigoria.setText(data.getcolumnKLASS());
                opisanie.setText(data.getcolumnOPIS());
               // opisanieA.setText(data.get());
                nomer1.setText(data.getcolumnLIST());

                String faktor = data.getColumnFAKTOR();

                texfak.setText(faktor != null ? faktor : "");
                if (faktor != null && faktor.equals("-")){
                    opisanieN.setText(data.getColumnFAKTOR_TEXT());
                } else { DroblenieTEXT(data.getColumnFAKTOR_TEXT(), faktor);}
                poiskFAKTOR();

                boolean allValid = isFieldValid(katigoria);
                singUpButtun.setVisible(allValid);
                singUpButtun.setDisable(!allValid);

                if (!allValid && primhanie.getText() != null && !primhanie.getText().isEmpty()) {
                    showAlert(primhanie.getText());
                }
            } catch (Exception e) {
                log.error("Error updating UI", e);
                showAlert("Ошибка при обновлении данных: " + e.getMessage());
            }
        });
    }
    public void DroblenieTEXT (String tfopis, String faktorValue) {
        // Очищаем поля перед загрузкой
        opisanieA.setText("");
        opisanieB.setText("");

        // Проверяем наличие данных
        if (tfopis == null || tfopis.isEmpty() || faktorValue == null) {
            return;
        }
        // Разделяем строку с учётом разделителя ";\n"
        String[] parts = tfopis.split(";\\s*\\n");
        int index = 0;

        // Распределяем части согласно порядку факторов
        if (faktorValue.contains("А")) {
            if (index < parts.length) {
                opisanieA.setText(parts[index].trim());
                index++;
            }
        }
        if (faktorValue.contains("Б")) {
            if (index < parts.length) {
                opisanieB.setText(parts[index].trim());
               // opisanieB.setDisable(true);
                index++;
            }
        }

    }
    private void poiskFAKTOR(){
        if ((texfak.getText() ==null) || texfak.getText().isEmpty() ){

            butnN.setStyle(seri);butnN.setDisable(false);opisanieN.setDisable(false);
            butnA.setStyle(seri);butnB.setStyle(seri);
            butnA.setDisable(false);butnB.setDisable(false);
        }  else {

            switch (texfak.getText()){
                case "А" ->{
                    butnN.setStyle(seri);butnN.setDisable(false);opisanieN.setDisable(false);
                butnA.setStyle(zoloto);butnB.setStyle(seri);
                butnA.setDisable(false);butnB.setDisable(false);
                }

                case "Б" ->{
                    butnN.setStyle(seri);butnN.setDisable(false);opisanieN.setDisable(false);
                    butnA.setStyle(seri);butnB.setStyle(zoloto);
                    butnA.setDisable(false);butnB.setDisable(false);
                }
                case "АБ" -> {
                    butnN.setStyle(seri);butnN.setDisable(false);opisanieN.setDisable(false);
                    butnA.setStyle(zoloto);butnB.setStyle(zoloto);
                    butnA.setDisable(false);butnB.setDisable(false);
                }
                case "-" ->{
                    butnN.setStyle(zoloto);butnN.setDisable(false);opisanieN.setDisable(false);
                butnA.setStyle(seri);butnB.setStyle(seri);
                butnA.setDisable(true);butnB.setDisable(true);
                }
            }}}


    private boolean validateRequiredFields() {
        return isFieldValid(nomer) &&
                isFieldValid(cehen) &&
                isFieldValid(dlina) &&
                isFieldValid(privazka);
    }

    private boolean isFieldValid(TextField field) {
        return field != null && field.getText() != null && !field.getText().trim().isEmpty();
    }

    private void populateFields(String horizon, String name) {
        Baza data = dbHandler.danii(horizon, name);
        if (data != null) {
            updateUI(data);

        } else {
            clearFields();
            singUpButtun.setVisible(false);
        }
    }

    private void populateGEO(String pas, String interval) {
        Baza_Geolg geo = dbHandler.getAllGEOMEX(pas, interval);

        if (geo != null) {
            updateGEO(geo);

        } else {
            clearGEO();
            singUpButtun.setVisible(false);
        }
    }



    private void clearFields() {
      //  namebox.setValue(null);
        nomer.clear();
        katigoria.clear();
        opisanie.setText("");
        cehen.clear();
        nomer1.clear();
        privazka.clear();
        dlina.clear();
        primhanie.clear();
        idi.clear();
        opisanieA.clear();
        proverkaImageGeolg(Put + "/" + nomer.getText() + "/" + "План", PlanVKL,PlanVKLNe);
        proverkaImageGeolg(Put + "/" + nomer.getText() + "/" + "Поперечный",PoperVKL,PoperVKLNe);
        proverkaImageGeolg(Put + "/" + nomer.getText() + "/" + "Продольный",ProdolVKL,ProdolVKLNe);
    }
    private void clearGEO() {
        //  namebox.setValue(null);
        katigoria.clear();
        opisanie.setText("");
        nomer1.clear();
        opisanieA.clear();
        texfak.setText("");
        opisanieA.setText("");
        opisanieB.setText("");
        butnN.setStyle(seri);butnA.setStyle(seri);butnB.setStyle(seri);
        butnA.setDisable(false);butnB.setDisable(false);
    }

    private void setupButtonAction() {
        singUpButtun.setOnAction(event -> handleDocumentGeneration());
    }

    private void handleDocumentGeneration() {
        String selectedGor = gorbox.getValue();
        String selectedName = namebox.getValue();
        String kategoriyaValue = katigoria.getText();
        String opisanieValue = opisanie.getText();
        String selectPrivizka = privazka.getText();
        String dlinaValue = dlina.getText();


        String faktorValue = texfak.getText();
        String AValue= opisanieA.getText();
        String BValue= opisanieB.getText();

        proverkaImageGeolg(Put + "/" + nomer.getText() + "/" + "План", PlanVKL,PlanVKLNe);
        proverkaImageGeolg(Put + "/" + nomer.getText() + "/" + "Поперечный",PoperVKL,PoperVKLNe);
        proverkaImageGeolg(Put + "/" + nomer.getText() + "/" + "Продольный",ProdolVKL,ProdolVKLNe);
        try {
            // Проверка полей по очереди
            StringBuilder errors = new StringBuilder();
            if (selectedGor == null || selectedGor.isEmpty()) {errors.append("- Не выбран горизонт\n");}
            if (selectedName == null || selectedName.isEmpty()) {errors.append("- Не выбрано название выработки\n");}
            if (selectPrivizka == null || selectPrivizka.isEmpty()) {errors.append("- Не заполнена привязка\n");}
            if (dlinaValue == null || dlinaValue.isEmpty()) {errors.append("- Не заполнена длина выработки\n");}
            if (kategoriyaValue == null || kategoriyaValue.isEmpty()) {errors.append("- Не заполнено поле категория \n");}
            if (opisanieValue == null || opisanieValue.isEmpty()) {errors.append("- Не заполнено поле геологическое описание\n");}

            if (butnA.getStyle().equals(zoloto)) {
                if( AValue == null || AValue.isEmpty()  ) {errors.append("- Поле описание для фактора A пустое\n");}}
            if (butnB.getStyle().equals(zoloto)) {
                if( BValue == null || BValue.isEmpty()  ) {errors.append("- Поле описание для фактора Б пустое\n");}}

            // Если есть ошибки - показываем их
            if (errors.length() > 0) {
                showAlert("Заполните обязательные поля:\n" + errors.toString());
                return;
            }
            assert faktorValue != null;
            tfopis = switch (faktorValue) {
                case "А" -> AValue;
                case "Б" -> BValue;
                case "АБ" -> AValue + ";\n"+ BValue;
                case "-" -> opisanieN.getText();
                default -> throw new IllegalStateException("Unexpected value: " + faktorValue);
            };

           // primhanie.setText("Все данные внесены");

            new DatabaseHandler().DobavlenieGEOMEX(faktorValue, tfopis,nomer.getText(),interval.getValue())    ;
            clearGEO();


        } catch (Exception e) {
           showAlert("Произошла ошибка: " + e.getMessage());
        }
    }

   private void setupImageHandlers() {
        openImageHandler(PlanVKL, "План",PlanVKLNe,nomer.getText());
        openImageHandler(PoperVKL, "Поперечный",PoperVKLNe,nomer.getText());
        openImageHandler(ProdolVKL, "Продольный",ProdolVKLNe,nomer.getText());
    }
     void proverkaImageGeolg(String imagePath,ImageView VKL,ImageView VKLNE) {
        File folder = new File(imagePath);
        // Проверяем, существует ли папка
        if (!folder.exists() || !folder.isDirectory()) {
            // System.err.println("Папка не найдена: " + imagePath);

            VKL.setVisible(false);VKLNE.setVisible(true);
            singUpButtun.setVisible(false);

            return;
        }
        // Получаем список файлов в папке
        File[] files = folder.listFiles((dir, name) -> {
            // Фильтруем файлы по расширению (изображения)
            String lowerCaseName = name.toLowerCase();
            return lowerCaseName.endsWith(".jpg") || lowerCaseName.endsWith(".png") ||
                    lowerCaseName.endsWith(".jpeg") || lowerCaseName.endsWith(".gif");
        });
        // Проверяем, есть ли изображения в папке
        if (files != null && files.length > 0) {
            VKL.setVisible(true);VKLNE.setVisible(false);

            singUpButtun.setVisible(true);

        } else {
            System.err.println("В папке нет изображений.");
            singUpButtun.setVisible(false);

            VKL.setVisible(false);VKLNE.setVisible(true);
        }
    }

    }
