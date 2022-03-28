package application.component;

import application.Core;
import application.movement.Position;
import application.sprite.Sprite;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public abstract class Weapon extends Component {
  public double damage;
  public double shotspeed;
  public double spread;
  public Weapon(Core parent, Sprite sprite, double radius, double health, double firerate, double range, double damage, double shotspeed, double spread) {
    super(parent, sprite, radius, health, firerate, range, false);
    this.damage = damage;
    this.shotspeed = shotspeed;
    this.spread = spread;
  }
}
