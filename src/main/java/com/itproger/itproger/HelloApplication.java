package com.itproger.itproger;

import com.itproger.itproger.models.User;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.SQLException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException, ClassNotFoundException {
        // Проверка подлючения к БД
        DB db = new DB();
//        try {
//            db.isConnected();
//        } catch (SQLException | ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }

// Вынесли в отельный метод, т.к. повторялся
//        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main.fxml"));
//        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
//        stage.setTitle("Программа itProger!");
//        stage.setResizable(false);
//        stage.setScene(scene);
//        stage.show();
        // Если существует сохраненный файл
        File file = new File("user.settings");
        if(file.exists()) {
            // Получаем данные (объект) из сохраненного файла
            FileInputStream fis = new FileInputStream("user.settings");
            ObjectInputStream ois = new ObjectInputStream(fis);
            User user = (User) ois.readObject();
            ois.close();

            if (db.isExistUser(user.getLogin())) // Если пользователь не пустая строка (есть данные)
                setScene("articles-panel.fxml", stage);
            else
                setScene("main.fxml", stage);
        } else
            setScene("main.fxml", stage);
    }

    public static void main(String[] args) {
        launch();
    }

    // Выносим повторяющийся кусок в отдельный метод
    public static void setScene(String sceneName, Stage stage) throws IOException {
        // код взят из HelloApplication.java для подгрузки другой страницы
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(sceneName));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        //Stage stage =(Stage) ((Node) event.getSource()).getScene().getWindow(); //Берем текущее окно
        stage.setTitle("Программа itProger!");
        stage.setResizable(false);          // меняем для него настройки
        stage.setScene(scene);
        stage.show();

    }
}