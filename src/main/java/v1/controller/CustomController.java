package v1.controller;

import v1.db.CustomDB;
import v1.db.PSQL_DB;
import v1.io.ConsoleIO;
import v1.io.CustomIO;

public class CustomController {
    private final CustomIO io = new ConsoleIO();
    private final CustomDB db = new PSQL_DB();
    private boolean isAuthorised = false;
    private final static String EMAIL_REGEX = "^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$";
    private boolean isAnonymous = true;

    public void run() {
        io.writeln("Welcome to \"Oversea Tea\" shop dear customer.");
        io.antialiasing(600);
        io.writeln("For your convenience in the shopping you can register or login if you already have an account. " +
                "Type (R/L).");
        io.antialiasing(600);
        io.write("Also you can proceed anonymously type (A): ");
        String param = io.readLine().toUpperCase();
        io.writeln();

        if ("R".equals(param)) register();
        else if ("L".equals(param)) login();
        else if (!"A".equals(param)) {
            io.writeln("Invalid parameter.");
            return;
        }


    }

    private void login() {
        for (int i = 2; i >= 0; i--) {
            io.write("Type your username: ");
            String username = io.readLine();
            io.writeln();

            io.write("Type your password: ");
            String password = io.readLine();
            io.writeln();

            if (db.verifyUser(username, password)) {
                isAuthorised = true;
                isAnonymous = false;
                return;
            } else {
                if (i == 0) {
                    io.writeln("Failed to login in 3 attempts. Continuing as anonymous.");
                    return;
                }
                io.writeln("Username or password is incorrect.");
                io.write("Try again (" + (i) + " times left) or continue as anonymous (Y/A): ");
                String param = io.readLine().toUpperCase();
                io.writeln();
                if ("A".equals(param)) return;
                else if (!"Y".equals(param)) {
                    io.writeln("Invalid parameter. Continuing as anonymous.");
                    return;
                }
            }
        }
    }

    private void register() {
        for (int i = 3; i > 0; i--) {
            io.writeln("Registration initiated.");

            io.write("Enter your username: ");
            String username = io.readLine();
            io.writeln();

            io.write("Enter your password: ");
            String password = io.readLine();
            io.writeln();

            if (password.length() < 4) {
                for (int j = 3; j > 0; j--) {
                    io.writeln("Invalid password.");
                    io.write("Enter valid password or proceed as anonymous (E/A): ");
                    String param = io.readLine().toUpperCase();
                    io.writeln();

                    if ("E".equals(param)) {
                        io.write("Enter your password: ");
                        password = io.readLine();
                        io.writeln();
                        if (password.length() > 4) break;
                    } else if ("A".equals(param)) {
                        io.writeln("Proceeding as anonymous.");
                        return;
                    } else {
                        io.writeln("Invalid param. Proceeding as anonymous.");
                        return;
                    }
                }
            }

            io.write("Enter your city: ");
            String city = io.readLine();
            io.writeln();

            io.write("Enter your country: ");
            String country = io.readLine();
            io.writeln();

            io.write("Enter your email: ");
            String email = io.readLine();
            io.writeln();

            if (!email.matches(EMAIL_REGEX)) {
                for (int j = 3; j > 0; j--) {
                    io.writeln("Invalid email.");
                    io.write("Enter valid email or proceed as anonymous (E/A): ");
                    String param = io.readLine().toUpperCase();
                    io.writeln();

                    if ("E".equals(param)) {
                        io.write("Enter your email: ");
                        email = io.readLine();
                        io.writeln();
                        if (email.matches(EMAIL_REGEX)) break;
                    } else if ("A".equals(param)) {
                        io.writeln("Proceeding as anonymous.");
                        return;
                    } else {
                        io.writeln("Invalid param. Proceeding as anonymous.");
                        return;
                    }
                }
            }

        }
    }
}
