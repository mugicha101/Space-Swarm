package application.component;

import application.Core;
import application.action.*;
import application.sprite.StaticSprite;
import javafx.scene.paint.Color;

public class Radar extends Support {
    public Radar(Core parent) {
        super(parent, new StaticSprite(null, "components/radar.png", new double[] {0, 0}, 0.1), 10, 100, 1, 50, 0.5);
    }

    @Override
    protected boolean action() {
        new RadarEffect(this, range, duration, Color.color(1, 0, 1), 1.2 * potency);
        return true;
    }
}
