package api.order;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Η κλάση Order αναπαριστά μια παραγγελιά. Κρατάει το όνομα του χριστή, την ημερομηνία και ενα Array με προϊόντα
 */
public class Order implements Serializable {
    private final String userId;
    private final ArrayList<OrderedProduct> products;
    private final LocalDateTime time;

    public Order(String userId, ArrayList<OrderedProduct> products, LocalDateTime time) {
        this.userId = userId;
        this.products = products;
        this.time = time;
    }

    public String getUserId() {
        return userId;
    }

    public List<OrderedProduct> getProducts() {
        return products;
    }

    public LocalDateTime getTime() {
        return time;
    }

    /**
     * Μέθοδος που επιστρέφει τη συνολική τιμή της παραγγελιάς
     * @return συνολική τιμή της παραγγελιάς
     */
    public double getTotalPrice(){
        double total=0;
        for (OrderedProduct product : products){
            total += product.getPrice()*product.getAmount();
        }
        return total;
    }
}
