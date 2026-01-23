package com.example.proburok.job;


import com.example.proburok.New_Class.ConfigLoader;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;

public class Configs {
    protected String dbHost = ConfigLoader.getProperty("dbHost");
    protected String dbPort = "3306";
    protected String dbUser = "TOPA";
    protected String dbPass = "300122";
    protected String dbName = "gorizont";

    protected String OUTPUT_PATH = ConfigLoader.getProperty("OUTPUT_PATH");
    protected String Put =  ConfigLoader.getProperty("Put");

    protected String Put_albom ="/com/example/proburok/docs/AlbomV.pdf";

    protected String Put_geolog ="/com/example/proburok/docs/Geologi.pdf";
    protected String Put_instr ="/com/example/proburok/docs/instruk.pdf";
    protected String Put_othet ="/com/example/proburok/docs/Othet.pdf";

    protected String Put_texusovia ="/com/example/proburok/docs/texuslovia.pdf";
    protected String TEMPLATE_PATH1 = "/com/example/proburok/docs/template_1.docx";
    protected String TEMPLATE_PATH2 = "/com/example/proburok/docs/template_2.docx";
    protected String TEMPLATE_PATH3 = "/com/example/proburok/docs/template_3.docx";

    protected String TEMPLATE_PATH4 = "/com/example/proburok/docs/template_4.docx";

    protected String TEMPLATE_PATH5 = "/com/example/proburok/docs/template_5.docx";
    protected String TEMPLATE_PATH6 = "/com/example/proburok/docs/template_6.docx";
    protected String TEMPLATE_PATH7 = "/com/example/proburok/docs/template_7.docx";
    protected String TEMPLATE_PATH8 = "/com/example/proburok/docs/template_8.docx";
    protected String HABLON_PATH = "/com/example/proburok/hablon";

    protected String HABLON_PATH_VID =HABLON_PATH+"/obvid";
    protected String HABLON_PATH_SOPR =HABLON_PATH+"/soprigenii";

    protected String HABLON_PATH_ILIMENT =HABLON_PATH+"/ilement";

    protected String HABLON_PATH_USTANOVKA =HABLON_PATH+"/ustanovka";

    public void openImagedok(ImageView tablI,ImageView instI,ImageView geologI,ImageView geomexandpas,String pas){
    tablI.setOnMouseClicked(mouseEvent -> openNewScene("/com/example/proburok/app.fxml"));
    instI.setOnMouseClicked(mouseEvent -> OpenDok(Put_instr,"Инструкция_"));
    geologI.setOnMouseClicked(mouseEvent -> OpenDok(Put_geolog,"Геология Гайского месторождения_"));
   if (pas.equals("yes")){
        geomexandpas.setOnMouseClicked(mouseEvent -> {OpenDok(Put_albom,"АЛЬБОМ ТИПОВЫХ ПАСПОРТОВ КРЕПЛЕНИЯ_");});
    }else{
    geomexandpas.setOnMouseClicked(mouseEvent -> OpenDok(Put_texusovia,"Технологические условия_"));}
    }
    public void validateField(String value, String fieldName, StringBuilder errors) {
        if (value == null || value.trim().isEmpty()) {
            errors.append("- Не заполнено поле ").append(fieldName).append("\n");
        }
    }
    public void openNewScene(String Window){
        // Загрузка нового окна
        FXMLLoader loader = new FXMLLoader();
        // Проверка пути к FXML-файлу
        URL fxmlUrl = getClass().getResource(Window);
        loader.setLocation(fxmlUrl);
        try {
            Parent root = loader.load(); // Загрузка FXML
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void OpenDok(String Put,String NemDok){
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            try {
                // Получаем поток входных данных для ресурса
                InputStream inputStream = getClass().getResourceAsStream(Put);
                if (inputStream == null) {
                    throw new FileNotFoundException("Ресурс не найден: " + Put);
                }
                // Создаем временный файл
                Path tempFile = Files.createTempFile(NemDok, ".pdf");
                Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);
                // Открываем временный файл
                desktop.open(tempFile.toFile());
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
        }
    }
    protected void setupCursor(ImageView VNS,ImageView VKL,ImageView VNSNE,ImageView VKLNE,ImageView OBNOV) {
        String hoverStyle = "-fx-cursor: hand;";//рука
        VNS.setStyle(hoverStyle);
        VKL.setStyle(hoverStyle);
        VNSNE.setStyle(hoverStyle);
        VKLNE.setStyle(hoverStyle);
        OBNOV.setStyle(hoverStyle);
    }
    protected void setupCursor_2(ImageView VKL,ImageView VKLNE) {
        String hoverStyle = "-fx-cursor: hand;";//рука
        VKL.setStyle(hoverStyle);
        VKLNE.setStyle(hoverStyle);
    }


    protected void setupTooltips(ImageView VNS,ImageView VKL,ImageView VKLNE,ImageView OBNOV,String T_VNS,String T_VKL,String T_VKLNE,String T_OBNOV) {
        Tooltip.install(VNS, createTooltip(T_VNS));
        Tooltip.install(VKL, createTooltip(T_VKL));
        Tooltip.install(VKLNE, createTooltip(T_VKLNE));
        Tooltip.install(OBNOV, createTooltip(T_OBNOV));
    }
    protected Tooltip createTooltip(String text) {
        Tooltip tooltip = new Tooltip(text);
        tooltip.setShowDelay(Duration.millis(300));
        tooltip.setStyle("-fx-font-size: 14; -fx-background-color: #aa9455;");
        return tooltip;
    }
    protected void openImageHandler(ImageView imageView, String folder, ImageView neimage,String pas) {
        imageView.setOnMouseClicked(e -> {

            if (pas.isEmpty())  return;
            openImage(Put + "/" + pas + "/" + folder,imageView,neimage,"yes");
        });
    }
    protected void obnovaImageHandler(ImageView imageView, String folder,String pas) {
        imageView.setOnMouseClicked(e -> {
            if (pas.isEmpty()) return;
            Sosdatpapky(pas,folder,imageView);
        });
    }
    public void Sosdatpapky(String gor, String papka,ImageView pl){

        String path = Put + "/" + gor + "/" + papka;
        File newFolder = new File(path);

        // Создаем папку, если её нет
        if (!newFolder.exists()) {
            newFolder.mkdirs();
        }

        // Удаляем все существующие файлы в папке
        File[] existingFiles = newFolder.listFiles();
        if (existingFiles != null) {
            for (File file : existingFiles) {
                if (!file.isDirectory()) {
                    file.delete();
                }
            }
        }
        // Диалог выбора файла
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите изображение");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Изображения", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        Window mainWindow = pl.getScene().getWindow(); // Получаем текущее окно
        File selectedFile = fileChooser.showOpenDialog(mainWindow);

        if (selectedFile != null) {
            try {
                File destinationFile = new File(newFolder.getAbsolutePath() + "/" + selectedFile.getName());
                Files.copy(selectedFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Файл скопирован: " + destinationFile.getAbsolutePath());
            } catch (IOException e) {
                System.err.println("Ошибка при копировании файла: " + e.getMessage());
            }
        }
    }
    protected void sozdaniiImageHandler(ImageView imageView, String folder, ImageView image1, ImageView image2, ImageView image3, ImageView image4,ImageView im,String pas) {
        imageView.setOnMouseClicked(e -> {
            System.out.println("Файл скопирован: " + pas);
              if (pas.isEmpty()) return;
            Sosdatpapky(pas,folder,im);
            image1.setVisible(true); image2.setVisible(false);
            image3.setVisible(false); image4.setVisible(true);
        });}

    public void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Внимание!");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    void openImage(String imagePath, ImageView VKL, ImageView VKLNE, String open) {
        File folder = new File(imagePath);

        // Проверяем, существует ли папка
        if (!folder.exists() || !folder.isDirectory()) {
            VKL.setVisible(false);
            VKLNE.setVisible(true);
            //System.err.println("Папка не найдена: " + imagePath);
            return;
        }
        if (open.equals("yes")) {
            // Получаем список файлов в папке
            File[] files = folder.listFiles((dir, name) -> {
                // Фильтруем файлы по расширению (изображения)
                String lowerCaseName = name.toLowerCase();
                return lowerCaseName.endsWith(".jpg") || lowerCaseName.endsWith(".png") ||
                        lowerCaseName.endsWith(".jpeg") || lowerCaseName.endsWith(".gif");
            });

            // Проверяем, есть ли изображения в папке
            if (files != null && files.length > 0) {
                // Берем первый файл (первое изображение)
                File imageFile = files[0];

                try {
                    // Открываем изображение
                    Desktop.getDesktop().open(imageFile);
                    System.out.println("Открыто изображение: " + imageFile.getAbsolutePath());
                } catch (IOException e) {
                    System.err.println("Не удалось открыть изображение: " + e.getMessage());
                }
            } else {
                System.err.println("В папке нет изображений.");
            }
        }
    }
    void openPas(String imagePath, int pas) {
        File folder = new File(imagePath);

        if (!folder.exists() || !folder.isDirectory()) {
            System.err.println("Папка не найдена: " + imagePath);
            return;
        }

        // Получаем список файлов
        File[] files = folder.listFiles((dir, name) -> {
            String lowerCaseName = name.toLowerCase();
            return lowerCaseName.endsWith(".jpg") || lowerCaseName.endsWith(".png") ||
                    lowerCaseName.endsWith(".jpeg") || lowerCaseName.endsWith(".gif");
        });

        if (files != null && files.length > 0) {
            // СОРТИРУЕМ файлы по числовому значению имени
            Arrays.sort(files, (a, b) -> {
                try {
                    // Извлекаем числовую часть имени файла (без расширения)
                    int numA = Integer.parseInt(a.getName().replaceAll("[^0-9]", ""));
                    int numB = Integer.parseInt(b.getName().replaceAll("[^0-9]", ""));
                    return Integer.compare(numA, numB);
                } catch (NumberFormatException e) {
                    // Если не удалось извлечь число, сортируем по имени
                    return a.getName().compareTo(b.getName());
                }
            });

            // Теперь файлы отсортированы: 1.jpg, 2.jpg, 3.jpg, ..., 16.jpg
            if (pas < 0 || pas >= files.length) {
                System.err.println("Номер изображения " + (pas+1) + " выходит за пределы (1-" + files.length + ")");
                return;
            }

            File imageFile = files[pas];
            try {
                Desktop.getDesktop().open(imageFile);
                System.out.println("Открыто изображение: " + imageFile.getAbsolutePath());
            } catch (IOException e) {
                System.err.println("Не удалось открыть изображение: " + e.getMessage());
            }
        } else {
            System.err.println("В папке нет изображений.");
        }
    }
    void proverkaImage(String imagePath,ImageView VNS,ImageView VNSNE,ImageView VKL,ImageView VKLNE,ImageView OBNV) {
        File folder = new File(imagePath);
        // Проверяем, существует ли папка
        String PutPlan=Put + "//" + "План";
        String PutPoper=Put + "//" +  "Поперечный";
        String PutProdol=Put + "//" + "Продольный";
        String PutSxema=Put + "//" + "Схема";

        if ( PutPlan.equals(imagePath)||PutPoper.equals(imagePath)||PutProdol.equals(imagePath)||PutSxema.equals(imagePath)){

            VNS.setVisible(false); VNSNE.setVisible(true);
            VKL.setVisible(false);VKLNE.setVisible(true);OBNV.setVisible(false);
            return;
        }
        if (!folder.exists() || !folder.isDirectory()) {
            System.err.println("Папка не найдена: " + imagePath);
            VNS.setVisible(true); VNSNE.setVisible(false);
            VKL.setVisible(false);VKLNE.setVisible(true);OBNV.setVisible(false);
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

            VNS.setVisible(false); VNSNE.setVisible(false);
            VKL.setVisible(true);VKLNE.setVisible(false);OBNV.setVisible(true);

        } else {
            System.err.println("В папке нет изображений.");
            VNS.setVisible(true); VNSNE.setVisible(false);
            VKL.setVisible(false);VKLNE.setVisible(true);OBNV.setVisible(false);
        }
    }
    public boolean checkAllFoldersHaveImages(String nomerValue) {
        String[] folderTypes = {"План", "Поперечный", "Продольный"};

        for (String folderType : folderTypes) {
            File folder = new File(Put, nomerValue + File.separator + folderType);

            if (!folder.exists() || !folder.isDirectory()) {
                return false;
            }

            File[] files = folder.listFiles((dir, name) -> {
                String lowerCaseName = name.toLowerCase();
                return lowerCaseName.endsWith(".jpg") || lowerCaseName.endsWith(".png") ||
                        lowerCaseName.endsWith(".jpeg") || lowerCaseName.endsWith(".gif");
            });

            if (files == null || files.length == 0) {
                return false;
            }
        }
        return true;
    }

}