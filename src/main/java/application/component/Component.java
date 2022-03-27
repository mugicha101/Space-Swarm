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
  private double radius;
  private double health;
  private final double maxHealth;
  public double armor;
  public double firerate;
  public double range;
  protected boolean incapacitated;
  public final Velocity velo;
  private Position targetPos;
  private Double delay;
  protected Core parent;
  public Component(Core parent, double radius, double health, double firerate, double range) {
    group = new Group();
    componentGroup.getChildren().add(group);
    this.radius = radius;
    this.health = health;
    maxHealth = health;
    armor = 1;
    this.firerate = firerate;
    this.range = range;
    incapacitated = false;
    velo = new Velocity(parent.velo.pos.clone(), 0.01);
    targetPos = velo.pos.clone();
    delay = null;
    this.parent = parent;
    parent.components.add(this);
  }

  public boolean isIncapacitated() {
    return incapacitated;
  }
  public double getRadius() {
    return radius;
  }
  public void damage(double amount) {
    health -= amount;
    if (health <= 0) {
      health = 0;
      incapacitated = true;
    }
  }
  public void heal(double amount) {
    health += amount;
    if (health >= maxHealth) {
      health = maxHealth;
      incapacitated = false;
    }
  }

  public void tick() {
    // swarm movement
    Position accel = new Position();
    double distSqd = velo.pos.distSqd(targetPos);
    double maxDistSqd = parent.components.size() * 100;
    accel.moveInDir(DirCalc.dirTo(velo.pos, targetPos), 0.1);
    if (distSqd < 50*50 || parent.velo.pos.distSqd(targetPos) >= maxDistSqd) {
      targetPos = parent.velo.pos.clone().moveInDir(rand.nextDouble()*360, rand.nextDouble()*Math.sqrt(maxDistSqd));
    }
    velo.add(accel);
    velo.tick();
    group.setTranslateX(velo.pos.x);
    group.setTranslateY(velo.pos.y);

    // action
    if (incapacitated) {
      delay = null;
    } else {
      if (delay == null && parent.attacking) {
        delay = rand.nextDouble() * 60.0 / firerate;
      } else if (delay != null) delay--;
      while (delay != null && delay <= 0) {
        if (parent.attacking) {
          delay += 60.0 / firerate;
          action();
        } else {
          delay = null;
        }
      }
    }

    // visuals
    group.setOpacity(incapacitated? 0.5 : 1);
  }

  protected abstract void action();
  public void remove() {
    componentGroup.getChildren().remove(group);
  }
}
