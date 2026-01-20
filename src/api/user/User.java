package api.user;

/**
 * Η κλάση User αναπαριστά έναν αυθαίρετο χριστή
 */
public class User {
    private String fName;
    private String lName;
    private String username;
    private String password;

    /**
     * Κατασκευαστής της κλάσης User.
     * @param username το όνομα χρήστη.
     * @param password ο κωδικός πρόσβασης.
     * @param fName το μικρό όνομα του χρήστη.
     * @param lName το επώνυμο του χρήστη.
     */
    public User(String username, String password, String fName, String lName) {
        this.username = username;
        this.password = password;
        this.fName = fName;
        this.lName = lName;
    }
    /**
     * Επιστρέφει το όνομα χρήστη.
     * @return το όνομα χρήστη.
     */
    public String getUsername() {
        return username;
    }
    /**
     * Επιστρέφει τον κωδικό πρόσβασης του χρήστη.
     * @return ο κωδικός πρόσβασης.
     */
    public String getPassword() {
        return password;
    }
    /**
     * Ορίζει έναν νέο κωδικό πρόσβασης.
     * @param password ο νέος κωδικός πρόσβασης.
     */
    public void setPassword(String password) {
        this.password = password;
    }
    /**
     * Επιστρέφει το μικρό όνομα του χρήστη.
     * @return το μικρό όνομα του χρήστη.
     */
    public String getfName() {
        return fName;
    }
    /**
     * Ορίζει ένα νέο μικρό όνομα για τον χρήστη.
     * @param fName το νέο μικρό όνομα.
     */
    public void setfName(String fName) {
        this.fName = fName;
    }
    /**
     * Επιστρέφει το επώνυμο του χρήστη.
     * @return το επώνυμο του χρήστη.
     */
    public String getlName() {
        return lName;
    }
    /**
     * Ορίζει ένα νέο επώνυμο για τον χρήστη.
     * @param lName το νέο επώνυμο.
     */
    public void setlName(String lName) {
        this.lName = lName;
    }
    /**
     * Ελέγχει αν ο δοθείς κωδικός πρόσβασης ταιριάζει με τον τρέχοντα κωδικό πρόσβασης του χρήστη.
     * @param password ο κωδικός προς έλεγχο.
     * @return true αν ο κωδικός είναι σωστός, αλλιώς false.
     */
    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }
    /**
     * Επιστρέφει αν ο χρήστης είναι Admin.
     * @return false, εκτός αν η μέθοδος έχει υπερκεραστεί από υποκλάσεις.
     */
    public boolean isAdmin() {
        return false;
    }

}
