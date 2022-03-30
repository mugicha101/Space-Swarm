package application;

import application.Game;
import application.Player;
import application.component.Component;
import application.movement.DirCalc;
import application.movement.Position;
import application.movement.Velocity;
import application.particle.CircleParticle;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.Random;

public class Core {
  private static final Random rand = new Random();
  public static final double activeRange = 10000;
  public static final double minSpawnDist = 1000;
  public static final Group coreGroup = new Group();
  public static ArrayList<Core> cores = new ArrayList<>();

  public static void tickCores() {
    ArrayList<Core> activeCores = new ArrayList<>();
    for (Core core : cores) {
      core.tick();
      if (core.isAlive()) activeCores.add(core);
      else core.remove();
    }
    cores = activeCores;
  }

  public final Velocity velo;
  public final ArrayList<Component> components;
  private final Group group;
  public final Position aimPos;
  public boolean attacking;
  private double playerDamage;
  private double otherDamage;
  public Core(Position pos) {
    velo = new Velocity((pos == null ? new Position() : pos), 0.05);
    group = new Group();
    components = new ArrayList<>();
    cores.add(this);
    attacking = false;
    if (pos == null) relocate(true);
    aimPos = new Position();
  }

  public Core() {
    this(null);
  }

  public boolean isAlive() {
    for (Component c : components)
      if (!c.isIncapacitated()) {
        return true;
      }
    return false;
  }

  public void tick() {
    velo.tick();
    group.setTranslateX(velo.pos.x);
    group.setTranslateY(velo.pos.y);
    for (Component c : components) c.tick();
  }

  public void relocate(boolean randDir) {
    double dir = randDir? Math.random() * 360 : DirCalc.dirTo(Player.getPos(), velo.pos) + 180;
    velo.pos.set(Player.getPos().clone().moveInDir(dir, minSpawnDist + (randDir? Math.random() : 1) * (Core.activeRange - 100 - minSpawnDist)));
    for (Component component : components) component.velo.pos.set(velo.pos);
  }

  public void remove(boolean silent) {
    if (!silent) {
      Sound.play("thump.wav", 1, 0.5, 0.25, this == Player.core? null : velo.pos);
      for (Component component : components)
        for (int i = 0; i < 25; i++)
          new CircleParticle(
                  5,
                  Color.color(1, i/25.0, 0),
                  1,
                  component.velo.pos,
                  rand.nextDouble() * 360,
                  5 + rand.nextDouble() * 20,
                  20 + rand.nextInt(80));
    }
    coreGroup.getChildren().remove(group);
    for (Component component : components)
     component.remove();
    LevelManager.addXP(components.size() * playerDamage / (playerDamage + otherDamage));
    if (this != Player.core) components.clear();
  }
  public void remove() {
    remove(false);
  }

  public void addDamage(double amount, boolean fromPlayer) {
    if (fromPlayer)
      playerDamage += amount;
    else
      otherDamage += amount;
  }
}
