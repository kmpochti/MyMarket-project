package gui;

import api.api;
import api.order.Order;
import api.order.OrderedProduct;
import api.product.MarketProduct;
import api.product.queryProduct;
import javafx.application.Platform;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;

public class GUICompletionOrder extends Application {

    private api apiInstance;
    private int totalCartItems = 0;
    private double totalCartCost = 0.0;
    private Label totalCostLabel;

    /**
     * Δημιουργεί το αντικείμενο GUICompletionOrder.
     *
     * @param apiInstance Το αντικείμενο API για την επικοινωνία με τη βάση δεδομένων.
     */
    public GUICompletionOrder(api apiInstance) {
        this.apiInstance = apiInstance;
    }

    /**
     * Εκκίνηση του παραθύρου για την ολοκλήρωση παραγγελίας.
     *
     * @param primaryStage Το κύριο στάδιο της εφαρμογής JavaFX.
     */
    @Override
    public void start(Stage primaryStage) {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("Ολοκλήρωση Παραγγελίας");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);

        VBox productList = new VBox(10);
        productList.setPadding(new Insets(10));
        productList.setAlignment(Pos.TOP_LEFT);
        scrollPane.setContent(productList);

        totalCostLabel = new Label("Συνολικό Κόστος: " + String.format("%.2f€", totalCartCost));
        totalCostLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: green;");

        ArrayList<OrderedProduct> cart = apiInstance.getCart();
        updateCartDisplay();

        if (cart.isEmpty()) {
            productList.getChildren().add(new Label("Το καλάθι σας είναι άδειο."));
        } else {
            for (OrderedProduct product : cart) {
                HBox productRow = new HBox(10);
                productRow.setAlignment(Pos.CENTER_LEFT);

                ImageView productImage;
                try {
                    productImage = new ImageView(loadImage(product.getTitle() + ".jpg"));
                } catch (Exception e) {
                    productImage = new ImageView(new Image("/pictures/placeholder.jpg"));
                }
                productImage.setFitWidth(50);
                productImage.setFitHeight(50);

                Label productName = new Label(product.getTitle());
                productName.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

                Label productPrice = new Label(String.format("%.2f€", product.getPrice()));
                productPrice.setStyle("-fx-font-size: 14px; -fx-text-fill: green;");

                TextField quantityField = new TextField(String.valueOf(product.getAmount()));
                quantityField.setPrefWidth(50);
                quantityField.setEditable(false);

                Button decrementButton = new Button("-");
                decrementButton.setOnAction(e -> updateQuantity(product, -1, quantityField));

                Button incrementButton = new Button("+");
                incrementButton.setOnAction(e -> updateQuantity(product, 1, quantityField));

                Button deleteButton = new Button("Διαγραφή");
                deleteButton.setOnAction(e -> {
                    MarketProduct marketProduct = apiInstance.searchProduct(
                            new queryProduct(product.getTitle(), null, null)
                    ).stream().findFirst().orElse(null);

                    if (marketProduct == null) {
                        showError("Το προϊόν δεν βρέθηκε στη βάση δεδομένων.");
                        return;
                    }

                    apiInstance.removeFromCart(marketProduct, product.getAmount());
                    updateCartDisplay();
                    reloadOrderWindow(primaryStage);
                });

                productRow.getChildren().addAll(productImage, productName, productPrice, decrementButton, quantityField, incrementButton, deleteButton);
                productList.getChildren().add(productRow);
            }
        }

        Button completeOrderButton = new Button("Ολοκλήρωση Παραγγελίας");
        completeOrderButton.setOnAction(e -> {
            if (apiInstance.getCart().isEmpty()) {
                showError("Το καλάθι σας είναι άδειο.");
                return;
            }

            try {
                Order newOrder = apiInstance.addOrder();
                if (newOrder != null) {
                    apiInstance.saveAllFiles();
                    showSuccess("Η παραγγελία ολοκληρώθηκε επιτυχώς!");

                    totalCartItems = 0;
                    totalCartCost = 0;
                    updateCartDisplay();
                    primaryStage.close();

                    GUIOrderDone orderDone = new GUIOrderDone();
                    orderDone.start(new Stage());
                } else {
                    showError("Αποτυχία ολοκλήρωσης παραγγελίας.");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                showError("Σφάλμα κατά την ολοκλήρωση παραγγελίας: " + ex.getMessage());
            }
        });

        layout.getChildren().addAll(titleLabel, scrollPane, totalCostLabel, completeOrderButton);

        Scene scene = new Scene(layout, 800, 600);
        primaryStage.setTitle("Ολοκλήρωση Παραγγελίας");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Ενημερώνει την ποσότητα ενός προϊόντος στο καλάθι.
     *
     * @param product       Το προϊόν για ενημέρωση.
     * @param delta         Η αλλαγή στην ποσότητα.
     * @param quantityField Το πεδίο ποσότητας προς ενημέρωση.
     */
    private void updateQuantity(OrderedProduct product, int delta, TextField quantityField) {
        int currentQuantity = Integer.parseInt(quantityField.getText());
        int newQuantity = currentQuantity + delta;

        if (newQuantity <= 0) {
            showError("Η ποσότητα δεν μπορεί να είναι μηδενική ή αρνητική.");
            return;
        }

        MarketProduct marketProduct = apiInstance.searchProduct(
                new queryProduct(product.getTitle(), null, null)
        ).stream().findFirst().orElse(null);

        if (marketProduct == null) {
            showError("Το προϊόν δεν βρέθηκε στη βάση δεδομένων.");
            return;
        }

        boolean success = false;
        if (delta > 0) {
            success = apiInstance.addToCart(marketProduct, delta);
        } else if (delta < 0) {
            success = apiInstance.removeFromCart(marketProduct, -delta);
        }

        if (success) {
            totalCartItems += delta;
            totalCartCost += delta * product.getPrice();
            quantityField.setText(String.valueOf(newQuantity));
            updateCartDisplay();
        } else {
            if (delta > 0) {
                showError("Δεν υπάρχει αρκετό απόθεμα.");
            } else {
                showError("Το προϊόν δεν βρίσκεται στο καλάθι.");
            }
        }
    }

    /**
     * Ενημερώνει την εμφάνιση του καλαθιού αγορών. Υπολογίζει το συνολικό αριθμό προϊόντων, το συνολικό κόστος
     * και ενημερώνει το UI με τις πληροφορίες αυτές. Επίσης, ειδοποιεί το listener του καλαθιού αν υπάρχει.
     */
    private void updateCartDisplay() {
        totalCartItems = Math.max(0, apiInstance.getCart().stream().mapToInt(OrderedProduct::getAmount).sum());
        totalCartCost = Math.max(0, apiInstance.getCart().stream()
                .mapToDouble(product -> product.getAmount() * product.getPrice()).sum());

        Platform.runLater(() -> {
            totalCostLabel.setText("Συνολικό Κόστος: " + String.format("%.2f€", totalCartCost));
            CartUpdateListener listener = GUISearch.getInstance(); // Χρήση του Singleton
            if (listener != null) {
                listener.updateCartDisplay();
            }
        });
    }

    /**
     * Επαναφορτώνει το παράθυρο παραγγελιών.
     * Κλείνει το τρέχον παράθυρο (primaryStage) και δημιουργεί ένα νέο παράθυρο εκτελώντας την ίδια εφαρμογή.
     *
     * @param primaryStage Το κύριο στάδιο (παράθυρο) που πρέπει να επαναφορτωθεί.
     */
    private void reloadOrderWindow(Stage primaryStage) {
        primaryStage.close();
        start(new Stage());
    }

    /**
     * Φορτώνει μια εικόνα από το καθορισμένο αρχείο.
     * Εάν η εικόνα δεν βρεθεί, επιστρέφει μια προκαθορισμένη εικόνα (default).
     *
     * @param fileName Το όνομα του αρχείου της εικόνας που πρέπει να φορτωθεί.
     * @return Ένα αντικείμενο Image που αντιπροσωπεύει την εικόνα.
     */
    private Image loadImage(String fileName) {
        String path = "/pictures/" + fileName;
        URL resource = getClass().getResource(path);

        if (resource == null) {
            System.out.println("Η εικόνα δεν βρέθηκε: " + path);
            return new Image("/pictures/jeshoots-com-7VOyZ0-iO0o-unsplash.jpg"); // Default εικόνα
        }

        return new Image(resource.toExternalForm());
    }

    /**
     * Εμφανίζει ένα μήνυμα σφάλματος στον χρήστη μέσω ενός Alert διαλόγου.
     *
     * @param message Το μήνυμα σφάλματος που θα εμφανιστεί.
     */
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Σφάλμα");
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Εμφανίζει ένα μήνυμα επιτυχίας στον χρήστη μέσω ενός Alert διαλόγου.
     *
     * @param message Το μήνυμα επιτυχίας που θα εμφανιστεί.
     */
    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Επιτυχία");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
