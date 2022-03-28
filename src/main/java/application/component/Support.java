package application.component;

import application.Core;
import application.sprite.Sprite;

public abstract class Support extends Component {
  public double potency;
  public double duration;
  public Support(Core parent, Sprite sprite, double radius, double health, double firerate, double range, double duration) {
    super(parent, sprite, radius, health, firerate, range, true);
    potency = 1;
    this.duration = duration;
  }
}
