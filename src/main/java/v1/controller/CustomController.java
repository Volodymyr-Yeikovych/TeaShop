package v1.controller;

import v1.db.CustomDB;
import v1.exceptions.SQLExceptionWrapper;
import v1.io.CustomIO;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CustomController {

    private final static String EMAIL_REGEX = "^([a-zA-Z0-9_\\-.]+)@([a-zA-Z0-9_\\-.]+)\\.([a-zA-Z]{2,5})$";

    private final CustomIO io;
    private final CustomDB db;

    private boolean isAuthorised = false;
    private boolean shopDelivery = false;

    private String address;
    private final Map<Integer, Integer> kgOrder = new HashMap<>();
    private String username;
    private String password;

    public CustomController(CustomIO io, CustomDB db){
        this.io = io;
        this.db = db;
    }

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

        io.antialiasing(600);
        if (!isAuthorised) {
            io.writeln("Hello, dear client.");
        }

        io.antialiasing(600);
        io.writeln("Please see our current stock and choose the tea type by entering its id in the console.");
        io.writeln();
        io.display(db.getStock());
        io.writeln();
        io.writeln("To end tea selection type \"E\".");
        if (!collectOrder()) return;

        io.antialiasing(600);
        io.write("Choose either to deliver to shop or selected address. Type (S/AD): ");
        param = io.readLine().toUpperCase();
        io.writeln();

        if ("S".equals(param)) initShopDelivery(isAuthorised);
        else if ("AD".equals(param)) initAddressDelivery(isAuthorised);
        else {
            io.writeln("Invalid parameter.");
            return;
        }

        if (!shopDelivery) io.writeln("Your order will be delivered to address: " + address);
        else io.writeln("Your order will be delivered to shop. Address: " + address);
    }

    @SuppressWarnings("all")
    private boolean collectOrder() {
        while (true) {
            io.write("Choose your tea type: ");
            String param = io.readLine().toUpperCase();
            io.writeln();

            if ("E".equals(param) && !kgOrder.isEmpty()) {
                io.writeln("Order successfully accepted.");
                return true;
            } else if ("E".equals(param) && kgOrder.isEmpty()) {
                io.writeln("Error: Empty order.");
                return false;
            }

            if (!isInteger(param) || !db.checkIfTeaExists(Integer.parseInt(param))) {
                io.writeln("Invalid value.");
                return false;
            }

            io.write("Enter desirable packs number (1 pack = 1 kg): ");
            String kgNumber = io.readLine();
            io.writeln();

            if (!isInteger(kgNumber) || !db.checkIfKgNumberAvailable
                    (Integer.parseInt(param), Integer.parseInt(kgNumber))) {
                io.writeln("Invalid value.");
                return false;
            }

            db.beginTransaction();
            if (!db.validateStock(Integer.parseInt(param), Integer.parseInt(kgNumber))) db.rollback();
            db.commit();
            kgOrder.put(Integer.parseInt(param), Integer.parseInt(kgNumber));
        }
    }

    @SuppressWarnings("all")
    private boolean isInteger(String kgNumber) {
        if (kgNumber == null) return false;

        try {
            int num = Integer.parseInt(kgNumber);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void login() {
        for (int i = 2; i >= 0; i--) {
            io.write("Type your username: ");
            username = io.readLine();
            io.writeln();

            io.write("Type your password: ");
            password = io.readLine();
            io.writeln();

            if (db.userExists(username, password)) {
                io.antialiasing(600);
                io.writeln("Hello, dear " + username + ".");
                isAuthorised = true;
                return;
            } else {
                if (i == 0) {
                    io.writeln("Failed to login in 3 attempts. Continuing as anonymous.");
                    return;
                }
                io.writeln("Username or password is incorrect.");
                io.write("Try again (" + (i) + " times left) or continue as anonymous (C/A): ");
                String param = io.readLine().toUpperCase();
                io.writeln();
                if ("A".equals(param)) return;
                else if (!"C".equals(param)) {
                    io.writeln("Invalid parameter. Continuing as anonymous.");
                    return;
                }
            }
        }
    }

    private void register() {
        io.writeln("Registration initiated.");

        io.write("Enter your username: ");
        username = io.readLine();
        io.writeln();

        io.write("Enter your password: ");
        password = io.readLine();
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

        io.write("Enter your street: ");
        String street = io.readLine();
        io.writeln();

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

        if (db.userExists(username, password))
            throw new SQLExceptionWrapper("Such client is already registered.");

        db.addClient(username, password, street, city, country, email);
        io.writeln("Wait...");
        io.antialiasing(600);
        io.writeln("Your account was successfully registered.");
        io.antialiasing(600);
        io.writeln("Hello, dear " + username + ".");
        isAuthorised = true;
    }

    private void initShopDelivery(boolean isAuthorised) {
        String clCity;
        if (isAuthorised) {
            clCity = db.getClientCity(username, password);
        } else {
            io.write("Enter your city: ");
            clCity = io.readLine();
            io.writeln();
        }
        checkIfShopsPresent(clCity);
    }

    private void initAddressDelivery(boolean isAuthorised) {
        if (isAuthorised) {
            address = db.getClientAddress(username, password);
        } else {
            io.write("Enter your address: ");
            address = io.readLine();
            io.writeln();
        }
    }

    private void checkIfShopsPresent(String city) {
        Optional<ResultSet> shopsOpt = db.getShops(city);
        if (shopsOpt.isEmpty()) {
            io.writeln("Unfortunately there are no shops in your city or selected city does not exist.");
            io.writeln("Proceeding with address delivery.");

            initAddressDelivery(isAuthorised);
        } else {
            shopDelivery = true;
            if (db.moreThenOneShopPresent(city)) {
                io.writeln("There are more than one shop in your city. Here is the list.");
                io.write("Choose the shop by entering its id: ");
                long shopId = Long.parseLong(io.readLine());
                io.writeln();
                address = db.getShopAddress(shopId);
                return;
            }
            address = db.getShopAddress(city);
        }
    }

    public void close() {
        this.io.close();
        this.db.closeConnection();
    }
}
