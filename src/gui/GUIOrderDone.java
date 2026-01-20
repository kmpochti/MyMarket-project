package gui;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.net.URL;

public class GUIOrderDone extends Application {

    /**
     * Εισαγωγική μέθοδος για την εμφάνιση μιας ευχαριστήριας σελίδας στο GUI.
     * Αυτή η μέθοδος εκκινεί τη σκηνή JavaFX που περιλαμβάνει φόντο, λογότυπο και μήνυμα ευχαριστίας.
     *
     * @param primaryStage Το κύριο στάδιο της εφαρμογής.
     */
    @Override
    public void start(Stage primaryStage) {
        StackPane root = new StackPane();
        root.setAlignment(Pos.CENTER);

        URL resource = getClass().getResource("/pictures/annie-spratt-X4l3CjcDvic-unsplash.jpg");
        if (resource == null) {
            throw new RuntimeException("Το φόντο δεν βρέθηκε.");
        }
        Image backgroundImage = new Image(resource.toExternalForm());
        BackgroundImage background = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
        );
        root.setBackground(new Background(background));

        Label logoLabel = new Label("My Market");
        logoLabel.setStyle("-fx-font-family: 'Comic Sans MS', 'Arial', sans-serif; " +
                "-fx-font-weight: bold; " +
                "-fx-font-size: 40px; " +
                "-fx-text-fill: red; " +
                "-fx-effect: dropshadow(gaussian, black, 5, 0.8, 2, 2);");

        Label message = new Label(
                "Σας ευχαριστούμε για την παραγγελία σας στο ηλεκτρονικό μας κατάστημα.\n" +
                        "Τα προϊόντα θα αποσταλούν στην διεύθυνση που έχετε καταχωρήσει.\n" +
                        "Καλή Χρονιά!!!");
        message.setStyle("-fx-font-size: 24px; -fx-text-fill: white; -fx-font-family: 'Comic Sans MS'; -fx-text-alignment: center;");
        message.setWrapText(true);

        VBox content = new VBox(20, logoLabel, message);
        content.setAlignment(Pos.CENTER);

        root.getChildren().add(content);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Ευχαριστούμε!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}

