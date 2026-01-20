package api.product;

/**
 * Η κλάση MarketProduct αναπαριστά ενα αυθαίρετο προϊόν που πωλείται στο κατάστημα
 */
abstract public class MarketProduct extends Product{
    private String description;
    private String category;
    private String subCategory;
    private double price;

    public MarketProduct(String title, int amount, String description, String category, String subCategory, double price) {
        super(title, amount);
        this.description = description;
        this.category = category;
        this.subCategory = subCategory;
        this.price = price;
    }

    /**
     *
     * @return επιστρέφει τη μονάδα μέτρησης του προϊόντος (kg, τεμάχια, κτλ)
     */
    abstract public String getMeasurement();

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
