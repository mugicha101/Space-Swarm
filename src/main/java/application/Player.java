package application;

import application.component.Component;
import application.movement.Position;
import application.movement.Velocity;

public class Player {
  private static final double thrust = 0.1;
  public static Core core = new Core(new Position());
  public static Velocity getVelo() {
    return core.velo;
  }
  public static Position getPos() {
    return core.velo.pos;
  }
  public static int getComponentAmount() {
    return core.getComponentAmount();
  }
  public static void addComponent(Component component) {
    core.addComponent(component);
  }
  public static void tick() {
    if (Input.getInput("left").isPressed())
      getVelo().x -= thrust;
    if (Input.getInput("right").isPressed())
      getVelo().x += thrust;
    if (Input.getInput("up").isPressed())
      getVelo().y -= thrust;
    if (Input.getInput("down").isPressed())
      getVelo().y += thrust;
    core.tick();
  }
}
