package gui;

import api.api;
import api.order.OrderedProduct;
import api.product.MarketProduct;
import api.product.queryProduct;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import java.net.URL;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.Popup;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



public class GUISearch extends Application implements CartUpdateListener {

    private api apiInstance;
    private Stage primaryStage;
    private int totalCartItems = 0;
    private double totalCartCost = 0.0;
    private Label cartLabel;
    private Label costLabel;
    private Popup cartPreviewPopup;
    private static GUISearch instance;
    private String keyword;

    private boolean isHoveringCartBox = false;
    private boolean isHoveringPopup = false;

    /**
     * Το κύριο σημείο εκκίνησης της εφαρμογής JavaFX.
     * Καλείται για να ξεκινήσει η εφαρμογή με το `launch`.
     *
     * @param args Τα επιχειρήματα γραμμής εντολών που περνιούνται στην εφαρμογή.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Παρέχει μια μοναδική στατική αναφορά στην τρέχουσα διεπαφή `GUISearch`.
     * Χρησιμοποιείται για πρόσβαση σε αυτή την κλάση από άλλα σημεία του κώδικα.
     *
     * @return Η τρέχουσα στατική αναφορά του `GUISearch`.
     */
    public static GUISearch getInstance() {
        return instance;
    }

    /**
     * Κατασκευαστής της κλάσης `GUISearch`.
     * Αρχικοποιεί το αντικείμενο με το API που συνδέεται με τα δεδομένα
     * και αποθηκεύει μια στατική αναφορά στην τρέχουσα διεπαφή.
     *
     * @param apiInstance Το αντικείμενο API που θα χρησιμοποιηθεί για την επικοινωνία με τα δεδομένα.
     */
    public GUISearch(api apiInstance) {
        this.apiInstance = apiInstance;
        instance = this;
    }

    /**
     * Αρχικοποιεί το GUI της αναζήτησης προϊόντων.
     * @param primaryStage Το κύριο παράθυρο της εφαρμογής.
     */
    @Override
    public void start(Stage primaryStage){
        this.primaryStage = primaryStage;

        BorderPane rootLayout = new BorderPane();
        rootLayout.setPadding(new Insets(20));

        setBackgroundImage(rootLayout, "/pictures/monika-grabkowska-ITixmXwUsJY-unsplash.jpg");

        HBox searchBar = createSearchBar();

        HBox cartDisplay = createCartDisplay();

        Button logoutButton = new Button("Αποσύνδεση");
        logoutButton.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-font-weight: bold;");
        logoutButton.setOnMouseEntered(e -> logoutButton.setStyle("-fx-background-color: darkred; -fx-text-fill: white;"));
        logoutButton.setOnMouseExited(e -> logoutButton.setStyle("-fx-background-color: red; -fx-text-fill: white;"));

        logoutButton.setOnAction(e -> {
            try {
                GUILogin loginScreen = new GUILogin();
                loginScreen.start(primaryStage); // Επιστροφή στη φόρμα σύνδεσης
            } catch (Exception ex) {
                ex.printStackTrace();
                showError("Σφάλμα κατά την αποσύνδεση.");
            }
        });

        BorderPane topBar = new BorderPane();
        topBar.setCenter(searchBar);
        topBar.setRight(cartDisplay);

        VBox leftPanel = createFilterPanel();

        Pagination pagination = createPagination(new queryProduct(null, null, null), "", "");

        StackPane centerPane = new StackPane(pagination);
        centerPane.setAlignment(Pos.CENTER);

        topBar.setCenter(searchBar);
        topBar.setRight(cartDisplay);

        rootLayout.setTop(topBar);
        rootLayout.setLeft(leftPanel);
        rootLayout.setCenter(centerPane);

        Scene scene = new Scene(rootLayout, 1200, 800);
        primaryStage.setTitle("MyMarket - Αναζήτηση Προϊόντων");
        primaryStage.setScene(scene);

        rootLayout.requestFocus();

        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    /**
     * Δημιουργεί ένα αντικείμενο Pagination για την εμφάνιση των προϊόντων με βάση το ερώτημα αναζήτησης.
     *
     * @param query Το ερώτημα αναζήτησης προϊόντων που περιέχει φίλτρα κατηγορίας και υποκατηγορίας.
     * @param sortKey Το κλειδί ταξινόμησης των προϊόντων (π.χ. τιμή ή αλφαβητικά).
     * @param keyword Η λέξη-κλειδί για την αναζήτηση προϊόντων.
     * @return Ένα αντικείμενο Pagination που εμφανίζει τα προϊόντα σε σελίδες.
     */
    private Pagination createPagination(queryProduct query, String sortKey, String keyword) {
        ArrayList<MarketProduct> products = apiInstance.searchProduct(query, sortKey, keyword);

        int itemsPerPage = 12;
        int totalPages = (int) Math.ceil((double) products.size() / itemsPerPage);

        Pagination pagination = new Pagination(totalPages, 0);
        pagination.setPageFactory(pageIndex -> {
            GridPane productGrid = new GridPane();
            productGrid.setPadding(new Insets(20));
            productGrid.setHgap(20);
            productGrid.setVgap(20);

            int columns = 4;
            int startIndex = pageIndex * itemsPerPage;

            for (int i = 0; i < itemsPerPage && startIndex + i < products.size(); i++) {
                VBox productCard = createProductCard(products.get(startIndex + i));
                int col = i % columns;
                int row = i / columns;
                productGrid.add(productCard, col, row);
            }


            ScrollPane scrollPane = new ScrollPane(productGrid);
            scrollPane.setFitToWidth(true);
            scrollPane.setPrefHeight(600);

            return scrollPane;
        });

        return pagination;
    }

    /**
     * Δημιουργεί μια μπάρα αναζήτησης για προϊόντα.
     * @return Ένα HBox που περιέχει το πεδίο αναζήτησης.
     */
    private HBox createSearchBar() {
        HBox searchBar = new HBox();
        searchBar.setPadding(new Insets(10));
        searchBar.setAlignment(Pos.CENTER);

        TextField searchField = new TextField();
        searchField.setPromptText("Αναζήτηση προϊόντος...");
        searchField.setPrefWidth(600);
        searchField.setPrefHeight(40);
        searchField.setStyle("-fx-font-size: 16px; -fx-border-color: #ccc; -fx-border-radius: 10px; -fx-padding: 5px;");

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            keyword = newValue.trim();
            updatePaginationWithFilters(keyword);
        });

        searchBar.getChildren().add(searchField);
        return searchBar;
    }

    /**
     * Μετατρέπει κείμενο από Greeklish σε Ελληνικά.
     *
     * @param input το κείμενο σε Greeklish.
     * @return το μεταφρασμένο κείμενο σε Ελληνικά.
     */
    private String convertGreeklishToGreek(String input) {
        if (input == null || input.isEmpty()) return input;

        Map<String, String> greeklishToGreekMap = new HashMap<>();
        greeklishToGreekMap.put("a", "α");
        greeklishToGreekMap.put("b", "β");
        greeklishToGreekMap.put("g", "γ");
        greeklishToGreekMap.put("d", "δ");
        greeklishToGreekMap.put("e", "ε");
        greeklishToGreekMap.put("z", "ζ");
        greeklishToGreekMap.put("h", "η");
        greeklishToGreekMap.put("th", "θ");
        greeklishToGreekMap.put("i", "ι");
        greeklishToGreekMap.put("k", "κ");
        greeklishToGreekMap.put("l", "λ");
        greeklishToGreekMap.put("m", "μ");
        greeklishToGreekMap.put("n", "ν");
        greeklishToGreekMap.put("ks", "ξ");
        greeklishToGreekMap.put("o", "ο");
        greeklishToGreekMap.put("p", "π");
        greeklishToGreekMap.put("r", "ρ");
        greeklishToGreekMap.put("s", "σ");
        greeklishToGreekMap.put("t", "τ");
        greeklishToGreekMap.put("y", "υ");
        greeklishToGreekMap.put("f", "φ");
        greeklishToGreekMap.put("x", "χ");
        greeklishToGreekMap.put("ps", "ψ");
        greeklishToGreekMap.put("w", "ω");

        greeklishToGreekMap.put("s ", "ς ");
        greeklishToGreekMap.put("s$", "ς");

        for (Map.Entry<String, String> entry : greeklishToGreekMap.entrySet()) {
            input = input.replaceAll("(?i)" + entry.getKey(), entry.getValue()); // Χειρισμός πεζών/κεφαλαίων
        }

        return input;
    }

    /**
     * Δημιουργεί το panel για φίλτρα αναζήτησης προϊόντων.
     * @return Ένα VBox που περιέχει τα φίλτρα.
     */
    private VBox createFilterPanel() {
        VBox filterPanel = new VBox(15);
        filterPanel.setPadding(new Insets(20));
        filterPanel.setAlignment(Pos.TOP_CENTER);
        filterPanel.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #ccc;");

        Label logoLabel = new Label("My Market");
        logoLabel.setStyle("-fx-font-family: 'Comic Sans MS', 'Arial', sans-serif; " +
                "-fx-font-weight: bold; " +
                "-fx-font-size: 40px; " +
                "-fx-text-fill: red; " +
                "-fx-effect: dropshadow(gaussian, black, 5, 0.8, 2, 2);");

        Button historyButton = new Button("👤 Ιστορικό Παραγγελιών");
        historyButton.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        historyButton.setOnAction(e -> {
            try {
                GUIOrderHistory orderHistory = new GUIOrderHistory(apiInstance, apiInstance.getCurrentUser().getUsername());
                orderHistory.start(new Stage());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        Label titleLabel = new Label("Φίλτρα Αναζήτησης");
        titleLabel.setFont(new Font("Arial", 18));
        titleLabel.setStyle("-fx-font-weight: bold;");

        double comboBoxWidth = 200;
        ComboBox<String> categoriesComboBox = new ComboBox<>();
        ComboBox<String> subcategoriesComboBox = new ComboBox<>();
        ComboBox<String> sortingComboBox = new ComboBox<>();

        Map<String, String[]> categoriesMap = new HashMap<>();
        categoriesMap.put("Φρέσκα τρόφιμα", new String[]{"Φρούτα", "Λαχανικά", "Ψάρια", "Κρέατα"});
        categoriesMap.put("Κατεψυγμένα τρόφιμα", new String[]{"Κατεψυγμένα λαχανικά", "Κατεψυγμένα κρέατα", "Κατεψυγμένες πίτσες", "Κατεψυγμένα γεύματα"});
        categoriesMap.put("Προϊόντα ψυγείου", new String[]{"Τυριά", "Γιαούρτια", "Γάλα", "Βούτυρο"});
        categoriesMap.put("Αλλαντικά", new String[]{"Ζαμπόν", "Σαλάμι", "Μπέικον"});
        categoriesMap.put("Αλκοολούχα ποτά", new String[]{"Μπύρα", "Κρασί", "Ούζο", "Τσίπουρο"});
        categoriesMap.put("Μη αλκοολούχα ποτά", new String[]{"Χυμοί", "Αναψυκτικά", "Νερό", "Ενεργειακά ποτά"});
        categoriesMap.put("Καθαριστικά για το σπίτι", new String[]{"Καθαριστικά για το πάτωμα", "Καθαριστικά για τα τζάμια", "Καθαριστικά κουζίνας"});
        categoriesMap.put("Απορρυπαντικά ρούχων", new String[]{"Σκόνες πλυντηρίου", "Υγρά πλυντηρίου", "Μαλακτικά"});
        categoriesMap.put("Καλλυντικά", new String[]{"Κρέμες προσώπου", "Μακιγιάζ", "Λοσιόν σώματος"});
        categoriesMap.put("Προϊόντα στοματικής υγιεινής", new String[]{"Οδοντόκρεμες", "Οδοντόβουρτσες", "Στοματικά διαλύματα"});
        categoriesMap.put("Πάνες", new String[]{"Πάνες για μωρά", "Πάνες ενηλίκων"});
        categoriesMap.put("Δημητριακά", new String[]{"Νιφάδες καλαμποκιού", "Μούσλι", "Βρώμη"});
        categoriesMap.put("Ζυμαρικά", new String[]{"Μακαρόνια", "Κριθαράκι", "Ταλιατέλες"});
        categoriesMap.put("Σνακ", new String[]{"Πατατάκια", "Κράκερς", "Μπάρες δημητριακών"});
        categoriesMap.put("Έλαια", new String[]{"Ελαιόλαδο", "Ηλιέλαιο", "Σογιέλαιο"});
        categoriesMap.put("Κονσέρβες", new String[]{"Κονσέρβες ψαριών", "Κονσέρβες λαχανικών", "Κονσέρβες φρούτων"});
        categoriesMap.put("Χαρτικά", new String[]{"Χαρτί υγείας", "Χαρτοπετσέτες", "Χαρτομάντηλα"});

        categoriesComboBox.getItems().add("Επιλέξτε Κατηγορία");
        categoriesComboBox.getItems().addAll(categoriesMap.keySet());
        categoriesComboBox.setPromptText("Επιλέξτε Κατηγορία");
        categoriesComboBox.setPrefWidth(comboBoxWidth);

        subcategoriesComboBox.getItems().add("Επιλέξτε Υποκατηγορία");
        subcategoriesComboBox.setPromptText("Επιλέξτε Υποκατηγορία");
        subcategoriesComboBox.setPrefWidth(comboBoxWidth);
        subcategoriesComboBox.setDisable(true);

        categoriesComboBox.setOnAction(e -> {
            subcategoriesComboBox.getItems().clear();
            subcategoriesComboBox.getItems().add("Επιλέξτε Υποκατηγορία");
            String selectedCategory = categoriesComboBox.getValue();
            if (selectedCategory != null && !selectedCategory.equals("Επιλέξτε Κατηγορία")) {
                subcategoriesComboBox.getItems().addAll(categoriesMap.get(selectedCategory));
                subcategoriesComboBox.setDisable(false);
            } else {
                subcategoriesComboBox.setDisable(true);
            }
            subcategoriesComboBox.setValue("Επιλέξτε Υποκατηγορία");
            updatePaginationWithFilters(keyword);
        });

        subcategoriesComboBox.setOnAction(e -> updatePaginationWithFilters(keyword));
        sortingComboBox.setOnAction(e -> updatePaginationWithFilters(keyword));

        sortingComboBox.getItems().add("Επιλέξτε Ταξινόμηση");
        sortingComboBox.getItems().addAll("Τιμή Αυξανόμενη", "Τιμή Φθίνουσα", "Αλφαβητικά Αυξανόμενη", "Αλφαβητικά Φθίνουσα");
        sortingComboBox.setPromptText("Επιλέξτε Ταξινόμηση");
        sortingComboBox.setPrefWidth(comboBoxWidth);
        sortingComboBox.setOnAction(e -> updatePaginationWithFilters(keyword));

        Button logoutButton = new Button("Αποσύνδεση");
        logoutButton.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-font-weight: bold;");
        logoutButton.setOnMouseEntered(e -> logoutButton.setStyle("-fx-background-color: darkred; -fx-text-fill: white;"));
        logoutButton.setOnMouseExited(e -> logoutButton.setStyle("-fx-background-color: red; -fx-text-fill: white;"));

        logoutButton.setOnAction(e -> {
            try {
                GUILogin loginScreen = new GUILogin();
                loginScreen.start(primaryStage); // Επιστροφή στη φόρμα σύνδεσης
            } catch (Exception ex) {
                ex.printStackTrace();
                showError("Σφάλμα κατά την αποσύνδεση.");
            }
        });

        VBox.setVgrow(logoutButton, Priority.ALWAYS);

        filterPanel.getChildren().addAll(logoLabel, historyButton, titleLabel, categoriesComboBox, subcategoriesComboBox, sortingComboBox, logoutButton);

        return filterPanel;
    }

    /**
     * Ενημερώνει την προβολή των προϊόντων με βάση τα επιλεγμένα φίλτρα και την αναζήτηση.
     *
     * @param keyword Η λέξη-κλειδί για αναζήτηση.
     */
    private void updatePaginationWithFilters(String keyword) {
        VBox leftPanel = (VBox) ((BorderPane) primaryStage.getScene().getRoot()).getLeft();
        ComboBox<String> categoriesComboBox = (ComboBox<String>) leftPanel.getChildren().get(3);
        ComboBox<String> subcategoriesComboBox = (ComboBox<String>) leftPanel.getChildren().get(4);
        ComboBox<String> sortingComboBox = (ComboBox<String>) leftPanel.getChildren().get(5);

        String selectedCategory = categoriesComboBox.getValue();
        String selectedSubcategory = subcategoriesComboBox.getValue();
        String selectedSorting = sortingComboBox.getValue();

        selectedCategory = "Επιλέξτε Κατηγορία".equals(selectedCategory) ? null : selectedCategory;
        selectedSubcategory = "Επιλέξτε Υποκατηγορία".equals(selectedSubcategory) ? null : selectedSubcategory;
        String sortingKey = convertSortingToKey(selectedSorting);

        String normalizedKeyword = convertGreeklishToGreek(keyword != null ? keyword : "");

        queryProduct query = new queryProduct("", selectedCategory, selectedSubcategory);

        Pagination newPagination = createPagination(query, sortingKey, normalizedKeyword);

        Platform.runLater(() -> {
            BorderPane root = (BorderPane) primaryStage.getScene().getRoot();
            root.setCenter(newPagination);
        });
    }

    /**
     * Δημιουργεί το οπτικό στοιχείο εμφάνισης του καλαθιού αγορών.
     *
     * @return Ένα HBox που περιέχει πληροφορίες για το καλάθι αγορών όπως τον αριθμό των προϊόντων,
     *         το συνολικό κόστος, και τη δυνατότητα πρόσβασης σε λεπτομέρειες καλαθιού.
     */
    private HBox createCartDisplay() {
        HBox cartDisplay = new HBox(10);
        cartDisplay.setAlignment(Pos.CENTER_RIGHT);
        cartDisplay.setPadding(new Insets(10));

        Label cartIcon = new Label("🛒");
        cartIcon.setStyle("-fx-font-size: 24px;");

        cartLabel = new Label("0");
        cartLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: white;");

        costLabel = new Label("0.00€");
        costLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: white;");

        HBox cartBox = new HBox(10, cartIcon, cartLabel, new Label("|"), costLabel);
        cartBox.setAlignment(Pos.CENTER);
        cartBox.setPadding(new Insets(10));
        cartBox.setStyle("-fx-background-color: red; -fx-border-color: darkred; -fx-border-width: 2; -fx-border-radius: 5; -fx-background-radius: 5;");

        cartBox.setOnMouseEntered(event -> {
            isHoveringCartBox = true;
            showCartPreview(cartBox);
        });
        cartBox.setOnMouseExited(event -> {
            isHoveringCartBox = false;
            hideCartPreviewIfOutside();
        });

        cartBox.setOnMouseClicked(event -> {
            try {
                GUICompletionOrder guiCompletionOrder = new GUICompletionOrder(apiInstance);
                guiCompletionOrder.start(new Stage());
            } catch (Exception e) {
                e.printStackTrace();
                showError("Σφάλμα κατά τη μετάβαση στην ολοκλήρωση παραγγελίας.");
            }
        });

        cartDisplay.getChildren().add(cartBox);
        return cartDisplay;
    }
    /**
     * Ενημερώνει την προεπισκόπηση του καλαθιού και την εμφανίζει ως αναδυόμενο παράθυρο.
     * Αν το καλάθι είναι άδειο, εμφανίζει μήνυμα ενημέρωσης.
     * Διαφορετικά, δημιουργεί μια λίστα με τα προϊόντα που βρίσκονται στο καλάθι,
     * συμπεριλαμβανομένων εικόνων, τίτλων και λεπτομερειών για κάθε προϊόν.
     */
    private void refreshCartPreview() {
        if (cartPreviewPopup == null) {
            cartPreviewPopup = new Popup();
        }

        VBox previewContent = new VBox(10);
        previewContent.setPadding(new Insets(10));
        previewContent.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-radius: 5px; -fx-background-radius: 5px;");
        previewContent.setAlignment(Pos.CENTER_LEFT);

        ArrayList<OrderedProduct> cart = apiInstance.getCart();
        if (cart.isEmpty()) {
            previewContent.getChildren().add(new Label("Το καλάθι σας είναι άδειο."));
        } else {
            for (OrderedProduct product : cart) {
                HBox productItem = new HBox(10);
                productItem.setAlignment(Pos.CENTER_LEFT);

                ImageView productImage;
                try {
                    productImage = new ImageView(loadImage(product.getTitle() + ".jpg"));
                } catch (Exception e) {
                    productImage = new ImageView(new Image("/pictures/default.jpg")); // Default εικόνα
                }
                productImage.setFitWidth(50);
                productImage.setFitHeight(50);

                Label productTitle = new Label(product.getTitle());
                Label productDetails = new Label("Ποσότητα: " + product.getAmount() + ", Τιμή: " +
                        String.format("%.2f€", product.getPrice()));

                productItem.getChildren().addAll(productImage, productTitle, productDetails);
                previewContent.getChildren().add(productItem);
            }
        }
        ScrollPane scrollPane = new ScrollPane(previewContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(300);

        cartPreviewPopup.getContent().clear();
        cartPreviewPopup.getContent().add(scrollPane);
    }

    /**
     * Εμφανίζει προεπισκόπηση του καλαθιού όταν ο χρήστης περάσει το ποντίκι πάνω από το κουτί του καλαθιού.
     *
     * @param cartBox το HBox που αντιπροσωπεύει το κουτί του καλαθιού.
     */
    private void showCartPreview(HBox cartBox) {
        if (cartPreviewPopup == null) {
            cartPreviewPopup = new Popup();
        }

        refreshCartPreview();

        cartPreviewPopup.getContent().forEach(node -> {
            node.setOnMouseEntered(null);
            node.setOnMouseExited(null);
        });

        ScrollPane scrollPane = (ScrollPane) cartPreviewPopup.getContent().get(0);
        scrollPane.setOnMouseEntered(event -> isHoveringPopup = true);
        scrollPane.setOnMouseExited(event -> {
            isHoveringPopup = false;
            hideCartPreviewIfOutside();
        });

        double popupX = cartBox.localToScreen(cartBox.getLayoutBounds()).getMinX();
        double popupY = cartBox.localToScreen(cartBox.getLayoutBounds()).getMaxY();
        cartPreviewPopup.show(cartBox, popupX, popupY);

        isHoveringCartBox = true;

        cartBox.setOnMouseExited(event -> {
            isHoveringCartBox = false;
            hideCartPreviewIfOutside();
        });
    }

    /**
     * Κρύβει την προεπισκόπηση του καλαθιού όταν ο κέρσορας δεν βρίσκεται πάνω στο κουτί του καλαθιού ή το αναδυόμενο παράθυρο.
     *
     * Αυτή η μέθοδος διασφαλίζει ότι το αναδυόμενο παράθυρο της προεπισκόπησης του καλαθιού δεν εμφανίζεται όταν ο χρήστης
     * δεν αλληλεπιδρά με το κουτί του καλαθιού ή το ίδιο το αναδυόμενο παράθυρο.
     */
    private void hideCartPreviewIfOutside() {
        if (!isHoveringCartBox && !isHoveringPopup) {
            Platform.runLater(() -> {
                if (cartPreviewPopup != null && cartPreviewPopup.isShowing()) {
                    cartPreviewPopup.hide();
                }
            });
        }
    }

    /**
     * Ενημερώνει την εμφάνιση του καλαθιού αγορών με τα συνολικά είδη και το συνολικό κόστος.
     * Υπολογίζει τον αριθμό των ειδών και το συνολικό κόστος από τη λίστα προϊόντων του καλαθιού
     * και ενημερώνει τις σχετικές ετικέτες (labels) και το προεπισκοπικό παράθυρο του καλαθιού.
     */
    public void updateCartDisplay() {
        totalCartItems = apiInstance.getCart().stream().mapToInt(OrderedProduct::getAmount).sum();
        totalCartCost = apiInstance.getCart().stream()
                .mapToDouble(product -> product.getAmount() * product.getPrice()).sum();

        Platform.runLater(() -> {
            cartLabel.setText(String.valueOf(totalCartItems));
            costLabel.setText(String.format("%.2f€", totalCartCost));
            if (cartPreviewPopup != null && cartPreviewPopup.isShowing()) {
                refreshCartPreview();
            }
        });
    }

    /**
     * Δημιουργεί την κάρτα προϊόντος για εμφάνιση στη γραφική διεπαφή.
     *
     *  @param product το αντικείμενο MarketProduct που αντιπροσωπεύει το προϊόν.
     * @return ένα VBox που περιέχει την κάρτα του προϊόντος.
     */
    private VBox createProductCard(MarketProduct product) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(10));
        card.setAlignment(Pos.CENTER);
        card.setStyle("-fx-border-color: #ccc; -fx-border-width: 1px; -fx-border-radius: 5px; -fx-background-color: #f9f9f9;");

        ImageView productImage = new ImageView(loadImage(product.getTitle() + ".jpg"));
        productImage.setFitWidth(150);
        productImage.setFitHeight(100);

        Label nameLabel = new Label(product.getTitle());
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        Label descriptionLabel = new Label(product.getDescription());
        descriptionLabel.setWrapText(true);

        Label priceLabel = new Label(String.format("%.2f€", product.getPrice()));
        priceLabel.setStyle("-fx-text-fill: #ff0000; -fx-font-size: 16px; -fx-font-weight: bold;");

        HBox buttons = new HBox(5);
        buttons.setAlignment(Pos.CENTER);

        Button decrementButton = new Button("-");
        TextField quantityField = new TextField("0");
        quantityField.setPrefWidth(50);
        quantityField.setEditable(false);
        Button incrementButton = new Button("+");

        Label popupMessage = new Label();
        popupMessage.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 12px; -fx-text-fill: darkblue;");

        StackPane popupContainer = new StackPane(popupMessage);
        popupContainer.setAlignment(Pos.CENTER);
        popupContainer.setStyle(
                "-fx-background-color: lightblue; " +
                        "-fx-border-color: blue; " +
                        "-fx-border-width: 2px; " +
                        "-fx-border-radius: 10px; " +
                        "-fx-background-radius: 10px; " +
                        "-fx-padding: 5px;"
        );
        popupContainer.setPrefSize(150, 30);
        popupContainer.setVisible(false);

        incrementButton.setOnAction(e -> {
            apiInstance.addToCart(product, 1);
            int newQuantity = apiInstance.getCart().stream()
                    .filter(p -> p.getTitle().equals(product.getTitle())) // Σύγκριση βάσει τίτλου
                    .mapToInt(OrderedProduct::getAmount)
                    .sum();
            quantityField.setText(String.valueOf(newQuantity));
            updateCartDisplay();
            showPopupMessage(popupMessage, popupContainer, "Προστέθηκε στο καλάθι");
        });

        decrementButton.setOnAction(e -> {
            int currentQuantity = Integer.parseInt(quantityField.getText());
            if (currentQuantity > 0) {
                apiInstance.removeFromCart(product, 1);
                int newQuantity = apiInstance.getCart().stream()
                        .filter(p -> p.getTitle().equals(product.getTitle())) // Σύγκριση βάσει τίτλου
                        .mapToInt(OrderedProduct::getAmount)
                        .sum();
                quantityField.setText(String.valueOf(newQuantity));
                updateCartDisplay();
                showPopupMessage(popupMessage, popupContainer, "Αφαιρέθηκε από το καλάθι");
            }
        });

        buttons.getChildren().addAll(decrementButton, quantityField, incrementButton);

        card.getChildren().addAll(productImage, nameLabel, descriptionLabel, priceLabel, buttons, popupContainer);
        return card;
    }
    /**
     * Εμφανίζει ένα μήνυμα σε ένα popup για 1 δευτερόλεπτο και στη συνέχεια το αποκρύπτει.
     * @param popupMessage το Label που θα εμφανίσει το μήνυμα.
     * @param popupContainer το StackPane που περιέχει το Label και είναι το container του popup.
     * @param message το μήνυμα που θα εμφανιστεί στο popup.
     */
    private void showPopupMessage(Label popupMessage, StackPane popupContainer, String message) {
        Platform.runLater(() -> {
            popupMessage.setText(message);
            popupContainer.setVisible(true);
        });

        new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Platform.runLater(() -> popupContainer.setVisible(false));
        }).start();
    }

    /**
     * Φορτώνει μια εικόνα από τον φάκελο πόρων του έργου.
     * @param fileName το όνομα του αρχείου εικόνας που θέλουμε να φορτώσουμε.
     * @return την εικόνα που φορτώθηκε από το αρχείο.
     * @throws RuntimeException αν η εικόνα δεν βρεθεί στον φάκελο πόρων.
     */
    private Image loadImage(String fileName) {
        String path = "/pictures/" + fileName;
        URL resource = getClass().getResource(path);

        if (resource == null) {
            throw new RuntimeException("Η εικόνα δεν βρέθηκε: " + path);
        }

        return new Image(resource.toExternalForm());
    }

    /**
     * Ορίζει μια εικόνα ως φόντο για το δεδομένο Pane.
     * @param pane το Pane στο οποίο θα οριστεί το φόντο.
     * @param imagePath η διαδρομή της εικόνας που θα χρησιμοποιηθεί ως φόντο.
     * @throws RuntimeException αν η εικόνα δεν βρεθεί στη διαδρομή που παρέχεται.
     */
    private void setBackgroundImage(Pane pane, String imagePath) {
        URL resource = getClass().getResource(imagePath);
        if (resource == null) {
            throw new RuntimeException("Το φόντο δεν βρέθηκε: " + imagePath);
        }
        Image backgroundImage = new Image(resource.toExternalForm());

        BackgroundImage background = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(
                        BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true
                )
        );

        pane.setBackground(new Background(background));
    }

    /**
     * Εμφανίζει ένα παράθυρο ειδοποίησης σφάλματος με το δοθέν μήνυμα.
     * @param message το μήνυμα σφάλματος που θα εμφανιστεί στο παράθυρο ειδοποίησης.
     */
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Σφάλμα");
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Μετατρέπει την επιλεγμένη επιλογή ταξινόμησης σε κλειδί που αντιστοιχεί στη λειτουργία ταξινόμησης.
     * @param sortingOption η επιλογή ταξινόμησης που έχει επιλέξει ο χρήστης.
     * @return το κλειδί που αντιστοιχεί στην επιλογή ταξινόμησης ή ένα κενό string αν η επιλογή δεν αναγνωρίζεται.
     */
    private String convertSortingToKey(String sortingOption) {
        if (sortingOption == null) {
            return "";
        }
        return switch (sortingOption) {
            case "Τιμή Αυξανόμενη" -> "priceAscending";
            case "Τιμή Φθίνουσα" -> "priceDescending";
            case "Αλφαβητικά Αυξανόμενη" -> "titleAscending";
            case "Αλφαβητικά Φθίνουσα" -> "titleDescending";
            default -> "";
        };
    }
}
