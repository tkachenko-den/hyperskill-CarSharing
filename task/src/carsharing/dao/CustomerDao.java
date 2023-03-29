package carsharing.dao;

import carsharing.data.Customer;

import java.util.List;

public interface CustomerDao {
    boolean addCustomer(Customer customer);
    List<Customer> getAllCustomers();
    Customer getCustomerFullInfoByID(int customerID);
    public boolean updateCustomer(Customer customer);
}
