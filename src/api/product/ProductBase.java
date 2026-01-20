package api.product;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Η κλάση OrderBase διαχειρίζεται τα προϊόντα του καταστήματος
 */
public class ProductBase {
    private HashMap<String, MarketProduct> products;

    /**
     * Constructor
     * @param f αρχείο απο το οποίο φορτώνει τα προϊόντα
     */
    public ProductBase(String f){
        try {
            products = ProductParser.readProductFile(f);
        } catch (FileNotFoundException e) {
            products = new HashMap<>();
        }
    }

    /**
     * Αναζήτηση προϊόντων
     * @param query αντικείμενο με τα πεδία που θέλουμε να αναζητήσουμε. Αν κάποιο πεδίο είναι null τότε το αγνοεί
     * @return HashMap<String, MarketProduct> με τα αποτελέσματα
     */
    public ArrayList<MarketProduct> search(queryProduct query){
        ArrayList<MarketProduct> results = new ArrayList<>();
        MarketProduct product;
        for (String key : products.keySet()){
            product = products.get(key);
            boolean match = true;
            if (query.getTitle() != null && !product.getTitle().toLowerCase().contains(query.getTitle().toLowerCase())){
                match = false;
            }
            if (query.getCategory() != null && !product.getCategory().equals(query.getCategory())){
                match = false;
            }
            if (query.getSubCategory() != null && !product.getSubCategory().equals(query.getSubCategory())){
                match = false;
            }
            if (query.getCategory() != null && !product.getCategory().equals(query.getCategory())){
                match = false;
            }
            if (query.getMinPrice() >= product.getPrice()){
                match = false;
            }
            if (query.getPrice() <= product.getPrice()){
                match = false;
            }
            if (match){
                results.add(product);
            }
        }
        return results;
    }

    /**
     * Μέθοδος που προσθέτει ενα προϊόν
     * @param a το προϊόν
     * @return a αν προστέθηκε και null αν υπάρχει ιδη
     */
    public MarketProduct add(MarketProduct a){
        if (products.containsKey(a.getTitle())){
            return null;
        }
        products.put(a.getTitle(),a);
        return a;
    }

    /**
     * Μέθοδος που επιστρέφει τα προϊόντα που δεν έχουν απόθεμα
     * @return Arraylist με τα αποτελέσματα
     */
    public ArrayList<MarketProduct> getOutOfStock(){
        ArrayList<MarketProduct> results = new ArrayList<>();
        MarketProduct product;
        for (String key : products.keySet()){
            product = products.get(key);
            if (product.getAmount() == 0){
                results.add(product);
            }
        }
        return results;
    }

    public MarketProduct remove(String a){
        return products.remove(a);
    }

    public MarketProduct remove(MarketProduct a){
        return products.remove(a.getTitle());
    }

    public void saveProductFile(String directory) throws IOException {
        ProductParser.saveProductFile(directory, products);
    }
}
