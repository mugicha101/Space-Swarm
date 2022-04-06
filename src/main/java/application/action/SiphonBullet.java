package application.action;

import application.Core;
import application.component.Component;
import application.component.Weapon;
import application.movement.Position;
import application.movement.Velocity;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;

public class SiphonBullet extends Bullet {
    public SiphonBullet(Core parentCore, Weapon parentWeapon, Position pos, double dir, Velocity sourceVelo) {
        super(parentCore, parentWeapon, pos, 5, dir, sourceVelo, 1, 1, 1, 1);
        Circle outer = new Circle(0, 0, 1 * radius);
        Circle inner = new Circle(0, 0, 0.75 * radius);
        outer.setFill(Color.color(0.65, 0.65, 0.65));
        inner.setFill(Color.WHITE);
        group.getChildren().addAll(outer, inner);
    }

    protected void hit(Component hitComponent) {
        // particles here
        hitParticles(Color.WHITE);
        hitComponent.velo.add((new Position()).moveInDir(dir, damage * speed / 500));
    }
}
