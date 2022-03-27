package application;

import application.Game;
import application.Player;
import application.component.Component;
import application.movement.Position;
import application.movement.Velocity;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;

public class Core {
  public static final Group coreGroup = new Group();

  public final Velocity velo;
  private final ArrayList<Component> components;
  private final Group group;
  public Core(Position pos) {
    this.velo = new Velocity(pos, 0.05);
    group = new Group();
    coreGroup.getChildren().add(group);
    Circle circle = new Circle(0, 0, 10);
    circle.setFill(Color.RED);
    group.getChildren().add(circle);
    components = new ArrayList<>();
  }
  public int getComponentAmount() {
    return components.size();
  }
  public void addComponent(Component component) {
    component.velo.pos.set(velo.pos);
    components.add(component);
  }
  public void tick() {
    velo.tick();
    group.setTranslateX(velo.pos.x);
    group.setTranslateY(velo.pos.y);
    for (Component c : components)
      c.tick(this);
  }
  public void remove() {
    coreGroup.getChildren().remove(group);
  }
}
