package application.component;

import application.Core;
import application.action.DurationEffect;
import application.action.OverclockEffect;
import application.action.PatchEffect;
import application.sprite.StaticSprite;
import javafx.scene.paint.Color;

public class Energizer extends Support {
    public Energizer(Core parent) {
        super(parent, new StaticSprite(null, "components/energizer.png", new double[] {0, 0}, 0.1), 10, 100, 0.2, 75, 5);
    }

    @Override
    protected boolean action() {
        new DurationEffect(this, range, duration, Color.color(1, 1, 0), 1.15 * potency);
        return true;
    }
}
