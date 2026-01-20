package api.product;

/**
 * Η κλάση PackagedProduct αναπαριστά ενα προϊόν που μετριέται σε τεμάχια
 */
public class PackagedProduct extends MarketProduct{
    private final String measurement;

    public PackagedProduct(String title, int amount, String description, String category, String subCategory, double price) {
        super(title, amount, description, category, subCategory, price);
        this.measurement = " τεμάχια";
    }

    public String getMeasurement() {
        return measurement;
    }
}
