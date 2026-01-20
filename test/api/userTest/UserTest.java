package api.userTest;
import api.user.Admin;
import api.user.Customer;
import api.user.User;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserTest {
    @Test
    public void testCheckPassword() {
        User test = new User("user1","password01", "fname", "lname");
        assertTrue(test.checkPassword("password01"));
        assertFalse(test.checkPassword("asterakiamouu"));
    }
    @Test
    public void testGetAndSet(){
        User user = new User("user1","password01", "fname", "lname");
        assertEquals("fname",user.getfName());
        assertEquals("lname",user.getlName());
        assertEquals("user1",user.getUsername());
        assertEquals("password01",user.getPassword());

        user.setfName("aggelos");
        user.setlName("manou");
        user.setPassword("123");

        assertEquals("aggelos",user.getfName());
        assertEquals("manou",user.getlName());
        assertEquals("123",user.getPassword());
    }

    @Test
    public void testAdminAndCustomer(){
        User admin = new Admin("admin","1234","fname","lname");
        assertTrue(admin.isAdmin());

        User customer = new Customer("username","1234","fname","lname");
        assertFalse(customer.isAdmin());
    }
}
