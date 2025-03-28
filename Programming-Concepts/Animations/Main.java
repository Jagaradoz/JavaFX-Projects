import javafx.animation.*;
import javafx.application.Application;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        // Create a rectangle.
        Rectangle rectangle = new Rectangle(50, 50, Color.BLUE);

        // Transitions.
        // FillTransition           -> Animates the fill color of a shape over time.
        // FadeTransition           -> Animates the opacity of a node, creating fade-in or fade-out effects.
        // ScaleTransition          -> Animates the size of a node by scaling it along the X, Y, and Z axes.
        // StrokeTransition         -> Animates the stroke (outline) color of a shape over time.
        // RotateTransition         -> Animates the rotation of a node around its pivot point.
        // TranslateTransition      -> Animates the movement of a node along the X, Y, and Z axes.
        FillTransition fill = new FillTransition();
        FadeTransition fade = new FadeTransition();
        ScaleTransition scale = new ScaleTransition();
        StrokeTransition stroke = new StrokeTransition();
        RotateTransition rotate = new RotateTransition();
        TranslateTransition translate = new TranslateTransition();

        // Transition methods.
        // .setNode(rectangle);                                 -> Specifies the node to animate.
        // .setByX(100);                                        -> Moves the node by 100 pixels along the X axis.
        // .setByY(100);                                        -> Moves the node by 100 pixels along the Y axis.
        // .setByAngle(360);                                    -> Rotates the node by 360 degrees.
        // .setDuration(Duration.seconds(1));                   -> Sets the duration of the animation to 1 second.
        // .setCycleCount(TranslateTransition.INDEFINITE);      -> Repeats the animation indefinitely.
        // .setAutoReverse(true);                               -> Reverses the animation direction after completing.
        // .setRate(1);                                         -> Sets the playback speed of the animation to normal.
        // .setDelay(Duration.seconds(1));                      -> Delays the animation start by 1 second.
        // .setInterpolator(Interpolator.EASE_BOTH);            -> Smoothly eases in and out during the animation.

        // Additional methods.
        // .setFromX(0);                                        -> Sets the starting X position for the animation.
        // .setFromY(0);                                        -> Sets the starting Y position for the animation.
        // .setToX(200);                                        -> Sets the ending X position for the animation.
        // .setToY(200);                                        -> Sets the ending Y position for the animation.
        // .stop();                                             -> Stops the animation immediately.
        // .pause();                                            -> Pauses the animation.
        // .play();                                             -> Starts or resumes the animation.
        translate.setNode(rectangle);
        translate.setByX(100);
        translate.setByY(100);

        rotate.setNode(rectangle);
        rotate.setByAngle(360);

        scale.setNode(rectangle);
        scale.setByX(100);
        scale.setByX(100);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
