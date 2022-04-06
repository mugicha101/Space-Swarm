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

public class TurretBullet extends Bullet {
  public TurretBullet(Core parentCore, Weapon parentWeapon, Position pos, double dir, Velocity sourceVelo) {
    super(parentCore, parentWeapon, pos, 5, dir, sourceVelo, 1, 1, 1, 1);
    Circle outer = new Circle(0, 0, 1.5 * radius);
    Circle inner = new Circle(0, 0, 1 * radius);
    outer.setFill(Color.color(1, 1, 0));
    inner.setFill(Color.WHITE);
    outer.setScaleY(0.5);
    inner.setScaleY(0.5);
    group.getChildren().addAll(outer, inner);
  }

  protected void hit(Component hitComponent) {
    // particles here
    hitParticles(Color.YELLOW);
    hitComponent.velo.add((new Position()).moveInDir(dir, damage * speed / 500));
  }
}
