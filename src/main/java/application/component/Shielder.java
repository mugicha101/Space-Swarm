package application.component;

import application.Core;
import application.action.ArmorEffect;
import application.action.OverclockEffect;
import application.component.Support;
import application.sprite.StaticSprite;
import javafx.scene.paint.Color;

public class Shielder extends Support {
    public Shielder(Core parent) {
        super(parent, new StaticSprite(null, "components/shielder.png", new double[] {0, 0}, 0.1), 10, 200, 0.2, 75, 5);
    }

    @Override
    protected boolean action() {
        new ArmorEffect(this, range, duration, Color.color(0.5, 1, 0.7), 1.15 * potency);
        return true;
    }
}
