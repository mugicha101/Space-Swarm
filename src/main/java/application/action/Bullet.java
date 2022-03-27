package application.action;

import application.Core;
import application.chunk.Chunk;
import application.component.Component;
import application.component.Weapon;
import application.movement.Position;
import javafx.geometry.Rectangle2D;

public abstract class Bullet extends Attack {
  protected double dir;
  protected double radius;
  protected double speed;
  protected double damage;
  protected double range;
  private double distTraveled;
  public Bullet(Core parentCore, Weapon parentWeapon, Position pos, double radius, double dir, double speedMulti, double rangeMulti, double damageMulti, double spreadMulti) {
    super(parentCore, pos);
    this.dir = dir + (rand.nextDouble() * 2 - 1) * parentWeapon.spread * spreadMulti;
    this.radius = radius;
    speed = parentWeapon.shotspeed * speedMulti;
    range = parentWeapon.range * rangeMulti;
    damage = parentWeapon.damage * damageMulti;
    distTraveled = 0;
  }

  protected void tick() {
    // movement
    double step = Math.min(range - distTraveled, speed);
    pos.moveInDir(dir, step);
    distTraveled += step;
    updateChunks();

    // visuals
    group.setRotate(-dir);
    group.setTranslateX(pos.x);
    group.setTranslateY(pos.y);
    double scale = Math.max(Math.min((range - distTraveled)/100, 1), 0);
    group.setScaleX(scale);
    group.setScaleY(scale);

    // collision
    // TODO: add chunking system for components and bullets for more efficient collision checking
    /*
    for (Core core : Core.cores) {
      if (core == parent) continue;
      for (Component component : core.components) {
        if (!component.isIncapacitated() && pos.distSqd(component.velo.pos) < Math.pow(component.getRadius() + radius, 2)) {
          component.damage(damage);
          hit(component);
          alive = false;
          return;
        }
      }
    }
    */
    for (Chunk chunk : getChunks()) {
      for (Component component : chunk.components) {
        if (component.parent == parent)
          continue;
        if (!component.isIncapacitated() && pos.distSqd(component.velo.pos) < Math.pow(component.getRadius() + radius, 2)) {
          component.damage(damage);
          hit(component);
          alive = false;
          return;
        }
      }
    }
    if (distTraveled >= range)
      alive = false;
  }

  protected abstract void hit(Component hitComponent); // action on hit

  @Override
  protected Rectangle2D getBounds() {
    return new Rectangle2D(pos.x - radius, pos.y - radius, radius * 2, radius * 2);
  }

  @Override
  protected void chunkAdd(Chunk chunk) {
    chunk.bullets.add(this);
  }

  @Override
  protected void chunkRemove(Chunk chunk) {
    chunk.bullets.remove(this);
  }
}
