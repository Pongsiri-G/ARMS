package ku.cs.models;

public class RequestHandlingOfficer {
    private String name;
    private String position;

    // Begin Constructor
    public RequestHandlingOfficer(String name, String position) {
        this.name = name;
        this.position = position;
    }
    // End Constructor

    public void setName(String name) {
        this.name = name;
    }
    public void setPosition(String position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }
    public String getPosition() {
        return position;
    }
    @Override
    public String toString() {
        return position + name;
    }


}
