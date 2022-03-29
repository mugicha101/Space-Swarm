package application.component;

import application.Core;
import application.action.TurretBullet;
import application.movement.DirCalc;
import application.sprite.*;

public class Turret extends Weapon {
  private int side;
  public Turret(Core parent) {
    super(parent, new StaticSprite(null, "components/turret.png", new double[] {0, 0}, 0.1), 10, 100, 3, 1200, 10, 10, 15);
    side = 1;
  }

  @Override
  public void tick() {
    super.tick();
  }

  @Override
  protected boolean action() {
    double dir = DirCalc.dirTo(velo.pos, parent.aimPos);
    // velo.add((new Position()).moveInDir(dir, -1));
    new TurretBullet(parent, this, velo.pos.clone().moveInDir(dir, 16).moveInDir(dir + 90 * side, 3), dir);
    side = -side;
    return true;
  }
}
