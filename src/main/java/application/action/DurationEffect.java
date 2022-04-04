package application.action;

import application.component.Component;
import application.component.Energizer;
import application.component.Support;
import javafx.scene.paint.Color;

public class DurationEffect extends RangeEffect {
    private final double durationMulti;
    public DurationEffect(Component sourceComponent, double range, double duration, Color color, double durationMulti) {
        super(sourceComponent, range, duration, color);
        this.durationMulti = durationMulti;
    }

    @Override
    protected boolean canBeAffected(Component component) {
        return !(component instanceof Energizer);
    }

    @Override
    protected void effectTick(int index, Component component, double timeMulti) {}

    @Override
    public void apply(Component component) {
        if (component instanceof Support support) {
            support.duration *= durationMulti;
        }
    }
}
