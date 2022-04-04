package application.component;

import application.Core;
import application.Player;
import application.Sound;
import application.action.TurretBullet;
import application.movement.DirCalc;
import application.movement.Position;
import application.sprite.*;

public class Turret extends Weapon {
  private int side;
  public Turret(Core parent) {
    super(parent, new StaticSprite(null, "components/turret.png", new double[] {0, 0}, 0.1), 10, 100, 2, 1200, 20, 15, 15);
    side = 1;
  }

  @Override
  public void tick() {
    super.tick();
  }

  @Override
  protected boolean action() {
    double dir = DirCalc.dirTo(velo.pos, parent.aimPos);
    // velo.add((new Position()).moveInDir(dir, -0.5));
    Sound.play("shot1.mp3", 0.2, 1, 0.25, parent == Player.core? null : velo.pos);
    new TurretBullet(parent, this, velo.pos.clone().moveInDir(dir, 16).moveInDir(dir + 90 * side, 3), dir, velo);
    side = -side;
    return true;
  }
}
