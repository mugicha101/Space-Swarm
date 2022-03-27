package application.action;

import application.movement.Position;
import javafx.scene.Group;

import java.util.ArrayList;

public abstract class Attack {
  private static ArrayList<Attack> attacks = new ArrayList<>();
  public static final Group attackGroup = new Group();
  public static void tickEffects() {
    ArrayList<Attack> activeAttacks = new ArrayList<>();
    for (Attack a : attacks) {
      a.tick();
      if (a.alive)
        activeAttacks.add(a);
      else
        a.kill();
    }
    attacks = activeAttacks;
  }

  protected final Group group;
  protected final Position pos;
  protected boolean alive;
  public Attack(Position pos) {
    this.pos = pos;
    group = new Group();
    attackGroup.getChildren().add(group);
  }

  public abstract void tick();
  private void kill() {
    attackGroup.getChildren().remove(group);
    remove();
  }
  protected abstract void remove(); // remove attack
}
