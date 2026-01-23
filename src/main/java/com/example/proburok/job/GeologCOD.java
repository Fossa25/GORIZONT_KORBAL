package com.example.proburok.job;
import com.example.proburok.New_Class.Baza;
import com.example.proburok.New_Class.Baza_Geolg;
import com.example.proburok.MQ.DatabaseHandler;
import com.example.proburok.New_Class.InputData;
import com.example.proburok.New_Class.ValidationException;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import java.io.File;
import java.time.Clock;
import java.util.*;
import java.util.stream.Collectors;
import javafx.beans.property.StringProperty;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Window;
import javafx.util.Duration;

public class GeologCOD extends Configs {


    @FXML private TextField DO;
    @FXML private TextField OT;
    @FXML private ImageView MINUS;
    @FXML private ImageView PLUS;
    @FXML private ImageView obnv;
    @FXML private ImageView droblen;


    @FXML private TextField OSTATOK;
    @FXML private TableView<ObservableList<StringProperty>> dataTable;

    @FXML private TableColumn<ObservableList<StringProperty>, String>  column1;

    @FXML private TableColumn<ObservableList<StringProperty>, String>  column2;

    @FXML private TableColumn<ObservableList<StringProperty>, String> column3;

    @FXML private TableColumn<ObservableList<StringProperty>, String> column4;
    @FXML private TableColumn<ObservableList<StringProperty>, String> column5;
    @FXML private TableColumn<ObservableList<StringProperty>, String> column6;
    @FXML private TableColumn<ObservableList<StringProperty>, String> column7;
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
    @FXML private ImageView nom;
    @FXML private TextField dlina;

    @FXML private AnchorPane AP;
    private String tippas;
    private String tippas_ROW;
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
        int editingRowIndex;
    private List<Node> nodesToAnimate = new ArrayList<>();
    private List<Node> nodesToAnimate_TEXT = new ArrayList<>();
    private Map<Node, Double> originalPositions = new HashMap<>();
    private Map<Node, Double> originalPositions_TEXT = new HashMap<>();
    @FXML
    void initialize() {
        OSTATOK.setVisible(true);
        idi.setVisible(false);
        nodesToAnimate.addAll(Arrays.asList(tabl, instr, dokumGeolog, dokumGeolog11, B1, B2, B3, B4, B5,nom));
        nodesToAnimate_TEXT.addAll(Arrays.asList(katigoria,opisanie,singUpButtun));
        saveOriginalPositions();
        setupTable();
        setupEventHandlers();
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

            try {
                // Проверка полей по очереди
                StringBuilder errors = new StringBuilder();
                if (selectedGor == null || selectedGor.isEmpty()) {errors.append("- Не выбран горизонт\n");}
                if (selectedName == null || selectedName.isEmpty()) {errors.append("- Не выбрано название выработки\n");}
                // Если есть ошибки - показываем их
                if (errors.length() > 0) {showAlert("Заполните обязательные поля:\n" + errors.toString());
                    return;
                }
                // Все данные валидны - выполняем сохранение
                if (soprigenii.contains(idi.getText())) {
                    OT.setText("-");  DO.setText("-");
                    validateInputs();
                    getPas(katigoria.getText());
                    new DatabaseHandler().DobavlenieGEOLOG_SOPR(katigoria.getText(),opisanie.getText(), selectedGor, selectedName,tippas,prim) ;
                }else {
                getPas_Table();
                new DatabaseHandler().DobavlenieGEOLOG( selectedGor, selectedName,tippas,prim) ;
                dbHandler.deleteRowsWithCheck(nomer.getText());
                saveTableToDatabase();
                }
                System.out.println(idi.getText()+   " ТИПОВОЙ ПАСПОРТ= "+this.tippas+" "+this.prim);
                ohistka();

            }  catch (Exception e) {
                showAlert("Произошла ошибка: " + e.getMessage());
            }
        });
        PLUS.setOnMouseClicked(mouseEvent ->  addRowToTable());
        MINUS.setOnMouseClicked(mouseEvent -> removeSelectedRow());
        obnv.setOnMouseClicked(mouseEvent -> saveEditedRow());
        droblen.setOnMouseClicked(mouseEvent -> doble_Row());
    }

    private void saveOriginalPositions() {
        for (Node node : nodesToAnimate) {
            originalPositions.put(node, node.getTranslateY());
        }
        for (Node node : nodesToAnimate_TEXT) {
            originalPositions_TEXT.put(node, node.getTranslateY());
        }
    }

    private void animateNodes(boolean moveUp) {
        double targetY = moveUp ? -160 : 0;
        double targetY1 = moveUp ? -130 : 0;

        for (Node node : nodesToAnimate) {
            animateSingleNode(node, targetY);
        }
        for (Node node : nodesToAnimate_TEXT) {
            animateSingleNode(node, targetY1);
        }
    }
    private void animateSingleNode(Node node, double targetY) {
        TranslateTransition transition = new TranslateTransition(Duration.millis(300), node);
        transition.setToY(targetY);
        transition.setInterpolator(Interpolator.EASE_OUT);
        transition.play();
    }
    private void poisksehen(){

        if (soprigenii.contains(idi.getText())) {
            dlina.setVisible(false);
            OT.setVisible(false);
            DO.setVisible(false);
            dataTable.setVisible(false);
            PLUS.setVisible(false);
            MINUS.setVisible(false);
            obnv.setVisible(false);
            droblen.setVisible(false);
            animateNodes(true);
            AP.setPrefHeight(950.0);
            Scene scene = AP.getScene();
            if (scene != null) {
                Window window = scene.getWindow();
                if (window != null) {
                    window.sizeToScene();
                }
            }

        }else {
            dlina.setVisible(true);
            OT.setVisible(true);
            DO.setVisible(true);
            dataTable.setVisible(true);
            PLUS.setVisible(true);
            MINUS.setVisible(true);
            obnv.setVisible(true);
            droblen.setVisible(true);
            animateNodes(false);
            AP.setPrefHeight(1116.0);
            Scene scene = AP.getScene();
            if (scene != null) {
                Window window = scene.getWindow();
                if (window != null) {
                    window.sizeToScene();
                }
            }

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
            dlina.setText(poluh.getDLINA());
            proverkaImageGeolg(Put + "/" + nomer.getText() + "/" + "План", PlanVKL,PlanVKLNe);
            proverkaImageGeolg(Put + "/" + nomer.getText() + "/" + "Поперечный",PoperVKL,PoperVKLNe);
            proverkaImageGeolg(Put + "/" + nomer.getText() + "/" + "Продольный",ProdolVKL,ProdolVKLNe);
            poisksehen();
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

            OSTATOK.setText(poluh.getDLINA());
            loadDataFromDatabase();
            List<Baza_Geolg> rows = getTableDataAsRows();
            for (Baza_Geolg row : rows) {
                double OT_dobl = Double.parseDouble(row.getcolumnOT());
                double DO_dobl = Double.parseDouble(row.getcolumnDO());
                double DLINA_dobl = Double.parseDouble(OSTATOK.getText());
                double sum  = DLINA_dobl + (OT_dobl-DO_dobl);
                OSTATOK.setText(String.valueOf(sum));
                OT.setText(row.getcolumnDO());
                katigoria.setText(row.getcolumnKLASS());
                opisanie.setText(row.getcolumnOPIS());

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
        clearTable();
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
        OT.setText("");  DO.setText("");OSTATOK.setText("");
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

    private void getPas ( String kat) {
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
        String star_pas;
        if (Objects.equals(this.tippas, "")){
            star_pas ="";
        }else {  star_pas = tippas +", ";}
        switch (kat) {
            case "1" -> {this.tippas =star_pas + x1;this.tippas_ROW =x1; this.prim=prim1;}
            case "2" -> {this.tippas =star_pas + x2;this.tippas_ROW =x2; this.prim=prim1;}
            case "3" -> {switch (idi.getText()) {
                case "1","2","3","5","4" -> {this.tippas =star_pas + x3;this.tippas_ROW =x3; this.prim=prim1;}
                default -> {this.tippas = star_pas + "-";this.tippas_ROW ="-"; this.prim=prim3;}
                    }}
            case "4" -> {switch (idi.getText()) {
                case "2","3","5" -> {this.tippas =star_pas +x4;this.tippas_ROW =x4; this.prim=prim1;}
                default -> {tippas = star_pas + "-";this.tippas_ROW ="-"; this.prim=prim2;}
            }}
            case "5" -> {switch (idi.getText()) {
                case "2","3","5" -> {this.tippas =star_pas + x5;this.tippas_ROW =x5; this.prim=prim1;}
                default -> {tippas = star_pas + "-";this.tippas_ROW ="-"; this.prim=prim2;}
            }}
            case "-" ->  {tippas =star_pas + "-";this.tippas_ROW ="-"; this.prim=prim2;}
        }
        }
    private void TP_SOPR (String kat,String x1,String x2,String x3,String x4,String x5) {

        switch (kat) {
            case "1" ->{switch (idi.getText()) {
                case "11","14","15"-> {this.tippas = x1;this.tippas_ROW = x1;this.prim=prim1;}
                default -> {tippas = "";this.tippas_ROW ="-"; this.prim=prim2;}
            }}
            case "2" -> {this.tippas = x2;this.tippas_ROW = x2;this.prim=prim1;}
            case "3" -> {switch (idi.getText()) {
                case "12","13" -> {this.tippas = x3;this.tippas_ROW = x3;this.prim=prim1;}
                default -> {tippas = "";this.tippas_ROW ="-"; this.prim=prim3;}
            }}

            case "4" -> {switch (idi.getText()) {
                case "12","13" -> {this.tippas = x4;this.tippas_ROW = x4;this.prim=prim1;}
                default -> {tippas = "";this.tippas_ROW ="-"; this.prim=prim2;}
            }}
            case "5" -> {switch (idi.getText()) {
                case "12","13" -> {this.tippas = x5;this.tippas_ROW = x5;this.prim=prim1;}
                default -> {tippas = "";this.tippas_ROW ="-"; this.prim=prim2;}
            }}
        }
    }

    private void setupTable() {
        // 1. Делаем таблицу редактируемой
        dataTable.setEditable(true);
        // 2. Настраиваем, как данные извлекаются для каждого столбца
        setupCellValueFactories();
        // 3. Настраиваем редактирование ячеек
        setupCellEditing();
        setupContextMenu();
    }
    private void setupCellValueFactories() {
        // Столбец 1: берет значение из первой ячейки строки
        column1.setCellValueFactory(param -> {
            // param.getValue() - получаем всю строку (ObservableList<StringProperty>)
            ObservableList<StringProperty> row = param.getValue();
            // Если в строке есть хотя бы 1 элемент, возвращаем его значение
            // Если нет - возвращаем пустую строку
            if (row.size() > 0) {
                return row.get(0);  // StringProperty первой ячейки
            } else {
                return new SimpleStringProperty("");  // Пустая строка
            }
        });

        // Аналогично для остальных столбцов
        column2.setCellValueFactory(param -> {
            ObservableList<StringProperty> row = param.getValue();
            return row.size() > 1 ? row.get(1) : new SimpleStringProperty("");
        });
        column3.setCellValueFactory(param -> {
            ObservableList<StringProperty> row = param.getValue();
            return row.size() > 2 ? row.get(2) : new SimpleStringProperty("");
        });
        column4.setCellValueFactory(param -> {
            ObservableList<StringProperty> row = param.getValue();
            return row.size() > 3 ? row.get(3) : new SimpleStringProperty("");
        });
        column5.setCellValueFactory(param -> {
            ObservableList<StringProperty> row = param.getValue();
            return row.size() > 4 ? row.get(4) : new SimpleStringProperty("");
        });
    }

    private void setupCellEditing() {
        // Для каждого столбца настраиваем возможность редактирования
        setupColumnForEditing(column1, 0);  // 0 - индекс столбца
        setupColumnForEditing(column2, 1);  // 1 - индекс столбца
        setupColumnForEditing(column3, 2);  // 2 - индекс столбца
        setupColumnForEditing(column4, 3);  // 3 - индекс столбца
        setupColumnForEditing(column5, 4);  // 3 - индекс столбца
    }

    private void setupColumnForEditing(TableColumn<ObservableList<StringProperty>, String> column,
                                       int columnIndex) {
        // 1. Устанавливаем фабрику ячеек - TextFieldTableCell
        // Это специальная ячейка, которая превращается в TextField при редактировании
        column.setCellFactory(TextFieldTableCell.forTableColumn());

        // 2. Настраиваем обработчик завершения редактирования
        column.setOnEditCommit(event -> {
            // event.getRowValue() - строка, которую редактировали
            ObservableList<StringProperty> row = event.getRowValue();

            // event.getNewValue() - новое значение, которое ввел пользователь
            String newValue = event.getNewValue();

            // Обновляем значение в строке
            if (row.size() > columnIndex) {
                // row.get(columnIndex) - получаем StringProperty нужной ячейки
                // .set(newValue) - устанавливаем новое значение
                row.get(columnIndex).set(newValue);
            }
        });
    }

    private void setupContextMenu() {
        // Создаем контекстное меню
        ContextMenu contextMenu = new ContextMenu();

        // 1. Пункт "Добавить строку"
        MenuItem addRowItem = new MenuItem("Добавить строку");
        addRowItem.setOnAction(event -> {
            // При нажатии вызываем метод добавления строки
            addRowToTable();
        });

        // 2. Пункт "Удалить строку"
        MenuItem removeRowItem = new MenuItem("Удалить строку");
        removeRowItem.setOnAction(event -> {
            // Удаляем выбранную строку
            removeSelectedRow();
        });

        // 3. Пункт "Очистить таблицу"
        MenuItem clearTableItem = new MenuItem("Очистить таблицу");
        clearTableItem.setOnAction(event -> {
            // Очищаем всю таблицу
            clearTable();
        });

        // Добавляем пункты в меню
        contextMenu.getItems().addAll(addRowItem, removeRowItem, clearTableItem);

        // Устанавливаем контекстное меню для таблицы
        dataTable.setContextMenu(contextMenu);
    }
    private InputData validateInputs() throws ValidationException {

        InputData data = new InputData();
        data.selectedGor = gorbox.getValue();
        data.selectedName = namebox.getValue();
        data.kategoriyaValue = katigoria.getText();
        data.opisanieValue = opisanie.getText();
        data.selectPrivizka = privazka.getText();
        data.dlinaValue = dlina.getText();
        if (dataTable.getItems().isEmpty()) {OT.setText("0.0");}
        data.OTValue = OT.getText();
        data.DOValue = DO.getText();

        checkGeologicalImages();
        validateRequiredFields(data);
        return data;
    }
    private void checkGeologicalImages() {
        String basePath = Put + "/" + nomer.getText();
        proverkaImageGeolg(basePath + "/" + "План", PlanVKL, PlanVKLNe);
        proverkaImageGeolg(basePath + "/" + "Поперечный", PoperVKL, PoperVKLNe);
        proverkaImageGeolg(basePath + "/" + "Продольный", ProdolVKL, ProdolVKLNe);
    }
    private void validateRequiredFields(InputData data) throws ValidationException {
        StringBuilder errors = new StringBuilder();

        validateField(data.selectedGor, "горизонт", errors);
        validateField(data.selectedName, "название выработки", errors);
        validateField(data.selectPrivizka, "привязка", errors);
        if (soprigenii.contains(idi.getText())) {
            data.dlinaValue = "-";
            dlina.setVisible(false);
        } else {
            validateField(data.dlinaValue, "длина выработки", errors);
        }
        validateField(data.kategoriyaValue, "категория", errors);
        validateField(data.opisanieValue, "описание", errors);
        validateField(data.OTValue, "начало интервала", errors);
        validateField(data.DOValue, "конец интервала", errors);

        if (errors.length() > 0) {
            throw new ValidationException("Заполните обязательные поля:\n" + errors.toString());
        }
    }
    private void addRowToTable() {
        try {
            InputData data = validateInputs();

            String OT_TOH = data.OTValue.trim().replace(',', '.');
            String DO_TOH = data.DOValue.trim().replace(',', '.');
            String DLINA_TOH;
            if (OSTATOK.getText() == null || OSTATOK.getText().isEmpty()) {
                DLINA_TOH = data.dlinaValue.trim().replace(',', '.');
            } else {
                DLINA_TOH = OSTATOK.getText().trim().replace(',', '.');
            }
            double OT_dobl = Double.parseDouble(OT_TOH);
            double DO_dobl = Double.parseDouble(DO_TOH);
            double DLINA_dobl = Double.parseDouble(DLINA_TOH);
            double sum = DLINA_dobl + (OT_dobl - DO_dobl);
            if (sum >= 0 && OT_dobl < DO_dobl) {
                // Все данные валидны - выполняем сохранение
                ObservableList<ObservableList<StringProperty>> items = dataTable.getItems();

                ObservableList<StringProperty> newRow = FXCollections.observableArrayList(
                        new SimpleStringProperty(String.valueOf(items.size() + 1)),
                        new SimpleStringProperty(data.OTValue),
                        new SimpleStringProperty(data.DOValue),
                        new SimpleStringProperty(data.kategoriyaValue),
                        new SimpleStringProperty(data.opisanieValue),
                        new SimpleStringProperty(data.OTValue + "-" + data.DOValue),
                          new SimpleStringProperty("")
                );
                items.add(newRow);
                dataTable.scrollTo(items.size() - 1);
                OT.setText(DO_TOH);
                DO.setText("");
                OSTATOK.setText(String.valueOf(sum));
            } else {
                showAlert("Длина интервала больше длины выработки \nИЛИ\nКонечный интервал меньше начального");
                return;
            }
            katigoria.setText("");
            opisanie.setText("");
            obnul("x", "");

        } catch (ValidationException e) {
            showAlert(e.getMessage());
            return;
        } catch (NumberFormatException e) {
            showAlert("Ошибка в числовых полях: " + e.getMessage());
        } catch (Exception e) {
            showAlert("Произошла ошибка: " + e.getMessage());
        }
    }

    public List<Baza_Geolg> getTableDataAsRows() {
        List<Baza_Geolg> rows = new ArrayList<>();
        for (ObservableList<StringProperty> tableRow : dataTable.getItems()) {
            Baza_Geolg row = new Baza_Geolg();

            // Безопасное получение значений (чтобы избежать IndexOutOfBoundsException)
            if (tableRow.size() > 0 && tableRow.get(1) != null) {
                row.setcolumnOT(tableRow.get(1).get());
            } else {
                row.setcolumnOT("");
            }
            if (tableRow.size() > 1 && tableRow.get(2) != null) {
                row.setcolumnDO (tableRow.get(2).get());
            } else {
                row.setcolumnDO ("");
            }
            if (tableRow.size() > 2 && tableRow.get(3) != null) {
                row.setcolumnKLASS(tableRow.get(3).get());
            } else {
                row.setcolumnKLASS("");
            }
            if (tableRow.size() > 3 && tableRow.get(4) != null) {
                row.setcolumnOPIS(tableRow.get(4).get());
            } else {
                row.setcolumnOPIS("");
            }
            if (tableRow.size() > 4 && tableRow.get(5) != null) {
                row.setcolumnOTDO(tableRow.get(5).get());
            } else {
                row.setcolumnOTDO("");
            }

            if (tableRow.size() > 6 && tableRow.get(6) != null) {
                row.setcolumnLIST(tableRow.get(6).get());
            } else {
                row.setcolumnLIST("");
            }
            rows.add(row);
        }
        return rows;
    }

    private void saveTableToDatabase() {
        // 1. Получаем данные из таблицы
        List<Baza_Geolg> rows = getTableDataAsRows();
        // 2. Получаем номер паспорта из текстового поля idi
        String nomPas = nomer.getText().trim();  // .trim() убирает пробелы по краям
        // 3. Проверяем, что номер паспорта не пустой
        if (nomPas.isEmpty()) {
            showAlert( "Поле 'Номер паспорта' не заполнено!");
            return;
        }
        // 4. Проверяем, что есть данные в таблице
        if (rows.isEmpty()) {
            showAlert( "Таблица пуста! Добавьте данные в таблицу.");
            return;
        }
        // 5. Сохраняем в БД, передавая номер паспорта отдельным параметром
        try {
            DatabaseHandler tableDAO = new DatabaseHandler();
            tableDAO.saveAllRows(rows, nomPas);
            showAlert("Сохранено " + rows.size() + " строк для паспорта №" + nomPas);
        } catch (Exception e) {
            showAlert( "Не удалось сохранить данные: " + e.getMessage());
            e.printStackTrace();
        }
    }
    private void removeSelectedRow() {
        // 1. Получаем индекс выбранной строки
        int selectedIndex = dataTable.getSelectionModel().getSelectedIndex();
        // 2. Проверяем, что строка действительно выбрана
        if (selectedIndex >= 0) {
            // 3. Удаляем строку из данных таблицы
            dataTable.getItems().remove(selectedIndex);
            updateRowNumbers();
        } else {
            showAlert( "Пожалуйста, выберите строку для удаления");
        }
    }
    private void editSelectedRow() {
        int selectedIndex = dataTable.getSelectionModel().getSelectedIndex();

        if (selectedIndex >= 0) {
            ObservableList<StringProperty> row = dataTable.getItems().get(selectedIndex);
            OT.setText(row.get(1).get());
            DO.setText(row.get(2).get());
            katigoria.setText(row.get(3).get());
            opisanie.setText(row.get(4).get());
            // Сохраняем индекс для последующего обновления
            editingRowIndex = selectedIndex;

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
        }else {
            showAlert( "Пожалуйста, выберите строку для обновления");
        }
    }

    private void saveEditedRow() {
        if (editingRowIndex >= 0 && editingRowIndex < dataTable.getItems().size()) {
            try {
                validateInputs();
                ObservableList<StringProperty> row = dataTable.getItems().get(editingRowIndex);
                // Обновляем значения StringProperty
                row.get(1).set(OT.getText());
                row.get(2).set(DO.getText());
                row.get(3).set(katigoria.getText());
                row.get(4).set(opisanie.getText());
                row.get(5).set(OT.getText() + "-" + DO.getText());

                dataTable.refresh();
                // Получаем исходную длину
                String DLINA_TOH = dlina.getText().trim().replace(',', '.');;
                double DLINA_dobl = Double.parseDouble(DLINA_TOH);

                // Проверяем все строки в таблице
                boolean allValid = true;
                double totalSum = DLINA_dobl;

                for (ObservableList<StringProperty> tableRow : dataTable.getItems()) {
                    try {
                        // Безопасное получение значений
                        if (tableRow.size() <= 2 || tableRow.get(1) == null || tableRow.get(2) == null) {
                            continue;
                        }

                        String rowOT = tableRow.get(1).get();
                        String rowDO = tableRow.get(2).get();

                        if (rowOT == null || rowOT.isEmpty() || rowDO == null || rowDO.isEmpty()) {
                            continue;
                        }

                        String OT_TOH = rowOT.trim().replace(',', '.');
                        String DO_TOH = rowDO.trim().replace(',', '.');

                        double OT_dobl = Double.parseDouble(OT_TOH);
                        double DO_dobl = Double.parseDouble(DO_TOH);

                        // Проверяем условия для каждой строки
                        if (OT_dobl >= DO_dobl) {
                            showAlert("Ошибка в строке: Конечный интервал меньше или равен начальному");
                            allValid = false;
                            break;
                        }

                        // Вычисляем сумму для каждой строки
                        totalSum += (OT_dobl - DO_dobl);

                    } catch (NumberFormatException e) {
                        showAlert("Ошибка в числовых полях строки: " + e.getMessage());
                        allValid = false;
                        break;
                    }
                }
                // Если все строки валидны и итоговая сумма >= 0
                if (allValid && totalSum >= 0) {
                    // Все данные валидны - выполняем сохранение
                    editingRowIndex = -1;  // Сбрасываем индекс редактирования

                    OT.setText(DO.getText());
                    DO.setText("");
                    OSTATOK.setText(String.valueOf(totalSum));
                    katigoria.setText("");
                    opisanie.setText("");
                    obnul("x", "");

                    dataTable.refresh(); // Обновляем таблицу_1
                } else if (allValid) {
                    showAlert("Длина интервала больше длины выработки");
                }

            } catch (ValidationException e) {
                showAlert(e.getMessage());
                return;
            } catch (NumberFormatException e) {
                showAlert("Ошибка в числовых полях: " + e.getMessage());
            } catch (Exception e) {
                showAlert("Произошла ошибка: " + e.getMessage());
            }
        }
    }
    private void doble_Row() {
        if (editingRowIndex >= 0 && editingRowIndex < dataTable.getItems().size()) {
            try {
                validateInputs();
                // Получаем список всех строк
                ObservableList<ObservableList<StringProperty>> allRows = dataTable.getItems();

                double DO_dobl = Double.parseDouble(DO.getText().trim().replace(',', '.'));
                String DO_string = String.valueOf(DO_dobl / 2);

                ObservableList<StringProperty> currentRow = allRows.get(editingRowIndex);
                currentRow.get(1).set(OT.getText());
                currentRow.get(2).set(DO_string);
                currentRow.get(3).set(katigoria.getText());
                currentRow.get(4).set(opisanie.getText());
                currentRow.get(5).set(OT.getText() + "-" + DO_string);

                ObservableList<StringProperty> newRow = FXCollections.observableArrayList();

                for (int i = 0; i < currentRow.size(); i++) {
                    newRow.add(new SimpleStringProperty(""));
                }
                // Заполняем новую строку
                newRow.get(0).set(String.valueOf(allRows.size() + 1));
                newRow.get(1).set(DO_string);
                newRow.get(2).set(DO.getText());
                newRow.get(3).set("");

                newRow.get(4).set("");
                newRow.get(5).set(DO_string + "-" + DO.getText());
                newRow.get(6).set("");

                // Вставляем новую строку ПОСЛЕ текущей
                allRows.add(editingRowIndex + 1, newRow);
                // Обновляем номера строк для всех последующих строк
                updateRowNumbers();
                // Сбрасываем состояние редактирования
                editingRowIndex = -1;
                // Обновляем поля формы
                OT.setText(DO.getText());
                DO.setText("");
                katigoria.setText("");
                opisanie.setText("");
                obnul("x", "");
                dataTable.refresh();
            } catch (ValidationException e) {
                showAlert(e.getMessage());
            } catch (NumberFormatException e) {
                showAlert("Ошибка в числовых полях: " + e.getMessage());
            } catch (Exception e) {
                showAlert("Произошла ошибка: " + e.getMessage());
            }
        }
    }
    private void getPas_Table() {
        ObservableList<ObservableList<StringProperty>> allRows = dataTable.getItems();
        for (int i = 0; i < allRows.size(); i++) {
            ObservableList<StringProperty> row = allRows.get(i);
            if (row.size() > 0) {
              getPas(row.get(3).get());
                row.get(6).set(this.tippas_ROW);
            }
        }
    }
    // Метод для обновления номеров строк
    private void updateRowNumbers() {
        ObservableList<ObservableList<StringProperty>> allRows = dataTable.getItems();
        for (int i = 0; i < allRows.size(); i++) {
            ObservableList<StringProperty> row = allRows.get(i);
            if (row.size() > 0) {
                row.get(0).set(String.valueOf(i + 1));
            }
        }
    }
    private void clearTable() {
        dataTable.getItems().clear();
    }

    private void setupEventHandlers() {
        // 2. Обработчик двойного клика по таблице
        dataTable.setOnMouseClicked(event -> {
            System.out.println("Кликов: " + event.getClickCount()); // Для отладки
            if (event.getClickCount() == 1) {editSelectedRow();}
            if (event.getClickCount() == 2) {
                // Простой способ получить позицию
                int row = dataTable.getSelectionModel().getSelectedIndex();
                int column = dataTable.getFocusModel().getFocusedCell().getColumn();
                System.out.println("Двойной клик по строке: " + row + ", столбец: " + column);
                // Пробуем начать редактирование
                if (row >= 0 && column >= 0) {
                    dataTable.edit(row, dataTable.getColumns().get(column));
                }
            }

        });
        // 3. Обработчик изменения данных в таблице
        dataTable.getItems().addListener((ListChangeListener.Change<?> change) -> {
            System.out.println("Данные таблицы изменились");
            System.out.println("Текущее количество строк: " + dataTable.getItems().size());
        });
    }
    private void loadDataFromDatabase() {
        try {
            DatabaseHandler tableDAO = new DatabaseHandler();
            // Получаем данные из БД
            List<Baza_Geolg> rows = tableDAO.getAllRows(nomer.getText());

            // Очищаем таблицу
            dataTable.getItems().clear();
            ObservableList<ObservableList<StringProperty>> items = dataTable.getItems();
            // Заполняем таблицу данными из БД
            for (Baza_Geolg row : rows) {
                ObservableList<StringProperty> tableRow = FXCollections.observableArrayList(
                        new SimpleStringProperty(String.format(String.valueOf(items.size()+1))),
                        new SimpleStringProperty(row.getcolumnOT()),
                        new SimpleStringProperty(row.getcolumnDO()),
                        new SimpleStringProperty( row.getcolumnKLASS()),
                        new SimpleStringProperty(row.getcolumnOPIS()),
                        new SimpleStringProperty(row.getcolumnOTDO()),
                        new SimpleStringProperty(row.getcolumnLIST())
                );
                dataTable.getItems().add(tableRow);
            }
        } catch (Exception e) {
            showAlert( "Не удалось загрузить данные из БД: " + e.getMessage());
            e.printStackTrace();
        }
    }
   }




