package v1.db;

import org.postgresql.ds.PGSimpleDataSource;
import v1.exceptions.SQLExceptionWrapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
            PreparedStatement pst = getConnection().prepareStatement
                    ("INSERT INTO clients (cl_name, city, country, email, cl_password) VALUES " +
                            "(?, ?, ?, ?, ?);");
        pst.setString(1, username);
        pst.setString(2, city);
        pst.setString(3, country);
        pst.setString(4, email);
        pst.setString(5, password);
        pst.executeQuery();
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
