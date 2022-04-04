package application.component;

import application.Core;
import application.movement.Position;
import application.sprite.Sprite;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public abstract class Weapon extends Component {
  private final double initDamage;
  private final double initShotspeed;
  private final double initSpread;
  public double damage;
  public double shotspeed;
  public double spread;
  public Weapon(Core parent, Sprite sprite, double radius, double health, double firerate, double range, double damage, double shotspeed, double spread) {
    super(parent, sprite, radius, health, firerate, range, false);
    initDamage = damage;
    initShotspeed = shotspeed;
    initSpread = spread;
    this.damage = initDamage;
    this.shotspeed = initShotspeed;
    this.spread = initSpread;
  }

  protected void updateStats() {
    damage = initDamage;
    shotspeed = initShotspeed;
    spread = initSpread;
    super.updateStats();
  }
}
