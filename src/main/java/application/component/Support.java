package application.component;

import application.Core;
import application.sprite.Sprite;

public abstract class Support extends Component {
  public final double initDuration;
  public double potency;
  public double duration;
  public Support(Core parent, Sprite sprite, double radius, double health, double firerate, double range, double duration) {
    super(parent, sprite, radius, health, firerate, range, true);
    potency = 1;
    initDuration = duration;
    this.duration = initDuration;
  }

  protected void updateStats() {
    potency = 1;
    duration = initDuration;
    super.updateStats();
    if (potency > 10)
      potency = 10;
    if (duration > initDuration * 10)
      duration = initDuration * 10;
  }
}
