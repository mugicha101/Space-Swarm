package application.component;

import application.movement.Position;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public abstract class Weapon extends Component {
  protected double damage;
  protected double shotspeed;
  protected double spread;
  public Weapon(Position pos, double health, double firerate, double damage, double shotspeed, double spread) {
    super(pos, health, firerate);
    this.damage = damage;
    this.shotspeed = shotspeed;
    this.spread = spread;
  }
}
