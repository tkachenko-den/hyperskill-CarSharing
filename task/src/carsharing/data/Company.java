package carsharing.data;

public class Company {
    int id;
    String name;

    public Company(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Company(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return id + ". " + name;
    }
}
