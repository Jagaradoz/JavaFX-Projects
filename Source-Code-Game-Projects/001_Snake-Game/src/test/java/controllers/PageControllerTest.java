package controllers;

import java.io.IOException;
import java.lang.reflect.Field;

import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.canvas.Canvas;
import javafx.application.Platform;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;

import org.testfx.api.FxRobot;
import org.testfx.util.WaitForAsyncUtils;
import org.testfx.framework.junit5.Start;
import org.testfx.framework.junit5.ApplicationExtension;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Initialize MainTest")
@ExtendWith(ApplicationExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PageControllerTest {
    private PageController controller;

    @Start
    public void start(Stage stage) {
        try {
            // GET LOADER AND CONTROLLER
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Page.fxml"));
            Scene scene = new Scene(loader.load());
            controller = loader.getController();

            // SET SCENE AND SHOW
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    public void setUp() {
        // CHECK THAT THE CONTROLLER IS NOT NULL
        assertNotNull(controller, "Controller should not be null");
    }

    @Test
    @DisplayName("All elements are loaded")
    public void allElementsAreLoaded(FxRobot robot) {
        // CHECK CANVAS ELEMENT
        // CHECK SCORE LABEL ELEMENT
        Canvas canvas = robot.lookup("#canvas").queryAs(Canvas.class);
        Label scoreLabel = robot.lookup("#scoreLabel").queryAs(Label.class);

        assertNotNull(canvas, "Canvas should be loaded");
        assertNotNull(scoreLabel, "Score label should be loaded");
    }

    @Test
    @DisplayName("Canvas size is correct (w:420, h:420)")
    public void canvasSizeIsCorrect(FxRobot robot) {
        Canvas canvas = robot.lookup("#canvas").queryAs(Canvas.class);

        // CHECK THAT WIDTH AND HEIGHT ARE 420
        assertEquals(420, canvas.getWidth(), "Canvas width should be 420");
        assertEquals(420, canvas.getHeight(), "Canvas height should be 420");
    }

    @Test
    @DisplayName("Start game works properly")
    public void startGameWorksProperly(FxRobot robot) {
        try {
            // START GAME
            Platform.runLater(() -> controller.startGame());
            WaitForAsyncUtils.waitForFxEvents();

            // ACCESS NEEDED FIELDS FOR TESTING
            Field gameOverField = PageController.class.getDeclaredField("gameOver");
            Field movePerDelayField = PageController.class.getDeclaredField("movePerDelay");
            Field snakeLengthField = PageController.class.getDeclaredField("snakeLength");
            Field scoreField = PageController.class.getDeclaredField("score");
            Field directionField = PageController.class.getDeclaredField("direction");
            Field randField = PageController.class.getDeclaredField("rand");
            Field snakePosXField = PageController.class.getDeclaredField("snakePosX");
            Field snakePosYField = PageController.class.getDeclaredField("snakePosY");
            Field appleXField = PageController.class.getDeclaredField("appleX");
            Field appleYField = PageController.class.getDeclaredField("appleY");

            // SET ACCESSIBLE
            gameOverField.setAccessible(true);
            movePerDelayField.setAccessible(true);
            snakeLengthField.setAccessible(true);
            scoreField.setAccessible(true);
            directionField.setAccessible(true);
            randField.setAccessible(true);
            snakePosXField.setAccessible(true);
            snakePosYField.setAccessible(true);
            appleXField.setAccessible(true);
            appleYField.setAccessible(true);

            // ASSERT GENERAL VALUES
            assertFalse((boolean) gameOverField.get(controller), "Game over should be false");
            assertFalse((boolean) movePerDelayField.get(controller), "Move per delay should be false");
            assertEquals(6, (int) snakeLengthField.get(controller), "Snake length should be 6");
            assertEquals(0, (int) scoreField.get(controller), "Score should be 0");
            assertEquals('d', (char) directionField.get(controller), "Direction should be 'd'");
            assertNotNull(randField.get(controller), "Random object should not be null");

            // ASSERT SNAKE POSITIONS
            int[] snakePosX = (int[]) snakePosXField.get(controller);
            int[] snakePosY = (int[]) snakePosYField.get(controller);
            assertEquals(PageController.SQUARE_PER_ROW * PageController.SQUARE_PER_ROW, snakePosX.length, "SnakePosX array length should be 28 * 28");
            assertEquals(PageController.SQUARE_PER_ROW * PageController.SQUARE_PER_ROW, snakePosY.length, "SnakePosY array length should be 28 * 28");

            // ASSERT APPLE POSITIONS
            int appleX = (int) appleXField.get(controller);
            int appleY = (int) appleYField.get(controller);
            assertTrue(appleX >= 0 && appleX < 28, "Apple X should be within bounds");
            assertTrue(appleY >= 0 && appleY < 28, "Apple Y should be within bounds");

            // ASSERT SCORE LABEL TEXT
            Label scoreLabel = robot.lookup("#scoreLabel").queryAs(Label.class);
            assertEquals("Score: 0", scoreLabel.getText(), "Score label should display 'Score: 0'");
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Collision should work properly")
    public void collisionShouldWorkProperly(FxRobot robot) {
        try {
            // START GAME
            Platform.runLater(() -> controller.startGame());
            WaitForAsyncUtils.waitForFxEvents();

            // ACCESS NEEDED FIELDS FOR TESTING
            Field snakePosXField = PageController.class.getDeclaredField("snakePosX");
            Field snakePosYField = PageController.class.getDeclaredField("snakePosY");
            Field appleXField = PageController.class.getDeclaredField("appleX");
            Field appleYField = PageController.class.getDeclaredField("appleY");
            Field snakeLengthField = PageController.class.getDeclaredField("snakeLength");
            Field scoreField = PageController.class.getDeclaredField("score");
            Field gameOverField = PageController.class.getDeclaredField("gameOver");

            // SET ACCESSIBLE
            snakePosXField.setAccessible(true);
            snakePosYField.setAccessible(true);
            appleXField.setAccessible(true);
            appleYField.setAccessible(true);
            snakeLengthField.setAccessible(true);
            scoreField.setAccessible(true);
            gameOverField.setAccessible(true);

            // TEST COLLISION WITH APPLE
            int[] snakePosX = (int[]) snakePosXField.get(controller);
            int[] snakePosY = (int[]) snakePosYField.get(controller);

            snakePosX[0] = 5;
            snakePosY[0] = 5;
            appleXField.set(controller, 5);
            appleYField.set(controller, 5);
            Platform.runLater(() -> controller.collide());
            WaitForAsyncUtils.waitForFxEvents();

            assertEquals(1, (int) scoreField.get(controller), "Score should increase after eating apple");
            assertEquals(7, (int) snakeLengthField.get(controller), "Snake length should increase after eating apple");

            // TEST COLLISION WITH BOUNDS
            snakePosX[0] = PageController.SQUARE_PER_ROW;
            snakePosY[0] = 5;
            Platform.runLater(() -> controller.collide());
            WaitForAsyncUtils.waitForFxEvents();

            assertTrue((boolean) gameOverField.get(controller), "Game should be over after hitting bounds");

            // RESET GAME AFTER HIT THE BOUNDS
            Platform.runLater(() -> controller.startGame());
            WaitForAsyncUtils.waitForFxEvents();

            // TEST COLLISION WITH ITSELF
            snakePosX = (int[]) snakePosXField.get(controller);
            snakePosY = (int[]) snakePosYField.get(controller);
            snakePosX[0] = 5;
            snakePosY[0] = 5;
            snakePosX[1] = 5;
            snakePosY[1] = 5;
            Platform.runLater(() -> controller.collide());
            WaitForAsyncUtils.waitForFxEvents();

            assertTrue((boolean) gameOverField.get(controller), "Game should be over after snake collides with itself");
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Error accessing fields: " + e.getMessage());
        }
    }
}