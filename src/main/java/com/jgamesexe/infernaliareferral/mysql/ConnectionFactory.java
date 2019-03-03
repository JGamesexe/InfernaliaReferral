package com.jgamesexe.infernaliareferral.mysql;

import com.jgamesexe.infernaliareferral.Config;
import com.jgamesexe.infernaliareferral.Main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionFactory {

    private static Connection createConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            return DriverManager.getConnection("jdbc:mysql://" + Config.mysqlIP + ":" + Config.mysqlPort + "/" + Config.mysqlDatabase, Config.mysqlUser, Config.mysqlPass);
        } catch (ClassNotFoundException | SQLException e) {
            if (e instanceof ClassNotFoundException) {
                Main.log("§4[ERROR] §fJDBC MySQL Driver not found!");
            } else {
                Main.log("§4[ERROR] §fCouldn't create connection!");
                e.printStackTrace();
            }
            return null;
        }
    }

    public static void closeConnection(Connection connection) {
        try {
            if (connection.isClosed()) return;
            connection.close();
        } catch (SQLException e) {
            Main.log("§4[ERROR] §fCouldn't close connection!");
            e.printStackTrace();
        }
    }

    public static void initConnection() {
        try {
            Main.connection = ConnectionFactory.createConnection();
            Connection conn = Main.connection;

            Statement st;

            st = conn.createStatement();
            st.executeUpdate("CREATE TABLE IF NOT EXISTS referrals(uuid VARCHAR(36) PRIMARY KEY, referralPoints INT, referencePlayer VARCHAR(36), lastLevelReferenced SMALLINT)");
            st.close();

            st = conn.createStatement();
            st.executeUpdate("CREATE TABLE IF NOT EXISTS referrals_Log(whenIt BIGINT(20), uuidFrom VARCHAR(36), uuidTarget VARCHAR(36), amount TINYINT)");
            st.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
