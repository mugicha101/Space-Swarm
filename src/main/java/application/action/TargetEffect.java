package application.action;

import application.component.Component;
import application.component.Radar;
import application.component.Targeter;
import application.component.Weapon;
import javafx.scene.paint.Color;

public class TargetEffect extends RangeEffect {
    private final double spreadMulti;
    private final double shotspeedMulti;
    public TargetEffect(Component sourceComponent, double range, double duration, Color color, double spreadMulti, double shotspeedMulti) {
        super(sourceComponent, range, duration, color);
        this.spreadMulti = spreadMulti;
        this.shotspeedMulti = shotspeedMulti;
    }

    @Override
    protected boolean canBeAffected(Component component) {
        return !(component instanceof Targeter);
    }

    @Override
    protected void effectTick(int index, Component component, double timeMulti) {}

    @Override
    public void apply(Component component) {
        if (component instanceof Weapon weapon) {
            weapon.spread *= spreadMulti;
            weapon.shotspeed *= shotspeedMulti;
        }
    }
}
