package application.action;

import application.component.*;
import javafx.scene.paint.Color;

public class BeaconEffect extends RangeEffect {
    private final double damageMulti;
    private final double potencyMulti;
    public BeaconEffect(Component sourceComponent, double range, double duration, Color color, double damageMulti, double potencyMulti) {
        super(sourceComponent, range, duration, color);
        this.damageMulti = damageMulti;
        this.potencyMulti = potencyMulti;
    }

    @Override
    protected boolean canBeAffected(Component component) {
        return !(component instanceof Beacon);
    }

    @Override
    protected void effectTick(int index, Component component, double timeMulti) {}

    @Override
    public void apply(Component component) {
        if (component instanceof Weapon weapon) {
            weapon.damage *= damageMulti;
        } else if (component instanceof Support support) {
            support.potency *= potencyMulti;
        }
    }
}
