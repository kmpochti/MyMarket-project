package api.user;

/**
 * Η κλάση Admin αναπαριστά έναν Admin
 */
public class Admin extends User {
    /**
     * Κατασκευαστής της κλάσης Admin.
     * @param username το όνομα χρήστη.
     * @param password ο κωδικός πρόσβασης.
     * @param fName το μικρό όνομα του διαχειριστή.
     * @param lName το επώνυμο του διαχειριστή.
     */
    public Admin(String username, String password, String fName, String lName) {
        super(username, password, fName, lName);
    }
    /**
     * Επιστρέφει αν ο χρήστης είναι διαχειριστής.
     * @return true, καθώς αυτή η κλάση αντιπροσωπεύει έναν διαχειριστή.
     */
    @Override
    public boolean isAdmin() {
        return true;
    }
}
