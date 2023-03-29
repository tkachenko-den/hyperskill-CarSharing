package carsharing.dao;

import carsharing.data.Company;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CompanyDaoImpl implements CompanyDao {
    static final String CREATE_SQL = """
            CREATE TABLE COMPANY(
                ID int NOT NULL PRIMARY KEY auto_increment,
                NAME varchar(300) NOT NULL UNIQUE
            )""";
    DataBaseAdapter db;

    public CompanyDaoImpl(DataBaseAdapter db) {
        this.db = db;
        db.createTable(CREATE_SQL);
    }

    @Override
    public List<Company> getAllCompanies() {
        String query = """
                SELECT 
                    ID,
                    NAME
                FROM COMPANY
                ORDER BY ID""";
        List<Company> result = new ArrayList<>();
        try (ResultSet rs = db.getConnection().createStatement().executeQuery(query)) {
            while (rs.next()) {
                result.add(new Company(rs.getInt("id"), rs.getString("name")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public boolean addCompany(Company company) {
        String query = "INSERT INTO  COMPANY (name) VALUES (?)";
        try (PreparedStatement statement = db.getConnection().prepareStatement(query)) {
            statement.setString(1, company.getName());
            statement.execute();
        } catch (SQLException e) {
            return false;
        }

        return true;
    }

    @Override
    public Company getByID(int companyID) {
        String query = "SELECT * FROM COMPANY WHERE id=?";
        try (PreparedStatement statement = db.getConnection().prepareStatement(query)) {
            statement.setInt(1, companyID);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() ?
                        new Company(rs.getInt("ID"), rs.getString("NAME")) :
                        null;
            }
        } catch (SQLException e) {
            return null;
        }
    }
}
