package gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import logic.ChatBot;
import javafx.animation.PauseTransition;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Objects;

/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox dialogContainer;

    @FXML
    private TextField userInput;

    @FXML
    private Button sendButton;

    private ChatBot cat;

    private final Image userImage = new Image(
            Objects.requireNonNull(getClass().getResourceAsStream("/images/User.png"))
    );

    private final Image catImage = new Image(
            Objects.requireNonNull(getClass().getResourceAsStream("/images/Cat.png"))
    );

    /**
     * Initializes the main window after its FXML components are loaded.
     * Configures the scroll pane to fit its width to the dialog container
     * and automatically scroll to the bottom whenever new messages are added.
     */
    @FXML
    public void initialize() {
        scrollPane.setFitToWidth(true);

        dialogContainer.heightProperty().addListener((obs, oldVal, newVal) ->
                scrollPane.setVvalue(1.0));

        String welcome = "Hello, furrr-iend! Do you need a helping paw?\n\n" +
                "Here's what CatBot can do for you:\n" +
                "• todo <description>\n" +
                "• deadline <description> /by <yyyy-MM-dd HHmm>\n" +
                "• event <description> /from <yyyy-MM-dd HHmm> /to <yyyy-MM-dd HHmm>\n" +
                "• list\n" +
                "• mark <task number>\n" +
                "• unmark <task number>\n" +
                "• delete <task number>\n" +
                "• find <keyword> or find <yyyy-MM-dd>\n" +
                "• cheer\n" +
                "• bye";

        dialogContainer.getChildren().add(
                DialogBox.getCatDialog(welcome, catImage)
        );
    }

    /** Injects the Cat instance */
    public void setCat(ChatBot c) {
        cat = c;
    }

    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        if (input == null || input.isBlank()) {
            return;
        }

        String response = cat.getResponse(input);

        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getCatDialog(response, catImage)
        );

        userInput.clear();

        if (input.trim().equalsIgnoreCase("bye")) {
            userInput.setDisable(true);
            sendButton.setDisable(true);
            PauseTransition pause = new PauseTransition(Duration.seconds(1));
            pause.setOnFinished(event -> {
                Stage stage = (Stage) dialogContainer.getScene().getWindow();
                stage.close();
            });
            pause.play();
        }
    }
}
