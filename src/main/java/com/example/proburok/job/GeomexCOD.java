package com.example.proburok.job;

import com.example.proburok.New_Class.Baza;
import com.example.proburok.MQ.DatabaseHandler;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

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
    @FXML private TextArea tfopis;
    @FXML private ImageView TP;
    @FXML
    void initialize() {
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
        addTextChangeListener(nomer);
        addTextChangeListener(katigoria);addTextChangeListener(cehen);
        addTextChangeListener(nomer1);addTextChangeListener(privazka);
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
                        .collect(Collectors.toList())
        );
        clearFields();
        namebox.setItems(names);
    }
    private void updateUI(Baza data) {
        Platform.runLater(() -> {
            try {
                nomer.setText(data.getNOMER());

                katigoria.setText(data.getKATEGORII());
                opisanie.setText(data.getOPISANIE());
                cehen.setText(data.getSEHEN());
                privazka.setText(data.getPRIVIZKA());
                nomer1.setText(data.getTIPPAS());
                dlina.setText(data.getDLINA());
                primhanie.setText(data.getPRIM());
                idi.setText(data.getIDI());
                tfopis.setText(data.getFAKTOR());
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
    }

    private boolean validateRequiredFields() {
        return isFieldValid(nomer) &&
                isFieldValid(katigoria) &&
                isFieldValid(cehen) &&
                isFieldValid(nomer1) &&
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
        tfopis.clear();
        proverkaImageGeolg(Put + "/" + nomer.getText() + "/" + "План", PlanVKL,PlanVKLNe);
        proverkaImageGeolg(Put + "/" + nomer.getText() + "/" + "Поперечный",PoperVKL,PoperVKLNe);
        proverkaImageGeolg(Put + "/" + nomer.getText() + "/" + "Продольный",ProdolVKL,ProdolVKLNe);
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
        String geomexops = tfopis.getText();


        proverkaImageGeolg(Put + "/" + nomer.getText() + "/" + "План", PlanVKL,PlanVKLNe);
        proverkaImageGeolg(Put + "/" + nomer.getText() + "/" + "Поперечный",PoperVKL,PoperVKLNe);
        proverkaImageGeolg(Put + "/" + nomer.getText() + "/" + "Продольный",ProdolVKL,ProdolVKLNe);
        try {
            // Проверка полей по очереди
            StringBuilder errors = new StringBuilder();
            if (selectedGor == null || selectedGor.isEmpty()) {
                errors.append("- Не выбран горизонт\n");
            }
            if (selectedName == null || selectedName.isEmpty()) {
                errors.append("- Не выбрано название выработки\n");
            }
            if (selectPrivizka == null || selectPrivizka.isEmpty()) {
                errors.append("- Не заполнена привязка\n");
            }
            if (dlinaValue == null || dlinaValue.isEmpty()) {
                errors.append("- Не заполнена длина выработки\n");
            }

            if (kategoriyaValue == null || kategoriyaValue.isEmpty()) {
                errors.append("- Не заполнено поле категория \n");
            }
            if (opisanieValue == null || opisanieValue.isEmpty()) {
                errors.append("- Не заполнено поле геологическое описание\n");
            }
            if (geomexops == null || geomexops.isEmpty()) {
                errors.append("- Не заполнено поле описание\n");
            }

            // Если есть ошибки - показываем их
            if (errors.length() > 0) {
                showAlert("Заполните обязательные поля:\n" + errors.toString());
                return;
            }
            primhanie.setText("Все данные внесены");
            new DatabaseHandler().DobavlenieGEOMEX(geomexops, selectedGor, selectedName,primhanie.getText())    ;
            clearFields();


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
