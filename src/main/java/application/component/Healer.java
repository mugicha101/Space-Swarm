package application.component;

import application.Core;
import application.action.HealEffect;
import application.sprite.StaticSprite;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;

public class Healer extends Support {
  public Healer(Core parent) {
    super(parent, new StaticSprite(null, "components/cannon.png", new double[] {0, 0}, 0.1), 10, 100, 2, 50, 0.25);
  }

  @Override
  protected boolean action() {
    Component target = null;
    for (Component component : parent.components) {
      if (component == this)
        continue;
      double distSqd = velo.pos.distSqd(component.velo.pos);
      if (distSqd < range*range) {
        if (target == null)
          target = component;
        else if (!target.incapacitated && !component.incapacitated) {
          if (component.getHealthProportion() < target.getHealthProportion())
            target = component;
        } else if (target.incapacitated && component.incapacitated) {
          if (component.getHealthProportion() > target.getHealthProportion())
            target = component;
        } else if (!target.incapacitated) {
          target = component;
        }
      }
    }
    if (target != null && target.getHealthProportion() < 1) {
      new HealEffect(target, duration, this, 0.25);
      return true;
    }
    return false;
  }
}
