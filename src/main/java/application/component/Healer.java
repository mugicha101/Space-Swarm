package application.component;

import application.Core;
import application.action.HealEffect;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Healer extends Support {
  public Healer(Core parent) {
    super(parent, 10, 120, 2, 75, 0.25);
    Circle circle = new Circle(0, 0, 10);
    circle.setFill(Color.YELLOW);
    group.getChildren().add(circle);
  }

  @Override
  protected boolean action() {
    Component target = null;
    for (Component component : parent.components) {
      if (component == this)
        continue;
      double distSqd = velo.pos.distSqd(component.velo.pos);
      if (distSqd < range*range
          && (target == null
              || ((target.incapacitated == component.incapacitated)
                  && component.getHealthProportion() < target.getHealthProportion())
              || (!target.incapacitated && component.incapacitated))) {
        target = component;
      }
    }
    if (target != null && target.getHealthProportion() < 1) {
      new HealEffect(target, duration, this, 0.25);
      return true;
    }
    return false;
  }
}
