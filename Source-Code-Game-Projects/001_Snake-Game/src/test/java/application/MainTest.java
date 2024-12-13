package application;

import java.net.URL;

import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.testfx.framework.junit5.Start;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Initialize MainTest")
@ExtendWith(ApplicationExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MainTest {
    private Stage stage;
    private Scene scene;

    @Start
    private void start(Stage stage) {
        Main app = new Main();
        app.start(stage);
        stage.show();

        this.stage = stage;
        this.scene = stage.getScene();
    }

    @Test
    @DisplayName("Root page should be loaded")
    public void rootPagePathShouldBeLoaded() {
        String expectedFxmlPath = "/fxml/Page.fxml";
        FXMLLoader loader = new FXMLLoader(getClass().getResource(expectedFxmlPath));
        assertNotNull(loader.getLocation(), "FXML file should be found at " + expectedFxmlPath);

        assertNotNull(scene.getRoot(), "Root page should be loaded");
        assertInstanceOf(Pane.class, scene.getRoot(), "Root should be a Pane");
    }

    @Test
    @DisplayName("CSS should be loaded")
    public void cssPathShouldBeLoaded() {
        String expectedCssPath = "/css/styles.css";
        URL cssUrl = getClass().getResource(expectedCssPath);
        assertNotNull(cssUrl, "CSS file should be found at " + expectedCssPath);

        assertFalse(scene.getStylesheets().isEmpty(), "CSS should be loaded");
        String loadedCssUrl = scene.getStylesheets().getFirst();
        assertTrue(loadedCssUrl.endsWith("styles.css"), "CSS file should be styles.css");
        assertTrue(loadedCssUrl.contains(expectedCssPath), "Loaded CSS should contain the correct path");
    }

    @Test
    @DisplayName("Icon path should be loaded")
    public void iconPathShouldBeLoaded() {
        String expectedIconPath = "/images/icon.png";
        URL iconUrl = getClass().getResource(expectedIconPath);
        assertNotNull(iconUrl, "Icon file should be found at " + expectedIconPath);

        assertFalse(stage.getIcons().isEmpty(), "Icon should be loaded");
        assertEquals(iconUrl.toExternalForm(), stage.getIcons().getFirst().getUrl(), "Loaded icon should match the expected path");
    }

    @Test
    @DisplayName("Stage should be resizable")
    public void stageShouldResizable() {
        assertTrue(stage.isResizable(), "Stage should be resizable");
    }

    @Nested
    @DisplayName("Test stage properties")
    class StageTest {
        @Test
        @DisplayName("Stage should have 'Snake Game' as the title")
        public void stageShouldHaveSnakeGameAsTitle() {
            assertEquals("Snake Game", stage.getTitle(), "Stage title should be 'Snake Game'");
        }

        @Test
        @DisplayName("Stage should have scene")
        public void stageShouldHaveScene() {
            assertNotNull(stage.getScene(), "Stage should have a scene");
        }
    }
}