package application.action;

import application.Core;
import application.Player;
import application.Sound;
import application.chunk.Chunk;
import application.component.Component;
import application.component.Weapon;
import application.movement.DirCalc;
import application.movement.Position;
import application.movement.Velocity;
import application.particle.CircleParticle;
import javafx.geometry.Rectangle2D;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;

import java.util.HashSet;

public class CannonBullet extends Bullet {
    public CannonBullet(Core parentCore, Weapon parentWeapon, Position pos, double dir, Velocity sourceVelo) {
        super(parentCore, parentWeapon, pos, 15, dir, sourceVelo, 1, 1, 1, 1);
        Circle outer = new Circle(0, 0, radius);
        Circle inner = new Circle(0, 0, 0.5 * radius);
        outer.setFill(Color.color(0.5, 0, 1));
        inner.setFill(Color.WHITE);
        group.getChildren().addAll(outer, inner);
    }

    @Override
    protected void tick() {
        super.tick();
        group.setScaleX(1);
        group.setScaleY(1);
        if (!alive)
            hit(null);
    }

    protected void hit(Component hitComponent) {
        Sound.play("shot4.wav", 1, 1, 0.25, pos);
        hitParticles(Color.color(0.5, 0, 1));
        double aoeRadius = damage * 5;
        double particleSize = aoeRadius * 0.25;
        double spawnRadius = aoeRadius -particleSize;

        // explosion damage
        HashSet<Component> hitSet = new HashSet<>();
        for (Component c : Chunk.getComponents(pos, aoeRadius)) {
            if (!c.parentMatches(parent) && c != hitComponent)
                hitSet.add(c);
        }
        for (Component c : hitSet) {
            c.damage(damage, parent);
            c.velo.add((new Position()).moveInDir(DirCalc.dirTo(pos, c.velo.pos), 2));
        }

        // explosion particles
        for (int i = 0; i < damage * 5; i++) {
            new CircleParticle(10, Color.color(Math.random(), 0, 1), 1, pos, Math.random() * 360, 10 * (0.25 + rand.nextDouble()/2), 5 + rand.nextInt(25));
        }
        for (int i = 0; i < spawnRadius * spawnRadius / 200; i++)
            new CircleParticle(particleSize * (Math.random() * 0.5 + 0.5), Color.color(Math.random(), 0, 1), 1, pos.clone().moveInDir(Math.random() * 360, Math.random() * spawnRadius), 0, 0, 10 + rand.nextInt(50));
    }
}
