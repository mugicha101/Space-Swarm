package application.action;

import application.Sound;
import application.component.Component;
import application.movement.DirCalc;
import application.movement.Position;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public class HealEffect extends BeamEffect {
  private final double healthPerSecond;
  private final boolean onlyIncapacitated;
  public HealEffect(Component sourceComponent, Component[] affected, double duration, Color color, double healthPerSecond, boolean onlyIncapacitated) {
    super(sourceComponent, affected, duration, color, healthPerSecond * 20);
    this.healthPerSecond = healthPerSecond;
    this.onlyIncapacitated = onlyIncapacitated;
  }

  @Override
  protected void effectTick(int index, Component component, double timeMulti) {
    if (component.isIncapacitated() || !onlyIncapacitated) component.healProportion(healthPerSecond * timeMulti);
  }

  @Override
  public void apply(Component component) {}
}
