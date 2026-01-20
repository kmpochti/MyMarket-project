package api.user;

/**
 * Η κλάση Customer αναπαριστά έναν πελάτη
 */
public class Customer extends User {
    /**
     * Κατασκευαστής της κλάσης Customer.
     * @param username το όνομα χρήστη.
     * @param password ο κωδικός πρόσβασης.
     * @param fName το μικρό όνομα του πελάτη.
     * @param lName το επώνυμο του πελάτη.
     */
    public Customer(String username, String password, String fName, String lName) {
        super(username, password, fName, lName);
    }
}
