package application.particle;

import application.movement.Position;
import javafx.scene.Group;

import java.util.ArrayList;

public abstract class Particle {
  private static ArrayList<Particle> particles = new ArrayList<>();
  private static final int offscreenLimit = 500; // max offscreen particles
  public static final Group particleGroup = new Group();

  public static void tickParticles() {
    ArrayList<Particle> aliveParticles = new ArrayList<>();

    int limit = offscreenLimit;
    for (int i = particles.size() - 1; i >= 0; i--) {
      Particle p = particles.get(i);
      if (!p.onScreen()) {
        if (limit > 0)
          limit--;
        else {
          p.delete();
          continue;
        }
      }
      p.tick();
      if (p.isAlive())
        aliveParticles.add(p);
      else
        p.delete();
    }
    particles = aliveParticles;
  }


  protected final Position pos;
  protected double dir;
  private final double speed;
  private final int lifetime;
  private int time;

  public Particle(Position pos, double dir, double speed, int time) {
    this.dir = dir;
    this.pos = pos.clone();
    this.speed = speed;
    lifetime = time;
    this.time = 0;
    particles.add(this);
  }

  public double getProgress() {
    return (double)time / lifetime;
  }

  public boolean isAlive() {
    return time < lifetime;
  }

  public final void tick() {
    pos.moveInDir(dir, speed);
    time++;
    drawUpdate();
  }

  protected abstract void drawUpdate();

  protected abstract void delete();

  protected abstract boolean onScreen();
}
