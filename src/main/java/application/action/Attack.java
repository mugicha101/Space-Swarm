package application.action;

import application.Core;
import application.chunk.Chunkable;
import application.movement.Position;
import javafx.scene.Group;

import java.util.ArrayList;
import java.util.Random;

public abstract class Attack extends Chunkable {
  protected static final Random rand = new Random();
  private static ArrayList<Attack> attacks = new ArrayList<>();
  public static final Group attackGroup = new Group();
  public static void tickAttacks() {
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
  protected Core parent;
  protected final Position pos;
  protected boolean alive;
  public Attack(Core parent, Position pos) {
    this.parent = parent;
    this.pos = pos.clone();
    group = new Group();
    attackGroup.getChildren().add(group);
    alive = true;
    attacks.add(this);
  }

  protected abstract void tick();
  private void kill() {
    attackGroup.getChildren().remove(group);
    removeFromChunks();
    remove();
  }
  protected void remove() {}
}
