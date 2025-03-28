import javafx.application.Application;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Need actual canvas to get graphics context.
        Canvas canvas = new Canvas(500, 500);

        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Additional Setup:
        // .setFill()           -> set fill color
        // .setStroke()         -> set stroke color
        // .setLineWidth()      -> set width of stroke
        // .setLineCap()        -> set line cap style of stroke (butt, round, square)
        // .setLineJoin()       -> set line join style of stroke(miter, round, bevel)
        // .setTransform()      -> set transformation matrix for graphics context
        // .translate()         -> move entire graphics
        // .rotate()            -> rotate entire graphics
        // .scale()             -> scale entire graphics
        gc.setFill(Color.RED);
        gc.setFill(Color.BLUE);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
        gc.setLineCap(javafx.scene.shape.StrokeLineCap.ROUND);
        gc.setLineJoin(javafx.scene.shape.StrokeLineJoin.ROUND);
        gc.translate(100, 100);
        gc.rotate(45);
        gc.scale(2, 2);


        // Draw Shapes:
        // .fillRect()           -> fill rectangle
        // .fillPolygon()        -> fill polygon
        // .fillOval()           -> fill oval
        // .fillText()           -> fill text
        // .fillArc()            -> fill arc
        // .strokeRect()         -> stroke rectangle
        // .strokePolygon()      -> stroke polygon
        // .strokeOval()         -> stroke oval
        // .strokeText()         -> stroke text
        // .strokeArc()          -> stroke arc
        // .strokeLine()         -> stroke line
        // .strokePolyline()     -> stroke poly line
        gc.fillRect(50, 50, 200, 100);
        gc.fillPolygon(new double[]{500, 550, 600}, new double[]{150, 50, 150}, 3);
        gc.fillOval(650, 50, 100, 100);
        gc.fillText("Shapes on Canvas", 300, 200);
        gc.fillArc(100, 300, 100, 100, 90, 180, ArcType.ROUND);
        gc.strokeLine(500, 150, 600, 150);
        gc.strokePolyline(new double[]{10, 20, 30, 40}, new double[]{10, 20, 10, 20}, 4);

        // Canvas Operations:
        // .save()              -> save current state of graphics context
        // .restore()           -> restore state of graphics context
        // .clearRect(...)      -> redraw canvas e.g. super.paintComponent(g)
        gc.save();
        gc.restore();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    public static void main(String[] args) {
        launch();
    }
}