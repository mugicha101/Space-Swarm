package application;

import application.movement.Position;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;

public class NoisePattern {
    public static ImagePattern create(double width, double height, Noise[] redNoise, Noise[] greenNoise, Noise[] blueNoise, double redMulti, double greenMulti, double blueMulti, Position offset) {
        WritableImage wi = new WritableImage((int) width, (int) height);
        OpenSimplexNoise os = new OpenSimplexNoise();
        for (int y = 0; y < wi.getHeight(); y++) {
            for (int x = 0; x < wi.getWidth(); x++) {
                double v1 = 1;
                if (redNoise != null)
                    for (Noise noise : redNoise)
                        v1 += noise.getVal(x, y) - noise.multi;
                double v2 = 1;
                if (greenNoise != null)
                    for (Noise noise : greenNoise)
                        v2 += noise.getVal(x, y) - noise.multi;
                double v3 = 1;
                if (blueNoise != null)
                    for (Noise noise : blueNoise)
                        v3 += noise.getVal(x, y) - noise.multi;
                wi.getPixelWriter().setColor(x, y, Color.color(v1 * redMulti, v2 * greenMulti, v3 * blueMulti));
            }
        }
        return new ImagePattern(wi, offset.x, offset.y, wi.getWidth(), wi.getHeight(), false);
    }

    public static ImagePattern create(double width, double height, Noise[] redNoise, Noise[] greenNoise, Noise[] blueNoise, double redMulti, double greenMulti, double blueMulti) {
        return create(width, height, redNoise, greenNoise, blueNoise, redMulti, greenMulti, blueMulti, new Position());
    }
}
