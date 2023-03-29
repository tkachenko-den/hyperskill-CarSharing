package carsharing.dao;

import carsharing.data.Car;
import carsharing.data.Company;
import carsharing.data.Customer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class CustomerDaoImpl implements CustomerDao {
    static final String CREATE_SQL = """
            CREATE TABLE CUSTOMER(
                ID int NOT NULL PRIMARY KEY auto_increment,
                NAME varchar(300) NOT NULL UNIQUE,
                RENTED_CAR_ID INT,
                FOREIGN KEY (RENTED_CAR_ID) REFERENCES CAR(ID)            
            )""";
    static final String INSERT_ROW = "INSERT INTO CUSTOMER (name) VALUES (?)";
    static final String SELECT_ALL = """
            SELECT ID, NAME, RENTED_CAR_ID
            FROM CUSTOMER
            ORDER BY ID""";
    static final String SELECT_FULLINFO_BY_ID = """
            SELECT  ct.ID     as ctID,
                    ct.NAME   as ctNAME,
                    ca.ID     as caID,
                    ca.NAME   as caNAME,
                    cm.ID     as cmID,
                    cm.NAME   as cmNAME
            FROM CUSTOMER ct LEFT JOIN CAR ca      ON ct.RENTED_CAR_ID=ca.ID
                             LEFT JOIN COMPANY cm  ON ca.COMPANY_ID=cm.ID
            WHERE ct.ID=?""";
    static final String UPDATE_CUSTOMER= """
            UPDATE CUSTOMER SET
                NAME = ?,
                RENTED_CAR_ID = ?
            WHERE ID=?""";


    DataBaseAdapter db;

    public CustomerDaoImpl(DataBaseAdapter db) {
        this.db = db;
        db.createTable(CREATE_SQL);
    }


    @Override
    public boolean addCustomer(Customer customer) {
        try (PreparedStatement statement = db.getConnection().prepareStatement(INSERT_ROW)) {
            statement.setString(1, customer.getName());
            statement.execute();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    @Override
    public List<Customer> getAllCustomers() {
        List<Customer> result = new ArrayList<>();
        try (ResultSet rs = db.getConnection().createStatement().executeQuery(SELECT_ALL)) {
            while (rs.next()) {
                result.add(
                        new Customer(
                                rs.getInt("ID"),
                                rs.getString("NAME"),
                                rs.getInt("RENTED_CAR_ID")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public Customer getCustomerFullInfoByID(int customerID) {
        try (PreparedStatement statement = db.getConnection().prepareStatement(SELECT_FULLINFO_BY_ID)) {
            statement.setInt(1, customerID);
            try (ResultSet rs = statement.executeQuery()) {
                return !rs.next() ?
                        null :
                        new Customer(
                                rs.getInt("ctID"),
                                rs.getString("ctNAME"),
                                rs.getObject("caID") == null ?
                                        null :
                                        new Car(
                                                rs.getInt("caID"),
                                                rs.getString("caNAME"),
                                                new Company(
                                                        rs.getInt("cmID"),
                                                        rs.getString("cmNAME")))
                        );
            }
        } catch (SQLException e) {
            return null;
        }
    }

    public boolean updateCustomer(Customer customer) {
        try (PreparedStatement statement = db.getConnection().prepareStatement(UPDATE_CUSTOMER)) {
            statement.setInt(3,customer.getId());
            statement.setString(1, customer.getName());
            if(customer.getRentedCarID()==0)
                statement.setNull(2, Types.INTEGER);
            else
                statement.setInt(2,customer.getRentedCarID());
            return statement.executeUpdate()==1;
        } catch (SQLException e) {
            return false;
        }
    }

}
