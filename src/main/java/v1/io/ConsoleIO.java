package v1.io;

import v1.exceptions.IOExceptionWrapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
                Runtime.getRuntime().wait(milliseconds);
            } catch (InterruptedException e) {
                throw new IOExceptionWrapper(e);
            }
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
