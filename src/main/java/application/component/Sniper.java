package application.component;

import application.Core;
import application.action.SniperBullet;
import application.action.TurretBullet;
import application.movement.DirCalc;
import application.movement.Position;
import application.sprite.StaticSprite;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Sniper extends Weapon {
  public Sniper(Core parent) {
    super(parent, new StaticSprite(null, "components/sniper.png", new double[] {0, 0}, 0.1),10, 80, 0.5, 1200, 20, 25, 3);
  }

  @Override
  protected boolean action() {
    double dir = DirCalc.dirTo(velo.pos, parent.aimPos);
    velo.add((new Position()).moveInDir(dir, -2));
    new SniperBullet(parent, this, velo.pos.clone(), dir);
    return true;
  }
}
