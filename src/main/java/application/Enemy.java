package application;

import application.component.*;
import application.movement.DirCalc;
import application.movement.Position;
import application.movement.Velocity;

import java.util.ArrayList;
import java.util.Random;

public class Enemy {
  public static final int minComponents = 3; // min components per enemy
  public static final double speed = 0.1;
  private static final int maxEnemies = 30; // max alive enemies
  private static final int maxComponents = 350; // max total enemy components
  private static final double maxDistFromPlayer =
      8000; // maximum distance the point the enemy is moving towards can be
  private static final double maxAttackingDist = 1800; // furthest distance enemy attacks from
  private static final double maxEngageDist = 1200; // furthest distance enemy starts engaging from
  private static final double minLoseSightDist =
      2000; // minimum distance to lose sight of enemy (for movement)
  private static final Random rand = new Random();
  private static ArrayList<Enemy> enemies = new ArrayList<>();

  public static void tickEnemies() {
    // move
    ArrayList<Enemy> activeEnemies = new ArrayList<>();
    int components = 0;
    for (Enemy e : enemies) {
      e.tick();
      if (e.isAlive()) {
        components += e.core.components.size();
        activeEnemies.add(e);
      }
    }
    enemies = activeEnemies;

    // spawn
    while (components < maxComponents && enemies.size() < maxEnemies) {
      double scaleMulti = ((1 - 20.0 / (Player.core.components.size() + 20)));
      int c = Math.max(
              minComponents,
              (int)
                      (scaleMulti
                              * (0.25 + 1.25 * Math.random())
                              * (rand.nextDouble() < 0.03 ? 2 : 1)
                              * Player.core.components.size()));
      new Enemy(c);
      components += c;
      }
  }

  private Core core;
  private final Position targetPos;
  private Core targetCore;

  public Enemy(int components) {
    core = new Core();
    targetPos = getPos().clone();
    for (int i = 0; i < components; i++) ComponentFactory.create(core);
    enemies.add(this);
    targetCore = null;
  }

  public Velocity getVelo() {
    return core.velo;
  }

  public Position getPos() {
    return core.velo.pos;
  }

  public boolean isAlive() {
    return core.isAlive();
  }

  private void tick() {
    if (Game.frame > 0
        && core.velo.pos.distSqd(Player.getPos()) > Core.activeRange * Core.activeRange) {
      core.relocate(false);
    }
    if (targetCore != null && !targetCore.isAlive()) targetCore = null;
    getVelo().add((new Position()).moveInDir(DirCalc.dirTo(getPos(), targetPos), speed));
    double tDistSqd = getPos().distSqd(targetPos);
    double maxTargetDist = targetCore != null ? maxAttackingDist : maxDistFromPlayer;
    if (tDistSqd < 50 * 50 || Player.getPos().distSqd(targetPos) >= maxTargetDist * maxTargetDist) {
      targetPos.set(
          Player.getPos()
              .clone()
              .moveInDir(rand.nextDouble() * 360, rand.nextDouble() * maxTargetDist));
    }
    if (targetCore != null) {
      if (getPos().distSqd(targetCore.velo.pos) > minLoseSightDist * minLoseSightDist)
        targetCore = null;
    } else {
      for (Core c : Core.cores) {
        if (c == core) continue;
        if (getPos().distSqd(c.velo.pos) < maxEngageDist * maxEngageDist) targetCore = c;
      }
    }
    // re-target if closer by a certain amount
    if (targetCore != null) {
      Core closest = targetCore;
      for (Core c : Core.cores) {
        if (c == core) continue;
        if (getPos().distSqd(c.velo.pos) < getPos().distSqd(closest.velo.pos)) closest = c;
      }
      if (getPos().distSqd(closest.velo.pos) < getPos().distSqd(targetCore.velo.pos) * 0.75)
        targetCore = closest;
    }
    if (targetCore != null) core.aimPos.set(targetCore.velo.pos);
    core.attacking = targetCore != null;
  }
}
