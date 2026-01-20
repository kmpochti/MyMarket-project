package api.order;

import java.io.IOException;
import java.util.ArrayList;


/**
 * Η κλάση OrderBase διαχειρίζεται τις παραγγελιές που έχουν γίνει στην εφαρμογή
 */
public class OrderBase {
    private ArrayList<Order> orders;

    /**
     * Constructor φορτώνει απο αρχείο το ιστορικό παραγγελιών
     * @param filename που θέλουμε να φορτώσουμε
     */
    public OrderBase(String filename){
        if (filename.isEmpty()){
            orders = new ArrayList<>();
            return;
        }
        try {
            orders = OrderParser.readOrders(filename);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e);
            orders = new ArrayList<>();
        }
    }

    /**
     * Προσθέτει μια παραγγελιά
     * @param order η παραγγελία που θα προστεθεί
     */
    public void addOrder(Order order){
        orders.add(order);
    }

    /**
     * Αναζήτηση με βάση έναν χριστή
     * @param user το όνομα χρήστη
     * @return ArrayList με τις παραγγελίες αυτού του χρήστη
     */
    public ArrayList<Order> searchByUser(String user){
        ArrayList<Order> results = new ArrayList<>();
        for (Order order : orders) {
            if (order.getUserId().equals(user)){
                results.add(order);
            }
        }
        return results;
    }

    /**
     * Αναζήτηση με βάση ενα προϊόν
     * @param productTitle τίτλος του προϊόντος
     * @return ArrayList με τις παραγγελίες που περιέχουν αυτό το προϊόν
     */
    public ArrayList<Order> searchByProduct(String productTitle){
        ArrayList<Order> results = new ArrayList<>();
        for (Order order : orders) {
            if (order.getProducts().contains(new OrderedProduct(productTitle,0,0))){
                results.add(order);
            }
        }
        return results;
    }

    /**
     * @param productTitle Τίτλος προϊόντος που αναζητούμε
     * @return τη συνολική ποσότητα που έχει πουληθεί απο αυτό το προϊόν
     */
    public int totalSoldOfProduct(String productTitle){
        int total = 0;
        for (Order order : this.searchByProduct(productTitle)){
            for (OrderedProduct product : order.getProducts()){
                if (product.getTitle().equals(productTitle)){
                    total += product.getAmount();
                }
            }
        }
        return total;
    }

    /**
     *
     * @param productTitle Τίτλος του προϊόν
     * @return Σε πόσες παραγγελίες εμφανίζεται ενα προϊόν
     */
    public int timesAppeared(String productTitle){
        return this.searchByProduct(productTitle).size();
    }

    public void saveOrderFile(String directory) throws IOException {
        OrderParser.saveOrders(directory,orders);
    }
}
