package ku.cs.models;

import java.util.ArrayList;

public class DepartmentList {
    private ArrayList<Department> departments;
    public DepartmentList() {departments = new ArrayList<>();}

    public void addNewDepartment(String name, String id) {
        name = name.trim();
        id = id.trim();
        if (!name.isEmpty() && !id.isEmpty()) {
            Department exist = findDepartmentByID(id);
            if (exist != null) {
                departments.add(new Department(name.trim(), id.trim()));
            }
        }
    }

    public Department findDepartmentByID(String id) {
        for (Department department : departments) {
            if (department.isDepartmentID(id)) {
                return department;
            }
        }
        return null;
    }

    public ArrayList<Department> getDepartments() {return departments;}
}
