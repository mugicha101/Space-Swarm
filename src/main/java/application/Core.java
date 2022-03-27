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
  public static ArrayList<Core> cores = new ArrayList<>();
  public static void tickCores() {
    ArrayList<Core> activeCores = new ArrayList<>();
    for (Core core : cores) {
      core.tick();
      if (core.isAlive())
        activeCores.add(core);
      else
        core.remove();
    }
    cores = activeCores;
  }

  public final Velocity velo;
  public final ArrayList<Component> components;
  private final Group group;
  public final Position aimPos;
  public boolean attacking;
  public Core(Position pos) {
    this.velo = new Velocity(pos, 0.05);
    group = new Group();
    coreGroup.getChildren().add(group);
    Circle circle = new Circle(0, 0, 15);
    circle.setFill(Color.RED);
    group.getChildren().add(circle);
    components = new ArrayList<>();
    cores.add(this);
    attacking = false;
    aimPos = new Position();
  }
  public boolean isAlive() {
    for (Component c : components)
      if (!c.isIncapacitated()) {
        return true;
      }
    return false;
  }
  public void tick() {
    velo.tick();
    group.setTranslateX(velo.pos.x);
    group.setTranslateY(velo.pos.y);
    for (Component c : components)
      c.tick();
  }
  public void remove() {
    coreGroup.getChildren().remove(group);
    for (Component component : components)
      component.remove();
  }
}
