package application;

import application.component.Component;
import application.movement.Position;
import application.movement.Velocity;

public class Player {
  public static Core core = new Core(new Position());
  public static Velocity getVelo() {
    return core.velo;
  }
  public static Position getPos() {
    return core == null? new Position() : core.velo.pos;
  }
  public static void tick() {
    if (Input.getInput("left").isPressed())
      getVelo().x -= Enemy.speed;
    if (Input.getInput("right").isPressed())
      getVelo().x += Enemy.speed;
    if (Input.getInput("up").isPressed())
      getVelo().y -= Enemy.speed;
    if (Input.getInput("down").isPressed())
      getVelo().y += Enemy.speed;
    core.aimPos.set(Game.aimPos);
    core.attacking = Game.mouseDown;
  }
}
