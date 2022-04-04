package application.component;

import application.Core;
import application.action.HealEffect;
import application.action.PatchEffect;
import application.sprite.StaticSprite;
import javafx.scene.paint.Color;

public class Patcher extends Support {
    public Patcher(Core parent) {
        super(parent, new StaticSprite(null, "components/patcher.png", new double[] {0, 0}, 0.1), 10, 100, 1, 50, 0.5);
    }

    @Override
    protected boolean action() {
        new PatchEffect(this, range, duration, Color.color(1, 0, 1), 0.1 * potency, false);
        return true;
    }
}
