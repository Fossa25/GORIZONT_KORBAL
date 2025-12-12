package com.example.proburok;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import java.io.File;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
public class GeologCOD extends Configs {

    @FXML private Button B1;
    @FXML private Button B2;
    @FXML private Button B3;
    @FXML private Button B4;
    @FXML private Button B5;

    @FXML private ImageView PlanVKL;
    @FXML private ImageView PlanVKLNe;
    @FXML private ImageView PoperVKL;
    @FXML private ImageView PoperVKLNe;
    @FXML private ImageView ProdolVKL;
    @FXML private ImageView ProdolVKLNe;
    @FXML private TextArea opisanie;

    @FXML private TextField katigoria;
    @FXML private TextField nomer;
    @FXML private Button singUpButtun;

    @FXML private ComboBox<String> gorbox;
    @FXML private ComboBox<String> namebox;
    @FXML private TextField privazka;


    @FXML private TextField idi;
    @FXML private ImageView instr;
    @FXML private ImageView dokumGeolog;
    @FXML private ImageView dokumGeolog11;
    @FXML private ImageView tabl;

    @FXML private TextField dlina;

    private String tippas;
    private String prim1 = "Требуется геомеханическое обоснование (указание)" ;
    private String prim2 =  "проходка не предусмотрена назначением выработки " ;
    private String prim3 = "проходка в данном классе нецелесообразна";
    private String prim;
    public String blak= " -fx-border-color: #00000000;-fx-background-color:#00000000;-fx-border-width: 0px";
    public String red = "-fx-border-color: #14b814;-fx-background-color:#00000000;-fx-border-width: 3px";

    List<String> soprigenii = Arrays.asList("11","12","13","14","15");

    public String tex1 = "Сплошной крепкий массив осадочных и вулканогенно- осадочных пород. Крупные угловатые зажатые блоки." +
            "Излом поверхности раковистый. Редкие крупные трещины, заполненные чешуйчатыми минералами. При простукивании геологическим молотком массив звенит.";
    public String tex2 = "Массив плохо связанных округлых блоков с прослоями измененных пород. Наличие борозд скольжения. Склонность к вывалообразованию.";
    public String tex3 = " Массив измененных горных пород до состояния мягкой чешуйчатой массы (метасоматиты)." +
            "При попадании воды склонен к быстрому набуханию и высыпанию в больших объемах.";
    public String tex4 = "Чередование прочных массивных и прожилково- вкрапленных руд с прослоями измененных горных пород.";
    public String tex5 = "Непрочная полиметаллическая руда. Вязкая, рыхлая, с прослоями измененных горных пород.";

    @FXML
    void initialize() {

        idi.setVisible(false);
        setupImageHandlers();
        dlina.setTextFormatter(new TextFormatter<>(change -> {
            // Всегда корректируем start и end
            int start = Math.min(change.getRangeStart(), change.getRangeEnd());
            int end = Math.max(change.getRangeStart(), change.getRangeEnd());
            change.setRange(start, end);
            String newText = change.getControlNewText();
            if (newText.isEmpty() || newText.matches("[0-9]*([.,][0-9]*)?")) {
                return change;
            }
            // Отклоняем невалидные изменения
            return null;
        }));

        openImagedok(tabl,instr,dokumGeolog,dokumGeolog11,"yes");

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
                viborKatigorii();
            }
        });


        viborKatigorii();
        idi.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.equals(oldValue)) {
                viborKatigorii();
            }
        });

        singUpButtun.setOnMouseClicked(mouseEvent -> {
            String selectedGor = gorbox.getValue();
            String selectedName = namebox.getValue();
            String kategoriyaValue = katigoria.getText();
            String opisanieValue = opisanie.getText();
            String selectPrivizka = privazka.getText();
            String dlinaValue = dlina.getText();

            proverkaImageGeolg(Put + "/" + nomer.getText() + "/" + "План", PlanVKL,PlanVKLNe);
            proverkaImageGeolg(Put + "/" + nomer.getText() + "/" + "Поперечный",PoperVKL,PoperVKLNe);
            proverkaImageGeolg(Put + "/" + nomer.getText() + "/" + "Продольный",ProdolVKL,ProdolVKLNe);
            try {
                // Проверка полей по очереди
                StringBuilder errors = new StringBuilder();
                if (selectedGor == null || selectedGor.isEmpty()) {errors.append("- Не выбран горизонт\n");}
                if (selectedName == null || selectedName.isEmpty()) {errors.append("- Не выбрано название выработки\n");}
                if (selectPrivizka == null || selectPrivizka.isEmpty()) {errors.append("- Не заполнена привязка\n");}
                if (soprigenii.contains(idi.getText())) {
                    dlinaValue = "-";
                    dlina.setVisible(false);
                }else {  if (dlinaValue == null || dlinaValue.isEmpty()) {errors.append("- Не заполнена длина выработки\n");}}
                if (kategoriyaValue == null || kategoriyaValue.isEmpty()) { errors.append("- Не заполнено поле категория \n"); }
                if (opisanieValue == null || opisanieValue.isEmpty()) {errors.append("- Не заполнено поле описание\n");}

                // Если есть ошибки - показываем их
                if (errors.length() > 0) {showAlert("Заполните обязательные поля:\n" + errors.toString());
                    return;
                }
                // Все данные валидны - выполняем сохранение
                getPas();
                new DatabaseHandler().DobavlenieGEOLOG(kategoriyaValue, opisanieValue,dlinaValue, selectedGor, selectedName,tippas,prim)    ;

                System.out.println("ТИПОВОЙ ПАСПОРТ= "+this.tippas+" "+this.prim);
                ohistka();
              // gorbox.setValue(null);
            }  catch (Exception e) {
                showAlert("Произошла ошибка: " + e.getMessage());
            }
        });
    }
    private void poisksehen(){
        if (soprigenii.contains(idi.getText())) {
            dlina.setVisible(false);
        }else {
            dlina.setVisible(true);
        }
    }
    private void viborKatigorii(){

            klikKatigorii(B1,"1",tex1);
            klikKatigorii(B2,"2",tex2);
            klikKatigorii(B3,"3",tex3);
            klikKatigorii(B4,"4",tex4);
            klikKatigorii(B5,"5",tex5);
        }

    private void klikKatigorii(Button imageView, String kat,  String tx ) {
        imageView.setOnMouseClicked(e -> {
            obnul(kat,tx);
            imageView.setStyle(red);

        });
    }

    private void setupImageHandlers() {
        openImageHandler(PlanVKL, "План",PlanVKLNe);
        openImageHandler(PoperVKL, "Поперечный",PoperVKLNe);
        openImageHandler(ProdolVKL, "Продольный",ProdolVKLNe);
    }

    protected void openImageHandler(ImageView imageView, String folder, ImageView neimage) {
        imageView.setOnMouseClicked(e -> {
            if (nomer.getText().isEmpty()) return;
            openImage(Put + "/" + nomer.getText() + "/" + folder,imageView,neimage,"yes");
        });
    }

    public void obnul(String x,String tx){
        if (x.equals("x")) {
            B1.setStyle(blak);
            B2.setStyle(blak);
            B3.setStyle(blak);
            B4.setStyle(blak);
            B5.setStyle(blak);
            opisanie.setText(tx);
        }else {
            B1.setStyle(blak);
            B2.setStyle(blak);
            B3.setStyle(blak);
            B4.setStyle(blak);
            B5.setStyle(blak);
            katigoria.setText(x);
            opisanie.setText(tx);
        }
    }

    private void poisk (String viborGOR, String viborName) {

        Platform.runLater(() -> {
        DatabaseHandler dbHandler = new DatabaseHandler();
        Baza poluh = dbHandler.danii(viborGOR, viborName);
        if (poluh != null) {
            nomer.setText(poluh.getNOMER());
            idi.setText(poluh.getIDI());
            privazka.setText(poluh.getPRIVIZKA());

            proverkaImageGeolg(Put + "/" + nomer.getText() + "/" + "План", PlanVKL,PlanVKLNe);
            proverkaImageGeolg(Put + "/" + nomer.getText() + "/" + "Поперечный",PoperVKL,PoperVKLNe);
            proverkaImageGeolg(Put + "/" + nomer.getText() + "/" + "Продольный",ProdolVKL,ProdolVKLNe);
            poisksehen();

            if (poluh.getDLINA() != null) {
                dlina.setText(poluh.getDLINA());
            } else {
                dlina.setText("");
            }
            if (poluh.getOPISANIE() != null) {
                opisanie.setText(poluh.getOPISANIE());
            } else {
                opisanie.setText("");
            }
            if (poluh.getKATEGORII() != null) {
                katigoria.setText(poluh.getKATEGORII());
            } else {
                katigoria.setText("");
            }
            if ((katigoria.getText() ==null) || katigoria.getText().isEmpty() ){
                obnul("x","");
            }else {
                switch (katigoria.getText()){
                    case"1" -> {obnul("x",opisanie.getText());B1.setStyle(red);}
                    case"2" -> {obnul("x",opisanie.getText());B2.setStyle(red);}
                    case"3" -> {obnul("x",opisanie.getText());B3.setStyle(red);}
                    case"4" -> {obnul("x",opisanie.getText());B4.setStyle(red);}
                    case"5" ->{obnul("x",opisanie.getText());B5.setStyle(red);}
                }}

        } else {
            System.out.println("Данные не найдены для " + viborGOR + " - " + viborName);
            nomer.clear();
            privazka.clear(); // Пользователь не найден
        }
        });

    }
    private void ohistka(){
        obnul("x","");
       // namebox.setValue(null);
        nomer.setText("");
        privazka.setText("");
        katigoria.setText("");
        opisanie.setText("");
        dlina.setText("");
       // dlina.setVisible(true);
        idi.setText("");
        this.tippas="";
        this.prim="";
        PlanVKL.setVisible(false);PlanVKLNe.setVisible(true);
        PoperVKL.setVisible(false);PoperVKLNe.setVisible(true);
        ProdolVKL.setVisible(false);ProdolVKLNe.setVisible(true);

    }

   private void proverkaImageGeolg(String imagePath,ImageView VKL,ImageView VKLNE) {
        File folder = new File(imagePath);
        // Проверяем, существует ли папка
        if (!folder.exists() || !folder.isDirectory()) {
           // System.err.println("Папка не найдена: " + imagePath);

            VKL.setVisible(false);VKLNE.setVisible(true);
            singUpButtun.setVisible(false);
            showAlert("Не внесены графические материалы");
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

            boolean allFoldersHaveImages = checkAllFoldersHaveImages(nomer.getText());

            if (allFoldersHaveImages) {
                try {
                    singUpButtun.setVisible(true);

                } catch (Exception e) {
                    showAlert("Произошла ошибка: " + e.getMessage());
                }
            } else {
                System.err.println("Не все папки содержат изображения для номера: " + nomer.getText());

                singUpButtun.setVisible(false);
                showAlert("Не внесены графические материалы");
            }

        } else {
            System.err.println("В папке нет изображений.");
            singUpButtun.setVisible(false);

            VKL.setVisible(false);VKLNE.setVisible(true);
        }
    }

    private void getPas () {
        String kat = katigoria.getText();
        String id=idi.getText();

        if (id.isEmpty()) {
            tippas = "Типовой паспорт не разработан";
            return;
        }
        if (!kat.isEmpty()){
            switch (id) {
                case "1" ->TP_3(kat,"1","3","4","-","-");
                case "2" ->TP_3(kat,"5","7","8","9","10");
                case "3" ->TP_3(kat,"11","13","14","15","16");
                case "4" ->TP_3(kat,"17","19","20","-","-");
                case "5" ->TP_3(kat,"21","23","24","25","26");
                case "6" ->TP_3(kat,"27","29","-","-","-");
                case "7" ->TP_3(kat,"30","32","-","-","-");
                case "8" ->TP_3(kat,"33","35","-","-","-");
                case "9" ->TP_3(kat,"36","38","-","-","-");
                case "10" ->TP_3(kat,"39","41","-","-","-");

                case "11" ->TP_SOPR(kat,"1","2","-","-","-");
                case "12" ->TP_SOPR(kat,"-","3","4","5","6");
                case "13" ->TP_SOPR(kat,"-","7","8","9","10");
                case "14" ->TP_SOPR(kat,"11","12","-","-","-");
                case "15" ->TP_SOPR(kat,"13","14","-","-","-");

                default ->  tippas = "Типовой паспорт не разработан";
            }}
    }

    private void TP_3 (String kat,String x1,String x2,String x3,String x4,String x5) {

        switch (kat) {
            case "1" -> {this.tippas = x1;this.prim=prim1;}
            case "2" -> {this.tippas = x2;this.prim=prim1;}
            case "3" -> {switch (idi.getText()) {
                case "1","2","3","5","4" -> {this.tippas = x3;this.prim=prim1;}
                default -> {tippas = "";this.prim=prim3;}
                    }}
            case "4" -> {switch (idi.getText()) {
                case "2","3","5" -> {this.tippas = x4;this.prim=prim1;}
                default -> {tippas = "";this.prim=prim2;}
            }}
            case "5" -> {switch (idi.getText()) {
                case "2","3","5" -> {this.tippas = x5;this.prim=prim1;}
                default -> {tippas = "";this.prim=prim2;}
            }}

        }

        }
    private void TP_SOPR (String kat,String x1,String x2,String x3,String x4,String x5) {

        switch (kat) {
            case "1" ->{switch (idi.getText()) {
                case "11","14","15"-> {this.tippas = x1;this.prim=prim1;}
                default -> {tippas = "";this.prim=prim2;}
            }}
            case "2" -> {this.tippas = x2;this.prim=prim1;}
            case "3" -> {switch (idi.getText()) {
                case "12","13" -> {this.tippas = x3;this.prim=prim1;}
                default -> {tippas = "";this.prim=prim3;}
            }}

            case "4" -> {switch (idi.getText()) {
                case "12","13" -> {this.tippas = x4;this.prim=prim1;}
                default -> {tippas = "";this.prim=prim2;}
            }}
            case "5" -> {switch (idi.getText()) {
                case "12","13" -> {this.tippas = x5;this.prim=prim1;}
                default -> {tippas = "";this.prim=prim2;}
            }}

        }

    }

   }




