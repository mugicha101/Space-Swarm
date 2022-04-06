package application.component;

import application.Core;
import application.Player;
import application.Sound;
import application.action.SiphonBullet;
import application.movement.DirCalc;
import application.sprite.StaticSprite;

public class Siphongun extends Weapon {
    public Siphongun(Core parent) {
        super(parent, new StaticSprite(null, "components/siphongun.png", new double[] {0, 0}, 0.1), 10, 90, 0.75, 1000, 20, 8, 15);
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    protected boolean action() {
        double dir = DirCalc.dirTo(velo.pos, parent.aimPos);
        // velo.add((new Position()).moveInDir(dir, -0.5));
        Sound.play("shot1.mp3", 0.2, 2, 0.25, parent == Player.core? null : velo.pos);
        new SiphonBullet(parent, this, velo.pos.clone().moveInDir(dir, 16), dir, velo);
        return true;
    }
}
