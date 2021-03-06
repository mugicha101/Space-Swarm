package application.component;

import application.Core;
import application.action.OverclockEffect;
import application.sprite.StaticSprite;
import javafx.scene.paint.Color;

public class Overclocker extends Support {
    public Overclocker(Core parent) {
        super(parent, new StaticSprite(null, "components/overclocker.png", new double[] {0, 0}, 0.1), 10, 100, 0.2, 75, 5);
    }

    @Override
    protected boolean action() {
        new OverclockEffect(this, range, duration, Color.color(0, 0, 1), 0.15 * potency);
        return true;
    }
}
