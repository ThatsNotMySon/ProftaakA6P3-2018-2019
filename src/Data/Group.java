package Data;

import java.io.Serializable;

public class Group implements Serializable {

    private String name;
    private int amountOfStudents;

    public Group(String name, int amountOfStudents) {
        this.name = name;
        this.amountOfStudents = amountOfStudents;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAmountOfStudents() {
        return amountOfStudents;
    }

    public void setAmountOfStudents(int amountOfStudents) {
        this.amountOfStudents = amountOfStudents;
    }

    @Override
    public String toString() {
        return this.name;
//        return "\nGroup: " +
//                "name='" + name + '\'' +
//                ", amountOfStudents=" + amountOfStudents;
    }
}
