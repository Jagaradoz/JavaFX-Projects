package application;

import java.net.URL;
import java.util.Objects;
import java.io.IOException;
import java.net.URISyntaxException;

import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.image.Image;
import javafx.application.Application;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        try {
            URL rootPagePath = getClass().getResource("/fxml/Page.fxml");
            URL cssPath = getClass().getResource("/css/styles.css");
            URL iconPath = getClass().getResource("/images/icon.png");

            if (rootPagePath == null) throw new IOException("Page not found.");
            if (cssPath == null) throw new IOException("Styles not found.");
            if (iconPath == null) throw new IOException("Icon not found.");

            Pane root = FXMLLoader.load(rootPagePath);
            Scene scene = new Scene(root);
            scene.getStylesheets().add(Objects.requireNonNull(cssPath).toExternalForm());

            Image icon = new Image(iconPath.toURI().toString());
            stage.getIcons().add(icon);

            stage.setTitle("Snake Game");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

    }

    public static void main(String[] args) {
        launch();
    }
}