package application;

import application.action.Attack;
import application.action.Effect;
import application.chunk.Chunk;
import application.component.*;
import application.component.Component;
import application.movement.Position;
import application.particle.Particle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Random;

public class Game extends Application {
  public static final Random rand = new Random();
  public static final int borderWidth = 20;
  public static final int topMargin = 20;
  public static final int width = 800;
  public static final int height = 800;
  public static final int guiWidth = 400;
  public static boolean debug = false;
  public static boolean paused = false;
  public static int frame = -1;
  public static Stage stage;
  public static Group rootGroup;
  public static Group mainGroup;
  public static Group scrollGroup;
  public static Group guiGroup;
  public static Position camPos;
  public static double zoom;
  public static double zoomLevel;
  public static double zoomMulti;
  public static Position cursorPos;
  public static Position aimPos;
  public static Circle cursor;
  public static boolean mouseDown;
  public static Rectangle renderArea;

  public void start(Stage stage) throws IOException {
    // setup JavaFX
    rootGroup = new Group();
    Scene scene = new Scene(rootGroup);
    stage.setScene(scene);
    stage.setWidth(width + guiWidth + borderWidth * 2);
    stage.setHeight(height + borderWidth * 2);
    stage.setMinWidth(borderWidth);
    stage.setMinHeight(borderWidth);
    // stage.initStyle(StageStyle.UNDECORATED);
    stage.setResizable(true);
    stage.show();
    stage.setTitle("Game");
    Game.stage = stage;

    // setup input
    stage.getScene().setOnKeyPressed(e -> Input.keyRequest(e.getCode(), true));
    stage.getScene().setOnKeyReleased(e -> Input.keyRequest(e.getCode(), false));
    stage.getScene().setOnScroll(e -> scrollInput(e.getDeltaY()));
    cursorPos = new Position();

    // setup camera
    camPos = new Position();
    zoom = 1;
    zoomMulti = 1;
    zoomLevel = 1;

    // setup screen regions
    mainGroup = new Group();
    scrollGroup = new Group();
    guiGroup = new Group();
    rootGroup.getChildren().addAll(mainGroup, guiGroup);

    mainGroup.setClip(new Rectangle(0, 0, width, height));
    Rectangle bg = new Rectangle(0, 0, width, height);
    bg.setFill(Color.color(0, 0, 0.1));
    mainGroup.getChildren().add(bg);
    mainGroup.getChildren().add(scrollGroup);

    guiGroup.setClip(new Rectangle(0, 0, guiWidth, height));
    bg = new Rectangle(0, 0, guiWidth, height);
    bg.setFill(Color.ORANGE);
    scene.setFill(Color.YELLOW);
    guiGroup.getChildren().add(bg);
    guiGroup.setTranslateX(width);

    // setup screen area object
    renderArea = new Rectangle(-width*0.5, -height*0.5, width, height);
    renderArea.setVisible(false);
    scrollGroup.getChildren().add(renderArea);

    // setup scene graph nodes
    scrollGroup
        .getChildren()
        .addAll(
            Core.coreGroup,
            Component.componentGroup,
            Attack.attackGroup,
            Effect.effectGroup,
            Particle.particleGroup);

    guiGroup
        .getChildren()
        .addAll(
            ComponentDisplay.componentDisplayGroup,
            LevelManager.barGroup,
            ComponentSelect.selectGroup);

    // setup mouse input
    mainGroup.setCursor(Cursor.NONE);
    mainGroup.setOnMouseMoved(Game::moveCursor);
    mainGroup.setOnMouseDragged(Game::moveCursor);
    cursor = new Circle(0, 0, 5);
    cursor.setStrokeWidth(2);
    cursor.setFill(Color.TRANSPARENT);
    scrollGroup.getChildren().add(cursor);
    aimPos = new Position();
    mouseDown = false;
    mainGroup.setOnMousePressed(e -> mouseDown = true);
    mainGroup.setOnMouseReleased(e -> mouseDown = false);

    // setup player
    for (int i = 0; i < 4; i++) {
      new Turret(Player.core);
      new Sniper(Player.core);
    }
    for (int i = 0; i < 2; i++)
      new Healer(Player.core);

    // music
    Sound.initMusic();

    // start game loop
    Timeline tl = new Timeline(new KeyFrame(Duration.millis(17), e -> run()));
    tl.setCycleCount(Timeline.INDEFINITE);
    tl.play();
  }

  private void run() {
    frame++;
    Input.keyTick();
    input();
    if (!paused) {
      tick();
      if (debug) debugRun();
    }
  }

  private void input() {
    toggles();
  }

  private void tick() {
    screenResize();
    updateScroll();
    Player.tick();
    Enemy.tickEnemies();
    Core.tickCores();
    Attack.tickAttacks();
    Effect.tickEffects();
    Particle.tickParticles();
    Chunk.tick();

    ComponentDisplay.tickDisplays();
    LevelManager.tick();
    ComponentSelect.tick();
  }

  private void toggles() {
    if (Input.getInput("debug").onInitialPress()) {
      debug = !debug;
    }
    if (Input.getInput("fullscreen").onInitialPress()) {
      stage.setFullScreen(!stage.isFullScreen());
    }
    if (Input.getInput("pause").onInitialPress()) {
      paused = !paused;
    }
  }

  private static void screenResize() {
    double w = borderWidth * 2 + width + guiWidth;
    double h = borderWidth * 2 + height;
    double scaleVal = Math.min((stage.getWidth()) / w, (stage.getHeight() - topMargin) / h);
    Scale scale = new Scale();
    scale.setPivotX(0);
    scale.setPivotY(0);
    scale.setX(scaleVal);
    scale.setY(scaleVal);
    rootGroup.getTransforms().setAll(scale);
    rootGroup.setTranslateX((stage.getWidth() - scaleVal * w) / 2 + borderWidth * scaleVal);
    rootGroup.setTranslateY((stage.getHeight() - scaleVal * h) / 2 + borderWidth * scaleVal - topMargin);
  }

  private static void updateScroll() {
    camPos.x += (Player.getPos().x - camPos.x) * 0.25;
    camPos.y += (Player.getPos().y - camPos.y) * 0.25;
    Scale scale = new Scale(zoom, zoom);
    scrollGroup.getTransforms().setAll(scale);
    scrollGroup.setTranslateX(-camPos.x*zoom + Game.width*0.5);
    scrollGroup.setTranslateY(-camPos.y*zoom + Game.height*0.5);
    aimPos.set((cursorPos.x - width * 0.5)/zoom + camPos.x, (cursorPos.y - height * 0.5)/zoom + camPos.y);
    cursor.setTranslateX(aimPos.x);
    cursor.setTranslateY(aimPos.y);
    cursor.setScaleX(1/zoom);
    cursor.setScaleY(1/zoom);
    double scaledWidth = width/zoom;
    double scaledHeight = height/zoom;
    renderArea.setWidth(scaledWidth);
    renderArea.setHeight(scaledHeight);
    renderArea.setX(-scaledWidth/2+camPos.x);
    renderArea.setY(-scaledHeight/2+camPos.y);
    cursor.setStroke(mouseDown? Color.RED : Color.WHITE);
    zoomLevel = 5/(5 + Math.sqrt(Player.core.components.size()));
    zoom = zoomMulti * zoomLevel;
  }

  private static void scrollInput(double y) {
    if (y > 0) {
      zoomMulti *= 1.1;
      if (zoomMulti * zoomLevel > 5)
        zoomMulti /= 1.1;
    } else if (y < 0) {
      zoomMulti /= 1.1;
      if (zoomMulti < 1)
        zoomMulti = 1;
    }
  }

  private static void moveCursor(MouseEvent e) {
    cursorPos.set(e.getX(), e.getY());
  }

  private static void debugRun() {

  }

  public static void main(String[] args) {
    Input.init();
    launch();
  }
}
