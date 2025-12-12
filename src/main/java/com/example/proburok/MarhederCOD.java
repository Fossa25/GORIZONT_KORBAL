package com.example.proburok;

import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


public class MarhederCOD extends Configs{


    @FXML private TextField nomer;
    @FXML private TextField idi;

    @FXML private ImageView instr;
    @FXML private ImageView dokumGeolog;
    @FXML private ImageView dokumGeolog11;

    @FXML private ImageView tabl;
    @FXML private ImageView othet;
    @FXML private ImageView PlanVKL;
    @FXML private ImageView PlanVKLNe;
    @FXML private ImageView PoperVKL;
    @FXML private ImageView PoperVKLNe;
    @FXML private ImageView ProdolVKL;
    @FXML private ImageView ProdolVKLNe;
    @FXML private ImageView planVNS;
    @FXML private ImageView planVNSNE;
    @FXML private ImageView planobnov;
    @FXML private ImageView poperVNS;
    @FXML private ImageView poperVNSNE;
    @FXML private ImageView poperobnov;
    @FXML private ImageView prodolVNS;
    @FXML private ImageView prodolVNSNE;
    @FXML private ImageView prodolobnov;
    @FXML private ComboBox<String> gorbox;
    @FXML private ComboBox<String> namebox;
    @FXML private TextField privazka;
    @FXML private Button singUpButtun;
    @FXML
    void initialize() {
        setupTooltips(planVNS,PlanVKL,PlanVKLNe,planobnov,
                "Внести изображение плана","Показать план",
                "План не внесён","Обновить изображение плана"); // Добавляем подсказки
        setupTooltips(poperVNS,PoperVKL,PoperVKLNe,poperobnov,
                "Внести изображение поперечного разреза","Показать поперечный разрез",
                "Поперечный разрез не внесён","Обновить изображение поперечного разреза");
        setupTooltips(prodolVNS,ProdolVKL,ProdolVKLNe,prodolobnov,
                "Внести изображение продольного разреза", "Показать продольный разрез",
                "Продольный разрез не внесён" ,"Обновить изображение продольного разреза");

        setupCursor(planVNS,PlanVKL,planVNSNE,PlanVKLNe,planobnov);   // Настраиваем курсор
        setupCursor( poperVNS, PoperVKL, poperVNSNE, PoperVKLNe, poperobnov);
        setupCursor(prodolVNS, ProdolVKL, prodolVNSNE, ProdolVKLNe, prodolobnov);

        setupImageHandlers();

        idi.setVisible(false);
        openImagedok(tabl,instr,dokumGeolog,dokumGeolog11,"yes");
        othet.setOnMouseClicked(mouseEvent -> {OpenDok(Put_othet,"Отчет_");});

        DatabaseHandler dbHandler = new DatabaseHandler();
        ObservableList<Baza> bazas = dbHandler.getAllBaza();
        List<String> nom = bazas.stream() //заполнение базы
                .map(Baza::getGORIZONT)
                .distinct()
                .collect(Collectors.toList());

        gorbox.setItems(FXCollections.observableArrayList(nom));
        gorbox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.equals(oldValue)) {
                ObservableList<Baza> namespisok = dbHandler.poiskName(newValue);
                List<String> imi = namespisok.stream() //заполнение базы
                        .map(Baza::getNAME)
                        .distinct()
                        .toList();
               ohistka();
                namebox.setItems(FXCollections.observableArrayList(imi));

            }
        });
        namebox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null  && !newValue.equals(oldValue)) {
                poisk(gorbox.getValue(),newValue);

            }
        });
        klik(planVNS);klik(poperVNS);klik(prodolVNS);
        klik(planobnov);klik(poperobnov);klik(prodolobnov);

        singUpButtun.setOnMouseClicked(mouseEvent -> {
            proverkaImage(Put + "/" + nomer.getText() + "/" + "План", planVNS, planVNSNE, PlanVKL, PlanVKLNe, planobnov);
            proverkaImage(Put + "/" + nomer.getText() + "/" + "Поперечный", poperVNS, poperVNSNE, PoperVKL, PoperVKLNe, poperobnov);
            proverkaImage(Put + "/" + nomer.getText() + "/" + "Продольный", prodolVNS, prodolVNSNE, ProdolVKL, ProdolVKLNe, prodolobnov);
            getPrimNext();
            String selectedGor = gorbox.getValue();
            String selectedName = namebox.getValue();

            String selectPrivizka = privazka.getText();
            try {
                // Проверка полей по очереди
                StringBuilder errors = new StringBuilder();
                if (selectedGor == null || selectedGor.isEmpty()) {errors.append("- Не выбран горизонт\n");}
                if (selectedName == null || selectedName.isEmpty()) {errors.append("- Не выбрано название выработки\n");}
                if (selectPrivizka == null || selectPrivizka.isEmpty()) {errors.append("- Не заполнена привязка\n");}
                if (poperVNS.isVisible() || poperVNSNE.isVisible()) {
                    errors.append("- Не внесён поперечный разрез\n");
                }
                if (prodolVNS.isVisible() || prodolVNSNE.isVisible()) {
                    errors.append("- Не внесён продольный разрез\n");
                }
                if (planVNS.isVisible() || planVNSNE.isVisible()) {
                    errors.append("- Не внесён план\n");
                }
                // Если есть ошибки - показываем их
                if (errors.length() > 0) {showAlert("Заполните обязательные поля:\n" + errors.toString());
                    return;
                }

                // Все данные валидны - выполняем сохранение
                String prim = "Требуется геологическое описание";
              new DatabaseHandler().DobavlenieMARH(prim,selectPrivizka, selectedGor, selectedName);
                ohistka();
                // gorbox.setValue(null);
            }  catch (Exception e) {
                showAlert("Произошла ошибка: " + e.getMessage());
            }
        });

    }
    private void poisk (String viborGOR, String viborName) {

        Platform.runLater(() -> {
            DatabaseHandler dbHandler = new DatabaseHandler();
            Baza poluh = dbHandler.danii(viborGOR, viborName);
            if (poluh != null) {
                nomer.setText(poluh.getNOMER());
                idi.setText(poluh.getIDI());
                privazka.setText(poluh.getPRIVIZKA());
                setupImageHandlers();
                proverkaImage(Put + "/" + nomer.getText() + "/" + "План", planVNS, planVNSNE, PlanVKL, PlanVKLNe, planobnov);
                proverkaImage(Put + "/" + nomer.getText() + "/" + "Поперечный", poperVNS, poperVNSNE, PoperVKL, PoperVKLNe, poperobnov);
                proverkaImage(Put + "/" + nomer.getText() + "/" + "Продольный", prodolVNS, prodolVNSNE, ProdolVKL, ProdolVKLNe, prodolobnov);
                getPrimNext();


            }else {
                System.out.println("Данные не найдены для " + viborGOR + " - " + viborName);
                nomer.clear();
                privazka.clear(); // Пользователь не найден
            }
        });


    }
    private void ohistka() {

        nomer.setText("");
        namebox.setValue("");
        privazka.setText("");

        idi.setText("");
        PlanVKL.setVisible(false);PlanVKLNe.setVisible(true);planobnov.setVisible(false);
        planVNS.setVisible(false); planVNSNE.setVisible(true);
        PoperVKL.setVisible(false);PoperVKLNe.setVisible(true);poperobnov.setVisible(false);
        poperVNS.setVisible(false); poperVNSNE.setVisible(true);
        ProdolVKL.setVisible(false);ProdolVKLNe.setVisible(true);prodolobnov.setVisible(false);
        prodolVNS.setVisible(false); prodolVNSNE.setVisible(true);
    }

    private void setupImageHandlers() {

        openImageHandler(PlanVKL, "План",PlanVKLNe,nomer.getText());
        openImageHandler(PoperVKL, "Поперечный",PoperVKLNe,nomer.getText());
        openImageHandler(ProdolVKL, "Продольный",ProdolVKLNe,nomer.getText());

        obnovaImageHandler(planobnov, "План",nomer.getText());
        obnovaImageHandler(poperobnov, "Поперечный",nomer.getText());
        obnovaImageHandler(prodolobnov, "Продольный",nomer.getText());

        sozdaniiImageHandler(planVNS, "План",PlanVKL,PlanVKLNe,planVNS,planobnov,planobnov,nomer.getText());
        sozdaniiImageHandler(poperVNS, "Поперечный",PoperVKL,PoperVKLNe,poperVNS,poperobnov,planobnov,nomer.getText());
        sozdaniiImageHandler(prodolVNS, "Продольный",ProdolVKL,ProdolVKLNe,prodolVNS,prodolobnov,planobnov,nomer.getText());

    }
    protected void klik(ImageView imageView) {
        imageView.setOnMouseClicked(e -> {
            getPrimNext();
        });
    }
    void getPrimNext() {

        String selectedGor = gorbox.getValue();
        String selectedName = namebox.getValue();
        String nomerValue = nomer.getText();

        if (nomerValue == null || nomerValue.isEmpty()) {
            System.err.println("Номер не указан!");
            return;
        }

        // Проверяем все три папки
        boolean allFoldersHaveImages = checkAllFoldersHaveImages(nomerValue);

        if (allFoldersHaveImages) {
            try {
                StringBuilder errors = new StringBuilder();
                if (selectedGor == null || selectedGor.isEmpty()) {
                    errors.append("- Не выбран горизонт\n");
                }
                if (selectedName == null || selectedName.isEmpty()) {
                    errors.append("- Не выбрано название выработки\n");
                }

                if (errors.length() > 0) {
                    showAlert("Заполните обязательные поля:\n" + errors.toString());
                    return;
                }

//                String prim = "Требуется геологическое описание";
//                new DatabaseHandler().DobavleniePRIM(prim, selectedGor, selectedName);
//                System.out.println("OKE");
            } catch (Exception e) {
                showAlert("Произошла ошибка: " + e.getMessage());
            }
        } else {
            System.err.println("Не все папки содержат изображения для номера: " + nomerValue);

            // Если нужно добавить примечание при отсутствии изображений
            if (selectedGor != null && !selectedGor.isEmpty() && selectedName != null && !selectedName.isEmpty()) {
                String prim = "Необходима привязка выработки";
                new DatabaseHandler().DobavleniePRIM(prim, selectedGor, selectedName);
                System.out.println("NOY OKE");
            }
        }
    }

}






