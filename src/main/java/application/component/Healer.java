package application.component;

import application.Core;
import application.action.HealEffect;
import application.sprite.StaticSprite;
import javafx.scene.paint.Color;

public class Healer extends Support {
  public Healer(Core parent) {
    super(parent, new StaticSprite(null, "components/healer.png", new double[] {0, 0}, 0.1), 10, 100, 5, 50, 0.2);
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
      new HealEffect(target, duration, this, 0.2, Color.color(0, 1, 0));
      return true;
    }
    return false;
  }
}
