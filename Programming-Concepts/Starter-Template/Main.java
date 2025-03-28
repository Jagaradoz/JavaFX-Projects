import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.AnchorPane;
import javafx.application.Application;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        AnchorPane root = new AnchorPane();

        Scene scene = new Scene(root, 500, 500);

        stage.setTitle("Application");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}


// Configuration (VM Arguments)
// --module-path "\path\to\javafx-sdk-15.0.1\lib" --add-modules javafx.controls,javafx.fxml