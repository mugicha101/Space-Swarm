package application;

import application.action.Attack;
import application.action.Effect;
import application.component.*;
import application.component.Component;
import application.movement.Position;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class Game extends Application {
  public static final int borderWidth = 20;
  public static final int topMargin = 20;
  public static final int width = 800;
  public static final int height = 800;
  public static final int guiWidth = 200;
  public static boolean debug = false;
  public static boolean paused = false;
  public static int frame = -1;
  public static Stage stage;
  public static Group rootGroup;
  public static Group mainGroup;
  public static Group scrollGroup;
  public static Group guiGroup;
  public static Position camPos;

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
    camPos = new Position(-width * 0.5, -height * 0.5);

    // setup input
    stage.getScene().setOnKeyPressed(e -> Input.keyRequest(e.getCode(), true));
    stage.getScene().setOnKeyReleased(e -> Input.keyRequest(e.getCode(), false));

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

    // setup scene graph nodes
    scrollGroup.getChildren().addAll(Core.coreGroup, Component.componentGroup, Attack.attackGroup, Effect.effectGroup);

    // setup player
    for (int i = 0; i < 100; i++)
      Player.addComponent(new Cannon(new Position()));

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
    Player.tick();
    moveCamera();
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

  private static void moveCamera() {
    camPos.x += (Player.getPos().x - Game.width*0.5 - camPos.x) * 0.25;
    camPos.y += (Player.getPos().y - Game.height*0.5 - camPos.y) * 0.25;
    scrollGroup.setTranslateX(-camPos.x);
    scrollGroup.setTranslateY(-camPos.y);
  }

  private static void debugRun() {

  }

  public static void main(String[] args) {
    Input.init();
    launch();
  }
}
