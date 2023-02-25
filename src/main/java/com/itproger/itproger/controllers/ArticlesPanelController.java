package com.itproger.itproger.controllers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

import com.itproger.itproger.DB;
import com.itproger.itproger.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ArticlesPanelController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button exit_btn, add_article_btn;

    @FXML
    private VBox panelVBox;

    @FXML
    void initialize() throws SQLException, IOException {

        DB db = new DB();
        ResultSet resultSet = db.getArticles();
        while (resultSet.next()) {
            //Node node;
        //for (int i = 0; i < 10; i++) {

            Node node = FXMLLoader.load(Objects.requireNonNull(HelloApplication.class.getResource("article.fxml")));

            // Находим объекты по заголовкам
            Label title = (Label) node.lookup("#title");
            title.setText(resultSet.getString("title"));

            Label intro = (Label) node.lookup("#intro");
            intro.setText(resultSet.getString("intro"));

            // Дополнительные украшения/эффекты
            // При наведении мыши
            node.setOnMouseEntered(event -> {
                node.setStyle("-fx-background-color: #707173");
            });

            // При убирании мыши
            node.setOnMouseExited(event -> {
                node.setStyle("-fx-background-color: #343434");
            });

            // Создаем 10 кнопок с отступом в 10 пкс.
//            Button button = new Button("Кнопка");
//            button.setId("new_btn");
//            // Если нужно реализовать событие при нажатии на кнопку
//            //button.setOnAction(event -> {});
//            HBox hBox =new HBox();  // чтобы выровнять кнопку по центру
//            hBox.getChildren().add(node);
//            hBox.setAlignment(Pos.BASELINE_CENTER);
            panelVBox.getChildren().add(node);
            panelVBox.setSpacing(10);   // Отступы между объектами по 10 пикселей
        }

        exit_btn.setOnAction(event -> {
            try {
                exitUser(event);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        add_article_btn.setOnAction(event -> {
            Stage stage =(Stage) ((Node) event.getSource()).getScene().getWindow(); //Берем текущее окно
            try {
                HelloApplication.setScene("add_article.fxml", stage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

    }

    private void exitUser(ActionEvent event) throws IOException {
        File file = new File("user.settings");
        file.delete();

        Stage stage =(Stage) ((Node) event.getSource()).getScene().getWindow(); //Берем текущее окно
        HelloApplication.setScene("main.fxml", stage);

        // код взят из HelloApplication.java для подгрузки другой страницы
//        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main.fxml"));
//        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
//        Stage stage =(Stage) ((Node) event.getSource()).getScene().getWindow(); //Берем текущее окно
//        //stage.setTitle("Программа itProger!");
//        stage.setResizable(false);          // меняем для него настройки
//        stage.setScene(scene);
//        stage.show();
    }

}

