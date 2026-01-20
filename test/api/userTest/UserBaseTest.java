package api.userTest;

import api.user.User;
import api.user.UserBase;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserBaseTest {
    private UserBase userBase;

    // Εκτελείται πριν από κάθε test
    @Before
    public void setUp() {
        userBase = new UserBase("Data/users.txt"); // Δημιουργία της UserBase και φόρτωση χρηστών
    }

    @Test
    public void testAddUser() {
        // Προσθήκη Admin
        boolean addedAdmin = userBase.addUser("newAdmin", "adminPass", true,"fname","lname");
        assertTrue("Admin should be added successfully", addedAdmin);

        // Προσθήκη Customer
        boolean addedCustomer = userBase.addUser("newCustomer", "customerPass", false,"fname","lname");
        assertTrue("Customer should be added successfully", addedCustomer);

        // Δοκιμή προσθήκης ίδιου χρήστη
        boolean addedDuplicate = userBase.addUser("newAdmin", "adminPass", true,"fname","lname");
        assertFalse("Duplicate username should not be added", addedDuplicate);
    }

    @Test
    public void testRemoveUser() {
        // Αρχικά πρέπει να υπάρχουν χρήστες από το αρχείο
        assertNotNull("admin1 should exist", userBase.findUser("admin1"));

        // Διαγραφή υπάρχοντος χρήστη
        boolean removed = userBase.removeUser("admin1");
        assertTrue("admin1 should be removed successfully", removed);

        // Δοκιμή διαγραφής μη υπάρχοντος χρήστη
        boolean removedNonExistent = userBase.removeUser("nonExistentUser");
        assertFalse("Non-existent user should not be removed", removedNonExistent);
    }

    @Test
    public void testFindUser() {
        // Αναζήτηση υπάρχοντος χρήστη
        User user = userBase.findUser("admin1");
        assertNotNull("admin1 should be found", user);
        assertEquals("Usernames should match", "admin1", user.getUsername());

        // Αναζήτηση μη υπάρχοντος χρήστη
        User nonExistentUser = userBase.findUser("nonExistentUser");
        assertNull("Non-existent user should not be found", nonExistentUser);
    }

    @Test
    public void testLogin() {
        // Επιτυχής σύνδεση
        User loggedInUser = userBase.login("admin1", "password1");
        assertNotNull("Login should succeed for valid credentials", loggedInUser);
        assertEquals("Logged in username should match", "admin1", loggedInUser.getUsername());

        // Αποτυχία σύνδεσης με λάθος κωδικό
        User wrongPasswordUser = userBase.login("admin1", "wrongPassword");
        assertNull("Login should fail for incorrect password", wrongPasswordUser);

        // Αποτυχία σύνδεσης για μη υπάρχοντα χρήστη
        User nonExistentUser = userBase.login("nonExistentUser", "password123");
        assertNull("Login should fail for non-existent user", nonExistentUser);
    }

    @Test
    public void testAddAndRemoveUser() {
        // Προσθήκη νέου χρήστη
        boolean added = userBase.addUser("tempUser", "tempPass", false,"fname","lname");
        assertTrue("tempUser should be added successfully", added);

        // Επαλήθευση ότι ο χρήστης προστέθηκε
        User tempUser = userBase.findUser("tempUser");
        assertNotNull("tempUser should exist after being added", tempUser);

        // Διαγραφή του ίδιου χρήστη
        boolean removed = userBase.removeUser("tempUser");
        assertTrue("tempUser should be removed successfully", removed);

        // Επαλήθευση ότι ο χρήστης διαγράφηκε
        User deletedUser = userBase.findUser("tempUser");
        assertNull("tempUser should not exist after being removed", deletedUser);
    }
}

