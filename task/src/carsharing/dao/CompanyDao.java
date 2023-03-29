package carsharing.dao;

import carsharing.data.Company;
import java.util.List;

public interface CompanyDao {
    List<Company> getAllCompanies();
    boolean addCompany(Company company);
    Company getByID(int companyID);
}
