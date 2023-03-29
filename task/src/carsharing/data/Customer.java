package carsharing.data;

public class Customer {
    int id;
    String name;
    int rentedCarID = 0;
    Car rentedCar = null;

    public Customer(int id, String name, Car rentedCar) {
        this.id = id;
        this.name = name;
        this.rentedCar = rentedCar;
        if (rentedCar != null)
            rentedCarID = rentedCar.getId();
    }

    public Customer(String name) {
        this.name = name;
    }

    public Customer(int id, String name, int rentedCarID) {
        this.id = id;
        this.name = name;
        this.rentedCarID = rentedCarID;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getRentedCarID() {
        return rentedCarID;
    }

    public Car getRentedCar() {
        return rentedCar;
    }

    public Customer setRentedCar(Car rentedCar) {
        this.rentedCar = rentedCar;
        this.rentedCarID = (rentedCar == null) ? 0 : rentedCar.getId();
        return this;
    }
}
