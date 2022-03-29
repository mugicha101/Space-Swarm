package application;

import application.component.*;
import application.movement.DirCalc;
import application.movement.Position;
import application.movement.Velocity;

import java.util.ArrayList;
import java.util.Random;

public class Enemy {
  private static final double speed = 0.1;
  private static final double maxDistFromPlayer = 50000;
  private static final double maxAttackingDist = 1000;
  private static final double maxEngageDist = 750;
  private static final double minLoseSightDist = 1200;
  private static final Random rand = new Random();
  private static ArrayList<Enemy> enemies = new ArrayList<>();
  public static void tickEnemies() {
    ArrayList<Enemy> activeEnemies = new ArrayList<>();
    for (Enemy e : enemies) {
      e.tick();
      if (e.isAlive())
        activeEnemies.add(e);
    }
    enemies = activeEnemies;
  }

  private Core core;
  private final Position targetPos;
  private Core targetCore;
  public Enemy(int components) {
    core = new Core(Player.getPos().clone().moveInDir(rand.nextDouble() * 360, rand.nextDouble() * maxDistFromPlayer));
    targetPos = getPos().clone();
    for (int i = 0; i < components; i++) {
      switch (rand.nextInt(3)) {
        case 0 -> new Turret(core);
        case 1 -> new Sniper(core);
        case 2 -> new Healer(core);
      }
    }
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
    if (Game.frame > 0 && core.velo.pos.distSqd(Player.getPos()) > Core.activeRange * Core.activeRange)
      return;
    if (targetCore != null && !targetCore.isAlive())
      targetCore = null;
    getVelo().add((new Position()).moveInDir(DirCalc.dirTo(getPos(), targetPos), speed));
    double tDistSqd = getPos().distSqd(targetPos);
    double maxTargetDist = targetCore != null? maxAttackingDist : maxDistFromPlayer;
    if (tDistSqd < 50*50 || Player.getPos().distSqd(targetPos) >= maxTargetDist*maxTargetDist) {
      targetPos.set(Player.getPos().clone().moveInDir(rand.nextDouble()*360, rand.nextDouble()*maxTargetDist));
    }
    if (targetCore != null) {
      if (getPos().distSqd(targetCore.velo.pos) > minLoseSightDist * minLoseSightDist) targetCore = null;
    } else {
      for (Core c : Core.cores) {
        if (c == core) continue;
        if (getPos().distSqd(c.velo.pos) < maxEngageDist * maxEngageDist)
          targetCore = c;
      }
    }
    if (targetCore != null)
      core.aimPos.set(targetCore.velo.pos);
    core.attacking = targetCore != null;
  }
}
