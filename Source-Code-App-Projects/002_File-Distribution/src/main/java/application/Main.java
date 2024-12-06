package application;

import java.util.Objects;
import java.io.IOException;

import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Screen;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.geometry.Rectangle2D;
import javafx.application.Application;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // GET THE PAGE FXML AND ADDS IT TO THE SCENE
        Pane root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/Page.fxml")));
        Scene scene = new Scene(root);

        // ADD CSS STYLE TO SCENE
        String css = Objects.requireNonNull(getClass().getResource("/css/styles.css")).toExternalForm();
        scene.getStylesheets().add(css);

        // ADD ICON TO THE STAGE
        Image icon = new Image(Objects.requireNonNull(getClass().getResource("/images/icon.png")).toExternalForm());

        stage.getIcons().add(icon);
        stage.setResizable(false);
        stage.setTitle("File Distribution");
        stage.setScene(scene);
        stage.show();

        // CENTER THE WINDOW ON THE SCREEN
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
    }

    public static void main(String[] args) {
        launch();
    }
}