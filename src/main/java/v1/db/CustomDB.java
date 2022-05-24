package v1.db;

import java.sql.Connection;

public interface CustomDB {
    String password = "admin4";
    String dbName = "tea_shop";
    String[] serverNames = {"localhost"};
    String user = "tea_shop_owner";

    void establishConnection();
    Connection getConnection();

    boolean verifyUser(String name, String password);
    void addClient (String username, String password, String city, String country, String email);
    void closeConnection();
}
