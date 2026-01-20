package api;

import api.order.Order;
import api.order.OrderBase;
import api.order.OrderedProduct;
import api.product.MarketProduct;
import api.product.ProductBase;
import api.product.queryProduct;
import api.user.User;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Η κλάση orderHandler συνδέει την OrderBase και ProductBase
 */
public class orderHandler {
    private OrderBase orders;
    private ProductBase products;
    private ArrayList<OrderedProduct> cart;

    public orderHandler(OrderBase orders, ProductBase products) {
        this.orders = orders;
        this.products = products;
        cart = new ArrayList<>();
    }

    public orderHandler() throws FileNotFoundException {
        products = new ProductBase("Data/products.txt");
        orders = new OrderBase("Data/orders.dat");
        cart = new ArrayList<>();
    }

    /**
     * Μέθοδος που προσθέτει ενα προϊόν στο καλάθι ή μια ποσότητα αν υπάρχει ιδη στο καλάθι αυτό το προϊόν
     * @param product το προϊόν που θα προστεθεί
     * @param amount η ποσότητα που θα προστεθεί
     * @return true αν προστεθεί στο καλάθι, false αν δεν προστεθεί. Εξαρτάται απο το αν υπάρχει αρκετό αποθύμα
     */
    public boolean addToCart(MarketProduct product,int amount){
        boolean check = true;
        if (product.getAmount() >= amount){
            for (OrderedProduct op: cart){
                if (op.getTitle().equals(product.getTitle())) {
                    check = false;
                    if (op.getAmount()+amount <= product.getAmount()){
                        op.addAmount(amount);
                        break;
                    }
                }
            }
            if (check && product.getAmount() >= amount){
                cart.add(new OrderedProduct(product.getTitle(),amount,product.getPrice()));
            }
            return true;
        }
        return false;
    }

    /**
     * Μέθοδος που αφαιρεί μια ποσότητα απο το καλάθι
     * @param product το προϊόν που θέλουμε να αφαιρέσουμε
     * @param amount η ποσότητα που θέλουμε να αφαιρέσουμε, αν είναι μεγαλύτερη απο αυτή που υπάρχει τότε το προϊόν αφαιρείται απο το καλάθι
     * @return true αν αφαιρέθηκε επιτυχώς false αν δεν υπάρχει στο καλάθι μας αυτό το προϊόν
     */
    public boolean removeFromCart(MarketProduct product,int amount){
        boolean check = false;
        Iterator<OrderedProduct> it = cart.iterator();
        while (it.hasNext()){
            OrderedProduct op = it.next();
            if (op.getTitle().equals(product.getTitle())) {
                if (op.getAmount() <= amount){
                    it.remove();
                }else {
                    op.removeAmount(amount);
                }
                check = true;
                break;
            }
        }
        return check;
    }

    /**
     * Μέθοδος για την ολοκλήρωση της παραγγελιάς
     * @param user ο χρηστής που έκανε την παραγγελιά
     * @return η παραγγελία που μόλις έγινε
     */
    public Order addOrder(User user) {
        for (OrderedProduct op: cart){
            products.search(new queryProduct(op.getTitle(),null,null)).getFirst().removeAmount(op.getAmount());
        }

        Order order = new Order(user.getUsername(),cart, LocalDateTime.now());
        orders.addOrder(order);
        cart = new ArrayList<>();
        return order;
    }

    public ArrayList<OrderedProduct> getCart() {return cart;}

    /**
     * Μέθοδος που αδειάζει το καλάθι αγορών
     */
    public void emptyCart(){
        cart = new ArrayList<>();
    }

    /**
     * Μέθοδος που επιστρέφει τα n πρώτα προϊόντα που εμφανίζονται στις περισσότερες παραγγελιές
     * @param n ποσά προϊόντα να επιστραφούν
     * @return ArrayList με τα προϊόντα που εμφανίζονται στις περισσότερες παραγγελιές
     */
    public ArrayList<MarketProduct> getBestSelling(int n) {
        ArrayList<MarketProduct> results = products.search(new queryProduct(null,null,null));
        results.sort((o1, o2)
                -> Integer.compare(timesAppeared(o2.getTitle()), timesAppeared(o1.getTitle())));
        ArrayList<MarketProduct> results1 = new ArrayList<>();
        for (int i=0;i<n;i++){
            results1.add(results.get(i));
        }
        return results1;
    }

    public ArrayList<Order> searchByUser(String user) {
        return orders.searchByUser(user);
    }

    public ArrayList<MarketProduct> getOutOfStock() {
        return products.getOutOfStock();
    }


    public int timesAppeared(String productTitle) {
        return orders.timesAppeared(productTitle);
    }

    public void saveOrderFile(String directory) throws IOException {
        orders.saveOrderFile(directory);
    }

    public ArrayList<MarketProduct> search(queryProduct query) {
        return products.search(query);
    }

    public MarketProduct add(MarketProduct a) {
        return products.add(a);
    }

    public MarketProduct remove(MarketProduct a) {
        return products.remove(a);
    }

    public void saveProductFile(String directory) throws IOException {
        products.saveProductFile(directory);
    }
}
