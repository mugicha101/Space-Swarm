package application.action;

import application.component.Component;
import application.component.Overclocker;
import application.component.Weapon;
import javafx.scene.paint.Color;

public class OverclockEffect extends RangeEffect {
    private final double firerateMulti;
    public OverclockEffect(Component sourceComponent, double range, double duration, Color color, double firerateMulti) {
        super(sourceComponent, range, duration, color);
        this.firerateMulti = firerateMulti;
    }

    @Override
    protected boolean canBeAffected(Component component) {
        return component instanceof Weapon;
    }

    @Override
    protected void effectTick(int index, Component component, double timeMulti) {}

    @Override
    public void apply(Component component) {
        component.firerate += component.initFirerate * firerateMulti;
    }
}
