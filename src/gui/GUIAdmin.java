package gui;

import api.api;
import api.product.MarketProduct;
import api.product.PackagedProduct;
import api.product.WeightedProduct;
import api.product.queryProduct;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GUIAdmin extends Application {

    private api apiInstance;
    private Stage primaryStage;
    private String keyword = "";

    /**
     * Δημιουργεί ένα νέο αντικείμενο GUIAdmin.
     *
     * @param apiInstance το αντικείμενο API για την επικοινωνία με τα δεδομένα.
     */
    public GUIAdmin(api apiInstance) {
        this.apiInstance = apiInstance;
    }

    /**
     * Εκκινεί την εφαρμογή GUI για τη διαχείριση προϊόντων.
     *
     * @param primaryStage το κύριο στάδιο της εφαρμογής JavaFX.
     */
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        BorderPane rootLayout = new BorderPane();
        rootLayout.setPadding(new Insets(20));

        BorderPane topBar = new BorderPane();
        topBar.setCenter(createSearchBar());
        rootLayout.setTop(topBar);

        VBox filterPanel = createFilterPanel();
        rootLayout.setLeft(filterPanel);

        Pagination productPagination = createPagination(new queryProduct(null, null, null), "", keyword);
        rootLayout.setCenter(productPagination);

        Scene scene = new Scene(rootLayout, 1200, 800);
        primaryStage.setTitle("Admin - Διαχείριση Προϊόντων");
        primaryStage.setScene(scene);

        rootLayout.requestFocus();

        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    /**
     * Δημιουργεί τη μπάρα αναζήτησης.
     * @return HBox που περιέχει το TextField αναζήτησης.
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
     * Δημιουργεί το πάνελ φίλτρων που περιλαμβάνει επιλογές για κατηγοριοποίηση, ταξινόμηση
     * και προβολή προϊόντων καθώς και λειτουργικότητα αποσύνδεσης.
     *
     * @return ένα VBox που περιέχει τα φίλτρα και τις σχετικές επιλογές.
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

        Label titleLabel = new Label("Φίλτρα Αναζήτησης");
        titleLabel.setFont(new Font("Arial", 18));
        titleLabel.setStyle("-fx-font-weight: bold;");

        Button bestSellingButton = new Button("Best Selling");
        bestSellingButton.setStyle("-fx-font-size: 14px; -fx-padding: 5px 10px;");
        bestSellingButton.setPrefWidth(200);

        bestSellingButton.setOnAction(e -> {
            ArrayList<MarketProduct> bestSellingProducts = apiInstance.getBestSelling(5); // Λαμβάνει τα 10 καλύτερα προϊόντα
            if (bestSellingProducts.isEmpty()) {
                showError("Δεν υπάρχουν διαθέσιμα προϊόντα με πωλήσεις.");
            } else {
                Pagination pagination = createPaginationFromResults(bestSellingProducts);
                BorderPane root = (BorderPane) primaryStage.getScene().getRoot();
                root.setCenter(pagination);
            }
        });

        double comboBoxWidth = 200;

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


        ComboBox<String> categoriesComboBox = new ComboBox<>();
        categoriesComboBox.getItems().add("Επιλέξτε Κατηγορία"); // Προσθήκη "κενής" επιλογής
        categoriesComboBox.getItems().addAll(categoriesMap.keySet());
        categoriesComboBox.setPromptText("Επιλέξτε Κατηγορία");
        categoriesComboBox.setPrefWidth(comboBoxWidth);


        ComboBox<String> subcategoriesComboBox = new ComboBox<>();
        subcategoriesComboBox.getItems().add("Επιλέξτε Υποκατηγορία");
        subcategoriesComboBox.setValue("Επιλέξτε Υποκατηγορία");
        subcategoriesComboBox.setPrefWidth(comboBoxWidth);
        subcategoriesComboBox.setDisable(true); // Αρχικά απενεργοποιημένο
        subcategoriesComboBox.setOnAction(e -> updatePaginationWithFilters(keyword));


        categoriesComboBox.setOnAction(e -> {
            String selectedCategory = categoriesComboBox.getValue();
            subcategoriesComboBox.getItems().clear();
            subcategoriesComboBox.getItems().add("Επιλέξτε Υποκατηγορία");
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

        ComboBox<String> sortingComboBox = new ComboBox<>();
        sortingComboBox.getItems().add("Επιλέξτε Ταξινόμηση");
        sortingComboBox.getItems().addAll("Τιμή Αυξανόμενη", "Τιμή Φθίνουσα", "Αλφαβητικά Αυξανόμενη", "Αλφαβητικά Φθίνουσα");
        sortingComboBox.setValue("Επιλέξτε Ταξινόμηση");
        sortingComboBox.setPrefWidth(comboBoxWidth);
        sortingComboBox.setOnAction(e -> updatePaginationWithFilters(keyword));


        Button outOfStockButton = new Button("Μη Διαθέσιμα Προϊόντα");
        outOfStockButton.setPrefWidth(comboBoxWidth);
        outOfStockButton.setStyle("-fx-font-size: 14px; -fx-padding: 5px 10px;");
        outOfStockButton.setOnAction(e -> {
            ArrayList<MarketProduct> outOfStockProducts = apiInstance.getOutOfStock();
            BorderPane root = (BorderPane) primaryStage.getScene().getRoot();

            if (outOfStockProducts.isEmpty()) {

                showError("Δεν υπάρχουν μη διαθέσιμα προϊόντα.");
            } else {

                Pagination pagination = createPaginationFromResults(outOfStockProducts);
                root.setCenter(pagination);
            }
        });

        Button logoutButton = new Button("Αποσύνδεση");
        logoutButton.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-font-weight: bold;");
        logoutButton.setOnAction(e -> {
            try {
                GUILogin loginScreen = new GUILogin();
                loginScreen.start(primaryStage);
            } catch (Exception ex) {
                ex.printStackTrace();
                showError("Σφάλμα κατά την αποσύνδεση.");
            }
        });

        VBox.setVgrow(logoutButton, Priority.ALWAYS);
        VBox.setMargin(logoutButton, new Insets(20, 0, 0, 0));

        filterPanel.getChildren().addAll(logoLabel, titleLabel, categoriesComboBox, subcategoriesComboBox, sortingComboBox,outOfStockButton, bestSellingButton, logoutButton);

        return filterPanel;
    }
    /**
     * Δημιουργεί μια Pagination (σελίδωση) για την εμφάνιση προϊόντων.
     *
     * @param products η λίστα των προϊόντων που θα εμφανιστούν.
     * @return ένα αντικείμενο Pagination που επιτρέπει τη σελίδωση των προϊόντων.
     */
    private Pagination createPaginationFromResults(ArrayList<MarketProduct> products) {
        int itemsPerPage = 12;
        int totalPages = (int) Math.ceil((double) products.size() / itemsPerPage);
        if (totalPages == 0) totalPages = 1;

        Pagination pagination = new Pagination(totalPages, 0);
        pagination.setPageFactory(pageIndex -> {
            GridPane productGrid = new GridPane();
            productGrid.setPadding(new Insets(20));
            productGrid.setHgap(20);
            productGrid.setVgap(20);

            int startIndex = pageIndex * itemsPerPage;

            for (int i = 0; i < itemsPerPage && startIndex + i < products.size(); i++) {
                VBox productCard = createProductCard(products.get(startIndex + i));
                int col = i % 4;
                int row = i / 4;
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
     * Μετατρέπει μια επιλογή ταξινόμησης από τη διεπαφή χρήστη (UI) σε ένα κλειδί
     * που χρησιμοποιείται για την ταξινόμηση των δεδομένων στην αναζήτηση.
     *
     * @param sortingOption η επιλογή ταξινόμησης από το UI (π.χ. "Τιμή Αυξανόμενη").
     * @return το αντίστοιχο κλειδί ταξινόμησης για χρήση στα δεδομένα (π.χ. "priceAscending").
     */
    private String convertSortingToKey(String sortingOption) {
        return switch (sortingOption) {
            case "Τιμή Αυξανόμενη" -> "priceAscending";
            case "Τιμή Φθίνουσα" -> "priceDescending";
            case "Αλφαβητικά Αυξανόμενη" -> "titleAscending";
            case "Αλφαβητικά Φθίνουσα" -> "titleDescending";
            default -> "";
        };
    }

    /**
     * Ενημερώνει την εμφάνιση των προϊόντων με βάση τα φίλτρα και την αναζήτηση.
     *
     * @param keyword η λέξη-κλειδί για την αναζήτηση.
     */
    private void updatePaginationWithFilters(String keyword) {
        VBox filterPanel = (VBox) ((BorderPane) primaryStage.getScene().getRoot()).getLeft();
        ComboBox<String> categoriesComboBox = (ComboBox<String>) filterPanel.getChildren().get(2);
        ComboBox<String> subcategoriesComboBox = (ComboBox<String>) filterPanel.getChildren().get(3);
        ComboBox<String> sortingComboBox = (ComboBox<String>) filterPanel.getChildren().get(4);

        String selectedCategory = categoriesComboBox.getValue();
        String selectedSubcategory = subcategoriesComboBox.getValue();
        String selectedSorting = sortingComboBox.getValue();


        selectedCategory = "Επιλέξτε Κατηγορία".equals(selectedCategory) ? null : selectedCategory;
        selectedSubcategory = "Επιλέξτε Υποκατηγορία".equals(selectedSubcategory) ? null : selectedSubcategory;


        String normalizedKeyword = convertGreeklishToGreek(keyword);


        queryProduct query = new queryProduct(normalizedKeyword, selectedCategory, selectedSubcategory);

        String sortingKey = convertSortingToKey(selectedSorting);


        Pagination pagination = createPagination(query, sortingKey, normalizedKeyword);
        BorderPane root = (BorderPane) primaryStage.getScene().getRoot();
        root.setCenter(pagination);
    }


    /**
     * Δημιουργεί το Pagination για εμφάνιση προϊόντων.
     * @param query το query για τα φίλτρα.
     * @param sortKey το κλειδί ταξινόμησης.
     * @param keyword η λέξη-κλειδί.
     * @return Pagination για προβολή προϊόντων.
     */
    private Pagination createPagination(queryProduct query, String sortKey, String keyword) {
        ArrayList<MarketProduct> products = apiInstance.searchProduct(query, sortKey, keyword);

        int itemsPerPage = 12;
        int totalPages = (int) Math.ceil((double) products.size() / itemsPerPage);
        if (totalPages == 0) totalPages = 1;

        Pagination pagination = new Pagination(totalPages, 0);
        pagination.setPageFactory(pageIndex -> {
            GridPane productGrid = new GridPane();
            productGrid.setPadding(new Insets(20));
            productGrid.setHgap(20);
            productGrid.setVgap(20);

            int startIndex = pageIndex * itemsPerPage;


            if (pageIndex == 0) {
                VBox addNewCard = createAddNewProductCard();
                productGrid.add(addNewCard, 0, 0);
            }


            for (int i = 0; i < itemsPerPage && startIndex + i < products.size(); i++) {
                VBox productCard = createProductCard(products.get(startIndex + i));
                int col = (i + (pageIndex == 0 ? 1 : 0)) % 4; // Αν είμαστε στην 1η σελίδα, κάνουμε offset κατά 1 για το κουμπί "Προσθήκη"
                int row = (i + (pageIndex == 0 ? 1 : 0)) / 4;
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
     * Δημιουργεί μια κάρτα διεπαφής χρήστη (UI) για την προσθήκη ενός νέου προϊόντος.
     *
     * Η κάρτα περιλαμβάνει έναν τίτλο και ένα κουμπί, που όταν πατηθεί, ανοίγει
     * έναν διάλογο για την εισαγωγή νέου προϊόντος.
     *
     * @return ένα VBox που αντιπροσωπεύει την κάρτα για την προσθήκη προϊόντος.
     */
    private VBox createAddNewProductCard() {
        VBox card = new VBox(10);
        card.setPadding(new Insets(10));
        card.setAlignment(Pos.CENTER);
        card.setStyle("-fx-border-color: #ccc; -fx-border-width: 1px; -fx-background-color: #e0e0e0;");

        Label addLabel = new Label("Προσθήκη Νέου Προϊόντος");
        addLabel.setStyle("-fx-font-weight: bold;");

        Button addButton = new Button("➕");
        addButton.setOnAction(e -> showAddProductDialog());

        card.getChildren().addAll(addLabel, addButton);
        return card;
    }

    /**
     * Δημιουργεί την κάρτα προϊόντος για εμφάνιση στο UI.
     *
     * @param product το προϊόν που θα εμφανιστεί.
     * @return ένα VBox που αντιπροσωπεύει την κάρτα προϊόντος.
     */
    private VBox createProductCard(MarketProduct product) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(10));
        card.setAlignment(Pos.CENTER);
        card.setStyle("-fx-border-color: #ccc; -fx-border-width: 1px; -fx-border-radius: 5px; -fx-background-color: #f9f9f9;");


        ImageView productImage;
        try {
            productImage = new ImageView(loadImage(product.getTitle() + ".jpg"));
        } catch (Exception e) {
            productImage = new ImageView(new Image("/pictures/default.jpg")); // Εικόνα προεπιλογής αν δεν βρεθεί
        }
        productImage.setFitWidth(150);
        productImage.setFitHeight(100);

        Label nameLabel = new Label(product.getTitle());
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        Label priceLabel = new Label(String.format("%.2f€", product.getPrice()));
        priceLabel.setStyle("-fx-text-fill: #ff0000; -fx-font-size: 16px; -fx-font-weight: bold;");


        int timesAppeared = apiInstance.timesProductAppeared(product.getTitle());
        Label appearedLabel = new Label("Εμφανίστηκε σε " + timesAppeared + " παραγγελίες.");
        appearedLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #555;");


        HBox buttons = new HBox(10);
        buttons.setAlignment(Pos.CENTER);

        Button editButton = new Button("Επεξεργασία");
        editButton.setOnAction(e -> showEditProductDialog(product));

        Button deleteButton = new Button("Διαγραφή");
        deleteButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
        deleteButton.setOnAction(e -> {
            apiInstance.removeProduct(product);
            try {
                apiInstance.saveAllFiles();
            } catch (IOException ex) {
                showError("Σφάλμα κατά την αποθήκευση των αλλαγών: " + ex.getMessage());
                ex.printStackTrace();
            }
            refreshPagination();
        });

        buttons.getChildren().addAll(editButton, deleteButton);

        card.getChildren().addAll(productImage, nameLabel, priceLabel, appearedLabel, buttons);

        return card;
    }

    /**
     * Φορτώνει μια εικόνα από το καθορισμένο αρχείο.
     *
     * @param fileName το όνομα του αρχείου της εικόνας.
     * @return ένα αντικείμενο Image.
     */
    private Image loadImage(String fileName) {
        String path = "/pictures/" + fileName;
        URL resource = getClass().getResource(path);
        if (resource == null) {
            return new Image(getClass().getResource("/pictures/default.jpg").toExternalForm());
        }
        return new Image(resource.toExternalForm());
    }
    /**
     * Εμφανίζει έναν διάλογο για την προσθήκη ενός νέου προϊόντος.
     *
     * Αυτή η μέθοδος δημιουργεί μια φόρμα όπου ο χρήστης μπορεί να εισαγάγει
     * τα χαρακτηριστικά του νέου προϊόντος, όπως τίτλος, περιγραφή, ποσότητα,
     * κατηγορία, υποκατηγορία, τιμή και μονάδα μέτρησης.
     */
    private void showAddProductDialog() {
        Stage dialog = new Stage();
        dialog.setTitle("Προσθήκη Νέου Προϊόντος");

        VBox dialogLayout = new VBox(15);
        dialogLayout.setPadding(new Insets(20));
        dialogLayout.setAlignment(Pos.CENTER);

        TextField titleField = new TextField();
        titleField.setPromptText("Τίτλος");

        TextArea descriptionField = new TextArea();
        descriptionField.setPromptText("Περιγραφή");

        TextField priceField = new TextField();
        priceField.setPromptText("Τιμή");

        TextField quantityField = new TextField();
        quantityField.setPromptText("Ποσότητα");


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

        ComboBox<String> categoryBox = new ComboBox<>();
        categoryBox.getItems().add("Επιλέξτε Κατηγορία");
        categoryBox.getItems().addAll(categoriesMap.keySet());
        categoryBox.setValue("Επιλέξτε Κατηγορία");

        ComboBox<String> subCategoryBox = new ComboBox<>();
        subCategoryBox.getItems().add("Επιλέξτε Υποκατηγορία");
        subCategoryBox.setValue("Επιλέξτε Υποκατηγορία");
        subCategoryBox.setDisable(true);


        categoryBox.setOnAction(e -> {
            String selectedCategory = categoryBox.getValue();
            subCategoryBox.getItems().clear();
            subCategoryBox.getItems().add("Επιλέξτε Υποκατηγορία");
            if (categoriesMap.containsKey(selectedCategory)) {
                subCategoryBox.getItems().addAll(categoriesMap.get(selectedCategory));
                subCategoryBox.setDisable(false);
            } else {
                subCategoryBox.setDisable(true);
            }
            subCategoryBox.setValue("Επιλέξτε Υποκατηγορία");
        });

        ComboBox<String> measurementBox = new ComboBox<>();
        measurementBox.getItems().addAll("τεμάχια", "kg");

        Button addButton = new Button("Προσθήκη");
        addButton.setOnAction(e -> {
            try {
                String title = titleField.getText();
                int amount = Integer.parseInt(quantityField.getText());
                String description = descriptionField.getText();
                String category = categoryBox.getValue();
                String subCategory = subCategoryBox.getValue();
                double price = Double.parseDouble(priceField.getText());
                String measurementType = measurementBox.getValue();

                if ("Επιλέξτε Κατηγορία".equals(category) || "Επιλέξτε Υποκατηγορία".equals(subCategory)) {
                    showError("Παρακαλώ επιλέξτε έγκυρη κατηγορία και υποκατηγορία.");
                    return;
                }

                MarketProduct newProduct;
                if ("τεμάχια".equals(measurementType)) {
                    newProduct = new PackagedProduct(title, amount, description, category, subCategory, price);
                } else if ("kg".equals(measurementType)) {
                    newProduct = new WeightedProduct(title, amount, description, category, subCategory, price);
                } else {
                    throw new IllegalArgumentException("Μη έγκυρος τύπος μέτρησης.");
                }

                apiInstance.addProduct(newProduct);
                apiInstance.saveAllFiles();
                dialog.close();
                refreshPagination();
            } catch (Exception ex) {
                showError("Σφάλμα κατά την προσθήκη του προϊόντος. Ελέγξτε τα δεδομένα που εισάγατε.");
                ex.printStackTrace();
            }
        });

        dialogLayout.getChildren().addAll(
                new Label("Τίτλος:"), titleField,
                new Label("Περιγραφή:"), descriptionField,
                new Label("Ποσότητα:"), quantityField,
                new Label("Κατηγορία:"), categoryBox,
                new Label("Υποκατηγορία:"), subCategoryBox,
                new Label("Τιμή:"), priceField,
                new Label("Μονάδα Μέτρησης:"), measurementBox,
                addButton
        );

        Scene dialogScene = new Scene(dialogLayout, 400, 600);
        dialog.setScene(dialogScene);

        dialog.requestFocus();

        dialog.show();
    }

    /**
     * Εμφανίζει έναν διάλογο επεξεργασίας για ένα προϊόν.
     *
     * Αυτή η μέθοδος επιτρέπει στον χρήστη να επεξεργαστεί τις πληροφορίες ενός υπάρχοντος προϊόντος
     * (όπως τίτλος, περιγραφή, τιμή, ποσότητα, κατηγορία, υποκατηγορία) και να αποθηκεύσει τις αλλαγές.
     *
     * @param product Το προϊόν που θα επεξεργαστεί.
     */
    private void showEditProductDialog(MarketProduct product) {
        Stage dialog = new Stage();
        dialog.setTitle("Επεξεργασία Προϊόντος");

        VBox dialogLayout = new VBox(15);
        dialogLayout.setPadding(new Insets(20));
        dialogLayout.setAlignment(Pos.CENTER);

        TextField titleField = new TextField(product.getTitle());
        TextArea descriptionField = new TextArea(product.getDescription());
        TextField priceField = new TextField(String.valueOf(product.getPrice()));
        TextField quantityField = new TextField(String.valueOf(product.getAmount()));
        Label measurementLabel = new Label(product instanceof PackagedProduct ? "τεμάχια" : "kg");

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


        ComboBox<String> categoryBox = new ComboBox<>();
        categoryBox.getItems().addAll(categoriesMap.keySet());
        categoryBox.setValue(product.getCategory());

        ComboBox<String> subCategoryBox = new ComboBox<>();
        if (categoriesMap.containsKey(product.getCategory())) {
            subCategoryBox.getItems().addAll(categoriesMap.get(product.getCategory()));
        }
        subCategoryBox.setValue(product.getSubCategory());

        categoryBox.setOnAction(e -> {
            String selectedCategory = categoryBox.getValue();
            subCategoryBox.getItems().clear();
            if (categoriesMap.containsKey(selectedCategory)) {
                subCategoryBox.getItems().addAll(categoriesMap.get(selectedCategory));
            }
            subCategoryBox.setValue(null);
        });

        Button saveButton = new Button("Αποθήκευση");
        saveButton.setOnAction(e -> {
            try {
                String title = titleField.getText();
                int amount = Integer.parseInt(quantityField.getText());
                String description = descriptionField.getText();
                String category = categoryBox.getValue();
                String subCategory = subCategoryBox.getValue();
                double price = Double.parseDouble(priceField.getText());

                if (category == null || subCategory == null) {
                    showError("Παρακαλώ επιλέξτε έγκυρη κατηγορία και υποκατηγορία.");
                    return;
                }

                MarketProduct updatedProduct;
                if (product instanceof PackagedProduct) {
                    updatedProduct = new PackagedProduct(title, amount, description, category, subCategory, price);
                } else if (product instanceof WeightedProduct) {
                    updatedProduct = new WeightedProduct(title, amount, description, category, subCategory, price);
                } else {
                    throw new IllegalArgumentException("Μη έγκυρος τύπος προϊόντος.");
                }

                apiInstance.removeProduct(product);
                apiInstance.addProduct(updatedProduct);
                apiInstance.saveAllFiles();
                dialog.close();
                refreshPagination();
            } catch (Exception ex) {
                showError("Σφάλμα κατά την αποθήκευση του προϊόντος. Ελέγξτε τα δεδομένα που εισάγατε.");
                ex.printStackTrace();
            }
        });

        dialogLayout.getChildren().addAll(
                new Label("Τίτλος:"), titleField,
                new Label("Περιγραφή:"), descriptionField,
                new Label("Ποσότητα:"), quantityField,
                new Label("Κατηγορία:"), categoryBox,
                new Label("Υποκατηγορία:"), subCategoryBox,
                new Label("Τιμή:"), priceField,
                new Label("Μονάδα Μέτρησης:"), measurementLabel,
                saveButton
        );

        Scene dialogScene = new Scene(dialogLayout, 400, 600);
        dialog.setScene(dialogScene);

        dialog.requestFocus();

        dialog.show();
    }

    /**
     * Ανανεώνει την προβολή της λίστας προϊόντων στο κεντρικό τμήμα της διεπαφής.
     *
     * Η μέθοδος αυτή δημιουργεί ένα νέο αντικείμενο `Pagination` με βάση τα τρέχοντα
     * φίλτρα και τη λέξη-κλειδί αναζήτησης, και το προσθέτει στο κεντρικό τμήμα
     * της διάταξης της διεπαφής.
     */
    private void refreshPagination() {
        Pagination updatedPagination = createPagination(new queryProduct(null, null, null), "", keyword);
        BorderPane root = (BorderPane) primaryStage.getScene().getRoot();
        root.setCenter(updatedPagination);
    }

    /**
     * Εμφανίζει μήνυμα σφάλματος.
     *
     * @param message το μήνυμα που θα εμφανιστεί.
     */
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Σφάλμα");
        alert.setContentText(message);
        alert.showAndWait();
    }
}