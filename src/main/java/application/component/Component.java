package application.component;

import application.Core;
import application.Player;
import application.Sound;
import application.chunk.Chunk;
import application.chunk.Chunkable;
import application.movement.DirCalc;
import application.movement.Position;
import application.movement.Velocity;
import application.particle.CircleParticle;
import application.sprite.Sprite;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.paint.Color;

import java.util.Random;

public abstract class Component extends Chunkable {
  public static final Group componentGroup = new Group();
  public static final Random rand = new Random();

  protected final Group group;
  protected final Sprite sprite;
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
  private final boolean passive;

  public Component(
      Core parent,
      Sprite sprite,
      double radius,
      double health,
      double firerate,
      double range,
      boolean passive) {
    group = new Group();
    this.sprite = sprite.clone();
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
    this.passive = passive;
    parent.components.add(this);
    this.sprite.setSceneGroup(group);
    this.sprite.enable();
    if (parent == Player.core)
      new ComponentDisplay(this);
  }

  public boolean isIncapacitated() {
    return incapacitated;
  }

  public double getRadius() {
    return radius;
  }

  public double getHealthProportion() {
    return health / maxHealth;
  }

  public boolean parentMatches(Core core) {
    return parent == core;
  }

  public void setParent(Core core) { // note: does not remove from previous parent (must do manuallY)
    parent = core;
    core.components.add(this);
    if (core == Player.core)
      new ComponentDisplay(this);
  }

  public void damage(double amount, Core source) {
    double oldHealth = health;
    health -= amount;
    if (health <= 0) {
      health = 0;
      incapacitated = true;
      for (int i = 0; i < 50; i++)
        new CircleParticle(
            5,
            Color.color(1, i/50.0, 0),
            1,
            velo.pos,
            rand.nextDouble() * 360,
            5 + rand.nextDouble() * 20,
            5 + rand.nextInt(20));
      Sound.play("shot3.wav", 0.5, 0.5, 0.25, parent == Player.core? null : velo.pos);
    } else {
      Sound.play("tap.wav", 0.1 * Math.sqrt(amount), 0.5, 0.25, parent == Player.core? null : velo.pos);
    }
    parent.addDamage(oldHealth - health, source == Player.core);
  }

  public void heal(double amount) {
    health += amount;
    if (health >= maxHealth) {
      health = incapacitated? maxHealth * 0.5 : maxHealth;
      incapacitated = false;
    }
  }

  public void healProportion(double amount) {
    heal(maxHealth * amount);
  }

  public void tick() {
    // swarm movement
    Position accel = new Position();
    double maxDistSqd = parent.components.size() * 100 + 60 * 60;
    accel.moveInDir(DirCalc.dirTo(velo.pos, targetPos), 0.1);
    while (velo.pos.distSqd(targetPos) < 50 * 50
        || parent.velo.pos.distSqd(targetPos) >= maxDistSqd) {
      targetPos =
          parent
              .velo
              .pos
              .clone()
              .moveInDir(rand.nextDouble() * 360, rand.nextDouble() * Math.sqrt(maxDistSqd));
    }
    velo.add(accel);
    velo.tick();
    group.setTranslateX(velo.pos.x);
    group.setTranslateY(velo.pos.y);
    updateChunks();

    // action
    if (incapacitated) {
      delay = null;
    } else {
      if (delay == null && (parent.attacking || passive)) {
        delay = rand.nextDouble() * 60.0 / firerate;
      } else if (delay != null) delay--;
      while (delay != null && delay <= 0) {
        if (parent.attacking || passive) {
          if (action()) delay += 60.0 / firerate;
          else delay = 1.0;
        } else {
          delay = null;
        }
      }
    }
    if (incapacitated)
      healProportion(0.01/60);

    // visuals
    sprite.dir =
        (incapacitated || parent.aimPos == null || passive || !parent.attacking)
            ? DirCalc.dirTo(velo.x, velo.y)
            : DirCalc.dirTo(velo.pos, parent.aimPos);
    sprite.drawUpdate();
    group.setOpacity(incapacitated? 0.25 : 1);
  }

  protected abstract boolean action();

  public void remove() {
    componentGroup.getChildren().remove(group);
    removeFromChunks();
    sprite.disable();
  }

  @Override
  protected Rectangle2D getBounds() {
    return new Rectangle2D(velo.pos.x - radius, velo.pos.y - radius, radius * 2, radius * 2);
  }

  @Override
  protected void chunkAdd(Chunk chunk) {
    chunk.components.add(this);
  }

  @Override
  protected void chunkRemove(Chunk chunk) {
    chunk.components.remove(this);
  }
}
