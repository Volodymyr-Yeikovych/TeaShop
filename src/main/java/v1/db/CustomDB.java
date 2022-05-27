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

    String getClientCity(String username, String password);

    Optional<ResultSet> getShops();

    Optional<ResultSet> getShops(String city);

    String getClientAddress(String username, String password);

    ResultSet getStock();

    String getShopAddress(long shopId);

    String getShopAddress(String city);

    void commit();

    void rollback();

    void beginTransaction();

    boolean checkIfKgNumberAvailable(int shopId, int kgRequested);

    boolean moreThenOneShopPresent(String city);

    boolean userExists(String name, String password);

    void addClient(String username, String password, String street, String city, String country, String email);

    boolean checkIfTeaExists(int shopId);

    boolean validateStock(int teaId, int kgRequested);

    void closeConnection();
}
