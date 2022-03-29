package application.component;

import application.Game;
import application.movement.Position;
import application.sprite.Sprite;
import javafx.scene.Group;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;

import java.util.ArrayList;
import java.util.Comparator;

public class ComponentDisplay {
  private static class DisplayComparator implements Comparator<ComponentDisplay> {
    private int value(ComponentDisplay cd) {
      Component o = cd.component;
      if (o instanceof Turret)
        return 1;
      else if (o instanceof Sniper)
        return 2;
      else if (o instanceof Cannon)
        return 3;
      else if (o instanceof Healer)
        return 201;
      else {
        if (o instanceof Weapon)
          return 0;
        else if (o instanceof Support)
          return 100;
        else {
          return -1;
        }
      }
    }

    @Override
    public int compare(ComponentDisplay o1, ComponentDisplay o2) {
      return value(o1) - value(o2);
    }
  }
  public static final Group componentDisplayGroup = new Group();
  public static final ArrayList<ComponentDisplay> displayList = new ArrayList<>();
  public static final int rowLength = 10;
  public static final double hMargin = 15;
  public static final double vMargin = 15;
  public static final double spacing = 50;
  public static final double healthBarWidth = 35;
  public static final double healthBarHeight = 3;
  public static final double healthBarOffset = 15;
  public static final double healthBarBorder = 1;
  private static boolean reSort = false;
  public static void tickDisplays() {
    if (reSort) {
      displayList.sort(new DisplayComparator());
      reSort = false;
    }
    for (int i = 0; i < displayList.size(); i++)
      displayList.get(i).tick(i);
    componentDisplayGroup.setTranslateX(hMargin);
    componentDisplayGroup.setTranslateY(vMargin);
    double width = getPos(rowLength-1).x;
    double height = getPos(displayList.size()-1).y;
    double scaledWidth = Game.guiWidth - hMargin * 2;
    double scaledHeight = Game.height - vMargin * 2;
    double scale = Math.min(scaledWidth/width, scaledHeight/height);
    Scale scaleTransform = new Scale(scale, scale, 0, 0);
    componentDisplayGroup.getTransforms().setAll(scaleTransform);
  }

  private final Component component;
  private final Group group;
  private final Sprite sprite;
  private final Position pos;
  private final ColorAdjust colorAdjust;
  private final Group healthBarGroup;
  private final Rectangle healthBarContainer;
  private final Rectangle healthBar;
  private final Scale healthBarScale;
  public ComponentDisplay(Component component) {
    this.component = component;
    group = new Group();
    sprite = component.sprite.clone();
    sprite.pos.set(0, 0);
    sprite.setSceneGroup(group);
    sprite.dir = 90;
    pos = new Position();
    sprite.enable();
    displayList.add(this);
    colorAdjust = new ColorAdjust();
    sprite.getIv().setEffect(colorAdjust);
    healthBarGroup = new Group();
    healthBarContainer = new Rectangle(-healthBarWidth/2 - healthBarBorder, healthBarOffset - healthBarBorder, healthBarWidth + healthBarBorder * 2, healthBarHeight + healthBarBorder * 2);
    healthBar = new Rectangle(0, healthBarOffset, healthBarWidth, healthBarHeight);
    healthBar.setTranslateX(-healthBarWidth/2);
    healthBarScale = new Scale();
    healthBar.getTransforms().setAll(healthBarScale);
    healthBarGroup.getChildren().addAll(healthBarContainer, healthBar);
    healthBar.setFill(Color.RED);
    healthBarContainer.setFill(Color.BLACK);
    group.getChildren().add(healthBarGroup);
    componentDisplayGroup.getChildren().add(group);
    reSort = true;
  }

  private static Position getPos(int index) {
    double x = (index % rowLength) * spacing;
    double y = (double)(index / rowLength) * spacing;
    return new Position(x, y);
  }

  private void tick(int index) {
    pos.set(getPos(index));
    group.setTranslateX(pos.x);
    group.setTranslateY(pos.y);
    sprite.drawUpdate();
    double health = Math.max(Math.min(component.getHealthProportion(), 1), 0);
    healthBarScale.setX(health);
    healthBar.setFill(component.incapacitated? Color.color(0.5 + health * 0.5, 0.5 + health * 0.5, 0.5 + health * 0.5) : Color.hsb(Math.pow(health, 1.5) * 120, 1, 1));
    colorAdjust.setBrightness(component.incapacitated? -0.5 : 0);
  }
}
