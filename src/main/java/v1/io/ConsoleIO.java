package v1.io;

import v1.exceptions.IOExceptionWrapper;
import v1.exceptions.SQLExceptionWrapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class ConsoleIO implements CustomIO{
    private static final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    @Override
    public char read() {
        try {
            return (char) reader.read();
        } catch (IOException e) {
            throw new IOExceptionWrapper(e);
        }
    }

    @Override
    public String readLine() {
        try {
            return reader.readLine();
        } catch (IOException e) {
            throw new IOExceptionWrapper(e);
        }
    }

    @Override
    public void write(String str) {
        System.out.print(str);
    }

    @Override
    public void writeln(String str) {
        System.out.println(str);
    }

    @Override
    public void writeln() {
        writeln("");
    }

    @Override
    public void antialiasing(long milliseconds) {
        Runtime process = Runtime.getRuntime();
        synchronized (process) {
            try {
                process.wait(milliseconds);
            } catch (InterruptedException e) {
                throw new IOExceptionWrapper(e);
            }
        }
    }

    @Override
    public void displayShops(ResultSet shops) {
        try {
            ResultSetMetaData shopsData = shops.getMetaData();
            for (int i = 1; i <= shopsData.getColumnCount(); i++) {
                write(shopsData.getColumnName(i) + " |");
            }
            writeln();
            while (shops.next()) {
                for (int i = 1; i <= shopsData.getColumnCount(); i++) {
                    write(shops.getString(i) + " |");
                }
                writeln();
            }
        } catch (SQLException e) {
            throw new SQLExceptionWrapper(e);
        }
    }

    @Override
    public void writef(String format, Object... args) {
        System.out.printf(format, args);
    }

    @Override
    public void close() {
        try {
            reader.close();
        } catch (IOException e) {
            throw new IOExceptionWrapper(e);
        }
    }
}
