package carsharing.dao;

import carsharing.data.Car;
import carsharing.data.Company;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CarDaoImpl implements CarDao {
    static final String CREATE_SQL = """
            CREATE TABLE CAR(
                ID int NOT NULL PRIMARY KEY auto_increment,
                NAME varchar(300) NOT NULL UNIQUE,
                COMPANY_ID INT NOT NULL,
                FOREIGN KEY (COMPANY_ID) REFERENCES COMPANY(ID)            
            )""";
    DataBaseAdapter db;


    public CarDaoImpl(DataBaseAdapter db) {
        this.db = db;
        db.createTable(CREATE_SQL);
    }




    @Override
    public List<Car> getCarsByCompany(Company company) {
        String query = """
                SELECT ID, NAME, COMPANY_ID
                FROM CAR
                WHERE COMPANY_ID=?
                ORDER BY ID""";
        List<Car> result = new ArrayList<>();

        try (PreparedStatement statement = db.getConnection().prepareStatement(query)) {
            statement.setInt(1, company.getId());
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    result.add(new Car(
                            rs.getInt("ID"),
                            rs.getString("NAME"),
                            rs.getInt("COMPANY_ID")));
                }
            }
        } catch (SQLException e) {
            ;
        }
        return result;
    }

    @Override
    public List<Car> getFreeCarsByCompany(Company company) {
        String query = """
                SELECT ID, NAME, COMPANY_ID
                FROM CAR
                WHERE COMPANY_ID=?
                     and ID not in (select nvl(RENTED_CAR_ID,-1) from CUSTOMER)
                ORDER BY ID""";
        List<Car> result = new ArrayList<>();

        try (PreparedStatement statement = db.getConnection().prepareStatement(query)) {
            statement.setInt(1, company.getId());
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    result.add(new Car(
                            rs.getInt("ID"),
                            rs.getString("NAME"),
                            rs.getInt("COMPANY_ID")));
                }
            }
        } catch (SQLException e) {
            ;
        }
        return result;
    }

    @Override
    public boolean addCar(Car car) {
        String query = "INSERT INTO  CAR (NAME, COMPANY_ID) VALUES (?,?)";
        try (PreparedStatement statement = db.getConnection().prepareStatement(query)) {
            statement.setString(1, car.getName());
            statement.setInt(2,car.getCompanyID());
            statement.execute();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }
}
