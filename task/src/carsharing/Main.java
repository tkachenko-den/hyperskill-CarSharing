package carsharing;

import carsharing.controller.ConsoleController;
import carsharing.dao.DataBaseAdapter;

import java.sql.SQLException;
import java.util.*;

public class Main {
    final static String defaultFileName="dataBase.h2";

    public static void main(String[] args) throws SQLException {
        List<String> listArgs = Arrays.asList(args);
        int databaseFileNameIndex=listArgs.indexOf("-databaseFileName");
        String fileName= (databaseFileNameIndex>=0 && databaseFileNameIndex<listArgs.size()-1) ?
                listArgs.get(databaseFileNameIndex+1) :
                defaultFileName;

        new ConsoleController(
                System.in,
                System.out,
                new DataBaseAdapter("jdbc:h2:./src/carsharing/db/"+fileName)
        ).run();




    }
}