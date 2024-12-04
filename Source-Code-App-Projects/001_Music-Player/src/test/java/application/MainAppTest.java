package application;

import java.io.IOException;
import java.util.Objects;

import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.image.Image;
import javafx.geometry.Rectangle2D;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;

import org.testfx.framework.junit5.Start;
import org.testfx.framework.junit5.ApplicationExtension;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("MainApp Tests")
@ExtendWith(ApplicationExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MainAppTest {
    private Stage stage;
    private Scene scene;

    @Start
    private void start(Stage stage) throws Exception {
        this.stage = stage;
        this.scene = stage.getScene();

        MainApp app = new MainApp();

        app.start(stage);
        stage.show();
    }

    @Test
    @DisplayName("Should get a fxml page properly")
    public void shouldGetFxmlPageProperly() throws IOException {
        Pane root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/MusicPlayerPage.fxml")));
        assertNotNull(root);
    }

    @Test
    @DisplayName("Should get a scene properly")
    void shouldGetSceneProperly() {
        assertNotNull(scene);
        assertInstanceOf(Pane.class, scene.getRoot());
    }

    @Test
    @DisplayName("Should get an image icon")
    void shouldHaveImageIcon() {
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/icon.png")));
        assertNotNull(icon);
    }

    @Nested
    @DisplayName("Should initialize stage properly")
    class ShouldInitializeStageProperly {
        @Test
        @DisplayName("Should get title properly")
        public void shouldGetTitleProperly() {
            assertEquals("Music Player",stage.getTitle());
        }

        @Test
        @DisplayName("Should set the exact image icon properly")
        public void shouldSetTheExactImageIconProperly() {
            Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/icon.png")));

            Image actualIcon = stage.getIcons().getFirst();

            assertEquals(icon.getUrl(), actualIcon.getUrl(), "The icon URL should match the expected URL.");
        }

        @Test
        @DisplayName("Should not be resizable")
        public void shouldNotBeResizable() {
            assertFalse(stage.isResizable());
        }

        @Test
        @DisplayName("Should set scene properly")
        public void shouldSetSceneProperly() throws Exception {
            Pane expectedRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/MusicPlayerPage.fxml")));

            Scene scene = stage.getScene();

            assertNotNull(scene, "The SCENE should not be null.");
            assertInstanceOf(Pane.class, scene.getRoot(), "The root of the SCENE should be an instance of Pane.");
            assertEquals(expectedRoot.getClass(), scene.getRoot().getClass(), "The root of the SCENE should match the expected Pane.");
        }
    }

    @Test
    @DisplayName("Should be centered on screen")
    void shouldBeCenteredOnScreen() {
        Rectangle2D bounds = javafx.stage.Screen.getPrimary().getVisualBounds();

        double expectedX = (bounds.getWidth() - stage.getWidth()) / 2;
        double expectedY = (bounds.getHeight() - stage.getHeight()) / 2;

        assertEquals(expectedX, stage.getX(), 1.0);
        assertEquals(expectedY, stage.getY(), 1.0);
    }
}