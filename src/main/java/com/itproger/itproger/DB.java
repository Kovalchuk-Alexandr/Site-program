package com.itproger.itproger;

import java.sql.*;

public class DB {

    private final String HOST = "localhost";
    private final String PORT = "3306";
    private final String DB_NAME = "itproger_java";
    private final String LOGIN = "root";
    private final String PASSWORD = "root";

    private Connection dbConn = null;

    private Connection getDbConnection() throws ClassNotFoundException, SQLException {
        // создание строки подключения
        String connStr = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DB_NAME;
        //При подключении может возникнуть ошибка, что будет связана с разными часовыми поясами.
        // Чтобы её исправить пропишите к строке подключения дополнительные опции:
//jdbc:mysql://localhost/db?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC
        Class.forName("com.mysql.cj.jdbc.Driver");

        // установка соединения
        dbConn = DriverManager.getConnection(connStr, LOGIN, PASSWORD);
        return dbConn;
    }

    // Проверка подключения
    public void isConnected() throws SQLException, ClassNotFoundException {
        dbConn = getDbConnection();
        System.out.println(dbConn.isValid(1000));
    }

    // Проверяем, есть ли такой пользователь
    public boolean isExistUser(String login) {
        String sql = "SELECT `id` FROM `users` WHERE `login` = ?";
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(sql);
            prSt.setString(1, login);

            ResultSet res = prSt.executeQuery();
            return res.next();  // Находим первую попавшуюся запись, если есть
                                // возвращаем thrue
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        //return false;
    }

    public void regUser(String login, String email, String pass) {
        String sql = "INSERT INTO `users` (`login`, `email`, `password`) VALUES (?, ?, ?)";

        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(sql);
            prSt.setString(1, login);
            prSt.setString(2, email);
            prSt.setString(3, pass);
            prSt.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    public boolean authUser(String login, String pass) {
        String sql = "SELECT `id` FROM `users` WHERE `login` = ? AND `password` = ?";
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(sql);
            prSt.setString(1, login);
            prSt.setString(2, pass);

            ResultSet res = prSt.executeQuery();
            return res.next();  // Находим первую попавшуюся запись, если есть
            // возвращаем thrue
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public ResultSet getArticles() {
        String sql = "SELECT `title`,`intro` FROM `articles`";
        Statement statement = null;
        try {
            statement = getDbConnection().createStatement();
            return statement.executeQuery(sql);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void addArticle(String title, String intro, String full_text) {
        String sql = "INSERT INTO `articles` (`title`, `intro`, `text`) VALUES (?, ?, ?)";

        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(sql);
            prSt.setString(1, title);
            prSt.setString(2, intro);
            prSt.setString(3, full_text);
            prSt.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
}
