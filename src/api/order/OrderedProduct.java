package api.order;

import api.product.Product;

import java.io.Serializable;

/**
 * Η κλάση OrderedProduct αναπαριστά ενα προϊόν που έχει αγοραστεί
 */
public class OrderedProduct extends Product implements Serializable {
    private final double price;

    public OrderedProduct(String title, int amount, double price) {
        super(title, amount);
        this.price = price;
    }

    public double getPrice() {
        return price;
    }
}
