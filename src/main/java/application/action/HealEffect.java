package application.action;

import application.component.Component;
import application.movement.DirCalc;
import application.movement.Position;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public class HealEffect extends Effect {
  private final Component sourceComponent;
  private final Line line;
  private final double healthPerSecond;
  private final boolean onlyIncapacitated;
  public HealEffect(Component affected, double duration, Component sourceComponent, double healthPerSecond, Color color, boolean onlyIncapacitated) {
    super(affected, duration);
    this.sourceComponent = sourceComponent;
    line = new Line(sourceComponent.velo.pos.x, sourceComponent.velo.pos.y, affected.velo.pos.x, affected.velo.pos.y);
    line.setOpacity(0.5);
    line.setStroke(color);
    effectGroup.getChildren().add(line);
    this.healthPerSecond = healthPerSecond;
    this.onlyIncapacitated = onlyIncapacitated;
  }

  @Override
  protected void action(double timeMulti) {
    Position origin = sourceComponent.velo.pos;
    Position target = affected.velo.pos;
    line.setStartX(origin.x);
    line.setStartY(origin.y);
    line.setEndX(target.x);
    line.setEndY(target.y);
    line.setStrokeWidth(healthPerSecond * 20 * Math.min(duration * 10, 1));
    if (affected.isIncapacitated() || !onlyIncapacitated) affected.healProportion(healthPerSecond * timeMulti);
  }

  @Override
  protected void remove() {
    effectGroup.getChildren().remove(line);
  }
}
