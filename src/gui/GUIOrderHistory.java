package gui;

import api.api;
import api.order.Order;
import api.user.User;
import api.order.OrderedProduct;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class GUIOrderHistory extends Application {
    private final api apiInstance;
    private final String username;

    /**
     * Κατασκευαστής της κλάσης GUIOrderHistory.
     *
     * @param apiInstance Αντικείμενο API για τη διαχείριση δεδομένων.
     * @param username Το όνομα του χρήστη του οποίου το ιστορικό παραγγελιών θα εμφανιστεί.
     */
    public GUIOrderHistory(api apiInstance, String username) {
        this.apiInstance = apiInstance;
        this.username = username;
    }

    /**
     * Εκκίνηση του γραφικού περιβάλλοντος χρήστη.
     *
     * @param primaryStage Το κύριο παράθυρο της εφαρμογής JavaFX.
     */
    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_LEFT);

        Label titleLabel = new Label("Ιστορικό Παραγγελιών του χρήστη: " + username);
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        root.getChildren().add(titleLabel);

        User currentUser = apiInstance.getCurrentUser();
        List<Order> orders;

        if (currentUser != null && currentUser.getUsername().equals(username)) {
            orders = apiInstance.searchOrdersByUser(currentUser.getUsername());
        } else {
            orders = apiInstance.searchOrdersByUser(username);
        }

        if (orders.isEmpty()) {
            Label noOrdersLabel = new Label("Δεν έχετε πραγματοποιήσει καμία παραγγελία.");
            noOrdersLabel.setStyle("-fx-font-size: 16px;");
            root.getChildren().add(noOrdersLabel);
        } else {
            for (Order order : orders) {
                VBox orderBox = new VBox(10);
                orderBox.setPadding(new Insets(10));
                orderBox.setStyle("-fx-border-color: gray; -fx-border-width: 1; -fx-border-radius: 5;");

                Label dateLabel = new Label("Ημερομηνία: " + order.getTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
                dateLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
                orderBox.getChildren().add(dateLabel);

                for (OrderedProduct product : order.getProducts()) {
                    HBox productRow = new HBox(10);
                    productRow.setAlignment(Pos.CENTER_LEFT);

                    ImageView imageView;
                    try {
                        imageView = new ImageView(new Image("/pictures/" + product.getTitle() + ".jpg"));
                    } catch (Exception e) {
                        imageView = new ImageView(new Image("/pictures/default.jpg")); // Default εικόνα
                    }
                    imageView.setFitWidth(50);
                    imageView.setFitHeight(50);

                    Label productDetails = new Label(
                            product.getTitle() + " - Ποσότητα: " + product.getAmount() +
                                    ", Τιμή: " + String.format("%.2f€", product.getPrice())
                    );
                    productDetails.setStyle("-fx-font-size: 14px;");

                    productRow.getChildren().addAll(imageView, productDetails);
                    orderBox.getChildren().add(productRow);
                }

                Label totalPriceLabel = new Label("Συνολική Τιμή: " + String.format("%.2f€", order.getTotalPrice()));
                totalPriceLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: green;");
                orderBox.getChildren().add(totalPriceLabel);

                root.getChildren().add(orderBox);
            }
        }

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(root);
        scrollPane.setFitToWidth(true);
        scrollPane.setPannable(true);

        Scene scene = new Scene(scrollPane, 800, 600);
        primaryStage.setTitle("Ιστορικό Παραγγελιών");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
