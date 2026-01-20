package gui;

import api.api;
import api.user.User;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.FileNotFoundException;

public class GUILogin extends Application {
    private BorderPane rootLayout;
    private api apiInstance;

    /**
     * Εκκίνηση της εφαρμογής.
     * @param args πίνακας από ορίσματα που περνούν κατά την εκκίνηση της εφαρμογής.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Αρχικοποιεί και εκκινεί την κύρια σκηνή της εφαρμογής.
     * @param primaryStage το κύριο παράθυρο της εφαρμογής JavaFX.
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            apiInstance = new api();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            showError("Σφάλμα: Δεν βρέθηκαν τα απαιτούμενα αρχεία.");
            return;
        }

        rootLayout = new BorderPane();

        setBackgroundImage("/pictures/jeshoots-com-7VOyZ0-iO0o-unsplash.jpg");

        HBox header = createHeader(primaryStage);

        VBox loginForm = createLoginForm();

        StackPane centerPane = new StackPane(loginForm);
        centerPane.setAlignment(Pos.CENTER);

        rootLayout.setTop(header);
        rootLayout.setCenter(centerPane);

        Scene scene = new Scene(rootLayout, 800, 600);

        primaryStage.setTitle("MyMarket - Σύνδεση/Εγγραφή");
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.show();
    }

    /**
     * Ρυθμίζει την εικόνα φόντου για το βασικό layout της εφαρμογής.
     * @param imagePath το μονοπάτι της εικόνας που θα χρησιμοποιηθεί ως φόντο.
     */
    private void setBackgroundImage(String imagePath) {
        Image backgroundImage = new Image(getClass().getResource(imagePath).toExternalForm());

        BackgroundImage background = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(
                        1.0, 1.0, true, true, false, false // Πλήρης κάλυψη
                )
        );

        rootLayout.setBackground(new Background(background));
    }

    /**
     * Δημιουργεί την κεφαλίδα της εφαρμογής με κουμπιά για εναλλαγή μεταξύ φόρμας σύνδεσης και εγγραφής.
     * @param stage το κύριο παράθυρο της εφαρμογής.
     * @return ένα HBox που περιέχει την κεφαλίδα.
     */
    private HBox createHeader(Stage stage) {
        HBox header = new HBox(20);
        header.setStyle("-fx-padding: 10px; -fx-background-color: #f0f0f0;");
        header.setAlignment(Pos.CENTER);

        ToggleButton loginButton = new ToggleButton("Σύνδεση");
        ToggleButton registerButton = new ToggleButton("Εγγραφή");

        ToggleGroup toggleGroup = new ToggleGroup();
        loginButton.setToggleGroup(toggleGroup);
        registerButton.setToggleGroup(toggleGroup);
        loginButton.setSelected(true);

        loginButton.setOnAction(e -> rootLayout.setCenter(new StackPane(createLoginForm())));
        registerButton.setOnAction(e -> rootLayout.setCenter(new StackPane(createRegisterForm())));

        header.getChildren().addAll(loginButton, registerButton);
        return header;
    }

    /**
     * Δημιουργεί τη φόρμα σύνδεσης για την εφαρμογή.
     * Η φόρμα περιλαμβάνει πεδία για όνομα χρήστη και κωδικό πρόσβασης,
     * καθώς και κουμπί για υποβολή των διαπιστευτηρίων.
     *
     * @return ένα VBox που περιέχει τη φόρμα σύνδεσης.
     */
    private VBox createLoginForm() {
        VBox loginLayout = new VBox(15);
        loginLayout.setAlignment(Pos.CENTER);
        loginLayout.setPadding(new Insets(40));

        Label logoLabel = new Label("My Market");
        logoLabel.setStyle("-fx-font-family: 'Comic Sans MS', 'Arial', sans-serif; " +
                "-fx-font-weight: bold; " +
                "-fx-font-size: 40px; " +
                "-fx-text-fill: red; " +
                "-fx-effect: dropshadow(gaussian, black, 5, 0.8, 2, 2);");

        Label usernameLabel = new Label("Username:");
        usernameLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: red;");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Εισάγετε το όνομα χρήστη");
        usernameField.setPrefWidth(500);
        usernameField.setMaxWidth(500);
        usernameField.setMinWidth(500);

        Label passwordLabel = new Label("Password:");
        passwordLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: red;");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Εισάγετε τον κωδικό πρόσβασης");
        passwordField.setPrefWidth(500);
        passwordField.setMaxWidth(500);
        passwordField.setMinWidth(500);

        TextField visiblePasswordField = new TextField();
        visiblePasswordField.setPromptText("Εισάγετε τον κωδικό πρόσβασης");
        visiblePasswordField.setVisible(false);
        visiblePasswordField.setPrefWidth(500);
        visiblePasswordField.setMaxWidth(500);
        visiblePasswordField.setMinWidth(500);

        Button toggleVisibility = new Button("👁");
        toggleVisibility.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        toggleVisibility.setOnAction(event -> {
            if (passwordField.isVisible()) {
                visiblePasswordField.setText(passwordField.getText());
                passwordField.setVisible(false);
                visiblePasswordField.setVisible(true);
            } else {
                passwordField.setText(visiblePasswordField.getText());
                visiblePasswordField.setVisible(false);
                passwordField.setVisible(true);
            }
        });

        StackPane passwordStackPane = new StackPane();
        passwordStackPane.setPrefWidth(500);
        passwordStackPane.setMaxWidth(500);

        passwordStackPane.getChildren().addAll(passwordField, visiblePasswordField, toggleVisibility);
        StackPane.setAlignment(toggleVisibility, Pos.CENTER_RIGHT); // Το ματάκι δεξιά στο TextField
        StackPane.setMargin(toggleVisibility, new Insets(0, 10, 0, 0)); // Προσθήκη περιθωρίου δεξιά

        Button loginButton = new Button("Είσοδος");
        loginButton.setStyle("-fx-background-color: #FF4500; -fx-text-fill: white; -fx-font-size: 14px;");
        loginButton.setOnMouseEntered(e -> loginButton.setStyle("-fx-background-color: #CD3700; -fx-text-fill: white;"));
        loginButton.setOnMouseExited(e -> loginButton.setStyle("-fx-background-color: #FF4500; -fx-text-fill: white;"));


        Runnable loginAction = () -> {
            String username = usernameField.getText();
            String password = passwordField.isVisible() ? passwordField.getText() : visiblePasswordField.getText();

            if (username.isEmpty() || password.isEmpty()) {
                showError("Το username ή το password δεν πρέπει να είναι κενά.");
                return;
            }

            User user = apiInstance.login(username, password);
            if (user != null) {
                if (user.isAdmin()) {
                    GUIAdmin adminScreen = new GUIAdmin(apiInstance);
                    try {
                        adminScreen.start((Stage) loginButton.getScene().getWindow());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        showError("Σφάλμα κατά τη μετάβαση στη διαχείριση.");
                    }
                } else {
                    GUISearch userScreen = new GUISearch(apiInstance);
                    try {
                        userScreen.start((Stage) loginButton.getScene().getWindow());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        showError("Σφάλμα κατά τη μετάβαση στην αναζήτηση.");
                    }
                }
            } else {
                showError("Λανθασμένο username ή password.");
            }
        };

        loginButton.setOnAction(e -> loginAction.run());

        usernameField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                passwordField.requestFocus();
            }
        });

        passwordField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                loginAction.run();
            }
        });

        visiblePasswordField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                loginAction.run();
            }
        });

        loginLayout.getChildren().addAll(logoLabel, usernameLabel, usernameField, passwordLabel, passwordStackPane, loginButton);

        return loginLayout;
    }

    /**
     * Δημιουργεί τη φόρμα εγγραφής για την εφαρμογή.
     * Η φόρμα περιλαμβάνει πεδία για όνομα, επίθετο, όνομα χρήστη, κωδικό πρόσβασης,
     * επιβεβαίωση κωδικού και επιλογή αποδοχής όρων χρήσης.
     *
     * @return ένα VBox που περιέχει τη φόρμα εγγραφής.
     */
        private VBox createRegisterForm() {
        VBox registerLayout = new VBox(15);
        registerLayout.setAlignment(Pos.CENTER);
        registerLayout.setStyle("-fx-padding: 40px;");

        Label logoLabel = new Label("My Market");
        logoLabel.setStyle("-fx-font-family: 'Comic Sans MS', 'Arial', sans-serif; " +
                "-fx-font-weight: bold; " +
                "-fx-font-size: 40px; " +
                "-fx-text-fill: red; " +
                "-fx-effect: dropshadow(gaussian, black, 5, 0.8, 2, 2);");


        Label firstNameLabel = new Label("Όνομα:");
        firstNameLabel.setStyle("-fx-font-family: 'Arial', sans-serif; -fx-font-size: 16px; -fx-text-fill: red;");
        TextField firstNameField = new TextField();
        firstNameField.setPromptText("Εισάγετε το όνομα");
        firstNameField.setPrefWidth(500);
        firstNameField.setMaxWidth(500);
        firstNameField.setMinWidth(500);

        Label lastNameLabel = new Label("Επίθετο:");
        lastNameLabel.setStyle("-fx-font-family: 'Arial', sans-serif; -fx-font-size: 16px; -fx-text-fill: red;");
        TextField lastNameField = new TextField();
        lastNameField.setPromptText("Εισάγετε το επίθετο");
        lastNameField.setPrefWidth(500);
        lastNameField.setMaxWidth(500);
        lastNameField.setMinWidth(500);

        Label usernameLabel = new Label("Username:");
        usernameLabel.setStyle("-fx-font-family: 'Arial', sans-serif; " +
                "-fx-font-size: 16px; " +
                "-fx-text-fill: red;");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Εισάγετε το όνομα χρήστη");
        usernameField.setPrefWidth(500);
        usernameField.setMaxWidth(500);
        usernameField.setMinWidth(500);

        Label passwordLabel = new Label("Password:");
        passwordLabel.setStyle("-fx-font-family: 'Arial', sans-serif; " +
                "-fx-font-size: 16px; " +
                "-fx-text-fill: red;");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Εισάγετε τον κωδικό πρόσβασης");
        passwordField.setPrefWidth(500);
        passwordField.setMaxWidth(500);
        passwordField.setMinWidth(500);

        TextField visiblePasswordField = new TextField();
        visiblePasswordField.setPromptText("Εισάγετε τον κωδικό πρόσβασης");
        visiblePasswordField.setVisible(false);
        visiblePasswordField.setPrefWidth(500);
        visiblePasswordField.setMaxWidth(500);
        visiblePasswordField.setMinWidth(500);


        Button passwordToggle = new Button("👁");
        passwordToggle.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        passwordToggle.setOnAction(e -> {
            if (passwordField.isVisible()) {
                visiblePasswordField.setText(passwordField.getText());
                passwordField.setVisible(false);
                visiblePasswordField.setVisible(true);
            } else {
                passwordField.setText(visiblePasswordField.getText());
                visiblePasswordField.setVisible(false);
                passwordField.setVisible(true);
            }
        });

        StackPane passwordPane = new StackPane(passwordField, visiblePasswordField, passwordToggle);
        passwordPane.setPrefWidth(500);
        passwordPane.setMaxWidth(500);
        StackPane.setAlignment(passwordToggle, Pos.CENTER_RIGHT);
        StackPane.setMargin(passwordToggle, new Insets(0, 10, 0, 0));//StackPane passwordStackPane = new StackPane();


        Label confirmPasswordLabel = new Label("Επιβεβαίωση Password:");
        confirmPasswordLabel.setStyle("-fx-font-family: 'Arial', sans-serif; " +
                "-fx-font-size: 16px; " +
                "-fx-text-fill: red;");

        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Επιβεβαιώστε τον κωδικό πρόσβασης");
        confirmPasswordField.setPrefWidth(500);
        confirmPasswordField.setMaxWidth(500);
        confirmPasswordField.setMinWidth(500);
        TextField visibleConfirmPasswordField = new TextField();
        visibleConfirmPasswordField.setPromptText("Επιβεβαιώστε τον κωδικό πρόσβασης");
        visibleConfirmPasswordField.setVisible(false);
        visibleConfirmPasswordField.setPrefWidth(500);
        visibleConfirmPasswordField.setMaxWidth(500);
        visibleConfirmPasswordField.setMinWidth(500);

        Button confirmPasswordToggle = new Button("👁");
        confirmPasswordToggle.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        confirmPasswordToggle.setOnAction(e -> {
            if (confirmPasswordField.isVisible()) {
                visibleConfirmPasswordField.setText(confirmPasswordField.getText());
                confirmPasswordField.setVisible(false);
                visibleConfirmPasswordField.setVisible(true);
            } else {
                confirmPasswordField.setText(visibleConfirmPasswordField.getText());
                visibleConfirmPasswordField.setVisible(false);
                confirmPasswordField.setVisible(true);
            }
        });

        StackPane confirmPasswordPane = new StackPane(confirmPasswordField, visibleConfirmPasswordField, confirmPasswordToggle);
        confirmPasswordPane.setPrefWidth(500);
        confirmPasswordPane.setMaxWidth(500);
        StackPane.setAlignment(confirmPasswordToggle, Pos.CENTER_RIGHT);
        StackPane.setMargin(confirmPasswordToggle, new Insets(0, 10, 0, 0));


        CheckBox agreementCheckBox = new CheckBox("Αποδέχομαι να βαθμολογήσω με 10 την εργασία των φοιτητών και τους όρους χρήσης");
        agreementCheckBox.setStyle("-fx-font-family: 'Arial', sans-serif; " +
                "-fx-font-size: 12px; " +
                "-fx-text-fill: black;");

        Button registerButton = new Button("Εγγραφή");
        registerButton.setDisable(true);
        registerButton.setStyle("-fx-background-color: #FF4500; -fx-text-fill: white; -fx-font-size: 14px;");
        registerButton.setOnMouseEntered(e -> registerButton.setStyle("-fx-background-color: #CD3700; -fx-text-fill: white;"));
        registerButton.setOnMouseExited(e -> registerButton.setStyle("-fx-background-color: #FF4500; -fx-text-fill: white;"));

        // Απενεργοποίηση κουμπιού αν δεν επιλέχθηκε το CheckBox
        agreementCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            registerButton.setDisable(!newValue);
            if (newValue) {
                agreementCheckBox.setStyle("-fx-font-family: 'Arial', sans-serif; -fx-font-size: 12px; -fx-text-fill: black;");
            }
        });

        registerButton.setOnAction(e -> {
            if (!agreementCheckBox.isSelected()) {
                agreementCheckBox.setStyle("-fx-font-family: 'Arial', sans-serif; -fx-font-size: 12px; -fx-text-fill: red;");
                showError("Πρέπει να αποδεχθείτε τους όρους χρήσης.");
                return;
            }
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String username = usernameField.getText();
            String password = passwordField.getText();
            String confirmPassword = confirmPasswordField.getText();

            if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                showError("Όλα τα πεδία είναι υποχρεωτικά.");
                return;
            }

            if (!password.equals(confirmPassword)) {
                showError("Τα passwords δεν είναι όμοια. Προσπαθήστε ξανά.");
                return;
            }

            boolean success = apiInstance.addUser(username, password, false, firstName, lastName);
            if (success) {
                try {
                    apiInstance.saveAllFiles();
                    showSuccess("Η εγγραφή ολοκληρώθηκε με επιτυχία!");
                    rootLayout.setCenter(createLoginForm());
                } catch (IOException ex) {
                    showError("Σφάλμα κατά την αποθήκευση των δεδομένων: " + ex.getMessage());
                }
            } else {
                showError("Το username υπάρχει ήδη. Επιλέξτε άλλο.");
            }
        });

        usernameField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                passwordField.requestFocus();
            }
        });

        passwordField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                confirmPasswordField.requestFocus();
            }
        });

        confirmPasswordField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (agreementCheckBox.isSelected() && !registerButton.isDisabled()) {
                    registerButton.fire();
                } else {
                    showError("Πρέπει να αποδεχθείτε τους όρους χρήσης.");
                }
            }
        });

        agreementCheckBox.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                agreementCheckBox.setSelected(!agreementCheckBox.isSelected());
            }
        });

        registerLayout.getChildren().addAll(
                logoLabel,firstNameLabel, firstNameField,
                lastNameLabel, lastNameField, usernameLabel, usernameField,
                passwordLabel, passwordPane,
                confirmPasswordLabel, confirmPasswordPane,
                agreementCheckBox, registerButton
        );

        return registerLayout;
    }
    /**
     * Εμφανίζει ένα μήνυμα σφάλματος στον χρήστη.
     * Δημιουργεί ένα παράθυρο διαλόγου τύπου Alert με προεπιλεγμένο τύπο σφάλματος
     * και το εμφανίζει μπροστά στον χρήστη.
     *
     * @param message Το μήνυμα που θα εμφανιστεί στον χρήστη.
     */
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Σφάλμα");
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Εμφανίζει ένα μήνυμα επιτυχίας στον χρήστη.
     * Δημιουργεί ένα παράθυρο διαλόγου τύπου Alert με προεπιλεγμένο τύπο πληροφοριών
     * και το εμφανίζει μπροστά στον χρήστη.
     *
     * @param message Το μήνυμα που θα εμφανιστεί στον χρήστη.
     */
    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Επιτυχία");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
