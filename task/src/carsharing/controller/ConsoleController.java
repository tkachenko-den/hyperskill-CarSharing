package carsharing.controller;

import carsharing.dao.DataBaseAdapter;
import carsharing.data.Car;
import carsharing.data.Company;
import carsharing.data.Customer;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class ConsoleController {
    InputStream in;
    PrintStream out;
    DataBaseAdapter db;
    Scanner scanner;
    String rootMenu = """            
            1. Log in as a manager
            2. Log in as a customer
            3. Create a customer
            0. Exit""";
    String operationsMenu = """            
            1. Company list
            2. Create a company
            0. Back""";
    String companyOperationsMenu = """
            1. Car list
            2. Create a car
            0. Back""";
    String customerOperationsMenu = """
            1. Rent a car
            2. Return a rented car
            3. My rented car
            0. Back""";

    public ConsoleController(InputStream in, PrintStream out, DataBaseAdapter db) {
        this.in = in;
        this.out = out;
        this.db = db;
        scanner = new Scanner(in);
    }

    int getMenuItem(String menu) {
        out.println("\n" + menu);
        return scanner.nextInt();
    }

    Company inputNewCompany() {
        out.println("\nEnter the company name:");
        scanner.nextLine();
        return new Company(scanner.nextLine());
    }

    Car inputNewCar(Company company) {
        out.println("\nEnter the car name:");
        scanner.nextLine();
        return new Car(scanner.nextLine(), company.getId());
    }

    Customer inputNewCustomer() {
        out.println("\nEnter the customer name:");
        scanner.nextLine();
        return new Customer(scanner.nextLine());
    }

    void printCompanyList(List<Company> list) {
        if (list.isEmpty())
            out.println("\nThe company list is empty!");
        else {
            out.println("\nCompany list:");
            AtomicInteger index = new AtomicInteger(1);
            list.forEach(e -> out.println(index.getAndIncrement() + ". " + e.getName()));
        }
    }

    void printCarList(Company company, List<Car> list) {
        if (list.isEmpty())
            out.println("\nThe car list is empty!");
        else {
            out.println("\nCar list:"); // or 'Company name' cars:
            AtomicInteger index = new AtomicInteger(1);
            list.forEach(e -> out.println(index.getAndIncrement() + ". " + e.getName()));
        }
    }

    Company chooseCompany(List<Company> list) {
        if (list.isEmpty()) {
            out.println("\nThe company list is empty!");
            return null;
        } else {
            out.println("\nChoose a company:"); // Choose the company:
            AtomicInteger index = new AtomicInteger(1);
            list.forEach(e -> out.println(index.getAndIncrement() + ". " + e.getName()));
            out.println("0. Back");
            int choose = scanner.nextInt();
            return choose == 0 ?
                    null :
                    list.get(choose - 1);
        }
    }

    Customer chooseCustomer(List<Customer> list) {
        if (list.isEmpty()) {
            out.println("\nThe customer list is empty!");
            return null;
        } else {
            out.println("\nCustomer list:");  // Choose a customer:
            AtomicInteger index = new AtomicInteger(1);
            list.forEach(e -> out.println(index.getAndIncrement() + ". " + e.getName()));
            out.println("0. Back");
            int choose = scanner.nextInt();
            return choose == 0 ?
                    null :
                    list.get(choose - 1);
        }
    }

    Car chooseCar(List<Car> list, Company company) {
        if (list.isEmpty()) {
            out.println("\nNo available cars in the '" + company.getName() + "' company");
            return null;
        } else {
            out.println("\nChoose a car:");
            AtomicInteger index = new AtomicInteger(1);
            list.forEach(e -> out.println(index.getAndIncrement() + ". " + e.getName()));
            out.println("0. Back");
            int choose = scanner.nextInt();
            return choose == 0 ?
                    null :
                    list.get(choose - 1);
        }
    }


    void companyOperations(Company company) {
        out.println("\n'" + company.getName() + "' company");
        companyOperationsLoop:
        while (true) {
            switch (getMenuItem(companyOperationsMenu)) {
                case 0:
                    break companyOperationsLoop;
                case 1:
                    // Car list
                    printCarList(company, db.getCarDao().getCarsByCompany(company));
                    break;
                case 2:
                    // Create a car
                    if (db.getCarDao().addCar(inputNewCar(company))) {
                        out.println("The car was added!");
                    }
                    break;
            }
        }
    }

    void customerOperations(Customer customer) {
        customerOperationsLoop:
        while (true) {
            customer = db.getCustomerDao().getCustomerFullInfoByID(customer.getId());
            switch (getMenuItem(customerOperationsMenu)) {
                case 0:
                    break customerOperationsLoop;
                case 1:
                    // Rent a car
                    if (customer.getRentedCar() != null)
                        out.println("You've already rented a car!");
                    else {
                        List<Company> companies = db.getCompanyDao().getAllCompanies();
                        if (companies.isEmpty())
                            out.println("The company list is empty!");
                        else {
                            Company company = chooseCompany(companies);
                            if (company != null) {
                                Car car = chooseCar(db.getCarDao().getFreeCarsByCompany(company), company);
                                if (car != null) {
                                    db.getCustomerDao().updateCustomer(customer.setRentedCar(car));
                                    out.println("\nYou rented '" + car.getName() + "'");
                                }
                            }
                        }
                    }
                    break;
                case 2:
                    // Return a rented car
                    if (customer.getRentedCar() == null)
                        out.println("\nYou didn't rent a car!");
                    else {
                        db.getCustomerDao().updateCustomer(customer.setRentedCar(null));
                        out.println("\nYou've returned a rented car!");
                    }
                    break;
                case 3:
                    // My rented car
                    if (customer.getRentedCar() == null)
                        out.println("\nYou didn't rent a car!");
                    else
                        out.printf("""
                                
                                Your rented car:
                                %s
                                Company:
                                %s\n""", customer.getRentedCar().getName(), customer.getRentedCar().getCompany().getName());
                    break;
            }
        }
    }

    public void run() {
        rootWhile:
        while (true) {
            switch (getMenuItem(rootMenu)) {
                case 0:
                    break rootWhile;
                case 1:
                    // Log in as a manager
                    operationsWhile:
                    while (true) {
                        switch (getMenuItem(operationsMenu)) {
                            case 0:
                                break operationsWhile;
                            case 1:
                                // Company list
                                Company company = chooseCompany(db.getCompanyDao().getAllCompanies());
                                if (company != null)
                                    companyOperations(company);
                                break;
                            case 2:
                                // Create a company
                                if (db.getCompanyDao().addCompany(inputNewCompany())) {
                                    out.println("The company was created!");
                                }
                                break;
                        }
                    }
                    break;
                case 2:
                    // Log in as a customer
                    Customer customer = chooseCustomer(db.getCustomerDao().getAllCustomers());
                    if (customer != null)
                        customerOperations(customer);
                    break;
                case 3:
                    // Create a customer
                    if (db.getCustomerDao().addCustomer(inputNewCustomer())) {
                        out.println("The customer was added!");
                    }
                    break;
            }
        }
    }
}
