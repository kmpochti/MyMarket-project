package api;

import api.order.Order;
import api.order.OrderedProduct;
import api.product.MarketProduct;
import api.product.queryProduct;
import api.user.User;
import api.user.UserBase;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;


public class api {

    private User currentUser;
    private final UserBase users;
    private final orderHandler orderHandler;

    public api() throws FileNotFoundException {
        currentUser = null;
        orderHandler = new orderHandler();
        users = new UserBase("Data/users.txt");
    }

    public boolean addUser(String username, String password, boolean isAdmin, String fName, String lName) {
        return users.addUser(username, password, isAdmin, fName, lName);
    }

    public boolean removeUser(String username) {
        return users.removeUser(username);
    }

    public User findUser(String username) {
        return users.findUser(username);
    }

    // Getter για τον currentUser
    public User getCurrentUser() {return currentUser;}

    public User login(String username, String password) {
        currentUser = users.login(username, password);
        return currentUser;
    }

    public void logout(String username, String password) {
        orderHandler.emptyCart();
        currentUser = null;
    }

    public void emptyCart() {
        orderHandler.emptyCart();
    }

    /**
     * Μέθοδος για την αναζήτηση προϊόντων
     * @param query αντικείμενο με τα πεδία που θέλουμε να αναζητήσουμε. Αν κάποιο πεδίο είναι null τότε το αγνοεί
     * @param sort τρόπος ταξινόμησης: "titleAscending","titleDescending","priceAscending","priceDescending" και "" χωρίς ταξινόμηση
     * @return ArrayList με τα αποτελέσματα
     */
    // Μέθοδος για αναζήτηση με query και sort μόνο
    public ArrayList<MarketProduct> searchProduct(queryProduct query, String sort) {
        return searchProduct(query, sort, ""); // Περνάει κενό string για το keyword
    }

    // Μέθοδος για αναζήτηση με query μόνο
    public ArrayList<MarketProduct> searchProduct(queryProduct query) {
        return searchProduct(query, "", ""); // Περνάει κενό string για sort και keyword
    }
    public ArrayList<MarketProduct> searchProduct(queryProduct query, String sort, String keyword) {
        ArrayList<MarketProduct> results = orderHandler.search(query);

        // Φίλτρο με λέξη-κλειδί στον τίτλο
        if (keyword != null && !keyword.isEmpty()) {
            String normalizedKeyword = normalizeString(keyword.trim());
            results.removeIf(product -> {
                String[] words = normalizeString(product.getTitle()).split("\\s+");
                for (String word : words) {
                    if (word.startsWith(normalizedKeyword)) {
                        return false; // Αν υπάρχει ταίριασμα, κράτησε το προϊόν
                    }
                }
                return true; // Αν καμία λέξη δεν ταιριάζει, αφαίρεσε το προϊόν
            });
        }

        results.removeIf(product -> {
            // Έλεγχος Κατηγορίας
            if (query.getCategory() != null && product.getCategory() != null) {
                if (!product.getCategory().equals(query.getCategory())) return true;
            }

            // Έλεγχος Υποκατηγορίας
            if (query.getSubCategory() != null && product.getSubCategory() != null) {
                if (!product.getSubCategory().equals(query.getSubCategory())) return true;
            }

            // Έλεγχος Ελάχιστης Τιμής
            return product.getPrice() < query.getMinPrice();
        });

        if (sort.isEmpty()) {
            return results;
        }
        results.sort((o1, o2) -> {
            switch (sort) {
                case "titleAscending" -> {return o1.getTitle().compareTo(o2.getTitle());}
                case "titleDescending" -> {return o2.getTitle().compareTo(o1.getTitle());}
                case "priceAscending" -> {return Double.compare(o1.getPrice(), o2.getPrice());}
                case "priceDescending" -> {return Double.compare(o2.getPrice(), o1.getPrice());}
                default -> {return 0;}
            }
        });

        return results;
    }
    // Μέθοδος για κανονικοποίηση (αφαίρεση τόνων και μετατροπή σε lowercase)
    private String normalizeString(String input) {
        if (input == null) return "";
        return java.text.Normalizer.normalize(input, java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "") // Αφαίρεση τόνων
                .toLowerCase(); // Μετατροπή σε μικρά γράμματα
    }


    public ArrayList<MarketProduct> getOutOfStock() {
        return orderHandler.getOutOfStock();
    }

    public boolean addToCart(MarketProduct product, int amount) {
        return orderHandler.addToCart(product, amount);
    }

    public boolean removeFromCart(MarketProduct product, int amount) {
        return orderHandler.removeFromCart(product, amount);
    }

    public ArrayList<MarketProduct> getBestSelling(int n) {
        return orderHandler.getBestSelling(n);
    }

    public ArrayList<OrderedProduct> getCart() {return orderHandler.getCart();}

    public MarketProduct addProduct(MarketProduct a) {
        return orderHandler.add(a);
    }

    public MarketProduct removeProduct(MarketProduct a) {
        return orderHandler.remove(a);
    }

    public Order addOrder() {
        return orderHandler.addOrder(currentUser);
    }

    public ArrayList<Order> searchOrdersByUser(String user) {
        return orderHandler.searchByUser(user);
    }

    public int timesProductAppeared(String productTitle) {
        return orderHandler.timesAppeared(productTitle);
    }

    public void saveAllFiles() throws IOException {
        orderHandler.saveProductFile("Data/products.txt");
        users.saveUsersToFile("Data/users.txt");
        orderHandler.saveOrderFile("Data/orders.dat");
    }
}
