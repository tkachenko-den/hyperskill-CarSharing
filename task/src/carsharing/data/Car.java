package carsharing.data;

public class Car {
    int id;
    String name;
    int companyID;
    Company company;

    public Car(int id, String name, Company company) {
        this.id = id;
        this.name = name;
        this.company = company;
        if(company!=null)
            companyID=company.getId();
    }

    public Car(int id, String name, int companyID) {
        this.id = id;
        this.name = name;
        this.companyID = companyID;
    }

    public Car(String name, int companyID) {
        this.name = name;
        this.companyID = companyID;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCompanyID() {
        return companyID;
    }

    @Override
    public String toString() {
        return id + ". " + name;
    }

    public Company getCompany() {
        return company;
    }
}
