package application.component;

import application.Core;
import application.action.TurretBullet;
import application.movement.DirCalc;
import application.movement.Position;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Turret extends Weapon {
  public Turret(Core parent) {
    super(parent, 10, 100, 3, 750, 5, 10, 15);
    Circle circle = new Circle(0, 0, 10);
    circle.setFill(Color.GREEN);
    group.getChildren().add(circle);
  }

  @Override
  protected void action() {
    double dir = DirCalc.dirTo(velo.pos, parent.aimPos);
    // velo.add((new Position()).moveInDir(dir, -1));
    new TurretBullet(parent, this, velo.pos.clone(), dir);
  }
}
