package application.component;

import application.Core;
import application.action.BeaconEffect;
import application.action.OverclockEffect;
import application.sprite.StaticSprite;
import javafx.scene.paint.Color;

public class Beacon extends Support {
    public Beacon(Core parent) {
        super(parent, new StaticSprite(null, "components/beacon.png", new double[] {0, 0}, 0.1), 10, 100, 0.2, 75, 5);
    }

    @Override
    protected boolean action() {
        new BeaconEffect(this, range, duration, Color.color(1, 0.5, 0), 0.15 * potency, 0.15 * potency);
        return true;
    }
}
