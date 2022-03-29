package application.component;

import application.Core;
import application.action.HealEffect;
import application.sprite.StaticSprite;
import javafx.scene.paint.Color;

public class Reviver extends Support {
  public Reviver(Core parent) {
    super(parent, new StaticSprite(null, "components/reviver.png", new double[] {0, 0}, 0.1), 10, 120, 1, 75, 0.5);
  }

  @Override
  protected boolean action() {
    Component target = null;
    for (Component component : parent.components) {
      if (!component.incapacitated || component == this)
        continue;
      double distSqd = velo.pos.distSqd(component.velo.pos);
      if (distSqd < range * range && (target == null || component.getHealthProportion() > target.getHealthProportion())) {
        target = component;
      }
    }
    if (target != null) {
      new HealEffect(target, duration, this, 0.5, Color.color(0.8, 0, 1));
      return true;
    }
    return false;
  }
}