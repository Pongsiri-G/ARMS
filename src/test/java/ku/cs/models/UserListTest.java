package ku.cs.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserListTest {
    @Test
    void testAdd() {
        UserList userList = new UserList();
        userList.addUser("user1", "1234", "user1", "Science");
        userList.addUser("user1.2", "5678", "user1.2", "Engineer");
        userList.addUser("user1.3", "5678", "user1.3", "Engineer");

        userList.addUser("user2", "1234", "user2", "Science", "Computer");
        userList.addUser("user2.2", "5678", "user2.2", "Science", "Math");
        userList.addUser("user2.3", "5678", "user2.3", "Engineer", "Mechatronic");
        userList.addUser("user2.4", "5678", "user2.4", "Engineer", "Electronics");

        System.out.println(userList.getAllUsers());

    }

    @Test
    void testLogin() {
        UserList userList = new UserList();
        userList.addUser("user1", "1234", "user1", "Science");
        userList.addUser("user1.2", "5678", "user1.2", "Engineer");
        userList.addUser("user1.3", "5678", "user1.3", "Engineer");

        userList.addUser("user2", "1234", "user2", "Science", "Computer");
        userList.addUser("user2.2", "5678", "user2.2", "Science", "Math");
        userList.addUser("user2.3", "5678", "user2.3", "Engineer", "Mechatronic");
        userList.addUser("user2.4", "5678", "user2.4", "Engineer", "Electronics");

        User a = userList.login("user1", "1234");
        assertEquals("Science", a.getFaculty().getFacultyName());
    }



}