package api.user;
import java.util.HashMap;

/**
 * Η κλάση OrderBase διαχειρίζεται τους χριστές της εφαρμογής
 */
public class UserBase {
    private HashMap<String, User> users;
    /**
     * Κατασκευαστής που φορτώνει τους χρήστες από το αρχείο που δίνεται.
     * @param path το μονοπάτι του αρχείου από το οποίο φορτώνονται οι χρήστες.
     */
    public UserBase(String path) {
        this.users =UserParser.loadUsers(path);

    }

    /**
     * Προσθέτει έναν νέο χρήστη στη βάση δεδομένων.
     * @param username το όνομα χρήστη.
     * @param password ο κωδικός πρόσβασης.
     * @param isAdmin αν είναι true, ο χρήστης είναι Admin, αλλιώς είναι Customer.
     * @param fName το μικρό όνομα του χρήστη.
     * @param lName το επώνυμο του χρήστη.
     * @return true αν η προσθήκη ήταν επιτυχής, false αν ο χρήστης υπάρχει ήδη.
     */
    public boolean addUser(String username, String password, boolean isAdmin, String fName, String lName){
        if(users.containsKey(username)){
            return false;
        }
        User newUser = isAdmin ? new Admin(username, password, fName, lName):new Customer(username, password, fName, lName);
        users.put(username, newUser);
        return true;
    }
    /**
     * Διαγράφει έναν χρήστη από τη βάση δεδομένων.
     * @param username το όνομα χρήστη που θα διαγραφεί.
     * @return true αν η διαγραφή ήταν επιτυχής, false αν ο χρήστης δεν υπάρχει.
     */
    public boolean removeUser(String username) {
        if (!this.users.containsKey(username)) {
            return false;

        }
        users.remove(username);
        return true;
    }
    /**
     * Αναζητά έναν χρήστη με βάση το όνομα χρήστη.
     * @param username το όνομα χρήστη που αναζητείται.
     * @return το αντικείμενο User αν βρέθηκε, αλλιώς null.
     */
    public User findUser(String username) {
        if (users.containsKey(username)) {
            return users.get(username);
        }else{
            return null;
        }
    }
    /**
     * Πραγματοποιεί login για έναν χρήστη με το όνομα χρήστη και τον κωδικό πρόσβασης.
     * @param username το όνομα χρήστη.
     * @param password ο κωδικός πρόσβασης.
     * @return το αντικείμενο User αν η σύνδεση ήταν επιτυχής, αλλιώς null.
     */
    public User login(String username, String password) {
        User user = findUser(username);
        if (user == null) {
            return null;
        }
        if (user.getPassword().equals(password)) {
            return user;
        }else{
            return null;
        }
    }
    /**
     * Αποθηκεύει όλους τους χρήστες στο αρχείο που δίνεται.
     * @param path το μονοπάτι του αρχείου όπου θα αποθηκευτούν οι χρήστες.
     */
    public void saveUsersToFile(String path){
        UserParser.saveUsers(this.users,path);
    }
}
