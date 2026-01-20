package api.product;

/**
 * Η κλάση queryProduct είναι βοηθητική κλάση για την αναζήτηση προϊόντος
 * Αποθηκεύει όλες τις τιμές που χρειάζεται η ProductBase για την αναζήτηση
 *
 */
public class queryProduct extends MarketProduct{
    private double minPrice;

    public queryProduct(String title, String category, String subCategory, double maxPrice, double minPrice) {
        super(title, 0, "", category, subCategory, maxPrice);
        this.minPrice = minPrice;
    }

    public queryProduct(String title, String category, String subCategory) {
        super(title, 0, "", category, subCategory, Double.MAX_VALUE);
        this.minPrice = 0;
    }

    public double getMinPrice() {
        return minPrice;
    }

    @Override
    public String getMeasurement() {
        return "";
    }
}
