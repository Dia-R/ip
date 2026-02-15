package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import logic.ChatBot;

import java.io.IOException;

/**
 * A GUI for Cat using FXML.
 */
public class Main extends Application {

    private ChatBot cat = new ChatBot("./data/cat.txt");

    /**
     * Initializes and displays the main application window.
     *
     * @param stage Primary stage provided by the JavaFX.
     */
    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            stage.setTitle("Cat ChatBot");
            fxmlLoader.<MainWindow>getController().setCat(cat);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}