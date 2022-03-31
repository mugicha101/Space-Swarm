package application;

import application.component.ComponentSelect;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;

public class LevelManager {
  public static double totalXP = 0;
  public static double nextLevelXP = 0;
  public static double xp = 10;
  public static double level = 0;
  public static final double barLength = Game.guiWidth * 0.8;
  public static final double barHeight = 10;
  public static final double barBorder = 2;
  public static final Group barGroup = new Group();
  public static final Rectangle barContainer = new Rectangle(-barLength/2 - barBorder, -barHeight/2 - barBorder, barLength + barBorder * 2, barHeight + barBorder * 2);
  public static final Rectangle bar = new Rectangle(0, -barHeight/2, barLength, barHeight);
  private static int pendingLevels = 0;
  private static double vTotalXP = totalXP;
  private static double vNextLevelXP = nextLevelXP;
  private static double vXP = xp;
  private static double vLevel = level;

  public static void addXP(double amount) {
    xp += amount;
    totalXP += amount;
    while (xp >= nextLevelXP) {
      xp -= nextLevelXP;
      nextLevelXP += 1 + nextLevelXP * 0.05;
      level++;
    }
  }
  private static void setBarFill() {
    Noise[] noiseArr = new Noise[] {new Noise(2, 0.25), new Noise(50, 25, 0.25)};
    bar.setFill(NoisePattern.create(barLength, barHeight, null, noiseArr, noiseArr, 0, 1, 1));
  }
  public static void tick() {
    if (Game.frame == 0) {
      bar.setTranslateX(-barLength/2);
      barGroup.getChildren().addAll(barContainer, bar);
      barContainer.setFill(Color.BLACK);
      setBarFill();
      barGroup.setTranslateY(Game.height * 0.6);
      barGroup.setTranslateX(Game.guiWidth * 0.5);
    }
    double vXPChange = Math.min((totalXP - vTotalXP) * 0.1, vNextLevelXP * 0.2);
    vXP += vXPChange;
    vTotalXP += vXPChange;
    while (vXP >= vNextLevelXP) {
      vXP -= vNextLevelXP;
      vNextLevelXP += 1 + vNextLevelXP * 0.05;
      vLevel++;
      pendingLevels++;
      setBarFill();
    }
    bar.setWidth(barLength * vXP/vNextLevelXP);
    if (pendingLevels > 0 && !ComponentSelect.isActive()) {
      ComponentSelect.activate();
      pendingLevels--;
    }
  }
}
