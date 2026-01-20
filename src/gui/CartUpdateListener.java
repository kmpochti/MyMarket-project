package gui;

public interface CartUpdateListener {
    /**
     * Ενημερώνει την εμφάνιση του καλαθιού.
     * Αυτή η μέθοδος καλείται όταν υπάρχουν αλλαγές στο καλάθι,
     * όπως προσθήκη, αφαίρεση ή ενημέρωση προϊόντων.
     */
    void updateCartDisplay();
}
