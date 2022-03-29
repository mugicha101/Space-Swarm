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
    Circle bullet = new Circle(0, 0, radius*1.5);
    bullet.setFill(new RadialGradient(0, 0, 0, 0, radius*1.5, false, CycleMethod.NO_CYCLE, new Stop(0.5, Color.WHITE), new Stop(0.8, Color.YELLOW)));
    bullet.setScaleY(0.5);
    group.getChildren().add(bullet);
  }

  protected void hit(Component hitComponent) {
    // particles here
    hitParticles(Color.YELLOW);
    hitComponent.velo.add((new Position()).moveInDir(dir, damage * speed / 500));
  }
}
