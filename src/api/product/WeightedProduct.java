package api.product;

/**
 * Η κλάση WeightedProduct αναπαριστά ενα προϊόν που μετριέται σε κιλά
 */
public class WeightedProduct extends MarketProduct{
    private final String measurement;

    public WeightedProduct(String title, int amount, String description, String category, String subCategory, double price) {
        super(title, amount, description, category, subCategory, price);
        this.measurement = "kg";
    }

    public String getMeasurement() {
        return measurement;
    }
}
