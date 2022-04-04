package application.component;

import application.Core;
import application.Player;
import application.Sound;
import application.action.CannonBullet;
import application.action.SniperBullet;
import application.movement.DirCalc;
import application.movement.Position;
import application.sprite.StaticSprite;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Cannon extends Weapon {
  public Cannon(Core parent) {
    super(parent, new StaticSprite(null, "components/cannon.png", new double[] {0, 0}, 0.1),10, 120, 0.5, 900, 12, 10, 5);
  }

  @Override
  protected boolean action() {
    double dir = DirCalc.dirTo(velo.pos, parent.aimPos);
    velo.add((new Position()).moveInDir(dir, -3/(firerate*2)));
    Sound.play("shot1.mp3", 1, 0.5, 0.25, parent == Player.core? null : velo.pos);
    new CannonBullet(parent, this, velo.pos.clone().moveInDir(dir, 14), dir, velo);
    return true;
  }
}
