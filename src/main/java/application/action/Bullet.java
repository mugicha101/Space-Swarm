package application.action;

import application.Core;
import application.component.Component;
import application.component.Weapon;
import application.movement.Position;

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

    // visuals
    group.setRotate(-dir);
    group.setTranslateX(pos.x);
    group.setTranslateY(pos.y);
    double scale = Math.max(Math.min((range - distTraveled)/100, 1), 0);
    group.setScaleX(scale);
    group.setScaleY(scale);

    // collision
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
    if (distTraveled >= range)
      alive = false;
  }

  protected abstract void hit(Component hitComponent); // action on hit
}
