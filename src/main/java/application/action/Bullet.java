package application.action;

import application.Core;
import application.Game;
import application.chunk.Chunk;
import application.component.Component;
import application.component.Weapon;
import application.movement.Position;
import application.movement.Velocity;
import application.particle.CircleParticle;
import javafx.geometry.Rectangle2D;
import javafx.scene.paint.Color;

public abstract class Bullet extends Attack {
  protected static double particleMulti = 1;

  protected double dir;
  protected double radius;
  protected double speed;
  protected double damage;
  protected double range;
  private double distTraveled;
  private final double driftX;
  private final double driftY;
  public Bullet(Core parentCore, Weapon parentWeapon, Position pos, double radius, double dir, Velocity sourceVelo, double speedMulti, double rangeMulti, double damageMulti, double spreadMulti) {
    super(parentCore, pos);
    this.dir = dir + (rand.nextDouble() * 2 - 1) * parentWeapon.spread * spreadMulti;
    this.radius = radius;
    speed = parentWeapon.shotspeed * speedMulti;
    range = parentWeapon.range * rangeMulti;
    damage = parentWeapon.damage * damageMulti;
    distTraveled = 0;
    driftX = sourceVelo.x;
    driftY = sourceVelo.y;
  }

  protected void tick() {
    // movement
    double step = Math.min(range - distTraveled, speed);
    pos.moveInDir(dir, step);
    pos.move(driftX, driftY);
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
    for (Chunk chunk : getChunks()) {
      for (Component component : chunk.components) {
        if (component.parentMatches(parent))
          continue;
        if (!component.isIncapacitated() && pos.distSqd(component.velo.pos) < Math.pow(component.getRadius() + radius, 2)) {
          component.damage(damage, parent);
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

  protected void hitParticles(Color color) {
    int amount = (int)(damage * particleMulti);
    if (rand.nextDouble() > (damage * particleMulti - amount))
      amount++;
    for (int i = 0; i < amount; i++)
      new CircleParticle(5, color, 1, pos, dir + 180 + (rand.nextDouble() * 2 - 1) * 45, speed * (0.25 + rand.nextDouble()/2), 5 + rand.nextInt(10));
  }
}
