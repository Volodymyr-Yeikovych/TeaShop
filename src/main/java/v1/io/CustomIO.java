package v1.io;

import java.io.IOException;

public interface CustomIO {

    char read();
    String readLine();

    void write(String str);
    void writeln(String str);
    void writeln();
    void writef(String format, Object... args);
    void antialiasing(long milliseconds);

    void close();
}
