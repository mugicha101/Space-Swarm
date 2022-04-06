package application.action;

import application.component.Component;
import javafx.scene.paint.Color;

public class ArmorEffect extends RangeEffect {
    private final double armorMulti;
    public ArmorEffect(Component sourceComponent, double range, double duration, Color color, double armorMulti) {
        super(sourceComponent, range, duration, color);
        this.armorMulti = armorMulti;
    }

    @Override
    protected void effectTick(int index, Component component, double timeMulti) {}

    @Override
    public void apply(Component component) {
        component.armor += armorMulti;
    }
}
