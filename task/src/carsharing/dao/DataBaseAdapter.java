package carsharing.dao;

import org.h2.api.ErrorCode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBaseAdapter {

    Connection connection;
    CarDao carDao = null;
    CompanyDao companyDao = null;
    CustomerDao customerDao = null;

    public CarDao getCarDao() {
        return carDao == null ?
                carDao = new CarDaoImpl(this) :
                carDao;
    }

    public CompanyDao getCompanyDao() {
        return companyDao == null ?
                companyDao = new CompanyDaoImpl(this) :
                companyDao;
    }

    public CustomerDao getCustomerDao() {
        return customerDao == null ?
                customerDao = new CustomerDaoImpl(this) :
                customerDao;
    }

    public DataBaseAdapter(String url) throws SQLException {
        connection = DriverManager.getConnection(url);
        connection.setAutoCommit(true);

        getCompanyDao();
        getCarDao();
        getCustomerDao();

    }

    public void createTable(String sql) {
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            if (e.getErrorCode() != ErrorCode.TABLE_OR_VIEW_ALREADY_EXISTS_1)
                throw new RuntimeException(e);
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
