package com.itproger.itproger.controllers;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;

import com.itproger.itproger.DB;
import com.itproger.itproger.HelloApplication;
import com.itproger.itproger.models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class RegController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button auth_btn;

    @FXML
    private TextField auth_login;

    @FXML
    private PasswordField auth_pass;

    @FXML
    private Button reg_btn;

    @FXML
    private TextField reg_email;

    @FXML
    private TextField reg_login;

    @FXML
    private PasswordField reg_pass;

    @FXML
    private Hyperlink reg_rights;

    @FXML
    private CheckBox reg_rights_chb;

    private DB db = new DB();

    @FXML
    void initialize() {
        reg_btn.setOnAction(event -> {
            registrationUser();
        });
        auth_btn.setOnAction(event -> {
            try {
                authUser(event);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

    }

    private void authUser(ActionEvent event) throws IOException {
        String login =  auth_login.getCharacters().toString();
        String pass =  auth_pass.getCharacters().toString();

        // При повторном нажатии устанавливаем исходный цвет обводки
        auth_login.setStyle("-fx-border-color: #fafafa");
        auth_pass.setStyle("-fx-border-color: #fafafa");

        // Проверяем, все ли поля заполнены корректно, если нет, делаем красную обводку
        if(login.length() <= 1)
            auth_login.setStyle("-fx-border-color: #e06249");
        else if (pass.length() <= 3 )
            auth_pass.setStyle("-fx-border-color: #e06249");
        else if (!db.authUser(login, md5String(pass)))
            auth_btn.setText("Пользователя нет");
        else {
            auth_login.setText("");  // Если пользователь существует
            auth_pass.setText("");   // очищаем поля
            auth_btn.setText("Все готово :)");

            // Перед переходом на новую сцену выполняем сериализацию
            // (сохранение данных пользователя)
            FileOutputStream fos  = new FileOutputStream("user.settings");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(new User(login));
            oos.close();


            Stage stage =(Stage) ((Node) event.getSource()).getScene().getWindow(); //Берем текущее окно
            HelloApplication.setScene("articles-panel.fxml", stage);
            // код взят из HelloApplication.java для подгрузки другой страницы
//            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("articles-panel.fxml"));
//            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
//            Stage stage =(Stage) ((Node) event.getSource()).getScene().getWindow(); //Берем текущее окно
//            //stage.setTitle("Программа itProger!");
//            stage.setResizable(false);          // меняем для него настройки
//            stage.setScene(scene);
//            stage.show();
        }
    }

    private void registrationUser() {
        String login =  reg_login.getCharacters().toString();
        String email =  reg_email.getCharacters().toString();
        String pass =  reg_pass.getCharacters().toString();

        // При повторном нажатии устанавливаем исходный цвет обводки
        reg_login.setStyle("-fx-border-color: #fafafa");
        reg_email.setStyle("-fx-border-color: #fafafa");
        reg_pass.setStyle("-fx-border-color: #fafafa");

        // Проверяем, все ли поля заполнены корректно, если нет, делаем красную обводку
        if(login.length() <= 1)
            reg_login.setStyle("-fx-border-color: #e06249");
        else if (email.length() <= 5 || !email.contains("@") || !email.contains("."))
            reg_email.setStyle("-fx-border-color: #e06249");
        else if (pass.length() <= 3 )
            reg_pass.setStyle("-fx-border-color: #e06249");
        else if (!reg_rights_chb.isSelected())
            reg_btn.setText("Поставьте галочку");
        else if (db.isExistUser(login))
            reg_btn.setText("Введите другой логин ");
        else {
            db.regUser(login, email, md5String(pass));
            reg_login.setText("");  // Если пользователь существует
            reg_email.setText("");  // очищаем поля
            reg_pass.setText("");
            reg_btn.setText("Все готово :)");
        }
    }

    // Кеширование (кодирование) пароля
    public static String md5String (String pass) {
        MessageDigest messageDigest = null;     // спец класс, который кодирует принимаемую строку
        byte[] digest = new byte[0];            // массив, который будет хранить строку в виде 0101

        try {
            messageDigest = messageDigest.getInstance("MD5");   // Метод кеширования
            messageDigest.reset();                      //очищаем объект от лишнего
            messageDigest.update(pass.getBytes());      // преобразуем полученную строку в байты
            digest = messageDigest.digest();            // заносим байтв в массив
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        BigInteger bigInteger = new BigInteger(1,digest); //преобразуме массив байт в большое число
        String md5Hex = bigInteger.toString(16);            //преобразуем число в строку

        while (md5Hex.length() < 32)        //обрезаем строку до 32 - кол-во символов в "MD5"
            md5Hex = "0" + md5Hex;          //вначале каждого символа добовляем 0

        return md5Hex;
    }


}
