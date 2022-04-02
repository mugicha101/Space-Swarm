package application.action;

import application.component.Component;
import javafx.scene.Group;

import java.util.ArrayList;

public abstract class Effect {
  private static ArrayList<Effect> effects = new ArrayList<>();
  public static final Group effectGroup = new Group();
  public static void tickEffects() {
    ArrayList<Effect> activeEffects = new ArrayList<>();
    for (Effect e : effects) {
      e.tick();
      if (e.isActive())
        activeEffects.add(e);
      else
        e.kill();
    }
    effects = activeEffects;
  }

  private final Component[] affected;
  protected double duration;
  protected final Group group;
  public Effect(Component[] affected, double duration) {
    this.affected = affected;
    this.duration = duration;
    this.group = new Group();
    effects.add(this);
    effectGroup.getChildren().add(group);
  }

  public final void tick() {
    double tickAmount = Math.min(duration, 1/60.0);
    for (int i = 0; i < affected.length; i++)
      action(i, affected[i], tickAmount);
    duration -= tickAmount;
  }

  public final boolean isActive() {
    return duration > 0;
  }

  protected abstract void action(int index, Component component, double timeMulti);
  private void kill() {
    effectGroup.getChildren().remove(group);
    for (int i = 0; i < affected.length; i++)
      remove(i, affected[i]);
  }
  protected abstract void remove(int index, Component component); // remove effect
}
