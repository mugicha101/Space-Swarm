package application.component;

import application.Core;
import application.Player;
import application.Sound;
import application.action.SniperBullet;
import application.action.TurretBullet;
import application.movement.DirCalc;
import application.movement.Position;
import application.sprite.StaticSprite;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Sniper extends Weapon {
  public Sniper(Core parent) {
    super(parent, new StaticSprite(null, "components/sniper.png", new double[] {0, 0}, 0.1),10, 80, 0.25, 1800, 50, 25, 1);
  }

  @Override
  protected boolean action() {
    double dir = DirCalc.dirTo(velo.pos, parent.aimPos);
    velo.add((new Position()).moveInDir(dir, -2));
    Sound.play("shot3.wav", 1, 1, 0.25, parent == Player.core? null : velo.pos);
    new SniperBullet(parent, this, velo.pos.clone().moveInDir(dir, 4), dir, velo);
    return true;
  }
}
