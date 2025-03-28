import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        // Panes:
        // Accordion            -> A container for collapsible TitledPanes, where only one can be expanded at a time.
        // AnchorPane           -> Allows nodes to be anchored to its edges with specified offsets.
        // BorderPane           -> Divides the layout into five regions: top, bottom, left, right, and center.
        // ButtonBar            -> A container for arranging buttons, with built-in styling and alignment features.
        // DialogPane           -> A specialized pane for creating dialogs, typically used with Alert or Dialog classes.
        // FlowPane             -> Arranges nodes in a flow that wraps at the pane's edges.
        // GridPane             -> Lays out nodes in a flexible grid of rows and columns.
        // HBox                 -> Arranges nodes horizontally in a single row.
        // Pane                 -> A base layout class for positioning children without automatic resizing.
        // ScrollPane           -> Provides a scrollable view of its content when the content exceeds its bounds.
        // StackPane            -> Stacks nodes on top of each other, with optional alignment.
        // TilePane             -> Arranges nodes in a grid of uniformly sized tiles.
        // VBox                 -> Arranges nodes vertically in a single column.
        Accordion accordion = new Accordion();
        AnchorPane anchorPane = new AnchorPane();
        BorderPane borderPane = new BorderPane();
        ButtonBar buttonBar = new ButtonBar();
        DialogPane dialogPane = new DialogPane();
        FlowPane flowPane = new FlowPane();
        GridPane gridPane = new GridPane();
        HBox hBox = new HBox();
        Pane pane = new Pane();
        ScrollPane scrollPane = new ScrollPane();
        StackPane stackPane = new StackPane();
        TilePane tilePane = new TilePane();
        VBox vBox = new VBox();

        // Scene:
        // It gives content of stages that contain (pane).
        Scene scene = new Scene(pane, 500, 500);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
