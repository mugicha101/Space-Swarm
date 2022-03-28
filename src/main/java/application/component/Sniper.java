package application.component;

import application.Core;
import application.action.SniperBullet;
import application.action.TurretBullet;
import application.movement.DirCalc;
import application.movement.Position;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Sniper extends Weapon {
  public Sniper(Core parent) {
    super(parent, 10, 80, 0.5, 1200, 20, 25, 3);
    Circle circle = new Circle(0, 0, 10);
    circle.setFill(Color.BLUE);
    group.getChildren().add(circle);
  }

  @Override
  protected boolean action() {
    double dir = DirCalc.dirTo(velo.pos, parent.aimPos);
    velo.add((new Position()).moveInDir(dir, -2));
    new SniperBullet(parent, this, velo.pos.clone(), dir);
    return true;
  }
}
