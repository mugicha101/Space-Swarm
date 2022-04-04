package application.action;

import application.component.Component;
import application.component.Radar;
import javafx.scene.paint.Color;

public class RadarEffect extends RangeEffect {
    private final double rangeMulti;
    public RadarEffect(Component sourceComponent, double range, double duration, Color color, double rangeMulti) {
        super(sourceComponent, range, duration, color);
        this.rangeMulti = rangeMulti;
    }

    @Override
    protected boolean canBeAffected(Component component) {
        return !(component instanceof Radar);
    }

    @Override
    protected void effectTick(int index, Component component, double timeMulti) {}

    @Override
    public void apply(Component component) {
        component.range *= rangeMulti;
    }
}
