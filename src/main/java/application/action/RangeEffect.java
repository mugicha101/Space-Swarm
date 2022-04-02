package application.action;

import application.chunk.Chunk;
import application.component.Component;
import application.movement.DirCalc;
import application.movement.Position;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.HashSet;

public abstract class RangeEffect extends Effect {
    private final double startDuration;
    protected final Component sourceComponent;
    private final Circle circle;
    private Position pos;
    private static Component[] getAffected(Position pos, double range) {
        HashSet<Component> affectedSet = Chunk.getComponents(pos, range);
        Component[] affected = new Component[affectedSet.size()];
        int i = 0;
        for (Component c : affectedSet)
            affected[i++] = c;
        return affected;
    }
    public RangeEffect(Component sourceComponent, double range, double duration, Color color) {
        super(getAffected(sourceComponent.velo.pos, range), duration);
        this.startDuration = duration;
        this.sourceComponent = sourceComponent;
        pos = sourceComponent.velo.pos;
        circle = new Circle(0, 0, range);
        circle.setFill(new RadialGradient(0, 0, 0, 0, range, false, CycleMethod.NO_CYCLE, new Stop(0.5, color), new Stop(1, Color.TRANSPARENT)));
        group.getChildren().add(circle);
    }

    @Override
    protected final void action(int index, Component component, double timeMulti) {
        circle.setTranslateX(pos.x);
        circle.setTranslateY(pos.y);
        circle.setOpacity(Math.min(1 - (startDuration - duration) * 2, 1) * 0.5);
        effectTick(index, component, timeMulti);
    }

    protected abstract void effectTick(int index, Component component, double timeMulti);

    @Override
    protected void remove(int index, Component component) {}
}
