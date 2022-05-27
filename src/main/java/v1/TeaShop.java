package v1;

import v1.controller.CustomController;
import v1.db.PSQL_DB;
import v1.io.ConsoleIO;

public class TeaShop {
    public static void main(String[] args) {
        CustomController program = new CustomController(new ConsoleIO(), new PSQL_DB());
        program.run();
        program.close();
    }
}
