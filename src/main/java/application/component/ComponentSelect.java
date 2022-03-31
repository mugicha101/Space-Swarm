package application.component;

import application.Game;
import application.Noise;
import application.NoisePattern;
import application.Player;
import application.movement.Position;
import application.sprite.Sprite;
import application.sprite.StaticSprite;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Scale;

import java.util.ArrayList;
import java.util.Random;

public class ComponentSelect {
    private enum Type {
        CANNON,
        TURRET,
        SNIPER,
        HEALER,
        REVIVER,
        PATCHER,
    }
    private interface ClickAction {
        void run();
    }
    private interface SpriteAction {
        Sprite run(Group group);
    }

    private static final Color weaponColor = Color.color(1, 0, 0);
    private static final Color supportColor = Color.color(0, 0, 1);
    private static final Color recoveryColor = Color.color(0, 1, 0);

    private static final Random rand = new Random();
    public static final Group selectGroup = new Group();
    private static final ArrayList<ComponentSelect> activeList = new ArrayList<>();
    public static void tickOptions() {
        for (ComponentSelect cs : activeList)
            cs.tick();
    }
    private static void clear() {
        for (ComponentSelect cs : activeList)
            cs.remove();
        activeList.clear();
    }
    public static boolean isActive() {
        return activeList.size() != 0;
    }
    public static void activate() {
        clear();
        for (int i = -1; i <= 1; i++) {
            Position pos = new Position(Game.guiWidth * 0.5 + i * Game.guiWidth * 0.32, Game.height * 0.7);
            if (i == -1) { // weapon
                switch(rand.nextInt(2)) {
                    case 0 -> ComponentSelect.create(Type.TURRET, pos);
                    case 1 -> ComponentSelect.create(Type.SNIPER, pos);
                }
            } else if (i == 0) { // support
                switch(rand.nextInt(2)) {
                    case 0 -> ComponentSelect.create(Type.TURRET, pos);
                    case 1 -> ComponentSelect.create(Type.SNIPER, pos);
                }
            } else {
                switch(rand.nextInt(2)) {
                    case 0 -> ComponentSelect.create(Type.HEALER, pos);
                    case 1 -> ComponentSelect.create(Type.REVIVER, pos);
                }
            }
        }
    }
    private static void create(Type type, Position pos) {
        switch(type) {
            case TURRET -> new ComponentSelect(pos, (group) -> new StaticSprite(group, "components/turret.png", new double[] {0, 0}, 0.1), "Turret", "Health: 100\nFirerate: 2\nDamage: 20\nSpread: 15\nShotspeed: 15\nRange: 1200", () -> new Turret(Player.core), weaponColor);
            case SNIPER -> new ComponentSelect(pos, (group) -> new StaticSprite(group, "components/sniper.png", new double[] {0, 0}, 0.1), "Sniper", "Health: 80\nFirerate: 0.25\nDamage: 50\nSpread: 1\nShotspeed: 25\nRange: 1800", () -> new Sniper(Player.core), weaponColor);
            case HEALER -> new ComponentSelect(pos, (group) -> new StaticSprite(group, "components/healer.png", new double[] {0, 0}, 0.1), "Healer", "Health: 100\nFirerate: 5\nRange: 50\n Heals nearby units a total of 4% every second\nPrioritizes broken units", () -> new Healer(Player.core), recoveryColor);
            case REVIVER -> new ComponentSelect(pos, (group) -> new StaticSprite(group, "components/reviver.png", new double[] {0, 0}, 0.1), "Reviver", "Health: 120\nFirerate: 1\nRange: 75\nHeals nearby broken units a total of 12.5% every second", () -> new Reviver(Player.core), recoveryColor);
        }
    }

    private final String name;
    private final String description;
    private final Group group;
    private final Scale scale;
    private final ClickAction clickAction;
    private final Sprite sprite;
    private int time;
    private ComponentSelect(Position pos, SpriteAction spriteAction, String name, String description, ClickAction clickAction, Color color) {
        time = 0;
        this.name = name;
        this.description = description;
        this.clickAction = clickAction;
        group = new Group();
        group.setOnMouseClicked(this::click);
        group.setTranslateX(pos.x);
        group.setTranslateY(pos.y);
        selectGroup.getChildren().add(group);
        activeList.add(this);

        Rectangle bg = new Rectangle(-60, 0, 120, 200);
        Rectangle box = new Rectangle(-60, 0, 120, 200);
        scale = new Scale();
        scale.setY(0);
        box.getTransforms().setAll(scale);
        bg.setFill(NoisePattern.create(bg.getWidth(), bg.getHeight(), new Noise[] {new Noise(2, 0.1), new Noise(10, 10, 0.1)}, new Noise[] {new Noise(2, 0.1), new Noise(10, 10, 0.1)}, new Noise[] {new Noise(2, 0.1), new Noise(10, 10, 0.1)}, color.getRed(), color.getGreen(), color.getBlue(), new Position(bg.getX(), bg.getY())));

        Label nameLabel = new Label(name);
        nameLabel.setFont(Font.font("AgencyFB", FontWeight.BLACK, 15));
        nameLabel.setTextAlignment(TextAlignment.CENTER);
        nameLabel.setAlignment(Pos.CENTER);
        nameLabel.setTranslateX(box.getX());
        nameLabel.setTranslateY(5);
        nameLabel.setMinWidth(box.getWidth());

        Label descLabel = new Label(description);
        descLabel.setFont(Font.font("AgencyFB", FontWeight.BLACK, 10));
        descLabel.setTextAlignment(TextAlignment.CENTER);
        descLabel.setAlignment(Pos.CENTER);
        descLabel.setTranslateX(box.getX());
        descLabel.setTranslateY(100);
        descLabel.setMinWidth(box.getWidth());
        descLabel.setMaxWidth(box.getWidth());
        descLabel.setWrapText(true);

        group.getChildren().addAll(bg, nameLabel, descLabel);
        group.setClip(box);

        sprite = spriteAction.run(group);
        sprite.dir = 90;
        sprite.pos.set(0, 60);
        sprite.enable();
    }

    private void tick() {
        time++;
        scale.setY(1 - 1.0/(time * 0.25 + 0.75));
        sprite.drawUpdate();
    }

    private void click(MouseEvent mouseEvent) {
        clickAction.run();
        clear();
    }

    private void remove() {
        selectGroup.getChildren().remove(group);
        sprite.disable();
    }
}
