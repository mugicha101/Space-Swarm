package application;

import application.component.ComponentSelect;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;

public class LevelManager {
  public static double nextLevelXP = 5;
  public static double xp = 0;
  public static double level = 0;
  public static final double barLength = Game.guiWidth * 0.8;
  public static final double barHeight = 10;
  public static final double barBorder = 2;
  public static final Group barGroup = new Group();
  public static final Rectangle barContainer = new Rectangle(-barLength/2 - barBorder, -barHeight/2 - barBorder, barLength + barBorder * 2, barHeight + barBorder * 2);
  public static final Rectangle bar = new Rectangle(0, -barHeight/2, barLength, barHeight);
  public static final Scale scale = new Scale();
  private static int pendingLevels = 0;

  public static void addXP(double amount) {
    xp += amount;
    while (xp >= nextLevelXP) {
      xp -= nextLevelXP;
      nextLevelXP *= 1.1;
      level++;
      pendingLevels++;
    }
  }
  public static void tick() {
    if (Game.frame == 0) {
      bar.setTranslateX(-barLength/2);
      bar.getTransforms().setAll(scale);
      barGroup.getChildren().addAll(barContainer, bar);
      barContainer.setFill(Color.BLACK);
      bar.setFill(Color.color(0, 1, 1));
      barGroup.setTranslateY(Game.height * 0.6);
      barGroup.setTranslateX(Game.guiWidth * 0.5);
    }
    scale.setX(xp/nextLevelXP);
    if (pendingLevels > 0 && !ComponentSelect.isActive()) {
      ComponentSelect.activate();
      pendingLevels--;
    }
  }
}
