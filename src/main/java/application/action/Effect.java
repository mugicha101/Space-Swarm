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
  public static void clear() {
    for (Effect effect : effects)
      effect.kill();
    effects.clear();
  }

  protected Component[] affected; // Note: should only be changeable before first tick
  protected double duration;
  protected final Group group;
  private boolean addedToComponents;
  public Effect(Component[] affected, double duration) {
    this.affected = affected;
    this.duration = duration;
    this.group = new Group();
    effects.add(this);
    effectGroup.getChildren().add(group);
    addedToComponents = false;
  }

  public final void tick() {
    if (!addedToComponents) {
      for (Component component : affected)
        component.addEffect(this);
    }
    double tickAmount = Math.min(duration, 1/60.0);
    baseAction(tickAmount);
    for (int i = 0; i < affected.length; i++) {
      if (!affected[i].isDeleted())
        action(i, affected[i], tickAmount);
    }
    duration -= tickAmount;
  }

  public final boolean isActive() {
    return duration > 0;
  }
  protected void baseAction(double timeMulti) {} // tick action (runs every tick)
  protected void action(int index, Component component, double timeMulti) {} // tick action (runs for every componet on every tick)
  private void kill() {
    effectGroup.getChildren().remove(group);
    for (Component component : affected)
      component.removeEffect(this);
  }
  public abstract void apply(Component component); // applies effect to component
}
