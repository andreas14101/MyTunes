package BE;

public class Category {
    private int id;
    private String name;
    public Category(int id, String name) {
        this.id = id;
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public int getId(){
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
