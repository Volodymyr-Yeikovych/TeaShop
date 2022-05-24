package v1.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Optional;

public interface CustomDB {
    String password = "admin4";
    String dbName = "tea_shop";
    String[] serverNames = {"localhost"};
    String user = "tea_shop_owner";

    void establishConnection();
    Connection getConnection();
    void commit();
    void rollback();
    void beginTransaction();

    String getClientCity(String username, String password);
    Optional<ResultSet> getShops();
    Optional<ResultSet> getShops(String city);
    boolean verifyUser(String name, String password);
    void addClient (String username, String password, String city, String country, String email);
    void closeConnection();
}
