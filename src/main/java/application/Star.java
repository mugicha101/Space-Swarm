package application;

import application.movement.Position;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;

import java.util.ArrayList;

public class Star {
    private static final int regionSize = 2000;
    private static final double starDensity = 3;
    public static final Group starGroup = new Group();
    private static final ArrayList<Star> stars = new ArrayList<>();
    public static void tickStars() {
        for (Star star : stars)
            star.tick();
    }

    public static void init() {
        int starCount = (int)(regionSize * starDensity);
        for (int i = 0; i < starCount; i++) {
            new Star();
        }
    }

    private final Circle circle;
    private final Position offset;
    private final double distMulti;
    private Star() {
        distMulti = 0.1 + 0.9 * Math.pow(Math.random(), 2);
        circle = new Circle(0, 0, distMulti * (Math.random() + 0.5));
        Color color;
        double r = Math.random();
        if (r < 0.5)
            color = Color.color(1, 0.8, 0.8);
        else if (r < 0.7)
            color = Color.color(1, 1, 0.8);
        else if (r < 0.9)
            color = Color.WHITE;
        else
            color = Color.color(0.5, 0.8, 1);
        circle.setFill(color);
        starGroup.getChildren().add(circle);
        offset = new Position(-regionSize + Math.random() * regionSize * 2, -regionSize + Math.random() * regionSize * 2);
        stars.add(this);
    }

    private void tick() {
        circle.setTranslateX(Game.camPos.x - regionSize * 0.5 + ((offset.x - Game.camPos.x * distMulti) % regionSize + regionSize) % regionSize);
        circle.setTranslateY(Game.camPos.y - regionSize * 0.5 + ((offset.y - Game.camPos.y * distMulti) % regionSize + regionSize) % regionSize);
    }
}
