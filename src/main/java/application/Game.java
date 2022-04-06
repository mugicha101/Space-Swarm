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
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Objects;
import java.util.Random;

public class Game extends Application {
    public static final Random rand = new Random();
    public static final int borderWidth = 25;
    public static final int topMargin = 26;
    public static final int rightMargin = 11;
    public static final int width = 800;
    public static final int height = 800;
    public static final int guiWidth = 400;
    public static final int guiSeperation = 0;
    public static boolean debug = false;
    public static boolean paused = false;
    public static int frame = -1;
    public static Stage stage;
    public static Group baseGroup;
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
    public static Rectangle sceneBG;
    public static Scale sceneBGScale;
    public static Label gameOverLabel;

    public void start(Stage stage) throws IOException {
        // setup JavaFX
        baseGroup = new Group();
        rootGroup = new Group();
        Scene scene = new Scene(baseGroup);
        stage.setScene(scene);
        stage.setWidth(width + guiWidth + borderWidth * 2 + guiSeperation);
        stage.setHeight(height + borderWidth * 2);
        stage.setMinWidth(borderWidth);
        stage.setMinHeight(borderWidth);
        stage.setFullScreen(true);
        // stage.initStyle(StageStyle.UNDECORATED);
        stage.setResizable(true);
        stage.show();
        stage.setTitle("Space Swarm");
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
        sceneBG = new Rectangle(width + guiWidth, height);
        sceneBG.setFill(NoisePattern.create(width  + guiWidth + borderWidth * 2 + guiSeperation, height + borderWidth * 2 + topMargin, new Noise[] {new Noise(2, 0.25), new Noise(50, 25, 0.25)}, null, new Noise[] {new Noise(2, 0.25), new Noise(50, 25, 0.25)}, 0.15, 0, 0.3));
        bg.setFill(NoisePattern.create(guiWidth, height, new Noise[] {new Noise(2, 0.25), new Noise(50, 25, 0.25)}, null, new Noise[] {new Noise(2, 0.25), new Noise(50, 25, 0.25)}, 0.5, 0, 0.5));
        guiGroup.getChildren().add(bg);
        guiGroup.setTranslateX(width + guiSeperation);
        sceneBGScale = new Scale();
        sceneBGScale.setPivotX(0);
        sceneBGScale.setPivotY(0);
        sceneBG.getTransforms().setAll(sceneBGScale);

        // setup screen area object
        renderArea = new Rectangle(-width*0.5, -height*0.5, width, height);
        renderArea.setVisible(false);
        scrollGroup.getChildren().add(renderArea);

        // setup scene graph nodes
        scrollGroup
                .getChildren()
                .addAll(
                        Core.coreGroup,
                        Star.starGroup,
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

        // setup background
        Star.init();

        // gameover label
        gameOverLabel = new Label("Press R to Restart");
        gameOverLabel.setFont(Font.font("Impact", FontWeight.BLACK, 50));
        gameOverLabel.setTextAlignment(TextAlignment.CENTER);
        gameOverLabel.setAlignment(Pos.CENTER);
        gameOverLabel.setTranslateX(0);
        gameOverLabel.setTranslateY(height * 0.5);
        gameOverLabel.setMinWidth(width);
        gameOverLabel.setMaxWidth(width);
        gameOverLabel.setTextFill(Color.WHITE);
        gameOverLabel.setVisible(false);
        mainGroup.getChildren().add(gameOverLabel);

        // setup music
        Sound.initMusic();

        // add all to scene
        baseGroup.getChildren().addAll(sceneBG, rootGroup);

        // reset game
        reset();

        // start game loop
        Timeline tl = new Timeline(new KeyFrame(Duration.millis(17), e -> run()));
        tl.setCycleCount(Timeline.INDEFINITE);
        tl.play();
    }

    public void reset() {
        camPos.set(0,0);
        ComponentDisplay.clear();
        Effect.clear();
        Attack.clear();
        Particle.clear();
        LevelManager.reset();

        Player.core = new Core(new Position());
        for (Core core : Core.cores) {
            core.remove(true);
        }
        for (int i = 0; i < 5; i++) {
            new Turret(Player.core);
        }
        for (int i = 0; i < 3; i++) {
            new Healer(Player.core);
        }
        for (int i = 0; i < 2; i++) {
            new Sniper(Player.core);
        }
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
        if (!Player.core.isAlive() && Input.getInput("restart").onInitialPress())
            reset();
    }

    private void tick() {
        screenResize();
        updateScroll();

        if (frame % 5 == 0 || !Input.getInput("slow").isPressed()) {
            Player.tick();
            Enemy.tickEnemies();
            Core.tickCores();
            Attack.tickAttacks();
            Effect.tickEffects();
            Particle.tickParticles();
            Chunk.tick();
            Star.tickStars();
        }

        ComponentDisplay.tickDisplays();
        LevelManager.tick();
        ComponentSelect.tickOptions();
        gameOverTick();
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
        double w = borderWidth * 2 + width + guiWidth + guiSeperation;
        double h = borderWidth * 2 + height;
        double scaleVal = Math.min((stage.getWidth() - rightMargin) / w, (stage.getHeight() - topMargin) / h);
        Scale scale = new Scale();
        scale.setPivotX(0);
        scale.setPivotY(0);
        scale.setX(scaleVal);
        scale.setY(scaleVal);
        rootGroup.getTransforms().setAll(scale);
        rootGroup.setTranslateX((stage.getWidth() - scaleVal * w) / 2 + borderWidth * scaleVal - rightMargin);
        rootGroup.setTranslateY((stage.getHeight() - scaleVal * h) / 2 + borderWidth * scaleVal - topMargin);
        sceneBG.setTranslateX(0);
        sceneBG.setTranslateY(0);
        scaleVal = Math.max(stage.getWidth()/width, stage.getHeight()/height);
        sceneBGScale.setX(scaleVal);
        sceneBGScale.setY(scaleVal);
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
        zoomLevel = 4/(4 + Math.sqrt(Player.core.components.size()));
        zoom = zoomMulti * zoomLevel;
    }

    private static void scrollInput(double y) {
        if (y > 0) {
            zoomMulti *= 1.1;
            if (zoomMulti * zoomLevel > 5)
                zoomMulti /= 1.1;
        } else if (y < 0) {
            zoomMulti /= 1.1;
            if (zoomMulti < 0.5)
                zoomMulti = 0.5;
        }
    }

    private static void gameOverTick() {
        gameOverLabel.setVisible(!Player.core.isAlive());
        if (Player.core.isAlive())
            return;
        gameOverLabel.setOpacity(0.75 + 0.25 * Math.sin(frame / Math.PI / 15));
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
