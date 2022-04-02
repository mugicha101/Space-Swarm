package application.action;

import application.component.Component;
import javafx.scene.paint.Color;

public class PatchEffect extends RangeEffect {
    private final double healthPerSecond;
    private final boolean onlyIncapacitated;
    public PatchEffect(Component sourceComponent, double range, double duration, Color color, double healthPerSecond, boolean onlyIncapacitated) {
        super(sourceComponent, range, duration, color);
        this.healthPerSecond = healthPerSecond;
        this.onlyIncapacitated = onlyIncapacitated;
    }

    @Override
    protected void effectTick(int index, Component component, double timeMulti) {
        if (component.isIncapacitated() || !onlyIncapacitated) component.healProportion(healthPerSecond * timeMulti);
    }
}
