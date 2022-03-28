package application.particle;

import application.movement.Position;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class CircleParticle extends Particle {
  private final Circle circle;
  public CircleParticle(double radius, Color color, double opacity, Position pos, double dir, double speed, int time) {
    super(pos, dir, speed, time);
    circle = new Circle(0, 0, radius);
    circle.setFill(color);
    circle.setOpacity(opacity);
    Particle.particleGroup.getChildren().add(circle);
  }

  protected void drawUpdate() {
    circle.setTranslateX(pos.x);
    circle.setTranslateY(pos.y);
    circle.setScaleX(1-getProgress());
    circle.setScaleY(1-getProgress());
  }

  protected void delete() {
    Particle.particleGroup.getChildren().remove(circle);
  }
}
