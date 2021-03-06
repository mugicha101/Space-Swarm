package application.component;

import application.*;
import application.movement.Position;
import application.sprite.Sprite;
import application.sprite.StaticSprite;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
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
        SIPHONGUN,
        LAZER,
        OVERCLOCKER,
        SHIELDER,
        BEACON,
        TARGETER,
        RADAR,
        ENERGIZER,
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

    private static final double boxWidth = Game.guiWidth / 3.2;
    private static final double boxHeight = Game.height * 0.32;

    private static final Random rand = new Random();
    public static final Group selectGroup = new Group();
    private static final ArrayList<ComponentSelect> activeList = new ArrayList<>();
    public static void tickOptions() {
        if (Player.core.isAlive())
            for (ComponentSelect cs : activeList)
                cs.tick();
        else
            clear();
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
            Position pos = new Position(Game.guiWidth * 0.5 + i * Game.guiWidth / 3.0, Game.height * 0.65);
            if (i == -1) { // weapon
                switch(rand.nextInt(5)) {
                    case 0 -> ComponentSelect.create(Type.TURRET, pos);
                    case 1 -> ComponentSelect.create(Type.SNIPER, pos);
                    case 2 -> ComponentSelect.create(Type.CANNON, pos);
                    case 3 -> ComponentSelect.create(Type.SIPHONGUN, pos);
                    case 4 -> ComponentSelect.create(Type.LAZER, pos);
                }
            } else if (i == 0) { // support
                switch(rand.nextInt(6)) {
                    case 0 -> ComponentSelect.create(Type.OVERCLOCKER, pos);
                    case 1 -> ComponentSelect.create(Type.SHIELDER, pos);
                    case 2 -> ComponentSelect.create(Type.BEACON, pos);
                    case 3 -> ComponentSelect.create(Type.TARGETER, pos);
                    case 4 -> ComponentSelect.create(Type.RADAR, pos);
                    case 5 -> ComponentSelect.create(Type.ENERGIZER, pos);
                }
            } else {
                switch(rand.nextInt(3)) {
                    case 0 -> ComponentSelect.create(Type.HEALER, pos);
                    case 1 -> ComponentSelect.create(Type.REVIVER, pos);
                    case 2 -> ComponentSelect.create(Type.PATCHER, pos);
                }
            }
        }
    }
    private static void create(Type type, Position pos) {
        switch(type) {
            case TURRET -> new ComponentSelect(pos, (group) -> new StaticSprite(group, "components/turret.png", new double[] {0, 0}, 0.2), "Turret", "Health: 100\nFirerate: 2\nDamage: 20\nSpread: 15\nShotspeed: 15\nRange: 1200", () -> new Turret(Player.core), weaponColor);
            case SNIPER -> new ComponentSelect(pos, (group) -> new StaticSprite(group, "components/sniper.png", new double[] {0, 0}, 0.2), "Sniper", "Health: 80\nFirerate: 0.25\nDamage: 50\nSpread: 1\nShotspeed: 25\nRange: 1800", () -> new Sniper(Player.core), weaponColor);
            case CANNON -> new ComponentSelect(pos, (group) -> new StaticSprite(group, "components/cannon.png", new double[] {0, 0}, 0.2), "Cannon", "Health: 120\nFirerate: 0.5\nDamage: 12\nAOE: scales with damage\nSpread: 5\nShotspeed: 10\nRange: 900", () -> new Cannon(Player.core), weaponColor);
            case SIPHONGUN -> new ComponentSelect(pos, (group) -> new StaticSprite(group, "components/siphongun.png", new double[] {0, 0}, 0.2), "Siphongun", "Health: 90\nFirerate: 0.75\nDamage: 20\nSpread: 15\nShotspeed: 8\nRange: 1000\nCan be buffed by any support unit\nSupport buffs are are doubled", () -> new Siphongun(Player.core), weaponColor);
            case LAZER -> new ComponentSelect(pos, (group) -> new StaticSprite(group, "components/lazer.png", new double[] {0, 0}, 0.2), "Lazer", "Health: 70\nFirerate: 0.2\nDamage: 50\nshoots a lazer\nSpread: 0\nLaser Width: 20\nRange: 900\nwidth scales with shotspeed", () -> new Lazer(Player.core), weaponColor);
            case OVERCLOCKER -> new ComponentSelect(pos, (group) -> new StaticSprite(group, "components/overclocker.png", new double[] {0, 0}, 0.2), "Overclocker", "Health: 100\nFirerate: 0.2\nRange: 75\nDuration: 5s\nIncreases the firerate of all weapons in range by 15%", () -> new Overclocker(Player.core), supportColor);
            case SHIELDER -> new ComponentSelect(pos, (group) -> new StaticSprite(group, "components/shielder.png", new double[] {0, 0}, 0.2), "Shielder", "Health: 200\nFirerate: 0.2\nRange: 75\nDuration: 5s\nIncreases the armor of all units in range by 15%", () -> new Shielder(Player.core), supportColor);
            case BEACON -> new ComponentSelect(pos, (group) -> new StaticSprite(group, "components/beacon.png", new double[] {0, 0}, 0.2), "Beacon", "Health: 100\nFirerate: 0.2\nRange: 75\nDuration: 5s\nIncreases the damage/potency of all units in range by 15%\nCannot affect other beacons", () -> new Beacon(Player.core), supportColor);
            case TARGETER -> new ComponentSelect(pos, (group) -> new StaticSprite(group, "components/targeter.png", new double[] {0, 0}, 0.2), "Targeter", "Health: 100\nFirerate: 0.2\nRange: 75\nDuration: 5s\nIncreases the shotspeed and accuracy of all weapons in range by 15%", () -> new Targeter(Player.core), supportColor);
            case RADAR -> new ComponentSelect(pos, (group) -> new StaticSprite(group, "components/radar.png", new double[] {0, 0}, 0.2), "Radar", "Health: 100\nFirerate: 0.2\nRange: 75\nDuration: 5s\nIncreases the range of all units in range by 15%", () -> new Radar(Player.core), supportColor);
            case ENERGIZER -> new ComponentSelect(pos, (group) -> new StaticSprite(group, "components/energizer.png", new double[] {0, 0}, 0.2), "Energizer", "Health: 100\nFirerate: 0.2\nRange: 75\nDuration: 5s\nIncreases the duration for all support/recovery units in range by 15%\nCannot affect other energizers", () -> new Energizer(Player.core), supportColor);
            case HEALER -> new ComponentSelect(pos, (group) -> new StaticSprite(group, "components/healer.png", new double[] {0, 0}, 0.2), "Healer", "Health: 100\nFirerate: 5\nRange: 50\n Heal a unit a total of 4% every second\nPrioritizes broken units", () -> new Healer(Player.core), recoveryColor);
            case REVIVER -> new ComponentSelect(pos, (group) -> new StaticSprite(group, "components/reviver.png", new double[] {0, 0}, 0.2), "Reviver", "Health: 120\nFirerate: 1\nRange: 75\nHeals a broken unit a total of 12.5% every second", () -> new Reviver(Player.core), recoveryColor);
            case PATCHER -> new ComponentSelect(pos, (group) -> new StaticSprite(group, "components/patcher.png", new double[] {0, 0}, 0.2), "Patcher", "Health: 100\nFirerate: 1\nRange: 50\nHeals all units in range a total of 5% every second", () -> new Patcher(Player.core), recoveryColor);
        }
    }

    private final Group group;
    private final Scale scale;
    private final ClickAction clickAction;
    private final Sprite sprite;
    private int time;
    private ComponentSelect(Position pos, SpriteAction spriteAction, String name, String description, ClickAction clickAction, Color color) {
        time = 0;
        this.clickAction = clickAction;
        group = new Group();
        group.setOnMouseClicked(this::click);
        group.setTranslateX(pos.x);
        group.setTranslateY(pos.y);
        selectGroup.getChildren().add(group);
        activeList.add(this);

        Rectangle bg = new Rectangle(-boxWidth*0.5, 0, boxWidth, boxHeight);
        Rectangle box = new Rectangle(-boxWidth*0.5, 0, boxWidth, boxHeight);
        scale = new Scale();
        scale.setY(0);
        box.getTransforms().setAll(scale);
        Noise[] noiseArr = new Noise[] {new Noise(2, 0.2), new Noise(10, 10, 0.2)};
        bg.setFill(NoisePattern.create(bg.getWidth(), bg.getHeight(), noiseArr, noiseArr, noiseArr, color.getRed(), color.getGreen(), color.getBlue(), new Position(bg.getX(), bg.getY())));

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

        Color textColor = Color.color(color.getRed() * 0.1 + 0.9, color.getGreen() * 0.1 + 0.9, color.getBlue() * 0.1 + 0.9);
        nameLabel.setTextFill(textColor);
        descLabel.setTextFill(textColor);

        group.getChildren().addAll(bg, nameLabel, descLabel);
        group.setClip(box);

        sprite = spriteAction.run(group);
        sprite.dir = 90;
        sprite.pos.set(0, 60);
        sprite.enable();
    }

    private void tick() {
        time++;
        scale.setY(scale.getY() + (1 - scale.getY()) * 0.1);
        sprite.drawUpdate();
    }

    private void click(MouseEvent mouseEvent) {
        Sound.play("option_select.mp3", 1, 1, 0, null);
        clickAction.run();
        clear();
    }

    private void remove() {
        selectGroup.getChildren().remove(group);
        sprite.disable();
    }
}
