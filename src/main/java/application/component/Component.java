package application.component;

import application.Core;
import application.Game;
import application.movement.DirCalc;
import application.movement.Position;
import application.movement.Velocity;
import javafx.scene.Group;

import java.util.Random;

public abstract class Component {
  public static final Group componentGroup = new Group();
  public static final Random rand = new Random();

  protected final Group group;
  private double health;
  private final double maxHealth;
  private double armor;
  private double firerate;
  protected boolean incapacitated;
  public final Velocity velo;
  private Position targetPos;
  public Component(Position pos, double health, double firerate) {
    group = new Group();
    componentGroup.getChildren().add(group);
    this.health = health;
    maxHealth = health;
    armor = 1;
    this.firerate = firerate;
    incapacitated = false;
    velo = new Velocity(pos.clone(), 0.01);
    targetPos = pos.clone();
  }

  public void tick(Core parent) {
    // swarm movement
    Position accel = new Position();
    double distSqd = velo.pos.distSqd(targetPos);
    double maxDistSqd = parent.getComponentAmount() * 100;
    accel.moveInDir(DirCalc.dirTo(velo.pos, targetPos), 0.1);
    if (distSqd < 50*50 || parent.velo.pos.distSqd(targetPos) >= maxDistSqd) {
      targetPos = parent.velo.pos.clone().moveInDir(rand.nextDouble()*360, rand.nextDouble()*Math.sqrt(maxDistSqd));
    }
    velo.add(accel);
    velo.tick();
    group.setTranslateX(velo.pos.x);
    group.setTranslateY(velo.pos.y);
  }

  protected abstract void action();
}
