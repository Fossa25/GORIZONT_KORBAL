package com.example.proburok.job;

import com.example.proburok.New_Class.Baza;
import com.example.proburok.MQ.DatabaseHandler;
import com.example.proburok.New_Class.TemplateResource;
import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.sl.usermodel.PictureData;
import org.apache.poi.util.IOUtils;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class PehatCOD extends Configs {

    private static final Logger log = LogManager.getLogger(PehatCOD.class);
    private String Photosxemtefat;
    private String Photovidkrepi;
    private String Photoilement;
    private String Photosxemprovet;
    private String Photovidkrepi2;
    private String ankerPM;
    private String ankerR;
    private String setkaPM;
    private String setkaR;
    private String torkretPM;
    private String torkretR;
    private String ampulaPM;
    private String ampulaR;
    private String smesPM;
    private String smesR;
    private String tolhina;
    private String ankerPM1;
    private String ankerR1;
    private String ankerPM2;
    private String ankerR2;
    List<String> soprigenii = Arrays.asList("11","12","13","14","15");

    List<String> ov2 = Arrays.asList(  "1", "5","11","17","21","27","30","33","36","39");

    List<String> zabutovka = Arrays.asList( "4","8","10","14","16","20","24","26");


    private static final Map<String, String> PLACEHOLDER_MAP;

    static {
        PLACEHOLDER_MAP = new HashMap<>();
        PLACEHOLDER_MAP.put("${nomer}", "nomer");
        PLACEHOLDER_MAP.put("${name}", "name");
        PLACEHOLDER_MAP.put("${sehen}", "sehen");
        PLACEHOLDER_MAP.put("${gorizont}", "gorizont");
        PLACEHOLDER_MAP.put("${opisanie}", "opisanie");
        PLACEHOLDER_MAP.put("${kategorii}", "kategorii");
        PLACEHOLDER_MAP.put("${privazka}", "privazka");
        PLACEHOLDER_MAP.put("${plan}", "plan");
        PLACEHOLDER_MAP.put("${poper}", "poper");
        PLACEHOLDER_MAP.put("${prodol}", "prodol");
        PLACEHOLDER_MAP.put("${dlina}", "dlina");
        PLACEHOLDER_MAP.put("${tipovoi}", "tipovoi");
        PLACEHOLDER_MAP.put("${sxematexfakt}", "sxematexfakt");
        PLACEHOLDER_MAP.put("${obvid}", "obvid");
        PLACEHOLDER_MAP.put("${konstrk}", "konstrk");
        PLACEHOLDER_MAP.put( "${ukazanie}", "tfops");
        PLACEHOLDER_MAP.put("${obvid2}", "obvid2");
        PLACEHOLDER_MAP.put("${sxema}", "sxema");
    }

    @FXML private ImageView instr;
    @FXML private TextField cehen;
    @FXML private TextField bdname;
    @FXML private ComboBox<String> gorbox;
    @FXML private TextField katigoria;
    @FXML private ComboBox<String> namebox;
    @FXML private TextField nomer;
    @FXML private TextField nomer1;
    @FXML private TextArea opisanie;
    @FXML private Button singUpButtun;
    @FXML private TextField privazka;
    @FXML private TextField idi;

    @FXML private TextField ush;

    @FXML private TextField dlina;
    private final DatabaseHandler dbHandler = new DatabaseHandler();
    @FXML private TextArea primhanie;
    @FXML private CheckBox cb;
    @FXML private Button singUpButtun1;
    @FXML private ImageView sxemaVKL;
    @FXML private ImageView sxemaVKLNe;
    @FXML private ImageView sxemaVNS;
    @FXML private ImageView sxemaVNSNE;
    @FXML private ImageView sxemaobnov;
    @FXML private ImageView PlanVKL;
    @FXML private ImageView PlanVKLNe;

    @FXML private ImageView PoperVKL;
    @FXML private ImageView PoperVKLNe;

    @FXML private ImageView ProdolVKL;
    @FXML private ImageView ProdolVKLNe;
    @FXML private TextArea tfopis;
    @FXML
    void initialize() {
        primhanie.visibleProperty().bind(cb.selectedProperty());
        singUpButtun1.visibleProperty().bind(cb.selectedProperty());
        idi.setVisible(false);
        bdname.setVisible(false);
        ush.setVisible(false);
        singUpButtun1.setOnMouseClicked(mouseEvent -> {
            String selectedGor = gorbox.getValue();
            String selectedName = namebox.getValue();
            try {
                // Проверка полей по очереди
                StringBuilder errors = new StringBuilder();
                if (selectedGor == null || selectedGor.isEmpty()) {
                    errors.append("- Не выбран горизонт\n");
                }
                if (selectedName == null || selectedName.isEmpty()) {
                    errors.append("- Не выбрано название выработки\n");
                }
                // Если есть ошибки - показываем их
                if (errors.length() > 0) {
                    showAlert("Заполните обязательные поля:\n" + errors.toString());
                    return;
                }
                new DatabaseHandler().DobavleniePRIM(primhanie.getText(), selectedGor, selectedName);
                clearFields();
                //gorbox.setValue(null);
            } catch (Exception e) {
                showAlert("Произошла ошибка: " + e.getMessage());
            }
        });
        setupComboBoxes();
        setupButtonAction();
        setupCursor(sxemaVNS,sxemaVKL,sxemaVNSNE,sxemaVKLNe,sxemaobnov);
        setupCursor_2(PlanVKL,PlanVKLNe);
        setupCursor_2(PoperVKL,PoperVKLNe);
        setupCursor_2(ProdolVKL,ProdolVKLNe);
        setupTooltips(sxemaVNS,sxemaVKL,sxemaVKLNe,sxemaobnov,
                "Внести изображение схемы вентиляции", "Показать схему вентиляции",
                "Схема вентиляции не внесена","Обновить изображение схемы вентиляции");

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
        addTextChangeListener(nomer);addTextChangeListener(bdname);
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
                bdname.setText(data.getNAME_BD());
                katigoria.setText(data.getKATEGORII());
                opisanie.setText(data.getOPISANIE());
                cehen.setText(data.getSEHEN());
                privazka.setText(data.getPRIVIZKA());
                nomer1.setText(data.getTIPPAS());
                dlina.setText(data.getDLINA());
                primhanie.setText(data.getPRIM());
                idi.setText(data.getIDI());
                ush.setText(data.getUHASTOK());
                tfopis.setText(data.getFAKTOR());
                setupImageHandlers();
                // Обновляем изображения
                proverkaImageGeolg(Put + "/" + nomer.getText() + "/" + "План", PlanVKL, PlanVKLNe);
                proverkaImageGeolg(Put + "/" + nomer.getText() + "/" + "Поперечный", PoperVKL, PoperVKLNe);
                proverkaImageGeolg(Put + "/" + nomer.getText() + "/" + "Продольный", ProdolVKL, ProdolVKLNe);
                proverkaImage(Put + "/" + nomer.getText() + "/" + "Схема", sxemaVNS, sxemaVNSNE, sxemaVKL, sxemaVKLNe, sxemaobnov);

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
                isFieldValid(bdname) &&
                isFieldValid(katigoria) &&
                isFieldValid(cehen) &&
                isFieldValid(nomer1) &&
                isFieldValid(dlina) &&
                isFieldValid(ush) &&
                isAraidValid(tfopis) &&
                isFieldValid(privazka)&&
                isPrimValid(primhanie.getText());
    }
    private boolean isFieldValid(TextField field) {
        return field != null && field.getText() != null && !field.getText().trim().isEmpty();
    }
    private boolean isAraidValid(TextArea field) {
        return field != null && field.getText() != null && !field.getText().trim().isEmpty();
    }
    private boolean isPrimValid(String tx) {
        if (tx == null) return true;

        return switch(tx.trim()) {
            case "Требуется геомеханическое обоснование (указание)",
                 "Требуется геологическое описание",
                 "Необходима привязка выработки" -> false;
            default -> true;
        };
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
        bdname.clear();
        katigoria.clear();
        opisanie.setText("");
        cehen.clear();
        nomer1.clear();
        privazka.clear();
        dlina.clear();
        primhanie.clear();
        cb.setSelected(false);
        idi.clear();
        ush.clear();
        tfopis.clear();
        this.Photosxemtefat = "";
        this.Photovidkrepi = "";
        this.Photoilement = "";
        this.Photosxemprovet = "";
        this.Photovidkrepi2 = "";
        this.ankerPM = "";
        this.ankerR = "";
        this.setkaPM = "";
        this.setkaR = "";
        this.torkretPM = "";
        this.torkretR = "";
        this.ampulaPM = "";
        this.ampulaR = "";
        this.smesPM = "";
        this.smesR = "";
        this.tolhina = "";
        this.ankerPM1 = "";
        this.ankerR1 = "";
        this.ankerPM2 = "";
        this.ankerR2 = "";
        proverkaImage(Put + "/" + nomer.getText() + "/" + "Схема", sxemaVNS, sxemaVNSNE, sxemaVKL, sxemaVKLNe, sxemaobnov);
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

        proverkaImage(Put + "/" + nomer.getText() + "/" + "Схема", sxemaVNS, sxemaVNSNE, sxemaVKL, sxemaVKLNe, sxemaobnov);
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
                errors.append("- Не заполнено поле описание\n");
            }

            if (sxemaVNS.isVisible() || sxemaVNSNE.isVisible()) {
                errors.append("- Не внесена схема вентиляции\n");
            }
            if (geomexops == null || geomexops.isEmpty()) {
                errors.append("- Не заполнено поле геомеханическое обоснование \n");
            }

            // Если есть ошибки - показываем их
            if (errors.length() > 0) {
                showAlert("Заполните обязательные поля:\n" + errors.toString());
                return;
            }
            String vidkripi = nomer1.getText() + ".jpg";

            if (soprigenii.contains(idi.getText())) {
                this.Photovidkrepi = getRESURS(HABLON_PATH_SOPR, vidkripi);
                this.Photosxemtefat = getUstanovka_SOPR(nomer1.getText());
                this.Photoilement = getIlement_SOPR(nomer1.getText());
                this.Photosxemprovet = getPoto(Put + "/" + nomer.getText() + "/" + "Схема", 0);

            } else {
                this.Photovidkrepi = getRESURS(HABLON_PATH_VID, vidkripi);
              if  (ov2.contains(nomer1.getText())) {
                    int ps2 = Integer.parseInt(nomer1.getText())+1;
                  String vidkripi2 = ps2 + ".jpg";
                  this.Photovidkrepi2 = getRESURS(HABLON_PATH_VID, vidkripi2);
              }
                this.Photosxemtefat = getUstanovka(nomer1.getText());
                this.Photoilement = getIlement(nomer1.getText());
                this.Photosxemprovet = getPoto(Put + "/" + nomer.getText() + "/" + "Схема", 0);
            }

            try {
                if (validateInput()) {
                    generateWordDocument(
                            nomer.getText(),
                            bdname.getText(),
                            gorbox.getValue(),
                            katigoria.getText(),
                            opisanie.getText(),
                            privazka.getText(),
                            getPoto(Put + "/" + nomer.getText() + "/" + "План", 0),
                            getPoto(Put + "/" + nomer.getText() + "/" + "Поперечный", 0),
                            getPoto(Put + "/" + nomer.getText() + "/" + "Продольный", 0),
                            dlina.getText(),
                            nomer1.getText(),
                            this.Photosxemtefat,
                            this.Photovidkrepi,
                            this.Photoilement,
                            tfopis.getText(),
                            this.Photovidkrepi2,
                            this.Photosxemprovet
                    );
                }
            } catch (IOException | InvalidFormatException e) {
                handleError(e);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
           showAlert("Произошла ошибка: " + e.getMessage());
        }
    }

    private boolean validateInput() {
        if (gorbox.getValue() == null || namebox.getValue() == null) {
            showAlert( "Выберите горизонт и название!");
            return false;
        }
        return true;
    }

    private void rashet(String list) throws ParseException {
        // Проверка входных данных
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("Значение list не может быть пустым");
        }

        if (dlina.getText() == null || dlina.getText().trim().isEmpty()) {
            throw new ParseException("Значение длины не может быть пустым", 0);
        }

        switch (list) {
            case "4" -> setTabl(1.33, 14.8, 3.7);
            case "20" -> setTabl(1.33, 13.7, 3.4);
            case "8", "10", "24","26" -> setTabl(1.33, 0.0, 1.6);
            case "14","16"  -> setTabl(1.0, 0.0, 1.4);


            case "1" -> setTabl(8.0, 7.4, 9.0);
            case "5","17" -> setTabl(7.0, 6.4, 8.0);
            case "11" -> setTabl(6.0, 5.4, 7.0);
            case "21" -> setTabl(7.0, 6.4, 7.0);
            case "27","33","39" -> setTabl(10.0, 9.4, 11.0);
            case "30" -> setTabl(8.0, 7.4, 8.0);
            case "36" -> setTabl(9.0, 8.4, 10.0);

            case "9" -> setTabl(13.8, 12.3, 0.0);
            case "15","25" -> setTabl(13.8, 8.4, 0.0);

            case "7" -> setTabl(18.6, 8.8, 0.66);
            case "13" -> setTabl(18.6, 8.8, 0.61);
            case "23" -> setTabl(18.6, 8.8, 0.65);
            case "32" -> setTabl(18.6, 8.8, 0.69);
            case "19" -> setTabl(22.9, 10.9, 0.75);
            case "35" -> setTabl(22.9, 10.9, 0.86);
            case "3" -> setTabl(24.3, 11.6, 0.81);
            case "38" -> setTabl(25.7, 12.3, 0.88);
            case "29" -> setTabl(27.1, 13.0, 0.94);
            case "41" -> setTabl(32.9, 15.8, 1.00);
            default -> throw new IllegalStateException("Unexpected value: " + list);
        }

        // Обработка ввода с заменой запятых на точки
        String input = dlina.getText().trim().replace(',', '.');
        if (zabutovka.contains(nomer1.getText())) {

            try {
                double dlina_Dobl = Double.parseDouble(input);
                double ankerPM_Dobl = Double.parseDouble(this.ankerPM);
                double setkaPM_Dobl = Double.parseDouble(this.setkaPM);
                double torkretPM_Dobl = Double.parseDouble(this.torkretPM);



                double ankerR_Doble = dlina_Dobl * ankerPM_Dobl;
                double setkaR_Doble = dlina_Dobl * setkaPM_Dobl;
                double torkretR_Doble = dlina_Dobl * torkretPM_Dobl;

                double ankerPM_Dobl1 = ankerPM_Dobl*2;
                double ankerR_Dobl1=  Math.ceil(ankerR_Doble) *2;

                double ankerPM_Dobl2 = ankerPM_Dobl*3;
                double ankerR_Dobl2=  Math.ceil(ankerR_Doble) *3;

                double ampulaPM_Dobl = ankerPM_Dobl * 5;
                double ampulaR_Doble = dlina_Dobl * Math.ceil(ampulaPM_Dobl);

                this.ankerR = String.format(Locale.US, "%.0f", Math.ceil(ankerR_Doble));
                this.setkaR = String.format(Locale.US, "%.1f", setkaR_Doble);
                this.torkretR = String.format(Locale.US, "%.1f", torkretR_Doble);
                this.ankerPM1 = String.format(Locale.US, "%.0f", Math.ceil( ankerPM_Dobl1));
                this.ankerR1 = String.format(Locale.US, "%.0f", Math.ceil(ankerR_Dobl1));
                this.ankerPM2 = String.format(Locale.US, "%.0f", Math.ceil( ankerPM_Dobl2));
                this.ankerR2 = String.format(Locale.US, "%.0f", ankerR_Dobl2);

                this.ampulaPM = String.format(Locale.US, "%.0f", ampulaPM_Dobl);
                this.ampulaR = String.format(Locale.US, "%.0f", Math.ceil(ampulaR_Doble));

                System.out.println(ankerPM_Dobl+ " "+this.ampulaPM + "  "+ this.ampulaR );

            } catch (NumberFormatException e) {
                throw new ParseException("Некорректный числовой формат", 0);
            }
        }else{

        try {
            double dlina_Dobl = Double.parseDouble(input);
            double ankerPM_Dobl = Double.parseDouble(this.ankerPM);
            double setkaPM_Dobl = Double.parseDouble(this.setkaPM);
            double torkretPM_Dobl = Double.parseDouble(this.torkretPM);
            double ampulaPM_Dobl = ankerPM_Dobl * 5;
            double ankerR_Doble = dlina_Dobl * ankerPM_Dobl;
            double setkaR_Doble = dlina_Dobl * setkaPM_Dobl;
            double torkretR_Doble = dlina_Dobl * torkretPM_Dobl;
            double ampulaR_Doble = dlina_Dobl * ampulaPM_Dobl;

            if (ov2.contains(nomer1.getText())) {
                this.torkretR = String.format(Locale.US, "%.0f", Math.ceil( torkretR_Doble));
            }else{  this.torkretR = String.format(Locale.US, "%.1f", torkretR_Doble);}

            this.ankerR = String.format(Locale.US, "%.0f", Math.ceil(ankerR_Doble));
            this.setkaR = String.format(Locale.US, "%.1f", setkaR_Doble);

            this.ampulaPM = String.format(Locale.US, "%.0f", ampulaPM_Dobl);
            this.ampulaR = String.format(Locale.US, "%.0f", Math.ceil(ampulaR_Doble));
            System.out.println(this.ampulaPM + "  "+ this.ampulaR );

        } catch (NumberFormatException e) {
            throw new ParseException("Некорректный числовой формат", 0);
        }}
    }
    private void setTabl(Double anker, Double setka, Double nabrizk) {
        this.ankerR = "";this.setkaR = "";
        this.torkretR = "";this.ampulaPM = "";
        this.ampulaR = "";
        this.ankerPM = String.valueOf(anker);
        this.setkaPM = String.valueOf(setka);
        this.torkretPM = String.valueOf(nabrizk);
    }

    private void setTabl2PAS(Double rama, Double stoki, Double verx,Double svaz, Double zamok, Double rask,Double zab) {
        this.ankerPM = String.valueOf(rama);
        this.setkaPM = String.valueOf(stoki);
        this.torkretPM = String.valueOf(verx);

        this.ankerR = String.valueOf(svaz);
        this.setkaR = String.valueOf(zamok);
        this.torkretR = String.valueOf(rask);
        this.ankerPM1 = String.valueOf(zab);
    }

    private void rashet_SOPR(String list) throws ParseException {
        // Проверка входных данных
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("Значение list не может быть пустым");
        }

        switch (list) {
            case "1" -> setTabl(256.0, 207.8, 0.0);
            case "11" -> setTabl(270.0, 188.1, 0.0);
            case "3" -> setTabl(316.0, 152.9, 10.6);
            case "5" -> setTabl(316.0, 152.9, 0.0);
            case "7" -> setTabl(367.0, 196.0, 12.5);
            case "9" -> setTabl(367.0, 196.0, 0.0);
            case "13" -> setTabl(369.0, 279.2, 0.0);
            case "12" -> setTabl(390.0, 243.8, 14.9);
            case "2" -> setTabl(413.0, 247.3, 15.8);
            case "14" -> setTabl(617.0, 348.5, 23.4);
            case "4","6" -> setTabl2PAS(24.0,61.0,37.0,105.0,74.0,190.0,34.7);
            case "8","10" -> setTabl2PAS(26.0,56.0,43.0,145.0,86.0,225.0,37.5);
            default -> throw new IllegalStateException("Unexpected value: " + list);
        }
        try {
            double ankerPM_Dobl = Double.parseDouble(this.ankerPM);
            double ampulaPM_Dobl = ankerPM_Dobl * 5;

            this.ampulaPM = String.format(Locale.US, "%.0f", ampulaPM_Dobl);

        } catch (NumberFormatException e) {
            throw new ParseException("Некорректный числовой формат", 0);
        }
    }


    private void generateWordDocument(String... params) throws IOException, InvalidFormatException, ParseException {

        if (soprigenii.contains(idi.getText())) {
            rashet_SOPR(nomer1.getText());

        } else {
            rashet(nomer1.getText());

        }
        Map<String, String> tableData = new HashMap<>();
        tableData.put("${table.ankPM}", this.ankerPM);
        tableData.put("${table.ankR}", this.ankerR);
        tableData.put("${table.stkPM}", this.setkaPM);
        tableData.put("${table.stkR}", this.setkaR);
        tableData.put("${table.torPM}", this.torkretPM);
        tableData.put("${table.torR}", this.torkretR);
        tableData.put("${table.ampulaPM}", this.ampulaPM);
        tableData.put("${table.ampulaR}", this.ampulaR);
        tableData.put("${table.smesPM}", this.smesPM);
        tableData.put("${table.smesR}", this.smesR);
        tableData.put("${table.tolhina}", this.tolhina);
        tableData.put("${name}", bdname.getText());
        tableData.put("${sehen}", cehen.getText());
        tableData.put("${ush}", ush.getText());
        tableData.put("${table.ankPM1}", this.ankerPM1);
        tableData.put("${table.ankR1}", this.ankerR1);
        tableData.put("${table.ankPM2}", this.ankerPM2);
        tableData.put("${table.ankR2}", this.ankerR2);
        System.out.println(this.ampulaPM + "  "+ this.ampulaR );
        String outputFileName = OUTPUT_PATH + nomer.getText() + "_" + gorbox.getValue() + ".docx";
        File outputFile = new File(outputFileName);
        try {
            TemplateResource templateResource;
            // Получаем поток входных данных для ресурса
            //InputStream inputStream = getClass().getResourceAsStream(TEMPLATE_PATH);
            if (soprigenii.contains(idi.getText())) {
                templateResource = getDokHablon_SOPR(nomer1.getText());
            } else {
                templateResource = getDokHablon(nomer1.getText());
            }

            InputStream inputStream = templateResource.getInputStream();
            String templatePath = templateResource.getTemplatePath();
            System.err.println("Берем шаблон: " + templatePath);
            if (inputStream == null) {
                throw new FileNotFoundException("Ресурс не найден: " + templatePath);
            }
            // Создаем временный файл
            Path tempFile = Files.createTempFile("hablon_", ".docx");
            Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);

            try (FileInputStream template = new FileInputStream(tempFile.toFile());
                 XWPFDocument document = new XWPFDocument(template)) {

                replacePlaceholders(document, params);

                replaceTablePlaceholders(document, tableData);

                try (FileOutputStream out = new FileOutputStream(outputFile)) {
                    document.write(out);
                }
                // Автоматическое открытие файла после сохранения
                if (Desktop.isDesktopSupported()) {
                    Desktop desktop = Desktop.getDesktop();
                    if (outputFile.exists()) {
                        desktop.open(outputFile);
                    } else {
                        System.err.println("Файл не найден: " + outputFile.getAbsolutePath());
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Ошибка генерации документа: " + e.getMessage());
            }
            // Опционально: удаление временного файла после использования
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    Files.deleteIfExists(tempFile);
                } catch (IOException e) {
                    System.err.println("Не удалось удалить временный файл: " + tempFile);
                }
            }));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибка при открытии документа", e);
        }
        String prim = "Паспорт создан";
        new DatabaseHandler().DobavleniePRIM(prim, gorbox.getValue(), namebox.getValue());
        clearFields();
    }
    private void replaceTablePlaceholders(XWPFDocument doc, Map<String, String> tableData) {
        // Перебираем все таблицы в документе
        for (XWPFTable table : doc.getTables()) {
            // Перебираем все строки в таблице
            for (XWPFTableRow row : table.getRows()) {
                // Перебираем все ячейки в строке
                for (XWPFTableCell cell : row.getTableCells()) {
                    // Перебираем все абзацы в ячейке
                    for (XWPFParagraph paragraph : cell.getParagraphs()) {
                        // Перебираем все плейсхолдеры
                        for (Map.Entry<String, String> entry : tableData.entrySet()) {
                            String placeholder = entry.getKey();
                            String value = entry.getValue();

                            // Если абзац содержит плейсхолдер - заменяем его
                            if (paragraph.getText().contains(placeholder)) {
                                replaceTextInParagraph(paragraph, placeholder, value);
                            }
                        }
                    }
                }
            }
        }
    }

    private void replacePlaceholders(XWPFDocument doc, String[] values) {
        // Списки плейсхолдеров для разных типов замен
        Set<String> textPlaceholders = Set.of(
                "${nomer}", "${name}", "${gorizont}",
                "${opisanie}", "${kategorii}", "${privazka}", "${tipovoi}", "${dlina}","${ukazanie}");
        Set<String> imagePlaceholders = Set.of("${plan}");
        Set<String> imagePlaceholders_2 = Set.of( "${poper}", "${prodol}");
        Set<String> imageSXPlaceholders = Set.of("${sxema}");

        Set<String> VidhablonPlaceholders = Set.of("${obvid}","${obvid2}");
        Set<String> SxmHablonPlaceholders = Set.of("${sxematexfakt}", "${konstrk}");
        // Обработка текстовых плейсхолдеров
        textPlaceholders.forEach(placeholder -> {
            String fieldName = PLACEHOLDER_MAP.get(placeholder);
            for (XWPFParagraph p : doc.getParagraphs()) {
                if (p.getText().contains(placeholder)) {
                    String value = getValueByFieldName(fieldName, values);
                    System.out.println("[ТЕКСТ] Замена " + placeholder + " → " + value);
                    replaceTextInParagraph(p, placeholder, value);
                }
            }
        });
        // Обработка плейсхолдеров изображений
        imagePlaceholders.forEach(placeholder -> {
            String fieldName = PLACEHOLDER_MAP.get(placeholder);
            for (XWPFParagraph p : doc.getParagraphs()) {
                if (p.getText().contains(placeholder)) {
                    String imagePath = getValueByFieldName(fieldName, values);
                    //System.out.println("[ИЗОБРАЖЕНИЕ] Замена " + placeholder + " → " + imagePath);
                    replaceImageInParagraph(p, placeholder, imagePath, 470, 350, false);
                }
            }
        });
        imagePlaceholders_2.forEach(placeholder -> {
            String fieldName = PLACEHOLDER_MAP.get(placeholder);
            for (XWPFParagraph p : doc.getParagraphs()) {
                if (p.getText().contains(placeholder)) {
                    String imagePath = getValueByFieldName(fieldName, values);
                    //System.out.println("[ИЗОБРАЖЕНИЕ] Замена " + placeholder + " → " + imagePath);
                    replaceImageInParagraph(p, placeholder, imagePath, 470, 308, false);
                }
            }
        });
        imageSXPlaceholders.forEach(placeholder -> {
            String fieldName = PLACEHOLDER_MAP.get(placeholder);
            for (XWPFParagraph p : doc.getParagraphs()) {
                if (p.getText().contains(placeholder)) {
                    String imagePath = getValueByFieldName(fieldName, values);
                    //System.out.println("[ИЗОБРАЖЕНИЕ] Замена " + placeholder + " → " + imagePath);
                    replaceImageInParagraph(p, placeholder, imagePath, 700, 368, false);
                }
            }
        });

        VidhablonPlaceholders.forEach(placeholder -> {
            String fieldName = PLACEHOLDER_MAP.get(placeholder);
            for (XWPFParagraph p : doc.getParagraphs()) {
                if (p.getText().contains(placeholder)) {
                    String imagePath = getValueByFieldName(fieldName, values);
                    System.out.println("[Vid] Замена " + placeholder + " → " + imagePath);
                    replaceImageInParagraph(p, placeholder, imagePath, 700, 420, true);
                }
            }
        });
        SxmHablonPlaceholders.forEach(placeholder -> {
            String fieldName = PLACEHOLDER_MAP.get(placeholder);
            for (XWPFParagraph p : doc.getParagraphs()) {
                if (p.getText().contains(placeholder)) {
                    String imagePath = getValueByFieldName(fieldName, values);
                    System.out.println("[Sxm] Замена " + placeholder + " → " + imagePath);
                    //replaceTextInParagraph(p, placeholder, "");
                    replaceImageInParagraph(p, placeholder, imagePath, 470, 700, true);
                }
            }
        });
    }

    private String getValueByFieldName(String name, String[] values) {
        return switch (name) { //зависимости от ключа возвращает значение из массива который вводим
            case "nomer" -> values[0];
            case "name" -> values[1];
            case "gorizont" -> values[2];
            case "kategorii" -> values[3];
            case "opisanie" -> values[4];
            case "privazka" -> values[5];

            case "plan" -> values[6];
            case "poper" -> values[7];
            case "prodol" -> values[8];

            case "dlina" -> values[9];
            case "tipovoi" -> values[10];

            case "sxematexfakt" -> values[11];
            case "obvid" -> values[12];
            case "konstrk" -> values[13];
            case "tfops" -> values[14];
            case "obvid2" -> values[15];
            case "sxema" -> values[16];
            default -> "";
        };
    }

    private void replaceTextInParagraph(XWPFParagraph p, String placeholder, String replacement) {
        String text = p.getText(); // получаем весь текст параметра
        p.getRuns().forEach(r -> r.setText("", 0)); //удаляем существующий из всех ронов и заменяем полностью
        XWPFRun newRun = p.createRun(); // создаем новый для нового текста
        String safeReplacement = replacement != null ? replacement : "";
        newRun.setText(text.replace(placeholder, safeReplacement));// заменяем "${nomer}", "123",
    }

    private void replaceImageInParagraph(XWPFParagraph p, String placeholder, String imagePath, int width, int height, boolean isResource) {
        replaceTextInParagraph(p, placeholder, "");

        if (imagePath == null || imagePath.isEmpty()) {
            System.err.println("Путь к изображению пуст");
            return;
        }
        try (InputStream is = isResource
                ? getClass().getResourceAsStream(imagePath.startsWith("/") ? imagePath : "/" + imagePath)
                : new FileInputStream(imagePath)) {
            if (is == null) {
                System.err.println("Источник не найден: " + imagePath);
                return;
            }
            byte[] imageBytes = IOUtils.toByteArray(is);
            PictureData.PictureType type = isResource
                    ? determineImageType(imageBytes)
                    : getImageType(imagePath);

            XWPFRun run = p.createRun();
            run.addPicture(
                    new ByteArrayInputStream(imageBytes),
                    type.ordinal(),
                    "image",
                    Units.toEMU(width),
                    Units.toEMU(height)
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private PictureData.PictureType determineImageType(byte[] imageData) {
        if (imageData.length >= 4 &&
                imageData[0] == (byte) 0xFF && imageData[1] == (byte) 0xD8) {
            return PictureData.PictureType.JPEG;
        } else if (imageData.length >= 8 &&
                new String(imageData, 0, 8).equals("\211PNG\r\n\032\n")) {
            return PictureData.PictureType.PNG;
        }
        return PictureData.PictureType.JPEG; // fallback
    }

    // Определение типа изображения
    private PictureData.PictureType getImageType(String imagePath) {
        String ext = imagePath.substring(imagePath.lastIndexOf(".") + 1).toLowerCase();
        return switch (ext) {
            case "jpg", "jpeg" -> PictureData.PictureType.JPEG;
            case "png" -> PictureData.PictureType.PNG;
            case "gif" -> PictureData.PictureType.GIF;
            case "emf" -> PictureData.PictureType.EMF;
            case "wmf" -> PictureData.PictureType.WMF;
            default -> throw new IllegalArgumentException("Unsupported image type: " + ext);
        };
    }
    // Сформируйте полный путь к ресурсу
    public String getRESURS(String resourcePath, String fileName) {
        // Возвращаем путь в формате "/папка/файл"
        if (fileName == null) return "";
        return (resourcePath.startsWith("/") ? "" : "/") +
                resourcePath.replace("\\", "/") +
                "/" + fileName;
    }
    public String getUstanovka(String list) {

        return switch (list) {
            case "1","5", "11", "17", "21", "27", "30","33","36","39" ->
                    getRESURS(HABLON_PATH_USTANOVKA, "1.jpg");
            case "9", "15", "25"-> getRESURS(HABLON_PATH_USTANOVKA, "2.jpg");
            case "3",  "7", "13", "19", "23", "29", "32", "35","38","41" ->
                    getRESURS(HABLON_PATH_USTANOVKA, "3.jpg");

            case "4", "8","14","20","24" -> getRESURS(HABLON_PATH_USTANOVKA, "4.jpg");
            case "10", "26" -> getRESURS(HABLON_PATH_USTANOVKA, "5.jpg");
            case "16" -> getRESURS(HABLON_PATH_USTANOVKA, "6.jpg");

            default -> "";
        };
    }
    public String getUstanovka_SOPR(String list) {
        return switch (list) {
            case "4","6","8","10" -> getRESURS(HABLON_PATH_USTANOVKA, "7.jpg");
            case "1", "11", "13"-> getRESURS(HABLON_PATH_USTANOVKA, "8.jpg");
            case "2", "3",  "7", "12", "14" ->
                    getRESURS(HABLON_PATH_USTANOVKA, "9.jpg");
            case "5", "9" -> getRESURS(HABLON_PATH_USTANOVKA, "10.jpg");

            default -> "";
        };
    }

    public String getIlement(String list) {

        return switch (list) {

            case "1", "3", "5", "7", "9", "11", "13",  "15", "17", "19", "21", "23", "25",
                 "27", "29", "30", "32","33","35","36","38","39","41" -> getRESURS(HABLON_PATH_ILIMENT, "1.jpg");

            case "4", "8", "14","20","24" -> getRESURS(HABLON_PATH_ILIMENT, "2.jpg");
            case "10", "16", "26" -> getRESURS(HABLON_PATH_ILIMENT, "3.jpg");


            default -> "";
        };
    }
    public String getIlement_SOPR(String list) {

        return switch (list) {
            case "4","6","8","10" -> getRESURS(HABLON_PATH_ILIMENT, "3.jpg");
            case "1","2","3","5","7","9","11","12","13","14" -> getRESURS(HABLON_PATH_ILIMENT, "4.jpg");
            default -> "";
        };
    }

    public TemplateResource getDokHablon(String list) {
        return switch (list) {
            case "9","15","25" -> new TemplateResource(getClass().getResourceAsStream(TEMPLATE_PATH1), TEMPLATE_PATH1);

            case "3", "7", "13", "19", "23", "29", "32", "35", "38", "41" ->
                    new TemplateResource(getClass().getResourceAsStream(TEMPLATE_PATH2), TEMPLATE_PATH2);
            case "4",  "20" -> new TemplateResource(getClass().getResourceAsStream(TEMPLATE_PATH3), TEMPLATE_PATH3);
            case  "8", "10", "14", "16", "24","26" ->
                    new TemplateResource(getClass().getResourceAsStream(TEMPLATE_PATH4), TEMPLATE_PATH4);

            case "1", "5","11","17","21","27","30","33","36","39" -> new TemplateResource(getClass().getResourceAsStream(TEMPLATE_PATH8), TEMPLATE_PATH8);
            default -> null;
        };
    }

    public TemplateResource getDokHablon_SOPR(String list) {
        return switch (list) {
            case "1","5","9","11","13" -> new TemplateResource(getClass().getResourceAsStream(TEMPLATE_PATH5), TEMPLATE_PATH5);

            case  "2","3","7","12","14"  -> new TemplateResource(getClass().getResourceAsStream(TEMPLATE_PATH6), TEMPLATE_PATH6);
            case "4","6","8","10" -> new TemplateResource(getClass().getResourceAsStream(TEMPLATE_PATH7), TEMPLATE_PATH7);
            default ->null;
        };
    }

    String getPoto(String imagePath, int i) {
        if (imagePath == null || imagePath.isEmpty()) return "";
        try {
            File folder = new File(imagePath);

            // Проверяем, существует ли папка
            if (!folder.exists() || !folder.isDirectory()) {
                System.err.println("Папка не найдена: " + imagePath);
                return "";
            }

            // Получаем список файлов в папке
            File[] files = folder.listFiles((dir, name) -> {
                // Фильтруем файлы по расширению (изображения)
                String lowerCaseName = name.toLowerCase();
                return lowerCaseName.endsWith(".jpg") || lowerCaseName.endsWith(".png") ||
                        lowerCaseName.endsWith(".jpeg") || lowerCaseName.endsWith(".gif");
            });

            // Проверяем, есть ли изображения в папке
            if (files == null || files.length == 0) {
                System.err.println("Нет изображений в: " + imagePath);
                return "";
            }

            return files[i].getAbsolutePath();
        } catch (SecurityException e) {
            showAlert( "Нет доступа к папке: " + imagePath);
            return "";
        }
    }
   private void setupImageHandlers() {

        openImageHandler(PlanVKL, "План",PlanVKLNe,nomer.getText());
        openImageHandler(PoperVKL, "Поперечный",PoperVKLNe,nomer.getText());
        openImageHandler(ProdolVKL, "Продольный",ProdolVKLNe,nomer.getText());

         openImageHandler(sxemaVKL, "Схема", sxemaVKLNe,nomer.getText());
        obnovaImageHandler(sxemaobnov, "Схема", nomer.getText());
        sozdaniiImageHandler(sxemaVNS, "Схема", sxemaVKL, sxemaVKLNe, sxemaVNS, sxemaobnov, sxemaobnov,nomer.getText());


    }



    private void handleError(Exception e) {
        e.printStackTrace();
        showAlert( "Произошла ошибка: " + e.getMessage());
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
