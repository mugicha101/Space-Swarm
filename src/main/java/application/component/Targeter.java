package application.component;

import application.Core;
import application.action.*;
import application.sprite.StaticSprite;
import javafx.scene.paint.Color;

public class Targeter extends Support {
    public Targeter(Core parent) {
        super(parent, new StaticSprite(null, "components/targeter.png", new double[] {0, 0}, 0.1), 10, 100, 0.2, 100, 5);
    }

    @Override
    protected boolean action() {
        new TargetEffect(this, range, duration, Color.color(1, 0, 0), 1/1.15, 0.15);
        return true;
    }
}
