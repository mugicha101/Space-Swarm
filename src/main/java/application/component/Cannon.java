package application.component;

import application.Core;
import application.movement.Position;
import application.sprite.StaticSprite;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Cannon extends Weapon {
  public Cannon(Core parent) {
    super(parent, new StaticSprite(null, "components/cannon.png", new double[] {0, 0}, 0.1),10, 120, 1, 500, 5, 5, 5);
    Circle circle = new Circle(0, 0, 10);
    circle.setFill(Color.RED);
    group.getChildren().add(circle);
  }

  @Override
  protected boolean action() {
    return false;
  }
}
