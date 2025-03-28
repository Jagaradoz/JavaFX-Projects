package controllers;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;

public class PageController implements Initializable {
    public static final int SQUARE_SIZE = 15;
    public static final int BOARD_RESOLUTION = 420;
    public static final int SQUARE_PER_ROW = BOARD_RESOLUTION / SQUARE_SIZE;

    @FXML
    public Canvas canvas;
    @FXML
    public Label scoreLabel;

    private int appleX;
    private int appleY;
    private int score = 0;
    private int snakeLength;
    private final int delay = 65;

    private int[] snakePosX;
    private int[] snakePosY;

    private char direction;

    private boolean gameOver = false;
    private boolean movePerDelay = false;

    private Random rand;
    private GraphicsContext gc;

    private AnimationTimer gameTimer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gc = canvas.getGraphicsContext2D();

        canvas.setWidth(BOARD_RESOLUTION);
        canvas.setHeight(BOARD_RESOLUTION);

        canvas.setFocusTraversable(true);
        canvas.requestFocus();
        canvas.setOnKeyPressed(this::handleKeyPress);

        startGame();
    }

    public void startGame() {
        // Set default values.
        gameOver = false;
        movePerDelay = false;
        snakeLength = 6;
        score = 0;
        direction = 'd';

        rand = new Random();
        snakePosX = new int[SQUARE_PER_ROW * SQUARE_PER_ROW];
        snakePosY = new int[SQUARE_PER_ROW * SQUARE_PER_ROW];

        // Snake starts at the center of the board.
        int startX = SQUARE_PER_ROW / 4;
        int startY = SQUARE_PER_ROW / 2;
        for (int i = 0; i < snakeLength; i++) {
            snakePosX[i] = startX - i;
            snakePosY[i] = startY;
        }

        // Randomize apple position.
        appleX = rand.nextInt(SQUARE_PER_ROW);
        appleY = rand.nextInt(SQUARE_PER_ROW);

        scoreLabel.setText("Score: 0");

        if (gameTimer != null) {
            gameTimer.stop();
        }

        // Animation timer.
        gameTimer = new AnimationTimer() {
            long lastUpdate = 0;

            @Override
            public void handle(long now) {
                if ((now - lastUpdate) >= delay * 1_000_000L) {
                    drawBoard();
                    drawApple();

                    moveSnake();
                    drawSnake();

                    collide();
                    lastUpdate = now;
                }
            }
        };
        gameTimer.start();

        canvas.requestFocus();
    }

    public void drawBoard() {
        // Draw board background and grid line.
        gc.setFill(Color.rgb(148, 193, 30));
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        gc.setLineWidth(0.5);
        gc.setStroke(Color.LIGHTGRAY);
        for (int i = 0; i <= SQUARE_PER_ROW; i++) {
            gc.strokeLine(i * SQUARE_SIZE, 0, i * SQUARE_SIZE, BOARD_RESOLUTION);
            gc.strokeLine(0, i * SQUARE_SIZE, BOARD_RESOLUTION, i * SQUARE_SIZE);
        }
    }

    public void drawApple() {
        // Draw apple.
        gc.setFill(Color.RED);
        gc.fillRect(appleX * SQUARE_SIZE + 1.5, appleY * SQUARE_SIZE + 1.5,
                SQUARE_SIZE - 3, SQUARE_SIZE - 3);
    }

    public void drawSnake() {
        // Draw snake body.
        for (int i = 0; i < snakeLength; i++) {
            gc.setFill(i == 0 ? Color.DARKGREEN : Color.GREEN);
            gc.fillRect(snakePosX[i] * SQUARE_SIZE, snakePosY[i] * SQUARE_SIZE,
                    SQUARE_SIZE - 1, SQUARE_SIZE - 1);
        }
    }

    public void moveSnake() {
        // Move snake each step.
        for (int i = snakeLength - 1; i > 0; i--) {
            snakePosX[i] = snakePosX[i - 1];
            snakePosY[i] = snakePosY[i - 1];
        }

        // Change direction.
        switch (direction) {
            case 'w':
                snakePosY[0]--;
                break;
            case 's':
                snakePosY[0]++;
                break;
            case 'a':
                snakePosX[0]--;
                break;
            case 'd':
                snakePosX[0]++;
                break;
        }

        movePerDelay = false;
    }

    public void collide() {
        // Check collision with an apple.
        if (snakePosX[0] == appleX && snakePosY[0] == appleY) {
            score++;
            snakeLength++;
            scoreLabel.setText("Score: " + score);

            appleX = rand.nextInt(SQUARE_PER_ROW);
            appleY = rand.nextInt(SQUARE_PER_ROW);
        }

        // Check collision with board bounds.
        if (snakePosX[0] >= SQUARE_PER_ROW || snakePosY[0] >= SQUARE_PER_ROW ||
                snakePosX[0] < 0 || snakePosY[0] < 0) {
            gameOver();
            return;
        }

        // Check collision with itself.
        for (int i = 1; i < snakeLength; i++) {
            if (snakePosX[0] == snakePosX[i] && snakePosY[0] == snakePosY[i]) {
                gameOver();
                return;
            }
        }
    }

    private void gameOver() {
        // Stop timer.
        // Set game over to true.
        gameTimer.stop();
        gameOver = true;

        gc.setFill(Color.WHITE);
        gc.setFont(javafx.scene.text.Font.font("Consolas", 48));

        String gameOverText = "Game Over";
        String restartText = "Press space bar to restart";

        // Adjust game over text.
        Text gameOverTextNode = new Text(gameOverText);
        gameOverTextNode.setFont(gc.getFont());
        double gameOverWidth = gameOverTextNode.getLayoutBounds().getWidth();
        double gameOverHeight = gameOverTextNode.getLayoutBounds().getHeight();

        double gameOverX = (BOARD_RESOLUTION - gameOverWidth) / 2;
        double gameOverY = (BOARD_RESOLUTION + gameOverHeight) / 2;

        gc.fillText(gameOverText, gameOverX, gameOverY);

        gc.setFont(javafx.scene.text.Font.font("Consolas", 24));

        // Adjust restart text.
        Text restartTextNode = new Text(restartText);
        restartTextNode.setFont(gc.getFont());
        double restartWidth = restartTextNode.getLayoutBounds().getWidth();

        double restartX = (BOARD_RESOLUTION - restartWidth) / 2;
        double restartY = gameOverY + gameOverHeight;

        gc.fillText(restartText, restartX, restartY);
    }

    private void handleKeyPress(KeyEvent event) {
        // Handle restart on space bar.
        if (gameOver) {
            if (event.getCode() == KeyCode.SPACE) {
                startGame();
            }
            return;
        }

        // Handle directional keys.
        if (!movePerDelay) {
            switch (event.getCode()) {
                case W:
                case UP:
                    if (direction != 's') {
                        direction = 'w';
                        movePerDelay = true;
                    }
                    break;
                case S:
                case DOWN:
                    if (direction != 'w') {
                        direction = 's';
                        movePerDelay = true;
                    }
                    break;
                case A:
                case LEFT:
                    if (direction != 'd') {
                        direction = 'a';
                        movePerDelay = true;
                    }
                    break;
                case D:
                case RIGHT:
                    if (direction != 'a') {
                        direction = 'd';
                        movePerDelay = true;
                    }
                    break;
            }
        }
    }
}
