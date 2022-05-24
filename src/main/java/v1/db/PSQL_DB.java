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
                    ("SELECT * FROM shops ORDER BY sh_id;");
            return Optional.ofNullable(shops.executeQuery());
        } catch (SQLException e) {
            throw new SQLExceptionWrapper(e);
        }
    }

    @Override
    public Optional<ResultSet> getShops(String city) {
        try {
            PreparedStatement shops = getConnection().prepareStatement
                    ("SELECT * FROM shops WHERE city = ? ORDER BY sh_id;",
                            ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            shops.setString(1, city);
            ResultSet rs  = shops.executeQuery();
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
    public void closeConnection() {
        try {
            psqlConnection.close();
        } catch (SQLException e) {
            throw new SQLExceptionWrapper(e);
        }
    }


}
