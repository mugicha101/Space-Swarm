package application.component;

import application.Core;
import application.action.TurretBullet;
import application.movement.DirCalc;
import application.movement.Position;
import application.sprite.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.io.IOException;

public class Turret extends Weapon {
  private final Sprite sprite;
  public Turret(Core parent) {
    super(parent, 10, 100, 3, 750, 5, 10, 15);
    try {
      sprite = new StaticSprite(group, "components/turret.png", new double[] {0, 0}, 0.1);
    } catch (IOException e) {
      throw new RuntimeException();
    }
    sprite.enable();
  }

  @Override
  public void tick() {
    super.tick();
    sprite.dir =
        (incapacitated || parent.aimPos == null || !parent.attacking)
            ? DirCalc.dirTo(velo.x, velo.y)
            : DirCalc.dirTo(velo.pos, parent.aimPos);
    sprite.drawUpdate();
  }

  @Override
  protected boolean action() {
    double dir = DirCalc.dirTo(velo.pos, parent.aimPos);
    // velo.add((new Position()).moveInDir(dir, -1));
    new TurretBullet(parent, this, velo.pos.clone(), dir);
    return true;
  }

  @Override
  public void remove() {
    sprite.disable();
  }
}
