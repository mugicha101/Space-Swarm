package application.action;

import application.Core;
import application.chunk.Chunk;
import application.component.Component;
import application.component.Weapon;
import application.movement.Position;
import application.movement.Velocity;
import application.particle.CircleParticle;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;

import java.util.HashSet;

public class LazerAttack extends Attack {
    protected static double particleMulti = 0.25;

    protected double dir;
    protected double width;
    protected double damage;
    protected double range;
    private final double driftX;
    private final double driftY;
    private final Rectangle beam;
    private final Position endPos;
    private int time;
    private final Color color;
    public LazerAttack(Core parentCore, Weapon parentWeapon, Position pos, double dir, Velocity sourceVelo, Color color, double damageMulti, double rangeMulti, double widthMulti) {
        super(parentCore, pos);
        this.dir = dir;
        this.color = color;
        width = parentWeapon.shotspeed * widthMulti;
        range = parentWeapon.range * rangeMulti;
        damage = parentWeapon.damage * damageMulti;
        driftX = sourceVelo.x;
        driftY = sourceVelo.y;
        endPos = pos.clone().moveInDir(dir, range);
        beam = new Rectangle(0, -width * 0.5, range, width);
        beam.setFill(new LinearGradient(0, -width * 0.5, 0, width * 0.5, false, CycleMethod.NO_CYCLE, new Stop(0, color), new Stop(0.5, Color.WHITE), new Stop(1, color)));
        group.getChildren().add(beam);
        Rotate rotate = new Rotate(-dir, 0, 0);
        group.getTransforms().setAll(rotate);
        time = 0;
    }

    protected void tick() {
        time++;
        // movement
        pos.move(driftX, driftY);
        endPos.move(driftX, driftY);

        // visuals
        group.setTranslateX(pos.x);
        group.setTranslateY(pos.y);
        double scale = Math.max(Math.min((20 - time)/10.0, 1), 0);
        beam.setScaleY(scale);

        // collision
        Line line = new Line(pos.x, pos.y, endPos.x, endPos.y);
        HashSet<Component> hitSet = new HashSet<>();
        for (Core core : Core.cores) {
            if (core == parent)
                continue;
            for (Component component : core.components) {
                if (component.isIncapacitated())
                    continue;
                Circle circle = new Circle(component.velo.pos.x, component.velo.pos.y, component.getRadius());
                Shape shape = Shape.intersect(circle, line);
                if (shape.getBoundsInLocal().getWidth() != -1)
                    hitSet.add(component);
            }
        }
        double dmg = damage / 20;
        for (Component component : hitSet) {
            component.damage(dmg, parent);
            int amount = (int)(dmg);
            if (rand.nextDouble() > (dmg - amount))
                amount++;
            for (int i = 0; i < amount; i++) {
                new CircleParticle(5, color, 1, component.velo.pos, dir + 180 + (rand.nextDouble() * 2 - 1) * 45, 20 * (0.25 + rand.nextDouble() / 2), 5 + rand.nextInt(10));
            }
        }

        // kill
        if (time >= 20)
            alive = false;
    }

    @Override
    protected void chunkAdd(Chunk chunk) {}

    @Override
    protected void chunkRemove(Chunk chunk) {}

    @Override
    protected Rectangle2D getBounds() {
        return null;
    }
}
