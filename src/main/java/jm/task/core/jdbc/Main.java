package jm.task.core.jdbc;

import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;

public class Main {
    public static void main(String[] args) {
        UserService service = new UserServiceImpl();
        service.createUsersTable();
        service.saveUser("Raida", "Oksordottir", (byte) 20);
        service.saveUser("Sigurd", "Sigurdsson", (byte) 25);
        service.saveUser("Jaur", "Komi", (byte) 31);
        service.saveUser("Stephan", "Permskiy", (byte) 38);
        service.removeUserById(2);
        System.out.println(service.getAllUsers());
        service.cleanUsersTable();
        service.dropUsersTable();
    }
}
