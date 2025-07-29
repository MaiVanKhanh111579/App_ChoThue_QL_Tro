package Model;

public class TimKiem_Filter {
    private int id;
    private String name;
    private Boolean arrow;

    public TimKiem_Filter(int id, String name, Boolean arrow) {
        this.id = id;
        this.name = name;
        this.arrow = arrow;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getArrow() {
        return arrow;
    }

    public void setArrow(Boolean arrow) {
        this.arrow = arrow;
    }
}
