package application.component;

import application.Game;
import application.movement.Position;
import application.sprite.Sprite;
import javafx.scene.Group;
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
        return 0;
      if (o instanceof Sniper)
        return 1;
      if (o instanceof Cannon)
        return 2;
      if (o instanceof Lazer)
        return 3;
      if (o instanceof Siphongun)
        return 4;
      if (o instanceof Overclocker)
        return 10;
      if (o instanceof Shielder)
        return 11;
      if (o instanceof Beacon)
        return 12;
      if (o instanceof Targeter)
        return 13;
      if (o instanceof Radar)
        return 13;
      if (o instanceof Energizer)
        return 14;
      if (o instanceof Healer)
        return 20;
      if (o instanceof Reviver)
        return 21;
      if (o instanceof Patcher)
        return 22;
      return -1;
    }

    @Override
    public int compare(ComponentDisplay o1, ComponentDisplay o2) {
      return value(o1) - value(o2);
    }
  }
  public static final Group componentDisplayGroup = new Group();
  public static final ArrayList<ComponentDisplay> displayList = new ArrayList<>();
  public static final int minRowLength = 5;
  public static final double hMargin = 30;
  public static final double vMargin = 30;
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
    double width = getPos(getRowLength()-1).x;
    double height = getPos(displayList.size()-1).y;
    double scaledWidth = Game.guiWidth - hMargin * 2;
    double scaledHeight = Game.height * 0.5 - vMargin * 2;
    double scale = Math.min(scaledWidth/width, scaledHeight/height);
    Scale scaleTransform = new Scale(scale, scale, 0, 0);
    componentDisplayGroup.getTransforms().setAll(scaleTransform);
    componentDisplayGroup.setTranslateX(hMargin + (scaledWidth - width * scale) * 0.5);
    componentDisplayGroup.setTranslateY(vMargin);
  }
  public static void clear() {
    displayList.clear();
    componentDisplayGroup.getChildren().clear();
  }

  private final Component component;
  private final Group group;
  private final Sprite sprite;
  private final Position pos;
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

  private static int getRowLength() {
    return  Math.max(minRowLength, (int)Math.ceil(Math.sqrt(displayList.size())));
  }

  private static Position getPos(int index) {
    int rowLength = getRowLength();
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
    group.setOpacity(component.incapacitated? 0.5 : 1);
  }
}
