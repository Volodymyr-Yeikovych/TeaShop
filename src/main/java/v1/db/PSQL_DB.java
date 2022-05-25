package v1.db;

import org.postgresql.ds.PGSimpleDataSource;
import v1.exceptions.SQLExceptionWrapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class PSQL_DB implements CustomDB {
    private Connection psqlConnection;


    @Override
    public void establishConnection() {
        PGSimpleDataSource source = new PGSimpleDataSource();
        source.setDatabaseName(dbName);
        source.setServerNames(serverNames);

        try {
            psqlConnection = source.getConnection(user, password);
        } catch (SQLException e) {
            throw new SQLExceptionWrapper(e);
        }
    }

    @Override
    public Connection getConnection() {
        if (psqlConnection == null) establishConnection();
        return psqlConnection;
    }

    @Override
    public void commit() {
        try {
            PreparedStatement commit = getConnection().prepareStatement("COMMIT;");
            commit.executeUpdate();
        } catch (SQLException e) {
            throw new SQLExceptionWrapper(e);
        }
    }

    @Override
    public void rollback() {
        try {
            PreparedStatement rollback = getConnection().prepareStatement("ROLLBACK;");
            rollback.executeUpdate();
        } catch (SQLException e) {
            throw new SQLExceptionWrapper(e);
        }
    }

    @Override
    public void beginTransaction() {
        try {
            PreparedStatement beginTransaction = getConnection().prepareStatement("BEGIN TRANSACTION;");
            beginTransaction.executeUpdate();
        } catch (SQLException e) {
            throw new SQLExceptionWrapper(e);
        }
    }

    @Override
    public String getClientCity(String username, String password) {
        try {
            PreparedStatement clCity = getConnection().prepareStatement
                    ("SELECT city FROM clients WHERE username = ?, password = ?;");
            clCity.setString(1, username);
            clCity.setString(2, password);
            ResultSet city = clCity.executeQuery();
            city.next();
            return city.getString("city");
        } catch (SQLException e) {
            throw new SQLExceptionWrapper(e);
        }
    }

    @Override
    public Optional<ResultSet> getShops() {
        try {
            PreparedStatement shops = getConnection().prepareStatement
                    ("SELECT sh_id, street, city, country, work_hours FROM shops ORDER BY sh_id;");
            return Optional.ofNullable(shops.executeQuery());
        } catch (SQLException e) {
            throw new SQLExceptionWrapper(e);
        }
    }

    @Override
    public Optional<ResultSet> getShops(String city) {
        try {
            PreparedStatement shops = getConnection().prepareStatement
                    ("SELECT sh_id, street, city, country, work_hours FROM shops WHERE city = ? ORDER BY sh_id;",
                            ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            shops.setString(1, city);
            ResultSet rs = shops.executeQuery();
            if (!rs.next()) return Optional.empty();
            rs.previous();
            return Optional.of(rs);
        } catch (SQLException e) {
            throw new SQLExceptionWrapper(e);
        }
    }

    @Override
    public boolean verifyUser(String name, String password) {
        try {
            PreparedStatement pst = getConnection().prepareStatement
                    ("SELECT * FROM clients WHERE cl_name = ? AND cl_password = ?;");
            pst.setString(1, name);
            pst.setString(2, password);
            ResultSet user = pst.executeQuery();
            return user.next();
        } catch (SQLException e) {
            throw new SQLExceptionWrapper(e);
        }
    }

    @Override
    public void addClient(String username, String password, String city, String country, String email) {
        try {
            PreparedStatement insertUser = getConnection().prepareStatement
                    ("INSERT INTO clients (cl_name, city, country, email, cl_password) VALUES " +
                            "(?, ?, ?, ?, ?);");
            insertUser.setString(1, username);
            insertUser.setString(2, city);
            insertUser.setString(3, country);
            insertUser.setString(4, email);
            insertUser.setString(5, password);
            insertUser.executeUpdate();
        } catch (SQLException e) {
            throw new SQLExceptionWrapper(e);
        }
    }

    @Override
    public boolean checkIfTeaExists(int shopId) {
        if (shopId <= 0) return false;
        try {
            return getStock().absolute(shopId);
        } catch (SQLException e) {
            throw new SQLExceptionWrapper(e);
        }
    }

    @Override
    public boolean validateStock(int teaId, int kgRequested) {
        try {
            int margin = getTeaNumAvailable(teaId) - kgRequested;
            PreparedStatement update = getConnection().prepareStatement
                    ("UPDATE stock SET available_kg = ? WHERE tea_id = ?;");

            if (margin < 30) orderMoreTea(teaId);
            if (margin < 0) return false;

            update.setInt(1, margin);
            update.setInt(2, teaId);
            update.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new SQLExceptionWrapper(e);
        }
    }

    private int getTeaNumAvailable(int teaId) {
        try {
            PreparedStatement selectKgAvailable = getConnection().prepareStatement
                    ("SELECT available_kg FROM stock WHERE tea_id = ?;");
            selectKgAvailable.setInt(1, teaId);
            ResultSet dbVar = selectKgAvailable.executeQuery();
            dbVar.next();
            return dbVar.getInt("available_kg");
        } catch (SQLException e) {
            throw new SQLExceptionWrapper(e);
        }
    }

    private void orderMoreTea(int teaId) {
        try {
            PreparedStatement orderTea = getConnection().prepareStatement
                    ("UPDATE stock SET available_kg = ? WHERE tea_id = ?;");
            orderTea.setInt(1, getTeaNumAvailable(teaId) + 100);
            orderTea.setInt(2, teaId);
        } catch (SQLException e) {
            throw new SQLExceptionWrapper(e);
        }
    }

    @Override
    public boolean checkIfKgNumberAvailable(int shopId, int kgRequested) {
        try {
            ResultSet stock = getStock();
            stock.absolute(shopId);
            return stock.getInt("available_kg") > kgRequested && kgRequested > 0;
        } catch (SQLException e) {
            throw new SQLExceptionWrapper(e);
        }
    }

    @Override
    @SuppressWarnings("all")
    public boolean moreThenOneShopPresent(String city) {
        ResultSet shops = getShops(city).get();
        try {
            return shops.absolute(2);
        } catch (SQLException e) {
            throw new SQLExceptionWrapper(e);
        }
    }

    @Override
    public void closeConnection() {
        try {
            psqlConnection.close();
        } catch (SQLException e) {
            throw new SQLExceptionWrapper(e);
        }
    }

    @Override
    public String getClientAddress(String username, String password) {
        try {
            PreparedStatement clAddress = getConnection().prepareStatement
                    ("SELECT street, city, country FROM clients WHERE username = ?, password = ?;");
            clAddress.setString(1, username);
            clAddress.setString(2, password);
            ResultSet address = clAddress.executeQuery();
            address.next();
            return String.format("%s, %s, %s", address.getString("street"),
                    address.getString("city"), address.getString("country"));
        } catch (SQLException e) {
            throw new SQLExceptionWrapper(e);
        }
    }

    @Override
    public ResultSet getStock() {
        try {
            PreparedStatement stock = getConnection().prepareStatement
                    ("SELECT tea_id, tea_name, country, price_kg, available_kg FROM stock;",
                            ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            return stock.executeQuery();
        } catch (SQLException e) {
            throw new SQLExceptionWrapper(e);
        }
    }

    @Override
    public String getShopAddress(long shopId) {
        try {
            PreparedStatement shop = getConnection().prepareStatement
                    ("SELECT street, city, country FROM shops WHERE sh_id = ?;");
            shop.setLong(1, shopId);
            ResultSet rs = shop.executeQuery();
            rs.next();
            return String.format("%s, %s, %s", rs.getString("street"),
                    rs.getString("city"), rs.getString("country"));
        } catch (SQLException e) {
            throw new SQLExceptionWrapper(e);
        }
    }

    @Override
    public String getShopAddress(String city) {
        try {
            PreparedStatement shop = getConnection().prepareStatement
                    ("SELECT street, city, country FROM shops WHERE city = ?;");
            shop.setString(1, city);
            ResultSet rs = shop.executeQuery();
            rs.next();
            return String.format("%s, %s, %s", rs.getString("street"),
                    rs.getString("city"), rs.getString("country"));
        } catch (SQLException e) {
            throw new SQLExceptionWrapper(e);
        }
    }

}
