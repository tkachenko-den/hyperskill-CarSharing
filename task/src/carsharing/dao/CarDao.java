package carsharing.dao;

import carsharing.data.Car;
import carsharing.data.Company;

import java.util.List;

public interface CarDao {
    List<Car> getCarsByCompany(Company company);
    List<Car> getFreeCarsByCompany(Company company);
    boolean addCar(Car car);
}
