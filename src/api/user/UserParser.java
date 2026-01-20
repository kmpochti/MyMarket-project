package api.user;

import java.io.*;
import java.util.HashMap;

/**
 * Η κλάση UserParser είναι βοηθητική κλάση για την ανάγνωση και εγγραφή των χριστών σε αρχεία
 */
public class UserParser {

    /**
     * Φορτώνει τους χρήστες από ένα αρχείο και επιστρέφει ένα HashMap με τους χρήστες.
     * @param path το μονοπάτι του αρχείου από το οποίο θα φορτωθούν οι χρήστες.
     * @return HashMap που περιέχει τους χρήστες με το username ως κλειδί.
     * @throws RuntimeException αν προκύψει σφάλμα κατά την ανάγνωση του αρχείου.
     */
    public static HashMap<String, User> loadUsers(String path) {
        HashMap<String, User> users = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if(parts.length != 5) {
                    continue;
                }
                String username = parts[0];
                String password = parts[1];

                String role = parts[2];
                String fname = parts[3];
                String lname = parts[4];

                if (role.equalsIgnoreCase("admin")) {
                    users.put(username, new Admin(username, password,fname,lname));
                } else if(role.equalsIgnoreCase("customer"))
                {
                    users.put(username, new Customer(username, password,fname,lname));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return users;
    }
    /**
     * Αποθηκεύει τους χρήστες σε ένα αρχείο.
     * @param users ένα HashMap που περιέχει τους χρήστες με το username ως κλειδί.
     * @param path το μονοπάτι του αρχείου στο οποίο θα αποθηκευτούν οι χρήστες.
     * @throws RuntimeException αν προκύψει σφάλμα κατά την εγγραφή στο αρχείο.
     */
    public static void saveUsers(HashMap<String, User> users, String path) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            for (User user : users.values()) {
                String role = (user instanceof Admin) ? "admin" : "customer";
                writer.write(user.getUsername() + "," + user.getPassword()+"," + role+","+user.getfName()+","+user.getlName());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
