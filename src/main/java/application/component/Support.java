package application.component;

import application.Core;

public abstract class Support extends Component {
  public double potency;
  public double duration;
  public Support(Core parent, double radius, double health, double firerate, double range, double duration) {
    super(parent, radius, health, firerate, range, true);
    potency = 1;
    this.duration = duration;
  }
}
