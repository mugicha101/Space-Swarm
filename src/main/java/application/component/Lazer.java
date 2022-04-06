package application.component;

import application.Core;
import application.Player;
import application.Sound;
import application.action.LazerAttack;
import application.movement.DirCalc;
import application.movement.Position;
import application.sprite.StaticSprite;
import javafx.scene.paint.Color;

public class Lazer extends Weapon {
    public Lazer(Core parent) {
        super(parent, new StaticSprite(null, "components/lazer.png", new double[] {0, 0}, 0.1),10, 70, 0.2, 2000, 50, 20, 0);
    }

    @Override
    protected boolean action() {
        double dir = DirCalc.dirTo(velo.pos, parent.aimPos);
        velo.add((new Position()).moveInDir(dir, -3/(firerate * 4)));
        Sound.play("plasma.wav", 1, 0.5, 0.25, parent == Player.core? null : velo.pos);
        new LazerAttack(parent, this, velo.pos.clone().moveInDir(dir, 16), dir, velo, Color.color(1, 0, 0), 1, 1, 1);
        return true;
    }
}
