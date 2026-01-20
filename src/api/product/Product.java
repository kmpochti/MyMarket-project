package api.product;


import java.io.Serializable;
import java.util.Objects;

/**
 * Η κλάση Product αναπαριστά αυθαίρετα ενα προϊόν
 */
abstract public class Product implements Serializable {
    private String title;
    private int amount;

    public Product(String title, int amount) {
        this.title = title;
        this.amount = amount;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title=title;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }


    public int getAmount() {
        return amount;
    }

    /**
     * Μέθοδος για την αφαίρεση μιας ποσότητας απο ενα προϊόν
     * @param amountSold η ποσότητα που θέλουμε να αφαιρεθεί
     * @return false αν δεν υπάρχει αρκετή διαθέσιμη ποσότητα αλλιώς true
     */
    public boolean removeAmount(int amountSold) {
        if (this.amount < amountSold || amountSold <= 0){
            return false;
        }else {
            this.amount -= amountSold;
            return true;
        }
    }

    /**
     * Μέθοδος για την πρόσθεση μιας ποσότητας σε ενα προϊόν
     * @param amountAdd η ποσότητα που θέλουμε να προστεθεί
     */
    public void addAmount(int amountAdd){
        if (amountAdd > 0){
            this.amount += amountAdd;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product product)) return false;
        return Objects.equals(title, product.title);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(title);
    }
}
